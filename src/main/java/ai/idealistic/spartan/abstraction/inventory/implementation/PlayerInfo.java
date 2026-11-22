/*
 * Decompiled with CFR 0.152.
 *
 * Could not load the following classes:
 *  org.bukkit.Bukkit
 *  org.bukkit.Material
 *  org.bukkit.command.CommandSender
 *  org.bukkit.inventory.InventoryView
 *  org.bukkit.inventory.ItemStack
 */
package ai.idealistic.spartan.abstraction.inventory.implementation;

import ai.idealistic.spartan.abstraction.check.Check;
import ai.idealistic.spartan.abstraction.check.CheckCancellation;
import ai.idealistic.spartan.abstraction.check.CheckEnums;
import ai.idealistic.spartan.abstraction.data.Cooldowns;
import ai.idealistic.spartan.abstraction.inventory.InventoryMenu;
import ai.idealistic.spartan.abstraction.profiling.PlayerProfile;
import ai.idealistic.spartan.abstraction.protocol.PlayerProtocol;
import ai.idealistic.spartan.api.Permission;
import ai.idealistic.spartan.compatibility.Compatibility;
import ai.idealistic.spartan.functionality.connection.PluginAddons;
import ai.idealistic.spartan.functionality.moderation.clickable.ClickableMessage;
import ai.idealistic.spartan.functionality.server.Config;
import ai.idealistic.spartan.functionality.server.Permissions;
import ai.idealistic.spartan.functionality.server.PluginBase;
import ai.idealistic.spartan.functionality.tracking.DetectionCharge;
import ai.idealistic.spartan.functionality.tracking.ResearchEngine;
import ai.idealistic.spartan.utils.java.OverflowMap;
import ai.idealistic.spartan.utils.java.TimeUtils;
import ai.idealistic.spartan.utils.minecraft.inventory.Spade;
import java.util.ArrayList;
import java.util.Collection;
import java.util.List;
import java.util.concurrent.ConcurrentHashMap;
import org.bukkit.Bukkit;
import org.bukkit.Material;
import org.bukkit.command.CommandSender;
import org.bukkit.inventory.InventoryView;
import org.bukkit.inventory.ItemStack;

