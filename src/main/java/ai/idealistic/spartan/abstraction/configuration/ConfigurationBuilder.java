/*
 * Decompiled with CFR 0.152.
 *
 * Could not load the following classes:
 *  org.bukkit.ChatColor
 *  org.bukkit.configuration.file.YamlConfiguration
 */
package ai.idealistic.spartan.abstraction.configuration;

import ai.idealistic.spartan.Register;
import ai.idealistic.spartan.utils.minecraft.server.ConfigUtils;
import java.io.File;
import java.util.Map;
import java.util.concurrent.ConcurrentHashMap;
import org.bukkit.ChatColor;
import org.bukkit.configuration.file.YamlConfiguration;

public abstract class ConfigurationBuilder {
    protected static final String prefix = "{prefix}";
    protected final File file;
    private final Map<String, Boolean> bool = new ConcurrentHashMap<String, Boolean>();
    private final Map<String, Boolean> exists = new ConcurrentHashMap<String, Boolean>();
    private final Map<String, Integer> ints = new ConcurrentHashMap<String, Integer>();
    private final Map<String, Double> dbls = new ConcurrentHashMap<String, Double>();
    private final Map<String, String> str = new ConcurrentHashMap<String, String>();

    public static String getDirectory(String fileName) {
        return Register.plugin.getDataFolder() + "/" + fileName + ".yml";
    }

    public ConfigurationBuilder(String fileName) {
        this.file = new File(ConfigurationBuilder.getDirectory(fileName));
    }

    protected final YamlConfiguration getPath() {
        if (!this.file.exists()) {
            this.create();
        }
        return YamlConfiguration.loadConfiguration((File)this.file);
    }

    protected final void internalClear() {
        this.bool.clear();
        this.exists.clear();
        this.ints.clear();
        this.str.clear();
    }

    public final File getFile() {
        return this.file;
    }

    public final boolean exists(String path) {
        Boolean data = this.exists.get(path);
        if (data != null) {
            return data;
        }
        boolean result = this.getPath().contains(path);
        this.exists.put(path, result);
        return result;
    }

    public final boolean getBoolean(String path) {
        Boolean data = this.bool.get(path);
        if (data != null) {
            return data;
        }
        boolean value = this.getPath().getBoolean(path);
        this.bool.put(path, value);
        return value;
    }

    public final int getInteger(String path) {
        Integer data = this.ints.get(path);
        if (data != null) {
            return data;
        }
        int value = this.getPath().getInt(path);
        this.ints.put(path, value);
        return value;
    }

    public final double getDouble(String path) {
        Double data = this.dbls.get(path);
        if (data != null) {
            return data;
        }
        double value = this.getPath().getDouble(path);
        this.dbls.put(path, value);
        return value;
    }

    public String getString(String path) {
        String data = this.str.get(path);
        if (data != null) {
            return data;
        }
        String value = this.getPath().getString(path);
        if (value == null) {
            return path;
        }
        this.str.put(path, value);
        return value;
    }

    public String getColorfulString(String path) {
        String value;
        String data = this.str.get(path);
        if (data != null) {
            return data;
        }
        if (!this.file.exists()) {
            this.create();
        }
        if ((value = this.getPath().getString(path)) == null) {
            return path;
        }
        value = ChatColor.translateAlternateColorCodes((char)'&', (String)value);
        this.str.put(path, value);
        return value;
    }

    public final void clearOption(String name) {
        this.bool.remove(name);
        this.exists.remove(name);
        this.ints.remove(name);
        this.dbls.remove(name);
        this.str.remove(name);
    }

    public final void setOption(String name, Object value) {
        ConfigUtils.set(this.file, name, value);
        this.clearOption(name);
    }

    protected final void addOption(String name, Object value) {
        ConfigUtils.add(this.file, name, value);
        this.clearOption(name);
    }

    public final String getOldOption(String old, String current) {
        return this.exists(old) ? old : current;
    }

    public void clear() {
        this.internalClear();
    }

    public abstract void create();
}

