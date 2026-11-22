/*
 * Decompiled with CFR 0.152.
 *
 * Could not load the following classes:
 *  org.bukkit.OfflinePlayer
 *  org.bukkit.entity.Player
 */
package ai.idealistic.spartan.compatibility.necessary;

import ai.idealistic.spartan.api.Permission;
import ai.idealistic.spartan.compatibility.necessary.Floodgate;
import ai.idealistic.spartan.compatibility.necessary.ProtocolSupport;
import ai.idealistic.spartan.compatibility.necessary.protocollib.ProtocolLib;
import ai.idealistic.spartan.functionality.server.Config;
import ai.idealistic.spartan.functionality.server.Permissions;
import org.bukkit.OfflinePlayer;
import org.bukkit.entity.Player;

public class BedrockCompatibility {
    public static boolean isPlayer(Player p) {
        return ProtocolSupport.isBedrockPlayer(p) || !ProtocolLib.isTemporary((OfflinePlayer)p) && Floodgate.isBedrockPlayer(p.getUniqueId(), p.getName()) || Config.settings.getBoolean("Important.bedrock_client_permission") && Permissions.onlyHas(p, Permission.BEDROCK);
    }
}

