/*
 * Decompiled with CFR 0.152.
 *
 * Could not load the following classes:
 *  com.comphenix.protocol.PacketType
 *  com.comphenix.protocol.PacketType$Play$Client
 *  com.comphenix.protocol.events.ListenerPriority
 *  com.comphenix.protocol.events.PacketAdapter
 *  com.comphenix.protocol.events.PacketContainer
 *  com.comphenix.protocol.events.PacketEvent
 *  com.comphenix.protocol.wrappers.BlockPosition
 *  com.comphenix.protocol.wrappers.EnumWrappers$Hand
 *  com.comphenix.protocol.wrappers.MovingObjectPositionBlock
 *  org.bukkit.GameMode
 *  org.bukkit.entity.Player
 *  org.bukkit.inventory.ItemStack
 */
package ai.idealistic.spartan.listeners.protocol;

import ai.idealistic.spartan.Register;
import ai.idealistic.spartan.abstraction.protocol.PlayerProtocol;
import ai.idealistic.spartan.compatibility.necessary.protocollib.BackgroundProtocolLib;
import ai.idealistic.spartan.functionality.concurrent.CheckThread;
import ai.idealistic.spartan.functionality.server.Config;
import ai.idealistic.spartan.functionality.server.MultiVersion;
import ai.idealistic.spartan.functionality.server.PluginBase;
import com.comphenix.protocol.PacketType;
import com.comphenix.protocol.events.ListenerPriority;
import com.comphenix.protocol.events.PacketAdapter;
import com.comphenix.protocol.events.PacketContainer;
import com.comphenix.protocol.events.PacketEvent;
import com.comphenix.protocol.wrappers.BlockPosition;
import com.comphenix.protocol.wrappers.EnumWrappers;
import com.comphenix.protocol.wrappers.MovingObjectPositionBlock;
import org.bukkit.GameMode;
import org.bukkit.entity.Player;
import org.bukkit.inventory.ItemStack;

public class UseItemStatusHandle
extends PacketAdapter {
    public UseItemStatusHandle() {
        super(Register.plugin, ListenerPriority.NORMAL, UseItemStatusHandle.resolvePacketTypes());
    }

    public void onPacketReceiving(PacketEvent event) {
        Player player = event.getPlayer();
        PlayerProtocol protocol = PluginBase.getProtocol(player);
        if (!Config.settings.getBoolean("Important.bedrock_on_protocollib") && protocol.isBedrockPlayer()) {
            return;
        }
        PacketContainer packet = event.getPacket();
        CheckThread.run(() -> {
            if (packet.getType().equals(PacketType.Play.Client.BLOCK_DIG)) {
                protocol.useItemPacket = false;
            } else {
                ItemStack itemStack;
                boolean isMainHand;
                BlockPosition blockPosition = new BlockPosition(0, 0, 0);
                if (packet.getHands().getValues().isEmpty()) {
                    if (!packet.getMovingBlockPositions().getValues().isEmpty()) {
                        blockPosition = ((MovingObjectPositionBlock)packet.getMovingBlockPositions().read(0)).getBlockPosition();
                    }
                    if (!packet.getBlockPositionModifier().getValues().isEmpty()) {
                        blockPosition = (BlockPosition)packet.getBlockPositionModifier().read(0);
                    }
                    if (blockPosition.getY() != -1) {
                        return;
                    }
                }
                isMainHand = !MultiVersion.isOrGreater(MultiVersion.MCVersion.V1_9) || event.getPacket().getHands().read(0) == EnumWrappers.Hand.MAIN_HAND;
                ItemStack itemStack2 = MultiVersion.isOrGreater(MultiVersion.MCVersion.V1_9) ? (isMainHand ? protocol.getInventory().getItemInMainHand() : protocol.getInventory().getItemInOffHand()) : (itemStack = player.getItemInHand());
                if (packet.getType().toString().contains("USE_ITEM_ON") && event.getPacket().getStructures().getValues().toString().contains("Serverbound")) {
                    return;
                }
                if (itemStack.getType().toString().contains("SHIELD") || itemStack.getType().isEdible() && (player.getFoodLevel() != 20 || itemStack.getType().toString().contains("GOLDEN_APPLE")) && !protocol.getGameMode().equals(GameMode.CREATIVE)) {
                    protocol.useItemPacket = true;
                    protocol.useItemPacketReset = !itemStack.getType().toString().contains("SHIELD") && !itemStack.getType().toString().contains("GOLDEN_APPLE");
                }
            }
        });
    }

    private static PacketType[] resolvePacketTypes() {
        if (MultiVersion.isOrGreater(MultiVersion.MCVersion.V1_17)) {
            return new PacketType[]{PacketType.Play.Client.USE_ITEM, PacketType.Play.Client.BLOCK_DIG};
        }
        if (BackgroundProtocolLib.isPacketSupported("USE_ITEM_ON")) {
            return new PacketType[]{PacketType.Play.Client.USE_ITEM_ON, PacketType.Play.Client.BLOCK_PLACE, PacketType.Play.Client.BLOCK_DIG};
        }
        return new PacketType[]{PacketType.Play.Client.BLOCK_PLACE, PacketType.Play.Client.BLOCK_DIG};
    }
}

