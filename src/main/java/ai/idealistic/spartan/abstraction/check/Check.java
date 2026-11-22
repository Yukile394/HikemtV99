/*
 * Decompiled with CFR 0.152.
 *
 * Could not load the following classes:
 *  lombok.Generated
 *  org.bukkit.Bukkit
 *  org.bukkit.configuration.ConfigurationSection
 *  org.bukkit.configuration.file.YamlConfiguration
 *  org.bukkit.event.Event
 *  org.bukkit.util.NumberConversions
 */
package ai.idealistic.spartan.abstraction.check;

import ai.idealistic.spartan.Register;
import ai.idealistic.spartan.abstraction.check.CheckEnums;
import ai.idealistic.spartan.api.CheckPunishmentToggleEvent;
import ai.idealistic.spartan.api.CheckSilentToggleEvent;
import ai.idealistic.spartan.api.CheckToggleEvent;
import ai.idealistic.spartan.api.ToggleAction;
import ai.idealistic.spartan.functionality.moderation.AwarenessNotifications;
import ai.idealistic.spartan.functionality.server.Config;
import ai.idealistic.spartan.functionality.tracking.ResearchEngine;
import ai.idealistic.spartan.utils.math.Distance;
import ai.idealistic.spartan.utils.minecraft.server.ConfigUtils;
import java.io.File;
import java.util.ArrayList;
import java.util.Collection;
import java.util.HashSet;
import java.util.List;
import java.util.Map;
import java.util.Set;
import java.util.concurrent.ConcurrentHashMap;
import lombok.Generated;
import org.bukkit.Bukkit;
import org.bukkit.configuration.ConfigurationSection;
import org.bukkit.configuration.file.YamlConfiguration;
import org.bukkit.event.Event;
import org.bukkit.util.NumberConversions;

public class Check {
    private static boolean a = false;
    public static final int b = 10;
    private static final File file = new File(Register.plugin.getDataFolder() + "/checks.yml");
    private static final File d = new File(Register.plugin.getDataFolder() + "/advanced.yml");
    private static final List<String> e = new ArrayList<String>(10);
    public final CheckEnums.HackType hackType;
    private String name;
    private final Map<String, Object> f = new ConcurrentHashMap<String, Object>();
    public final boolean g;
    private final boolean[] i;
    private final boolean[] j;
    private final boolean[] k;
    private final boolean[] n;
    private final Set<String> o;
    private final Set<String> q;
    private Collection<String> r;

    public static void handleCheck(boolean panic) {
        a = panic;
        Config.create();
    }

