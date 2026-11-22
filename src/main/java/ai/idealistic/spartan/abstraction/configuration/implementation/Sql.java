/*
 * Decompiled with CFR 0.152.
 *
 * Could not load the following classes:
 *  org.bukkit.Material
 *  org.bukkit.util.NumberConversions
 */
package ai.idealistic.spartan.abstraction.configuration.implementation;

import ai.idealistic.spartan.abstraction.check.CheckEnums;
import ai.idealistic.spartan.abstraction.configuration.ConfigurationBuilder;
import ai.idealistic.spartan.abstraction.protocol.PlayerProtocol;
import ai.idealistic.spartan.api.BackgroundAPI;
import ai.idealistic.spartan.functionality.concurrent.GeneralThread;
import ai.idealistic.spartan.functionality.moderation.AwarenessNotifications;
import ai.idealistic.spartan.functionality.moderation.CrossServerNotifications;
import ai.idealistic.spartan.functionality.server.MultiVersion;
import ai.idealistic.spartan.functionality.server.PluginBase;
import ai.idealistic.spartan.functionality.tracking.AntiCheatLogs;
import ai.idealistic.spartan.utils.java.StringUtils;
import ai.idealistic.spartan.utils.math.Distance;
import java.sql.Connection;
import java.sql.DriverManager;
import java.sql.ResultSet;
import java.sql.SQLException;
import java.sql.Statement;
import org.bukkit.Material;
import org.bukkit.util.NumberConversions;

