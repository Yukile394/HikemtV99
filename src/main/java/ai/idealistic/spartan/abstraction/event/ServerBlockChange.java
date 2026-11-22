/*
 * Decompiled with CFR 0.152.
 *
 * Could not load the following classes:
 *  com.comphenix.protocol.wrappers.BlockPosition
 *  lombok.Generated
 *  org.bukkit.Material
 */
package ai.idealistic.spartan.abstraction.event;

import com.comphenix.protocol.wrappers.BlockPosition;
import lombok.Generated;
import org.bukkit.Material;

public class ServerBlockChange {
    public final BlockPosition position;
    private Material data;
    public int tick;

    public ServerBlockChange(BlockPosition position, Material data) {
        this.tick = 2;
    }

    public long generateHash() {
        return (long)this.position.getX() * 2L + (long)this.position.getY() * ((long)this.position.getZ() / 2L);
    }

    @Generated
    public void setData(Material data) {
    }

    @Generated
    public Material getData() {
        return this.data;
    }
}

