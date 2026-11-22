/*
 * Decompiled with CFR 0.152.
 *
 * Could not load the following classes:
 *  org.bukkit.Material
 *  org.bukkit.command.CommandSender
 *  org.bukkit.inventory.ItemStack
 */
package ai.idealistic.spartan.abstraction.inventory.implementation;

import ai.idealistic.spartan.abstraction.check.Check;
import ai.idealistic.spartan.abstraction.check.CheckDetection;
import ai.idealistic.spartan.abstraction.check.CheckEnums;
import ai.idealistic.spartan.abstraction.check.CheckRunner;
import ai.idealistic.spartan.abstraction.inventory.InventoryMenu;
import ai.idealistic.spartan.abstraction.profiling.PlayerProfile;
import ai.idealistic.spartan.abstraction.protocol.PlayerProtocol;
import ai.idealistic.spartan.api.Permission;
import ai.idealistic.spartan.functionality.connection.PluginAddons;
import ai.idealistic.spartan.functionality.moderation.clickable.ClickableMessage;
import ai.idealistic.spartan.functionality.server.PluginBase;
import ai.idealistic.spartan.functionality.tracking.ResearchEngine;
import ai.idealistic.spartan.utils.java.TimeUtils;
import ai.idealistic.spartan.utils.math.Distance;
import java.util.ArrayList;
import java.util.Collection;
import java.util.HashMap;
import org.bukkit.Material;
import org.bukkit.command.CommandSender;
import org.bukkit.inventory.ItemStack;

public class Title
extends InventoryMenu {
    public static final String title = "Plugin Statistics";

    public Title() {
        super(title, 54, new Permission[]{Permission.MANAGE, Permission.INFO});
    }

    @Override
    public boolean internalOpen(PlayerProtocol protocol, boolean permissionMessage, Object object) {
        if (!PluginAddons.isSyn()) {
            protocol.bukkit().closeInventory();
            String missing = "\u00a7aClick to visit https://spartan.top/syn.".replace("{feature}", title);
            ClickableMessage.sendURL(CommandSenderprotocol.bukkit(), missing, missing + " (https://spartan.top/syn)", "https://spartan.top/syn");
            return false;
        }
        ArrayList<String> list = new ArrayList<String>();
        int startingLine = 1;
        Collection<PlayerProfile> profiles = ResearchEngine.getPlayerProfiles();
        int java = 0;
        long totalOnlineTime = 0L;
        long averageOnlineTime = 0L;
        long violationsFired = 0L;
        HashMap<String, Long> detectionViolationTimes = new HashMap<String, Long>();
        if (!profiles.isEmpty()) {
            for (PlayerProfile profile : profiles) {
                if (profile.getLastDataType() == Check.DataType.JAVA) {
                    ++java;
                }
                totalOnlineTime += profile.getContinuity().getOnlineTime();
                averageOnlineTime += profile.getContinuity().getAverageOnlineTime();
                for (CheckRunner runner : protocol.getRunners()) {
                    for (CheckDetection detection : runner.getDetections()) {
                        int amount = profile.getTimeDifferences(runner.hackType, profile.getLastDataType(), detection.name).size();
                        violationsFired += (long)amount;
                        detectionViolationTimes.put(detection.name, detectionViolationTimes.getOrDefault(detection.name, 0L) + (long)amount + (long)profile.getTimeDifferences(runner.hackType, profile.getOppositeDataType(), detection.name).size());
                    }
                }
            }
        }
        averageOnlineTime /= (long)profiles.size();
        for (CheckEnums.HackCategoryType categoryType : CheckEnums.HackCategoryType.values()) {
            int slot = startingLine * 11;
            for (CheckEnums.HackType hackType : CheckEnums.HackType.values()) {
                if (hackType.category != categoryType) continue;
                int count = 0;
                list.clear();
                list.add("\u00a76" + hackType.category.toString());
                for (CheckDetection detection : protocol.getRunner(hackType).getDetections()) {
                    long fired = detectionViolationTimes.getOrDefault(detection.name, 0L);
                    list.add("");
                    String name = detection.name;
                    name = Distance.validInteger(name) ? "default" : name.toLowerCase().replace("_", "-");
                    list.add("\u00a77" + name + "\u00a78:");
                    list.add("\u00a7aViolations Fired\u00a78: \u00a7f" + fired);
                    list.add("\u00a7eViolations Fired From Total\u00a78: \u00a7f" + Distance.computeAlgebra((double)fired / (double)violationsFired * 100.0, 2.0) + "%");
                    for (Check.DataType dataType : Check.DataType.values()) {
                        double average = ResearchEngine.getAverageViolationTime(detection, dataType);
                        list.add("\u00a7cAverage " + (Object)dataType + " Violation Ms\u00a78: \u00a7f" + (long)AlgebraUtils.computeAlgebra(average, 2.0));
                    }
                    if (++count != 4) continue;
                }
                this.add("\u00a73" + hackType.getCheck().getName(), list, new ItemStack(hackType.material), slot);
                ++slot;
            }
            ++startingLine;
        }
        list.clear();
        list.add("");
        list.add("\u00a77Total Player Profiles\u00a78: \u00a7f" + profiles.size());
        list.add("\u00a77Java Player Profiles\u00a78: \u00a7f" + java);
        list.add("\u00a77Bedrock Player Profiles\u00a78: \u00a7f" + (profiles.size() - java));
        list.add("\u00a77Total Player Online Time\u00a78: \u00a7f" + TimeUtils.convertMilliseconds(totalOnlineTime));
        list.add("\u00a77Average Player Online Time\u00a78: \u00a7f" + TimeUtils.convertMilliseconds(averageOnlineTime));
        list.add("\u00a77Total Violations Fired\u00a78: \u00a7f" + violationsFired);
        this.add("\u00a74General Statistics", list, new ItemStack(Material.ENDER_CHEST), 4);
        this.add("\u00a74Back", null, new ItemStack(Material.ARROW), 49);
        return true;
    }

    @Override
    public boolean internalHandle(PlayerProtocol protocol) {
        String item = this.itemStack.getItemMeta().getDisplayName();
        if (item.equals("\u00a74Back")) {
            PluginBase.synMenu.open(protocol, protocol.bukkit().getName());
        }
        return true;
    }
}

