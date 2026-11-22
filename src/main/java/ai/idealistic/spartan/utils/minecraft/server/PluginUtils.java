/*
 * Decompiled with CFR 0.152.
 *
 * Could not load the following classes:
 *  org.bukkit.Bukkit
 *  org.bukkit.plugin.Plugin
 *  org.bukkit.plugin.PluginDescriptionFile
 */
package ai.idealistic.spartan.utils.minecraft.server;

import java.util.ArrayList;
import java.util.List;
import org.bukkit.Bukkit;
import org.bukkit.plugin.Plugin;
import org.bukkit.plugin.PluginDescriptionFile;

public class PluginUtils {
    public static List<Plugin> getDependentPlugins(String independent) {
        Plugin[] plugins = Bukkit.getPluginManager().getPlugins();
        ArrayList<Plugin> results = new ArrayList<Plugin>(plugins.length);
for (Plugin p : plugins) {
            if (p.getName().equals(independent)) continue;
            PluginDescriptionFile descriptionFile = p.getDescription();
            ArrayList dependentList = new ArrayList(descriptionFile.getDepend());
            dependentList.addAll(descriptionFile.getSoftDepend());
            if (dependentList.isEmpty()) continue;
            for (String dependent : dependentList) {
                if (!independent.equals(dependent)) continue;
                results.add(p);
                continue block0;
            }
        }
        return results;
    }

    public static boolean contains(String key) {
        String[] split = key.toLowerCase().split(" ");
        for (Plugin p : Bukkit.getPluginManager().getPlugins()) {
            if (!p.isEnabled()) continue;
            String plugin = p.getName().toLowerCase();
            for (String name : split) {
                if (!plugin.contains(name)) continue;
                return true;
            }
        }
        return false;
    }

    public static boolean exists(String name) {
        for (Plugin p : Bukkit.getPluginManager().getPlugins()) {
            if (!p.isEnabled() || !p.getName().equalsIgnoreCase(name)) continue;
            return true;
        }
        return false;
    }

    public static Plugin get(String name) {
        for (Plugin p : Bukkit.getPluginManager().getPlugins()) {
            if (!p.isEnabled() || !p.getName().equalsIgnoreCase(name)) continue;
            return p;
        }
        return null;
    }
}

