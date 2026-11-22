/*
 * Decompiled with CFR 0.152.
 *
 * Could not load the following classes:
 *  org.bukkit.Material
 *  org.bukkit.configuration.file.YamlConfiguration
 */
package ai.idealistic.spartan.compatibility.manual.building;

import ai.idealistic.spartan.abstraction.protocol.PlayerProtocol;
import ai.idealistic.spartan.compatibility.Compatibility;
import java.io.File;
import java.util.HashSet;
import java.util.Set;
import org.bukkit.Material;
import org.bukkit.configuration.file.YamlConfiguration;

public class MineBomb {
    private static final Set<Material> hs = new HashSet<Material>();

    public static void reload() {
        hs.clear();
    }

    public static boolean isUsing(PlayerProtocol p) {
        if (Compatibility.CompatibilityType.MINE_BOMB.isFunctional()) {
            if (MineBomb.cacheCanUse(p)) {
                return true;
            }
            File file = new File("plugins/MineBomb/config.yml");
            if (file.exists()) {
                boolean result = false;
                YamlConfiguration filea = YamlConfiguration.loadConfiguration((File)file);
                for (String key : filea.getKeys(true)) {
                    Material m;
                    if (!key.endsWith(".item") || (m = Material.getMaterial((String)filea.getString(key))) == null) continue;
                    hs.add(m);
                    result = true;
                }
                return result;
            }
        }
        return false;
    }

    private static boolean cacheCanUse(PlayerProtocol p) {
        for (Material m : hs) {
            if (p.getItemInHand().getType() != m) continue;
            return true;
        }
        return false;
    }
}

