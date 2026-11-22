/*
 * Decompiled with CFR 0.152.
 *
 * Could not load the following classes:
 *  com.comphenix.protocol.PacketType
 *  com.comphenix.protocol.PacketType$Play$Client
 *  com.comphenix.protocol.ProtocolLibrary
 *  com.comphenix.protocol.ProtocolManager
 *  com.comphenix.protocol.events.PacketListener
 */
package ai.idealistic.spartan.compatibility.necessary.protocollib;

import ai.idealistic.spartan.functionality.server.MultiVersion;
import ai.idealistic.spartan.listeners.protocol.AbilitiesListener;
import ai.idealistic.spartan.listeners.protocol.BlockPlaceListener;
import ai.idealistic.spartan.listeners.protocol.ClicksListener;
import ai.idealistic.spartan.listeners.protocol.DeathListener;
import ai.idealistic.spartan.listeners.protocol.ExplosionListener;
import ai.idealistic.spartan.listeners.protocol.MovementListener;
import ai.idealistic.spartan.listeners.protocol.MultiBlockLegacyListener;
import ai.idealistic.spartan.listeners.protocol.MultiBlockListener;
import ai.idealistic.spartan.listeners.protocol.PacketLatencyHandler;
import ai.idealistic.spartan.listeners.protocol.PacketPistonHandle;
import ai.idealistic.spartan.listeners.protocol.PacketServerBlockHandle;
import ai.idealistic.spartan.listeners.protocol.TeleportListener;
import ai.idealistic.spartan.listeners.protocol.TridentListener;
import ai.idealistic.spartan.listeners.protocol.UseEntityListener;
import ai.idealistic.spartan.listeners.protocol.UseItemStatusHandle;
import ai.idealistic.spartan.listeners.protocol.VehicleHandle;
import ai.idealistic.spartan.listeners.protocol.VelocityListener;
import ai.idealistic.spartan.listeners.protocol.combat.CombatListener;
import ai.idealistic.spartan.listeners.protocol.combat.LegacyCombatListener;
import ai.idealistic.spartan.listeners.protocol.standalone.EntityActionListener;
import ai.idealistic.spartan.listeners.protocol.standalone.JoinListener;
import ai.idealistic.spartan.utils.minecraft.entity.PlayerUtils;
import com.comphenix.protocol.PacketType;
import com.comphenix.protocol.ProtocolLibrary;
import com.comphenix.protocol.ProtocolManager;
import com.comphenix.protocol.events.PacketListener;
import java.util.ArrayList;
import java.util.List;

public class BackgroundProtocolLib {
    private static final List<String> packets = new ArrayList<String>();

    static boolean isPacketSupported(String packet) {
        return packets.contains(packet);
    }

    private static void handle() {
        for (PacketType type : PacketType.Play.Client.getInstance()) {
            packets.add(type.name());
        }
    }

    static void run() {
        BackgroundProtocolLib.handle();
        ProtocolManager protocolManager =  ProtocolLibrary.getProtocolManager();
        p.addPacketListener((PacketListener)new JoinListener());
        p.addPacketListener((PacketListener)new EntityActionListener());
        p.addPacketListener((PacketListener)new VelocityListener());
        if (MultiVersion.isOrGreater(MultiVersion.MCVersion.V1_17)) {
            p.addPacketListener((PacketListener)new CombatListener());
        } else {
            p.addPacketListener((PacketListener)new LegacyCombatListener());
        }
        if (MultiVersion.isOrGreater(MultiVersion.MCVersion.V1_13)) {
            p.addPacketListener((PacketListener)new MultiBlockListener());
        } else {
            p.addPacketListener((PacketListener)new MultiBlockLegacyListener());
        }
        p.addPacketListener((PacketListener)new MovementListener());
        p.addPacketListener((PacketListener)new TeleportListener());
        p.addPacketListener((PacketListener)new VehicleHandle());
        p.addPacketListener((PacketListener)new DeathListener());
        p.addPacketListener((PacketListener)new BlockPlaceListener());
        p.addPacketListener((PacketListener)new ClicksListener());
        p.addPacketListener((PacketListener)new PacketPistonHandle());
        p.addPacketListener((PacketListener)new ExplosionListener());
        p.addPacketListener((PacketListener)new PacketServerBlockHandle());
        p.addPacketListener((PacketListener)new PacketLatencyHandler());
        p.addPacketListener((PacketListener)new AbilitiesListener());
        p.addPacketListener((PacketListener)new UseItemStatusHandle());
        p.addPacketListener((PacketListener)new UseEntityListener());
        if (PlayerUtils.trident) {
            p.addPacketListener((PacketListener)new TridentListener());
        }
    }
}

