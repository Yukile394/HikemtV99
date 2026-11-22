/*
 * Decompiled with CFR 0.152.
 *
 * Could not load the following classes:
 *  me.gwndaan.printer.PrinterModeAPI
 *  org.bukkit.entity.Player
 */
package ai.idealistic.spartan.compatibility.manual.building;

import ai.idealistic.spartan.abstraction.protocol.PlayerProtocol;
import ai.idealistic.spartan.compatibility.Compatibility;
import me.gwndaan.printer.PrinterModeAPI;
import org.bukkit.entity.Player;

public class PrinterMode {
    public static boolean isUsing(PlayerProtocol p) {
        return Compatibility.CompatibilityType.PRINTER_MODE.isFunctional() && PrinterModeAPI.isInPrinterMode((Player)p.bukkit());
    }
}

