/*
 * Decompiled with CFR 0.152.
 *
 * Could not load the following classes:
 *  org.bukkit.Bukkit
 *  org.bukkit.OfflinePlayer
 *  org.bukkit.entity.Player
 */
package ai.idealistic.spartan.utils.minecraft.server;

import ai.idealistic.spartan.Register;
import ai.idealistic.spartan.abstraction.protocol.PlayerProtocol;
import ai.idealistic.spartan.compatibility.necessary.protocollib.ProtocolLib;
import ai.idealistic.spartan.functionality.moderation.AwarenessNotifications;
import ai.idealistic.spartan.functionality.server.Permissions;
import ai.idealistic.spartan.functionality.server.PluginBase;
import java.io.ByteArrayOutputStream;
import java.io.DataOutputStream;
import java.util.Iterator;
import java.util.Map;
import java.util.Set;
import java.util.UUID;
import org.bukkit.Bukkit;
import org.bukkit.OfflinePlayer;
import org.bukkit.entity.Player;

public class ProxyUtils {
    private static final String name = "BungeeCord";

    public static void register() {
        if (!Bukkit.getOnlineMode()) {
            Register.plugin.getServer().getMessenger().registerOutgoingPluginChannel(Register.plugin, name);
        }
    }

    public static void unregister() {
        Register.plugin.getServer().getMessenger().unregisterIncomingPluginChannel(Register.plugin);
    }

    public static boolean executeCommand(Player player, String command) {
        if (!Bukkit.getOnlineMode() && !ProtocolLib.isTemporary((OfflinePlayer)player)) {
            boolean isNull;
            isNull = player == null;
            if (isNull || !player.isOp()) {
                Set<Map.Entry<UUID, PlayerProtocol>> entries = PluginBase.getPlayerEntries();
                if (!isNull) {
                    Iterator<Map.Entry<UUID, PlayerProtocol>> iterator = entries.iterator();
                    while (iterator.hasNext()) {
                        UUID uuid = iterator.next().getKey();
                        if (!player.getUniqueId().equals(uuid)) continue;
                        iterator.remove();
                        break;
                    }
                }
                if (!entries.isEmpty()) {
                    for (Map.Entry<UUID, PlayerProtocol> entry : entries) {
                        PlayerProtocol protocol = entry.getValue();
                        if (protocol.bukkit().isOp()) {
                            player = protocol.bukkit();
                            break;
                        }
                        if (!Permissions.isStaff(protocol.bukkit())) continue;
                        player = protocol.bukkit();
                    }
                }
            }
            if (player != null) {
                ByteArrayOutputStream b = new ByteArrayOutputStream();
                DataOutputStream out = new DataOutputStream(b);
                try {
                    out.writeUTF("Message");
                    out.writeUTF("ALL");
                    out.writeUTF("/" + command);
                    player.sendPluginMessage(Register.plugin, name, b.toByteArray());
                    return true;
                }
                catch (Exception ex) {
                    AwarenessNotifications.optionallySend("BungeeCord Command Failed: \nPlayer: " + player.getName() + "\nCommand: " + command);
                    ex.printStackTrace();
                }
            } else {
                AwarenessNotifications.optionallySend("BungeeCord Command Failed: \nPlayer: NULL\nCommand: " + command);
            }
        } else {
            AwarenessNotifications.optionallySend("BungeeCord Command Failed: \nServer: Not-Proxy-Supported\nCommand: " + command);
        }
        return false;
    }
}

