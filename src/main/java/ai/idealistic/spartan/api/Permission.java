/*
 * Decompiled with CFR 0.152.
 */
package ai.idealistic.spartan.api;

public enum Permission {
    CONDITION,
    WAVE,
    ADMIN,
    RELOAD,
    KICK,
    BYPASS,
    MANAGE,
    INFO,
    WARN,
    USE_BYPASS,
    NOTIFICATIONS,
    BEDROCK;

    private final String key = "spartan." + this.name().toLowerCase();

    public String getKey() {
        return this.key;
    }
}