    public Check(CheckEnums.HackType hackType) {
        int size;
        String[] worldsSplit;
        Object handleCancelledEvents = this.handleCheck("cancelled_event", false, false);
        String name = this.handleCheck("name", this.hackType.toString(), false).toString();
        String worlds_config = this.handleCheck("disabled_worlds", "exampleDisabledWorld1, exampleDisabledWorld2", false).toString();
        String silents_config = this.handleCheck("silent_worlds", "exampleSilentWorld1, exampleSilentWorld2", false).toString();
        this.i = new boolean[DataType.values().length];
        this.j = new boolean[DataType.values().length];
        this.k = new boolean[DataType.values().length];
        this.n = new boolean[DataType.values().length];
        for (DataType dataType : DataType.values()) {
            boolean enabled;
            Object optionValue = this.handleCheck("enabled." + dataType.toString().toLowerCase(), true, false);
            this.i[dataType.ordinal()] = enabled = optionValue instanceof Boolean ? (Boolean)optionValue : (optionValue instanceof Long || optionValue instanceof Integer || optionValue instanceof Short ? (Long)optionValue > 0L : (optionValue instanceof Double || optionValue instanceof Float ? (Double)optionValue > 0.0 : Boolean.parseBoolean(optionValue.toString().toLowerCase())));
            optionValue = this.handleCheck("silent." + dataType.toString().toLowerCase(), false, false);
            this.j[dataType.ordinal()] = optionValue instanceof Boolean ? (Boolean)optionValue : (optionValue instanceof Long || optionValue instanceof Integer || optionValue instanceof Short ? (Long)optionValue > 0L : (optionValue instanceof Double || optionValue instanceof Float ? (Double)optionValue > 0.0 : Boolean.parseBoolean(optionValue.toString().toLowerCase())));
            optionValue = this.handleCheck("punishments.enabled." + dataType.toString().toLowerCase(), true, false);
            this.k[dataType.ordinal()] = optionValue instanceof Boolean ? (Boolean)optionValue : (optionValue instanceof Long || optionValue instanceof Integer || optionValue instanceof Short ? (Long)optionValue > 0L : (optionValue instanceof Double || optionValue instanceof Float ? (Double)optionValue > 0.0 : Boolean.parseBoolean(optionValue.toString().toLowerCase())));
            optionValue = this.handleCheck("detection_details." + dataType.toString().toLowerCase(), true, false);
            this.n[dataType.ordinal()] = optionValue instanceof Boolean ? (Boolean)optionValue : (optionValue instanceof Long || optionValue instanceof Integer || optionValue instanceof Short ? (Long)optionValue > 0L : (optionValue instanceof Double || optionValue instanceof Float ? (Double)optionValue > 0.0 : Boolean.parseBoolean(optionValue.toString().toLowerCase())));
        }
        if (name != null) {
            if (name.length() > 32) {
                name = name.substring(0, 32);
            }
        } else {
            this.name = hackType.toString();
        }
        this.g = handleCancelledEvents != null ? (handleCancelledEvents instanceof Boolean ? (Boolean)handleCancelledEvents : (handleCancelledEvents instanceof Long || handleCancelledEvents instanceof Integer || handleCancelledEvents instanceof Short ? (Long)handleCancelledEvents > 0L : (handleCancelledEvents instanceof Double || handleCancelledEvents instanceof Float ? (Double)handleCancelledEvents > 0.0 : Boolean.parseBoolean(handleCancelledEvents.toString().toLowerCase())))) : false;
        if (worlds_config != null) {
            worldsSplit = worlds_config.split(",");
            size = worldsSplit.length;
            if (size > 0) {
                HashSet<String> set = new HashSet<String>(size);
                for (String world : worldsSplit) {
                    if ((world = world.replace(" ", "")).isEmpty()) continue;
                    set.add(world.toLowerCase());
                }
                this.o = !set.isEmpty() ? set : new HashSet<String>(0);
            } else {
                this.o = new HashSet<String>(0);
            }
        } else {
            this.o = new HashSet<String>(0);
        }
        if (silents_config != null) {
            worldsSplit = silents_config.split(",");
            size = worldsSplit.length;
            if (size > 0) {
                HashSet<String> set = new HashSet<String>(size);
                for (String world : worldsSplit) {
                    if ((world = world.replace(" ", "")).isEmpty()) continue;
                    set.add(world.toLowerCase());
                }
                this.q = !set.isEmpty() ? set : new HashSet<String>(0);
            } else {
                this.q = new HashSet<String>(0);
            }
        } else {
            this.q = new HashSet<String>(0);
        }
        this.handleDataType();
    }

    public boolean handleCheck(DataType dataType, String world) {
        if (dataType == null) {
            boolean enabled = false;
            for (DataType type : DataType.values()) {
                if (!this.i[type.ordinal()]) continue;
                enabled = true;
                break;
            }
            if (!enabled) {
                return false;
            }
        } else if (!this.i[dataType.ordinal()]) {
            return false;
        }
        return world == null || !this.o.contains(world.toLowerCase());
    }

    public void handleCheck(DataType dataType, boolean b) {
        DataType[] dataTypes;
        if (dataType == null) {
            dataTypes = DataType.values();
        } else {
            dataTypes = null;
            for (DataType type : DataType.values()) {
                if (type != dataType) continue;
                dataTypes = new DataType[]{dataType};
                break;
            }
            if (dataTypes == null) {
                return;
            }
        }
        for (DataType type : dataTypes) {
            CheckToggleEvent event;
            if (Config.settings.getBoolean("Important.enable_developer_api")) {
                event = new CheckToggleEvent(this.hackType, b ? ToggleAction.ENABLE : ToggleAction.DISABLE);
                Bukkit.getPluginManager().callEvent((Event)event);
            } else {
                event = null;
            }
            if (event != null && event.isCancelled()) continue;
            this.i[type.ordinal()] = b;
            ResearchEngine.queueToCache(this.hackType, type);
            this.setOption("enabled." + type.toString().toLowerCase(), b);
            this.f.clear();
        }
    }

    public void handleCheck(String name) {
        if (name.length() > 32) {
            name = name.substring(0, 32);
        }
        for (CheckEnums.HackType hackType : CheckEnums.HackType.values()) {
            if ((hackType.equals(this.hackType) || !name.equalsIgnoreCase(hackType.toString())) && !name.equalsIgnoreCase(hackType.getCheck().getName())) continue;
            return;
        }
        this.setOption("name", name);
    }