public class Sql
extends ConfigurationBuilder {
    private static final GeneralThread.ThreadPool sqlThread = new GeneralThread.ThreadPool(50L);
    private static boolean enabled = false;
    private static Connection con = null;

    public Sql() {
        super("sql");
    }

    public String getHost() {
        String result = this.getString("host");
        if (result != null) {
            result = result.toLowerCase().replace("localhost", "127.0.0.1").replace(" ", "");
        }
        return result;
    }

    public String getUser() {
        String result = this.getString("user");
        if (result != null) {
            result = result.replace(" ", "");
        }
        return result;
    }

    public String getPassword() {
        String result = this.getString("password");
        if (result != null && this.getBoolean("escape_special_characters")) {
            result = StringUtils.escapeMetaCharacters(result);
        }
        return result;
    }

    public String getDatabase() {
        String result = this.getString("database");
        if (result != null) {
            result = result.replace(" ", "");
        }
        return result;
    }

    public String getTable() {
        String result = this.getString("table");
        if (result != null) {
            result = result.replace(" ", "");
        }
        return result;
    }

    public String getPort() {
        String result = this.getString("port");
        if (result != null) {
            Double decimal = Distance.returnValidDecimal(result = result.replace(" ", ""));
            if (decimal != null) {
                result = String.valueOf(NumberConversions.floor((double)decimal));
            }
            return result;
        }
        return null;
    }

    public String getDriver() {
        String result = this.getString("driver");
        if (result == null) {
            result = "mysql";
        }
        return result;
    }

    public String getTLSVersion() {
        return this.getString("tls_Version");
    }

    public boolean getSSL() {
        return this.getBoolean("use_SSL");
    }

    public boolean getPublicKeyRetrieval() {
        return this.getBoolean("allow_public_key_retrieval");
    }

    @Override
    public void clear() {
        super.clear();
        enabled = true;
    }

    public void refreshDatabase() {
        if (this.isConnected(false)) {
            try {
                con.close();
            }
            catch (Exception exception) {
                // empty catch block
            }
        }
        con = null;
    }

    public boolean isEnabled() {
        return enabled;
    }

    @Override
    public void create() {
        this.addOption("host", "");
        this.addOption("user", "");
        this.addOption("password", "");
        this.addOption("database", "");
        this.addOption("table", "spartan_logs");
        this.addOption("port", "3306");
        this.addOption("driver", "mysql");
        this.addOption("tls_Version", "");
        this.addOption("use_SSL", true);
        this.addOption("allow_public_key_retrieval", false);
        this.addOption("escape_special_characters", false);
        sqlThread.executeWithPriority(this::connect);
    }

    private boolean isConnected(boolean message) {
        block3: {
            if (con != null) {
                try {
                    return !con.isClosed();
                }
                catch (Exception e) {
                    if (!message) break block3;
                    AwarenessNotifications.forcefullySend("SQL Connection Check Error:\n" + e.getMessage());
                }
            }
        }
        return false;
    }

    public void connect() {
        String host = this.getHost();
        String user = this.getUser();
        String password = this.getPassword();
        String database = this.getDatabase();
        String table = this.getTable();
        String port = this.getPort();
        int hostLength = host.length();
        int userLength = user.length();
        int passwordLength = password.length();
        int databaseLength = database.length();
        if (hostLength > 0 || userLength > 0 || passwordLength > 0 || databaseLength > 0) {
            if (hostLength == 0) {
                enabled = false;
                AwarenessNotifications.forcefullySend("SQL Configuration Error: Host is blank");
            } else if (userLength == 0) {
                enabled = false;
                AwarenessNotifications.forcefullySend("SQL Configuration Error: User is blank");
            } else if (passwordLength == 0) {
                enabled = false;
                AwarenessNotifications.forcefullySend("SQL Configuration Error: Password is blank");
            } else if (databaseLength == 0) {
                enabled = false;
                AwarenessNotifications.forcefullySend("SQL Configuration Error: Database is blank");
            } else if (table.isEmpty()) {
                enabled = false;
                AwarenessNotifications.forcefullySend("SQL Configuration Error: Table is blank");
            } else if (!Distance.validInteger(port) && !Distance.validDecimal(port)) {
                enabled = false;
                AwarenessNotifications.forcefullySend("SQL Configuration Error: Port is not a valid number");
            } else if (!this.isConnected(true)) {
                String driver = this.getDriver();
                try {
                    if (driver.isEmpty()) {
                        AwarenessNotifications.forcefullySend("SQL Configuration Error: Driver is blank");
                    } else {
                        String tlsVersion = this.getTLSVersion();
                        con = DriverManager.getConnection("jdbc:" + driver + "://" + host + ":" + port + "/" + database + "?autoReconnect=true&maxReconnects=10" + (tlsVersion != null && !tlsVersion.isEmpty() ? "&enabledTLSProtocols=TLSv" + tlsVersion : "") + "&useSSL=" + this.getSSL() + "&allowPublicKeyRetrieval=" + this.getPublicKeyRetrieval(), user, password);
                        this.createTable(table);
                    }
                }
                catch (SQLException e) {
                    AwarenessNotifications.forcefullySend("SQL Initial Connection Error:\n" + e.getMessage());
                    e.printStackTrace();
                }
            }
        }
    }

    public void update(String command) {
        sqlThread.execute(() -> {
            this.connect();
            try {
                if (con != null) {
                    Statement statement =  con.createStatement();
                    st.executeUpdate(command);
                    st.close();
                }
            }
            catch (Exception e) {
                AwarenessNotifications.forcefullySend("SQL Update Error:\nCommand: " + command + "\nException: " + e.getMessage());
            }
        });
    }

    public ResultSet query(String command) {
        ResultSet[] rs = new ResultSet[1];
        Thread thread = Thread.currentThread();
        sqlThread.execute(() -> {
            block5: {
                this.connect();
                try {
                    if (con == null) break block5;
                    Statement statement =  con.createStatement();
                    rs[0] = st.executeQuery(command);
                    synchronized (thread) {
                        thread.notifyAll();
                    }
                }
                catch (Exception e) {
                    AwarenessNotifications.forcefullySend("SQL Query Error:\nCommand: " + command + "\nException: " + e.getMessage());
                }
            }
        });
        synchronized (thread) {
            if (rs[0] == null) {
                try {
                    thread.wait();
                }
                catch (Exception exception) {
                    // empty catch block
                }
            }
        }
        return rs[0];
    }

    private void createTable(String table) {
        this.update("CREATE TABLE IF NOT EXISTS " + table + " (id INT(11) NOT NULL AUTO_INCREMENT, creation_date VARCHAR(30), server_name VARCHAR(64), plugin_version VARCHAR(16), server_version VARCHAR(7), online_players INT(11), type VARCHAR(32), information VARCHAR(4096), notification VARCHAR(4096), player_uuid VARCHAR(36), player_name VARCHAR(24), player_latency INT(11), functionality VARCHAR(32), primary key id);");
    }

    public void logInfo(PlayerProtocol p, String notification, String information, Material material, CheckEnums.HackType hackType, long time) {
        if (enabled) {
            String table = this.getTable();
            boolean hasPlayer = p != null;
            boolean hasCheck = hackType != null;
            boolean hasMaterial = material != null;
            this.update("INSERT INTO " + table + " (creation_date, server_name, plugin_version, server_version, online_players, type, notification, information, player_uuid, player_name, player_latency, functionality) VALUES (" + this.syntaxForColumn(AntiCheatLogs.getDate("yyyy/MM/dd HH:mm:ss:SSSXXX", time)) + ", " + this.syntaxForColumn(CrossServerNotifications.getServerName()) + ", " + this.syntaxForColumn(BackgroundAPI.getVersion()) + ", " + this.syntaxForColumn(MultiVersion.serverVersion.toString()) + ", " + this.syntaxForColumn(PluginBase.getPlayerCount()) + ", " + this.syntaxForColumn(hasMaterial ? "mining" : (hasCheck ? "violation" : "other")) + ", " + (notification != null ? this.syntaxForColumn(notification) : "NULL") + ", " + this.syntaxForColumn(information) + ", " + (hasPlayer ? this.syntaxForColumn(p.getUUID()) : "NULL") + ", " + (hasPlayer ? this.syntaxForColumn(p.bukkit().getName()) : "NULL") + ", " + (hasPlayer ? this.syntaxForColumn(p.getPing()) : "NULL") + ", " + (hasMaterial ? this.syntaxForColumn(material) : (hasCheck ? this.syntaxForColumn(hackType) : "NULL")) + ");");
        }
    }

    private String syntaxForColumn(Object obj) {
        return "'" + obj.toString() + "'";
    }

    @Override
    public final String getString(String path) {
        return super.getString(path);
    }

    @Override
    public final String getColorfulString(String path) {
        return super.getColorfulString(path);
    }
}

