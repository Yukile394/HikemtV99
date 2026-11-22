/*
 * Decompiled with CFR 0.152.
 *
 * Could not load the following classes:
 *  org.bukkit.entity.Player
 *  org.bukkit.event.Cancellable
 *  org.bukkit.event.HandlerList
 *  org.bukkit.event.player.PlayerEvent
 *  org.bukkit.util.Vector
 *  org.jetbrains.annotations.NotNull
 */
package ai.idealistic.spartan.abstraction.event;

import org.bukkit.entity.Player;
import org.bukkit.event.Cancellable;
import org.bukkit.event.HandlerList;
import org.bukkit.event.player.PlayerEvent;
import org.bukkit.util.Vector;
import org.jetbrains.annotations.NotNull;

public class CPlayerVelocityEvent
extends PlayerEvent
implements Cancellable {
    private static final HandlerList handlers = new HandlerList();
    private boolean cancel;
    private Vector velocity;

    public CPlayerVelocityEvent(@NotNull Player player, @NotNull Vector velocity) {
        if (player == null) {
            CPlayerVelocityEvent.$$$reportNull$$$0(0);
        }
        if (velocity == null) {
            CPlayerVelocityEvent.$$$reportNull$$$0(1);
        }
        super(player);
        this.cancel = false;
    }

    public boolean isCancelled() {
        return this.cancel;
    }

    public void setCancelled(boolean cancel) {
    }

    @NotNull
    public Vector getVelocity() {
        Vector vector = this.velocity;
        if (vector == null) {
            CPlayerVelocityEvent.$$$reportNull$$$0(2);
        }
        return vector;
    }

    public void setVelocity(@NotNull Vector velocity) {
        if (velocity == null) {
            CPlayerVelocityEvent.$$$reportNull$$$0(3);
        }
    }

    @NotNull
    public HandlerList getHandlers() {
        if (handlers == null) {
            CPlayerVelocityEvent.$$$reportNull$$$0(4);
        }
        return handlers;
    }

    @NotNull
    public static HandlerList getHandlerList() {
        if (handlers == null) {
            CPlayerVelocityEvent.$$$reportNull$$$0(5);
        }
        return handlers;
    }

    private static /* synthetic */ void $$$reportNull$$$0(int n) {
        RuntimeException runtimeException;
        Object[] objectArray;
        Object[] objectArray2;
        int n2;
        String string;
        switch (n) {
            default: {
                string = "Argument for @NotNull parameter '%s' of %s.%s must not be null";
                break;
            }
            case 2:
            case 4:
            case 5: {
                string = "@NotNull method %s.%s must not return null";
            }
        }
        switch (n) {
            default: {
                n2 = 3;
                break;
            }
            case 2:
            case 4:
            case 5: {
                n2 = 2;
            }
        }
        Object[] objectArray3 = new Object[n2];
        switch (n) {
            default: {
                objectArray2 = objectArray3;
                objectArray3[0] = "player";
                break;
            }
            case 1:
            case 3: {
                objectArray2 = objectArray3;
                objectArray3[0] = "velocity";
                break;
            }
            case 2:
            case 4:
            case 5: {
                objectArray2 = objectArray3;
                objectArray3[0] = "ai/idealistic/spartan/abstraction/event/CPlayerVelocityEvent";
            }
        }
        switch (n) {
            default: {
                objectArray = objectArray2;
                objectArray2[1] = "ai/idealistic/spartan/abstraction/event/CPlayerVelocityEvent";
                break;
            }
            case 2: {
                objectArray = objectArray2;
                objectArray2[1] = "getVelocity";
                break;
            }
            case 4: {
                objectArray = objectArray2;
                objectArray2[1] = "getHandlers";
                break;
            }
            case 5: {
                objectArray = objectArray2;
                objectArray2[1] = "getHandlerList";
            }
        }
        switch (n) {
            default: {
                objectArray[2] = "<init>";
                break;
            }
            case 2:
            case 4:
            case 5: {
                break;
            }
            case 3: {
                objectArray[2] = "setVelocity";
            }
        }
        String string2 = String.format(string, objectArray);
        switch (n) {
            default: {
                runtimeException = new IllegalArgumentException(string2);
                break;
            }
            case 2:
            case 4:
            case 5: {
                runtimeException = new IllegalStateException(string2);
            }
        }
        throw runtimeException;
    }
}

