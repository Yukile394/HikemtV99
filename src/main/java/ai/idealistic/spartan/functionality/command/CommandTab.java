/*
 * Decompiled with CFR 0.152.
 *
 * Could not load the following classes:
 *  org.bukkit.command.Command
 *  org.bukkit.command.CommandSender
 *  org.bukkit.command.TabCompleter
 *  org.bukkit.entity.Player
 */
package ai.idealistic.spartan.functionality.command;

import ai.idealistic.spartan.abstraction.inventory.implementation.PlayerInfo;
import ai.idealistic.spartan.abstraction.inventory.implementation.SynMenu;
import ai.idealistic.spartan.abstraction.protocol.PlayerProtocol;
import ai.idealistic.spartan.api.Permission;
import ai.idealistic.spartan.functionality.server.Permissions;
import ai.idealistic.spartan.functionality.server.PluginBase;
import java.util.ArrayList;
import java.util.Collection;
import java.util.LinkedHashMap;
import java.util.List;
import java.util.Map;
import org.bukkit.command.Command;
import org.bukkit.command.CommandSender;
import org.bukkit.command.TabCompleter;
import org.bukkit.entity.Player;

public class CommandTab
implements TabCompleter {
    private static final Map<String, Permission[]> commands = new LinkedHashMap<String, Permission[]>18;

    public List<String> onTabComplete(CommandSender sender, Command command, String arg, String[] args) {
        ArrayList<String> list;
        block9: {
            Collection<PlayerProtocol> protocols;
            int length;
            block8: {
                list = new ArrayList<String>();
                length = args.length;
                if (length != 1) break block8;
                boolean isPlayer = sender instanceof Player;
                Player p = isPlayer ? (Player)sender : null;
                String argAbstract = args[0].toLowerCase();
                for (Map.Entry<String, Permission[]> entry : commands.entrySet()) {
                    String key;
                    boolean add;
                    if (!isPlayer) {
                        add = true;
                    } else {
                        add = false;
                        if (entry.getValue().length > 0) {
                            for (Permission permission : entry.getValue()) {
                                if (!Permissions.has(p, permission)) continue;
                                add = true;
                                break;
                            }
                        } else {
                            add = Permissions.has(p, Permission.ADMIN);
                        }
                    }
                    if (!add || !(key = entry.getKey()).contains(argAbstract)) continue;
                    list.add(key);
                }
                break block9;
            }
            if (length <= 1 || (protocols = PluginBase.getProtocols()).isEmpty()) break block9;
            String argAbstract = args[length - 1].toLowerCase();
            boolean player = sender instanceof Player;
            PlayerProtocol p = player ? PluginBase.getProtocol((Player)sender) : null;
            player &= p != null;
            for (PlayerProtocol protocol : protocols) {
                String name;
                if (player && !p.bukkit().canSee(protocol.bukkit()) || !(name = protocol.bukkit().getName()).toLowerCase().contains(argAbstract)) continue;
                list.add(name);
            }
        }
        return list;
    }

    static {
        commands.put("info", PlayerInfo.permissions);
        commands.put("syn", SynMenu.permissions);
        commands.put("charge", Permissions.staffPermissions);
        commands.put("panic", new Permission[]{Permission.MANAGE});
        commands.put("toggle", new Permission[]{Permission.MANAGE});
        commands.put("rl", new Permission[]{Permission.RELOAD});
        commands.put("reload", new Permission[]{Permission.RELOAD});
        commands.put("notifications", new Permission[]{Permission.NOTIFICATIONS});
        commands.put("verbose", new Permission[]{Permission.NOTIFICATIONS});
        commands.put("kick", new Permission[]{Permission.KICK});
        commands.put("warn", new Permission[]{Permission.WARN});
        commands.put("bypass", new Permission[]{Permission.USE_BYPASS});
        commands.put("conditions", new Permission[]{Permission.CONDITION});
        commands.put("moderation", new Permission[]{Permission.KICK, Permission.WARN, Permission.USE_BYPASS, Permission.WAVE});
        commands.put("proxy-command", new Permission[0]);
        commands.put("wave add", new Permission[]{Permission.WAVE});
        commands.put("wave remove", new Permission[]{Permission.WAVE});
        commands.put("wave clear", new Permission[]{Permission.WAVE});
        commands.put("wave run", new Permission[]{Permission.WAVE});
        commands.put("wave list", new Permission[]{Permission.WAVE});
    }
}