public class PlayerInfo
extends InventoryMenu {
    public static final Permission[] permissions = new Permission[]{Permission.MANAGE, Permission.INFO};
    private static final Cooldowns cooldowns = new Cooldowns(new OverflowMap<String, Long>(new ConcurrentHashMap(), 512));
    private static final String menu = "Player Info: ";
    private static final int[] slots = new int[]{20, 21, 23, 24};

    public PlayerInfo() {
        super(menu, 45, permissions);
    }

    @Override
    public boolean internalOpen(PlayerProtocol protocol, boolean permissionMessage, Object object) {
        PlayerProfile profile;
        PlayerProtocol target = PluginBase.getAnyCaseProtocol(object.toString());
        boolean isOnline = target != null;
        PlayerProfile playerProfile = profile = isOnline ? target.profile() : ResearchEngine.getAnyCasePlayerProfile(object.toString());
        if (profile == null) {
            protocol.bukkit().closeInventory();
            ClickableMessage.sendURL(CommandSenderprotocol.bukkit(), Config.messages.getColorfulString("player_not_found_message"), "Click to receive plugin support.", "https://www.spartan.top/discord");
            return false;
        }
        this.setTitle(protocol, menu + (isOnline ? target.bukkit().getName() : profile.name));
        ArrayList<String> lore = new ArrayList<String>();
        lore.add("");
        if (isOnline) {
            lore.add("\u00a77Version\u00a78:\u00a7c " + target.version.toString());
            lore.add("\u00a77Latency\u00a78:\u00a7c " + target.getPing() + "ms");
            lore.add("\u00a77Edition\u00a78:\u00a7c " + (Object)target.getDataType());
        } else {
            lore.add("\u00a77Last Known Edition\u00a78:\u00a7c " + (Object)profile.getLastDataType());
        }
        long time = profile.getContinuity().getOnlineTime();
        if (time > 0L) {
            lore.add("\u00a77Total Active Time\u00a78:\u00a7c " + TimeUtils.convertMilliseconds(time));
        }
        lore.add("");
        lore.add("\u00a7cClick to delete the player's stored data.");
        this.add("\u00a7c" + profile.name, lore, profile.getSkull(), 4);
        for (CheckEnums.HackCategoryType checkType : CheckEnums.HackCategoryType.values()) {
            this.addChecks(slots[checkType.ordinal()], isOnline, target, profile, lore, checkType);
        }
        lore.clear();
        lore.add("");
        lore.add("\u00a77Click to join our Discord community!");
        this.add("\u00a7aDiscord", lore, new ItemStack(Spade.get("crafting_table")), 39);
        this.add("\u00a7cClose", null, new ItemStack(Material.ARROW), 40);
        List<Compatibility.CompatibilityType> activeCompatibilities = Config.compatibility.getActiveCompatibilities();
        int activeCompatibilitiesSize = activeCompatibilities.size();
        lore.clear();
        lore.add("");
        lore.add("\u00a77Identified\u00a78:\u00a7a " + activeCompatibilitiesSize);
        lore.add("\u00a77Total\u00a78:\u00a7a " + Config.compatibility.getTotalCompatibilities().size());
        if (activeCompatibilitiesSize > 0) {
            lore.add("");
            lore.add("\u00a77Compatibilities\u00a78:");
            for (Compatibility.CompatibilityType compatibility : activeCompatibilities) {
                lore.add("\u00a7a" + compatibility.toString());
            }
        }
        this.add("\u00a7aCompatibilities", lore, new ItemStack(Spade.get("enchanting_table")), 41);
        return true;
    }

    private void addChecks(int slot, boolean isOnline, PlayerProtocol protocol, PlayerProfile profile, List<String> lore, CheckEnums.HackCategoryType checkType) {
        lore.clear();
        ItemStack item = new ItemStack(checkType.material);
        boolean added = false;
        for (CheckEnums.HackType hackType : CheckEnums.HackType.values()) {
            if (hackType.category != checkType) continue;
            String state = this.getDetectionNotification(protocol, hackType, profile.getLastDataType(), isOnline);
            if (state != null) {
                if (!added) {
                    added = true;
                    lore.add("");
                }
                lore.add("\u00a77" + hackType.getCheck().getName() + "\u00a78:\u00a7f " + state);
                continue;
            }
            List<String> evidence = protocol.getRunner(hackType).getEvidence();
            if (evidence.isEmpty()) continue;
            if (!added) {
                added = true;
                lore.add("");
            }
            lore.add("\u00a74" + hackType.getCheck().getName() + "\u00a78: \u00a7c" + evidence.size() + (evidence.size() == 1 ? " suspicion" : " suspicions"));
        }
        if (!added) {
            lore.add("");
            lore.add("\u00a77No useful information currently available.");
        }
        this.add("\u00a72" + (Object)checkType + " checks", lore, item, slot);
    }

    private String getDetectionNotification(PlayerProtocol protocol, CheckEnums.HackType hackType, Check.DataType dataType, boolean hasPlayer) {
        if (!hasPlayer) {
            return "Player is offline";
        }
        if (!PluginAddons.ownsCheck(hackType)) {
            return "Check is not purchased";
        }
        if (!PluginAddons.ownsEdition(dataType)) {
            return (Object)dataType + " edition is not purchased";
        }
        if (!DetectionCharge.has()) {
            return "detections need to be charged, run /spartan charge";
        }
        String worldName = protocol.getWorld().getName();
        Check check = hackType.getCheck();
        if (!check.handleCheck(dataType, worldName)) {
            return "Check is disabled";
        }
        CheckCancellation disabledCause = protocol.getRunner(hackType).getDisableCause();
        return Permissions.isBypassing(protocol.bukkit(), hackType) ? "Player has permission bypass" : (disabledCause != null ? "Custom: " + disabledCause.getS() : (DetectionCharge.isLow() ? "detections are low on charge, run /spartan charge" : null));
    }

    public void refresh(String targetName) {
        Collection<PlayerProtocol> protocols = PluginBase.getProtocols();
        if (!protocols.isEmpty()) {
            for (PlayerProtocol protocol : protocols) {
                InventoryView inventoryView = protocol.bukkit().getOpenInventory();
                if (!inventoryView.getTitle().equals(menu + targetName) || !cooldowns.canDo("player-info=" + protocol.getUUID())) continue;
                cooldowns.add("player-info=" + protocol.getUUID(), 1);
                PluginBase.playerInfo.open(protocol, targetName);
            }
        }
    }

    @Override
    public boolean internalHandle(PlayerProtocol protocol) {
        String playerName;
        String item = this.itemStack.getItemMeta().getDisplayName();
        item = item.startsWith("\u00a7") ? item.substring(2) : item;
        if (item.equalsIgnoreCase(playerName = this.title.substring(menu.length()))) {
            if (!Permissions.has(protocol.bukkit(), Permission.MANAGE)) {
                protocol.bukkit().closeInventory();
                ClickableMessage.sendURL(CommandSenderprotocol.bukkit(), Config.messages.getColorfulString("no_permission"), "Click to receive plugin support.", "https://www.spartan.top/discord");
            } else {
                String name = Bukkit.getOfflinePlayer((String)playerName).getName();
                if (name == null) {
                    ClickableMessage.sendURL(CommandSenderprotocol.bukkit(), Config.messages.getColorfulString("player_not_found_message"), "Click to receive plugin support.", "https://www.spartan.top/discord");
                } else {
                    ResearchEngine.resetData(name);
                    protocol.bukkit().sendMessage(Config.messages.getColorfulString("player_stored_data_delete_message").replace("{player}", name));
                }
                protocol.bukkit().closeInventory();
            }
        } else if (item.equals("Close")) {
            protocol.bukkit().closeInventory();
        } else if (item.equals("Discord")) {
            protocol.sendImportantMessage("\u00a72\u00a7lhttps://www.spartan.top/discord");
            protocol.bukkit().closeInventory();
        } else if (PluginAddons.freeEdition) {
            protocol.sendImportantMessage("\u00a72\u00a7lhttps://spartan.top");
            protocol.bukkit().closeInventory();
        }
        return true;
    }
}

