/*
 * Decompiled with CFR 0.152.
 *
 * Could not load the following classes:
 *  org.bukkit.util.NumberConversions
 */
package ai.idealistic.spartan.abstraction.data;

import java.util.Map;
import org.bukkit.util.NumberConversions;

public class Cooldowns {
    private final Map<String, Long> storage;

    public Cooldowns(Map<String, Long> map) {
        this.storage = map;
    }

    public int get(String name) {
        Long object = this.storage.get(name);
        if (object == null) {
            return 0;
        }
        return (object = Long.valueOf(object - System.currentTimeMillis())) < 0L ? 0 : NumberConversions.ceil((double)((double)object.(long)Value() / 50.0));
    }

    public boolean canDo(String name) {
        return this.get(name) == 0;
    }

    public void add(String name, int ticks) {
        this.storage.put(name, System.currentTimeMillis() + (long)ticks * 50L);
    }

    public void add(String[] names, int ticks) {
        for (String name : names) {
            this.storage.put(name, System.currentTimeMillis() + (long)ticks * 50L);
        }
    }

    public void remove(String name) {
        this.storage.remove(name);
    }

    public void remove(String[] names) {
        for (String name : names) {
            this.storage.remove(name);
        }
    }
}

