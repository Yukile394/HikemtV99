/*
 * Decompiled with CFR 0.152.
 *
 * Could not load the following classes:
 *  com.comphenix.protocol.ProtocolLibrary
 *  com.viaversion.viaversion.api.Via
 *  org.bukkit.Bukkit
 *  org.bukkit.OfflinePlayer
 *  org.bukkit.entity.Player
 */
package ai.idealistic.spartan.functionality.server;

import ai.idealistic.spartan.compatibility.necessary.protocollib.ProtocolLib;
import ai.idealistic.spartan.functionality.server.PluginBase;
import ai.idealistic.spartan.utils.java.ReflectionUtils;
import ai.idealistic.spartan.utils.minecraft.server.PluginUtils;
import com.comphenix.protocol.ProtocolLibrary;
import com.viaversion.viaversion.api.Via;
import org.bukkit.Bukkit;
import org.bukkit.OfflinePlayer;
import org.bukkit.entity.Player;

public class MultiVersion {
    private static final boolean paperClass = ReflectionUtils.classExists("com.destroystokyo.paper.network.NetworkClient");
    public static final MCVersion serverVersion = MultiVersion.judge();
    public static final boolean folia = MultiVersion.folia();

    public static boolean isOrGreater(MCVersion trialVersion) {
        return serverVersion.ordinal() >= trialVersion.ordinal();
    }

    public static MCVersion get(Player player) {
        int protocol = ProtocolLib.isTemporary((OfflinePlayer)player) ? -1 : (PluginUtils.exists("viaversion") ? Via.getAPI().getPlayerVersion(player) : (paperClass ? player.getProtocolVersion() : (PluginBase.packetsEnabled() ? ProtocolLibrary.getProtocolManager().getProtocolVersion(player) : -1)));
        if (protocol >= 0) {
            for (MCVersion version : MCVersion.values()) {
                if (protocol > version.maxProtocol) continue;
                return version;
            }
        }
        return MCVersion.OTHER;
    }

    private static MCVersion judge() {
        String version = Bukkit.getVersion();
        version = version.substring(0, version.length() - 1);
        try {
            for (int i = 0; i <= version.length(); ++i) {
                if (!version.substring(i).startsWith("(MC: ")) continue;
                version = version.substring(i + 5);
                for (int x = 0; x <= version.length(); ++x) {
                    for (MCVersion mcversion : MCVersion.values()) {
                        if (!mcversion.defaultString().equalsIgnoreCase("V" + version.substring(0, version.length() - x).replace(".", "_"))) continue;
                        return mcversion;
                    }
                }
            }
        }
        catch (Exception ex) {
            ex.printStackTrace();
        }
        return MCVersion.OTHER;
    }

    private static boolean folia() {
        try {
            return io.papermc.paper.threadedregions.RegionizedServer.class != null;
        }
        catch (Exception e) {
            return false;
        }
    }

    public static enum MCVersion {
        V1_7(5),
        V1_8(47),
        V1_9(110),
        V1_10(210),
        V1_11(316),
        V1_12(340),
        V1_13(404),
        V1_14(498),
        V1_15(578),
        V1_16(754),
        V1_17(756),
        V1_18(758),
        V1_19(762),
        V1_20(766),
        V1_21(Integer.MAX_VALUE),
        OTHER(-1);

        public final int maxProtocol;

        private MCVersion(int maxProtocol) {
        }

        public String toString() {
            return this.maxProtocol == -1 ? "Unknown" : this.defaultString().substring(1).replace("_", ".");
        }

        private String defaultString() {
            return super.toString();
        }
    }
}

