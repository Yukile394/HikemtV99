/*
 * Decompiled with CFR 0.152.
 *
 * Could not load the following classes:
 *  com.comphenix.protocol.PacketType
 *  com.comphenix.protocol.PacketType$Play$Client
 *  com.comphenix.protocol.PacketType$Play$Server
 *  com.comphenix.protocol.ProtocolLibrary
 *  com.comphenix.protocol.events.ListenerPriority
 *  com.comphenix.protocol.events.PacketAdapter
 *  com.comphenix.protocol.events.PacketContainer
 *  com.comphenix.protocol.events.PacketEvent
 *  com.comphenix.protocol.wrappers.EnumWrappers$EntityUseAction
 *  com.comphenix.protocol.wrappers.WrappedEnumEntityUseAction
 *  org.bukkit.Bukkit
 *  org.bukkit.entity.Entity
 *  org.bukkit.entity.LivingEntity
 *  org.bukkit.entity.Player
 *  org.bukkit.event.entity.EntityDamageByEntityEvent
 *  org.bukkit.event.entity.EntityDamageEvent$DamageCause
 */
package ai.idealistic.spartan.listeners.protocol.combat;

import ai.idealistic.spartan.Register;
import ai.idealistic.spartan.abstraction.event.PlayerUseEvent;
import ai.idealistic.spartan.abstraction.protocol.PlayerProtocol;
import ai.idealistic.spartan.functionality.concurrent.CheckThread;
import ai.idealistic.spartan.functionality.moderation.AwarenessNotifications;
import ai.idealistic.spartan.functionality.server.Config;
import ai.idealistic.spartan.functionality.server.PluginBase;
import ai.idealistic.spartan.listeners.bukkit.CombatEvent;
import ai.idealistic.spartan.utils.java.OverflowMap;
import com.comphenix.protocol.PacketType;
import com.comphenix.protocol.ProtocolLibrary;
import com.comphenix.protocol.events.ListenerPriority;
import com.comphenix.protocol.events.PacketAdapter;
import com.comphenix.protocol.events.PacketContainer;
import com.comphenix.protocol.events.PacketEvent;
import com.comphenix.protocol.wrappers.EnumWrappers;
import com.comphenix.protocol.wrappers.WrappedEnumEntityUseAction;
import java.util.Map;
import java.util.UUID;
import java.util.concurrent.ConcurrentHashMap;
import org.bukkit.Bukkit;
import org.bukkit.entity.Entity;
import org.bukkit.entity.LivingEntity;
import org.bukkit.entity.Player;
import org.bukkit.event.entity.EntityDamageByEntityEvent;
import org.bukkit.event.entity.EntityDamageEvent;

public class CombatListener
extends PacketAdapter {
    private final Map<UUID, Integer> pendingAttacks = new OverflowMap<UUID, Integer>(new ConcurrentHashMap(), 1024);

    public CombatListener() {
        super(Register.plugin, ListenerPriority.HIGHEST, new PacketType[]{PacketType.Play.Client.USE_ENTITY, PacketType.Play.Server.DAMAGE_EVENT});
    }

    public void onPacketReceiving(PacketEvent event) {
        if (event.getPacketType() == PacketType.Play.Client.USE_ENTITY) {
            PlayerProtocol protocol = PluginBase.getProtocol(event.getPlayer());
            if (!Config.settings.getBoolean("Important.bedrock_on_protocollib") && protocol.isBedrockPlayer()) {
                return;
            }
            PacketContainer packet = event.getPacket();
            int entityId = (Integer)packet.getIntegers().read(0);
            Player target = null;
            for (Player player : Bukkit.getOnlinePlayers()) {
                if (player.getEntityId() != entityId) continue;
                target = player;
                break;
            }
            if (!packet.getEntityUseActions().getValues().isEmpty() ? ((EnumWrappers.EntityUseAction)packet.getEntityUseActions().read(0)).equals(EnumWrappers.EntityUseAction.ATTACK) : ((WrappedEnumEntityUseAction)packet.getEnumEntityUseActions().read(0)).getAction().equals(EnumWrappers.EntityUseAction.ATTACK)) {
                if (protocol.isSDesync()) {
                    AwarenessNotifications.optionallySend(protocol.bukkit().getName() + " attack faster than the transaction response.");
                }
                if (target != null) {
                    CheckThread.run(() -> CombatEvent.use(new PlayerUseEvent(event.getPlayer(), (LivingEntity)target, event.isCancelled())));
                }
                this.pendingAttacks.put(protocol.getUUID(), entityId);
            }
        }
    }

    public void onPacketSending(PacketEvent event) {
        if (event.getPacketType() == PacketType.Play.Server.DAMAGE_EVENT) {
            PlayerProtocol protocol = PluginBase.getProtocol(event.getPlayer());
            if (!Config.settings.getBoolean("Important.bedrock_on_protocollib") && protocol.isBedrockPlayer()) {
                return;
            }
            int entityId = (Integer)event.getPacket().getIntegers().read(0);
            this.pendingAttacks.entrySet().removeIf(entry -> {
                UUID playerUUID = (UUID)entry.getKey();
                int pendingEntityId = (Integer)entry.getValue();
                if (pendingEntityId == entityId) {
                    Player attacker = this.plugin.getServer().getPlayer(playerUUID);
                    Entity target = ProtocolLibrary.getProtocolManager().getEntityFromID(protocol.getWorld(), entityId);
                    if (attacker != null && target != null) {
                        EntityDamageByEntityEvent event1 = new EntityDamageByEntityEvent((Entity)attacker, target, EntityDamageEvent.DamageCause.ENTITY_ATTACK, 0.0);
                        event1.setCancelled(event.isCancelled());
                        CheckThread.run(() -> CombatEvent.event(event1, true));
                    }
                    return true;
                }
                return false;
            });
        }
    }
}

