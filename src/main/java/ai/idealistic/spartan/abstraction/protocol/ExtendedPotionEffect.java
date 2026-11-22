/*
 * Decompiled with CFR 0.152.
 *
 * Could not load the following classes:
 *  org.bukkit.potion.PotionEffect
 */
package ai.idealistic.spartan.abstraction.protocol;

import org.bukkit.potion.PotionEffect;

public class ExtendedPotionEffect {
    public final PotionEffect bukkitEffect;
    private final long expiration;

    public ExtendedPotionEffect(PotionEffect effect) {
        this.bukkitEffect = effect;
        this.expiration = System.currentTimeMillis() + (long)effect.getDuration() * 50L;
    }

    public long timePassed() {
        long remaining = this.expiration - System.currentTimeMillis();
        return remaining >= 0L ? 0L : Math.abs(remaining);
    }

    public boolean isActive() {
        return this.expiration - System.currentTimeMillis() >= 0L;
    }
}