    private void setOption(String option, Object value) {
        try {
            String key = (Object)this.hackType + "." + option;
            YamlConfiguration configuration = YamlConfiguration.loadConfiguration((File)file);
            configuration.set(key, value);
            try {
                configuration.save(file);
                this.f.remove(key);
            }
            catch (Exception ex) {
                AwarenessNotifications.forcefullySend("Failed to store '" + key + "' option in '" + file.getName() + "' file.");
                ex.printStackTrace();
            }
            return;
        }
        catch (Exception ex) {
            AwarenessNotifications.forcefullySend("Failed to find/create the '" + file.getName() + "' file.");
            ex.printStackTrace();
            AwarenessNotifications.forcefullySend("Failed to find/create the '" + file.getName() + "' file.");
            return;
        }
    }

    private Object handleCheck(String option, Object def, boolean cache) {
        Object cached;
        if (cache && (cached = this.f.get(option)) != null) {
            return cached;
        }
        try {
            String key = (Object)this.hackType + "." + option;
            boolean isDefaultNull = def == null;
            File file = cache ? d : Check.file;
            YamlConfiguration configuration = YamlConfiguration.loadConfiguration((File)file);
            if (cache) {
                if (configuration.contains(key)) {
                    Object value = configuration.get(key, def);
                    if (!isDefaultNull) {
                        this.f.put(option, value);
                    }
                    return value;
                }
                if (!isDefaultNull) {
                    configuration.set(key, def);
                    try {
                        configuration.save(file);
                        this.f.put(option, def);
                    }
                    catch (Exception ex) {
                        AwarenessNotifications.forcefullySend("Failed to store '" + key + "' option in '" + file.getName() + "' file.");
                        ex.printStackTrace();
                    }
                }
            } else {
                if (configuration.contains(key)) {
                    return configuration.get(key, def);
                }
                if (!isDefaultNull) {
                    configuration.set(key, def);
                    try {
                        configuration.save(file);
                    }
                    catch (Exception ex) {
                        AwarenessNotifications.forcefullySend("Failed to store '" + key + "' option in '" + file.getName() + "' file.");
                        ex.printStackTrace();
                    }
                }
            }
        }
        catch (Exception ex) {
            AwarenessNotifications.forcefullySend("Failed to find/create the '" + file.getName() + "' file.");
            ex.printStackTrace();
        }
        return def;
    }

    public boolean handleCheck(String option, Boolean def) {
        Object object = this.handleCheck(option, def, true);
        return object instanceof Boolean ? (Boolean)object : (object instanceof String ? Boolean.parseBoolean(object.toString().toLowerCase()) : (object instanceof Long || object instanceof Integer || object instanceof Short ? (Long)object > 0L : (object instanceof Double || object instanceof Float ? (Double)object > 0.0 : def != null && def)));
    }

    public int handleCheck(String option, int def) {
        Object object = this.handleCheck(option, def, true);
        if (object instanceof Integer || object instanceof Short) {
            return (Integer)object;
        }
        if (object instanceof String || object instanceof Long) {
            try {
                return Integer.parseInt(object.toString());
            }
            catch (Exception ex) {
                return def;
            }
        }
        if (object instanceof Double || object instanceof Float) {
            try {
                return NumberConversions.round((double)Double.parseDouble(object.toString()));
            }
            catch (Exception ex) {
                return def;
            }
        }
        return def;
    }

    public double handleCheck(String option, double def) {
        Object object = this.handleCheck(option, def, true);
        if (object instanceof Double || object instanceof Float) {
            return (Double)object;
        }
        if (object instanceof Long) {
            return ((Long)object).doubleValue();
        }
        if (object instanceof Integer) {
            return ((Integer)object).doubleValue();
        }
        if (object instanceof Short) {
            return ((Short)object).doubleValue();
        }
        if (object instanceof String) {
            try {
                return Double.parseDouble(object.toString());
            }
            catch (Exception ex) {
                return def;
            }
        }
        return def;
    }

    public boolean handleCheck(DataType dataType) {
        if (dataType == null) {
            for (DataType type : DataType.values()) {
                if (!this.n[type.ordinal()]) continue;
                return true;
            }
            return false;
        }
        return this.n[dataType.ordinal()];
    }

    public void checkCheckCancellation(DataType dataType, boolean b) {
        DataType[] dataTypes = dataType == null ? DataType.values() : new DataType[]{dataType};
        for (DataType type : dataTypes) {
            this.n[type.ordinal()] = b;
            this.setOption("detection_details." + type.toString().toLowerCase(), b);
            this.f.clear();
        }
    }

    public boolean checkCheckCancellation(DataType dataType) {
        if (a) {
            return false;
        }
        if (dataType == null) {
            for (DataType type : DataType.values()) {
                if (!this.k[type.ordinal()]) continue;
                return true;
            }
            return false;
        }
        return this.k[dataType.ordinal()];
    }

