/*
 * Decompiled with CFR 0.152.
 *
 * Could not load the following classes:
 *  lombok.Generated
 */
package ai.idealistic.spartan.abstraction.check;

import ai.idealistic.spartan.compatibility.Compatibility;
import lombok.Generated;

public class CheckCancellation {
    private final String s;
    private final String t;
    private final long expiration;

    CheckCancellation(Compatibility.CompatibilityType compatibilityType) {
        this.s = compatibilityType.toString();
        this.t = null;
        this.expiration = 0L;
    }

    CheckCancellation(String reason, String pointer, int ticks) {
        this.s = reason;
        this.t = pointer;
        this.expiration = ticks == 0 ? Long.MAX_VALUE : System.currentTimeMillis() + (long)ticks * 50L;
    }

    boolean isCheckCancellation() {
        return this.expiration < System.currentTimeMillis();
    }

    boolean checkCheckCancellation(String info) {
        return this.t == null || info.contains(this.t);
    }

    @Generated
    public String getS() {
        return this.s;
    }

    @Generated
    public String getT() {
        return this.t;
    }
}

