/*
 * Decompiled with CFR 0.152.
 *
 * Could not load the following classes:
 *  org.bukkit.ChatColor
 */
package ai.idealistic.spartan.functionality.connection;

import ai.idealistic.spartan.Register;
import ai.idealistic.spartan.abstraction.check.Check;
import ai.idealistic.spartan.functionality.connection.CloudBase;
import ai.idealistic.spartan.functionality.connection.User;
import ai.idealistic.spartan.functionality.moderation.CrossServerNotifications;
import ai.idealistic.spartan.functionality.server.Config;
import ai.idealistic.spartan.functionality.server.PluginBase;
import ai.idealistic.spartan.utils.java.RequestUtils;
import ai.idealistic.spartan.utils.java.StringUtils;
import ai.idealistic.spartan.utils.math.Distance;
import java.net.URLEncoder;
import java.util.UUID;
import org.bukkit.ChatColor;

public class CloudConnections {
    static int getUserIdentification() {
        try {
            String line;
            String[] reply = RequestUtils.get("https://www.vagdedes.com/minecraft/cloud/?action=get&data=userIdentification&version=" + Register.plugin.getDescription().getVersion(), 30000);
            if (reply.length > 0 && Distance.validInteger(line = reply[0])) {
                int id = Integer.parseInt(line);
                User.set(id, id);
                return id;
            }
        }
        catch (Exception e) {
            CloudBase.throwError(e, "userIdentification:GET");
            return 0;
        }
        return -1;
    }

    static String[][] getStaffAnnouncements() {
        try {
            String[] results = RequestUtils.get("https://www.vagdedes.com/minecraft/cloud/?" + CloudBase.identification() + "&action=get&data=staffAnnouncements&version=" + Register.plugin.getDescription().getVersion());
            if (results.length > 0) {
                String[] announcements = results[0].split(">@#&!%<;=");
                String[][] array = new String[results.length][0];
                for (int i = 0; i < announcements.length; ++i) {
                    array[i] = StringUtils.decodeBase64(announcements[i]).split(">@#&!%<;=");
                }
                return array;
            }
        }
        catch (Exception e) {
            CloudBase.throwError(e, "staffAnnouncements:GET");
        }
        return new String[0][];
    }

    static String[] getOwnedEditions() {
        try {
            String[] results = RequestUtils.get(StringUtils.decodeBase64("aHR0cHM6Ly93d3cudmFnZGVkZXMuY29tL21pbmVjcmFmdC9jbG91ZC8=") + "?" + CloudBase.identification() + "&action=get&data=ownedEditions&email_address=" + URLEncoder.encode(Config.settings.getString("Purchases.email_address"), "UTF-8") + "&patreon_full_name=" + URLEncoder.encode(Config.settings.getString("Purchases.patreon_full_name"), "UTF-8") + "&version=" + Register.plugin.getDescription().getVersion());
            if (results.length > 0) {
                return results[0].split(">@#&!%<;=", Check.DataType.values().length);
            }
        }
        catch (Exception e) {
            CloudBase.throwError(e, "ownedEditions:GET");
        }
        if (User.enabled) {
            return new String[]{Check.DataType.JAVA.toString(), Check.DataType.BEDROCK.toString()};
        }
        return null;
    }

    public static void executeDiscordWebhook(String webhook, UUID uuid, String name, int x, int y, int z, String type, String information) {
        String color;
        int length;
        String url = Config.settings.getString("Discord." + webhook + "_webhook_url");
        if ((url.startsWith("https://") || url.startsWith("http://")) && (length = (color = Config.settings.getString("Discord.webhook_hex_color")).length()) >= 3 && length <= 6) {
            PluginBase.connectionThread.executeIfUnknownThreadElseHere(() -> {
                try {
                    int webhookVersion = 2;
                    String crossServerInformationOption = CrossServerNotifications.getServerName();
                    RequestUtils.get("https://www.vagdedes.com/minecraft/cloud/?" + CloudBase.identification() + "&action=add&data=discordWebhooks&version=" + Register.plugin.getDescription().getVersion() + "&value=" + URLEncoder.encode(webhookVersion + ">@#&!%<;=" + url + ">@#&!%<;=" + color + ">@#&!%<;=" + (!crossServerInformationOption.isEmpty() ? crossServerInformationOption : "NULL") + ">@#&!%<;=" + name + ">@#&!%<;=" + uuid + ">@#&!%<;=" + x + ">@#&!%<;=" + y + ">@#&!%<;=" + z + ">@#&!%<;=" + ChatColor.stripColor((String)type) + ">@#&!%<;=" + ChatColor.stripColor((String)information), "UTF-8"));
                }
                catch (Exception e) {
                    CloudBase.throwError(e, "discordWebhooks:ADD");
                }
            });
        }
    }
}

