/*
 * Decompiled with CFR 0.152.
 *
 * Could not load the following classes:
 *  org.bukkit.entity.Player
 *  protocolsupport.api.Connection
 *  protocolsupport.api.ProtocolSupportAPI
 */
package ai.idealistic.spartan.compatibility.necessary;

import ai.idealistic.spartan.compatibility.Compatibility;
import org.bukkit.entity.Player;
import protocolsupport.api.Connection;
import protocolsupport.api.ProtocolSupportAPI;

public class ProtocolSupport {
    static boolean isBedrockPlayer(Player p) {
        if (Compatibility.CompatibilityType.PROTOCOL_SUPPORT.isFunctional()) {
            try {
                Connection c = ProtocolSupportAPI.getConnection((Player)p);
                return c != null && c.getVersion().toString().contains("_PE_");
            }
            catch (Exception exception) {
                // empty catch block
            }
        }
        return false;
    }
}

