/*
 * Decompiled with CFR 0.152.
 *
 * Could not load the following classes:
 *  org.bukkit.configuration.file.YamlConfiguration
 */
package ai.idealistic.spartan.compatibility.manual.entity;

import ai.idealistic.spartan.compatibility.Compatibility;
import java.io.File;
import org.bukkit.configuration.file.YamlConfiguration;

public class CraftBook {
    private static double limit = -1.0;

    public static void resetBoatLimit() {
        limit = -1.0;
    }

    public static double getBoatLimit() {
        File file;
        if (limit != -1.0) {
            return limit;
        }
        if (Compatibility.CompatibilityType.CRAFT_BOOK.isFunctional() && (file = new File("plugins/CraftBook/mechanisms.yml")).exists()) {
            YamlConfiguration filea = YamlConfiguration.loadConfiguration((File)file);
            double value = filea.getDouble("mechanics.BoatSpeedModifiers.max-speed");
            value = value < 0.0 ? 0.1 : (value += 0.1);
            limit = value;
            return value;
        }
        limit = 0.0;
        return 0.0;
    }
}