    public boolean checkCheckCancellation(DataType dataType, String world) {
        if (a || world != null && this.q.contains(world.toLowerCase())) {
            return true;
        }
        if (dataType == null) {
            for (DataType type : DataType.values()) {
                if (!this.j[type.ordinal()]) continue;
                return true;
            }
            return false;
        }
        return this.j[dataType.ordinal()];
    }

    public void handleDataType(DataType dataType, boolean b) {
        DataType[] dataTypes;
        if (dataType == null) {
            dataTypes = DataType.values();
        } else {
            dataTypes = null;
            for (DataType type : DataType.values()) {
                if (type != dataType) continue;
                dataTypes = new DataType[]{dataType};
                break;
            }
            if (dataTypes == null) {
                return;
            }
        }
        for (DataType type : dataTypes) {
            CheckSilentToggleEvent event;
            if (Config.settings.getBoolean("Important.enable_developer_api")) {
                event = new CheckSilentToggleEvent(this.hackType, b ? ToggleAction.ENABLE : ToggleAction.DISABLE);
                Bukkit.getPluginManager().callEvent((Event)event);
            } else {
                event = null;
            }
            if (event != null && event.isCancelled()) continue;
            this.j[type.ordinal()] = b;
            this.setOption("silent." + type.toString().toLowerCase(), b);
            this.f.clear();
        }
    }

    public List<String> checkCheckCancellation() {
        return new ArrayList<String>(this.r);
    }

    private void handleDataType() {
        ConfigurationSection section;
        String sectionString = (Object)this.hackType + ".punishments.commands";
        YamlConfiguration configuration = YamlConfiguration.loadConfiguration((File)file);
        if (!configuration.contains(sectionString)) {
            for (int position = 0; position < e.size(); ++position) {
                ConfigUtils.add(file, sectionString + "." + (position + 1), e.get(position));
            }
        }
        if ((section = configuration.getConfigurationSection(sectionString)) != null) {
            ArrayList<String> list = new ArrayList<String>();
            Set keys = section.getKeys(true);
            if (!keys.isEmpty()) {
                sectionString = sectionString + ".";
                for (String key : keys) {
                    String command;
                    int number;
                    if (!Distance.validInteger(key) || (number = Integer.parseInt(key)) < 1 || number > 10 || (command = configuration.getString(sectionString + number)) == null || command.isEmpty()) continue;
                    list.add(command);
                }
            }
            this.r = list;
        } else {
            this.r = new ArrayList<String>(0);
        }
    }

    public void handleCheck(int position, String command) {
        if (position < 1 || position > 10) {
            return;
        }
        if (command == null) {
            command = "";
        }
        String sectionString = (Object)this.hackType + ".punishments.commands." + position;
        YamlConfiguration configuration = YamlConfiguration.loadConfiguration((File)file);
        configuration.set(sectionString, command);
        try {
            configuration.save(file);
            this.handleDataType();
        }
        catch (Exception ex) {
            AwarenessNotifications.forcefullySend("Failed to store '" + sectionString + "' option in '" + file.getName() + "' file.");
            ex.printStackTrace();
        }
    }

    public void isCheckCancellation(DataType dataType, boolean b) {
        DataType[] dataTypes = dataType == null ? DataType.values() : new DataType[]{dataType};
        for (DataType type : dataTypes) {
            CheckPunishmentToggleEvent event;
            if (Config.settings.getBoolean("Important.enable_developer_api")) {
                event = new CheckPunishmentToggleEvent(this.hackType, b ? ToggleAction.ENABLE : ToggleAction.DISABLE);
                Bukkit.getPluginManager().callEvent((Event)event);
            } else {
                event = null;
            }
            if (event != null && event.isCancelled()) continue;
            this.k[type.ordinal()] = b;
            this.setOption("punishments.enabled." + type.toString().toLowerCase(), b);
            this.f.clear();
        }
    }

    @Generated
    public String getName() {
        return this.name;
    }

    static {
        e.add("spartan kick {player} {detections}");
        for (int position = e.size() + 1; position <= 10; ++position) {
            e.add("");
        }
    }

    public static enum DataType {
        JAVA,
        BEDROCK;

        private final String string;

        private DataType() {
            switch (this.ordinal()) {
                case 0: {
                    this.string = "Java";
                    break;
                }
                default: {
                    this.string = "Bedrock";
                }
            }
        }

        public String toString() {
            return this.string;
        }
    }

    public static enum DetectionType {
        BUKKIT,
        PACKETS;

        private final String string = this.ordinal() == 0 ? "Bukkit" : "Packets";

        public String toString() {
            return this.string;
        }
    }
}

