/*
 * Decompiled with CFR 0.152.
 *
 * Could not load the following classes:
 *  com.comphenix.protocol.PacketType
 *  com.comphenix.protocol.PacketType$Play$Client
 *  com.comphenix.protocol.events.ListenerPriority
 *  com.comphenix.protocol.events.PacketAdapter
 *  com.comphenix.protocol.events.PacketEvent
 */
package ai.idealistic.spartan.listeners.protocol.standalone;

import ai.idealistic.spartan.Register;
import ai.idealistic.spartan.abstraction.protocol.PlayerProtocol;
import ai.idealistic.spartan.functionality.concurrent.CheckThread;
import ai.idealistic.spartan.functionality.server.Config;
import ai.idealistic.spartan.functionality.server.PluginBase;
import com.comphenix.protocol.PacketType;
import com.comphenix.protocol.events.ListenerPriority;
import com.comphenix.protocol.events.PacketAdapter;
import com.comphenix.protocol.events.PacketEvent;

public class EntityActionListener
extends PacketAdapter {
    public EntityActionListener() {
        super(Register.plugin, ListenerPriority.HIGHEST, new PacketType[]{PacketType.Play.Client.ENTITY_ACTION});
    }

    public void onPacketReceiving(PacketEvent event) {
        PlayerProtocol protocol = PluginBase.getProtocol(event.getPlayer());
        if (!Config.settings.getBoolean("Important.bedrock_on_protocollib") && protocol.isBedrockPlayer()) {
            return;
        }
        CheckThread.run(() -> {
            String typeString;
            Shift type;
            if (event.getPacket().getModifier().getValues().size() > 1 && (type = this.getEnum(typeString = event.getPacket().getModifier().getValues().get(1).toString())) != null) {
                CheckThread.run(() -> {
                    if (type == Shift.PRESS_SHIFT_KEY) {
                        protocol.sneaking = true;
                    } else if (type == Shift.RELEASE_SHIFT_KEY) {
                        protocol.sneaking = false;
                    } else if (type == Shift.START_SPRINTING) {
                        protocol.sprinting = true;
                    } else if (type == Shift.STOP_SPRINTING) {
                        protocol.sprinting = false;
                    }
                });
            }
        });
    }

    private Shift getEnum(String s) {
        for (Shift type : Shift.values()) {
            if (!type.toString().equals(s)) continue;
            return type;
        }
        return null;
    }

    private static enum Shift {
        START_SPRINTING,
        STOP_SPRINTING,
        PRESS_SHIFT_KEY,
        RELEASE_SHIFT_KEY;
    }
}

