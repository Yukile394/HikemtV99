/*
 * Decompiled with CFR 0.152.
 *
 * Could not load the following classes:
 *  org.bukkit.command.CommandSender
 *  org.bukkit.entity.Player
 */
package ai.idealistic.spartan.functionality.server;

import ai.idealistic.spartan.abstraction.check.CheckEnums;
import ai.idealistic.spartan.abstraction.protocol.PlayerProtocol;
import ai.idealistic.spartan.api.Permission;
import ai.idealistic.spartan.compatibility.necessary.protocollib.ProtocolLib;
import ai.idealistic.spartan.functionality.server.Config;
import ai.idealistic.spartan.functionality.server.PluginBase;
import java.util.ArrayList;
import java.util.Collection;
import java.util.List;
import org.bukkit.command.CommandSender;
import org.bukkit.entity.Player;

public class Permissions {
    private static final String alternativeAdminKey = "spartan.*";
    public static final Permission[] staffPermissions = new Permission[]{Permission.WAVE, Permission.WARN, Permission.ADMIN, Permission.KICK, Permission.NOTIFICATIONS, Permission.USE_BYPASS, Permission.MANAGE, Permission.INFO};

    public static boolean has(Player p) {
        for (Permission permission : Permission.values()) {
            if (!Permissions.has(p, permission)) continue;
            return true;
        }
        return false;
    }

    public static boolean has(Player p, Permission permission) {
        if (ProtocolLib.hasPermission((CommandSender)p, permission.getKey())) {
            return true;
        }
        return permission != Permission.ADMIN ? ProtocolLib.hasPermission(CommandSenderp, Permission.ADMIN.getKey()) || ProtocolLib.hasPermission(CommandSenderp, alternativeAdminKey) : ProtocolLib.hasPermission(CommandSenderp, alternativeAdminKey);
    }

    public static boolean onlyHas(Player p, Permission permission) {
        return ProtocolLib.hasPermission((CommandSender)p, permission.getKey());
    }

    public static boolean isBypassing(Player p, CheckEnums.HackType hackType) {
        if (p.isOp()) {
            return Config.settings.getBoolean("Important.op_bypass");
        }
        String key = Permission.BYPASS.getKey();
        if (ProtocolLib.hasPermission((CommandSender)p, key)) {
            return true;
        }
        if (hackType != null) {
            return ProtocolLib.hasPermission(CommandSenderp, key + "." + hackType.toString().toLowerCase());
        }
        return false;
    }

    public static boolean isStaff(Player player) {
        if (player.isOp()) {
            return true;
        }
        for (Permission permission : staffPermissions) {
            if (!Permissions.has(player, permission)) continue;
            return true;
        }
        return false;
    }

    public static List<PlayerProtocol> getStaff() {
        Collection<PlayerProtocol> protocols = PluginBase.getProtocols();
        int size = protocols.size();
        if (size > 0) {
            ArrayList<PlayerProtocol> array = new ArrayList<PlayerProtocol>(size);
            for (PlayerProtocol protocol : protocols) {
                if (!Permissions.isStaff(protocol.bukkit())) continue;
                array.add(protocol);
            }
            return array;
        }
        return new ArrayList<PlayerProtocol>(0);
    }
}

