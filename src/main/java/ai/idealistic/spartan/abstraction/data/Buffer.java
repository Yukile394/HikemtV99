/*
 * Decompiled with CFR 0.152.
 *
 * Could not load the following classes:
 *  org.bukkit.util.NumberConversions
 */
package ai.idealistic.spartan.abstraction.data;

import java.util.Map;
import org.bukkit.util.NumberConversions;

public class Buffer {
    private final Map<String, Storage> storage;

    public Buffer(Map<String, Storage> map) {
        this.storage = map;
    }

    public double get(String name) {
        Storage obj = this.storage.get(name);
        return obj != null ? obj.count : 0.0;
    }

    public void set(String name, int amount) {
        Storage obj = this.storage.get(name);
        if (obj == null) {
            obj = new Storage();
            this.storage.put(name, obj);
        }
        obj.reset();
        obj.increase(amount);
    }

    public double increase(String name, int amount) {
        return this.storage.computeIfAbsent(name, k -> new Storage()).increase(amount);
    }

    public double decrease(String name, int amount) {
        Storage obj = this.storage.get(name);
        return obj != null ? obj.decrease(amount) : 0.0;
    }

    public int getRemainingTicks(String name, int maxTicks) {
        Storage obj = this.storage.get(name);
        if (obj == null) {
            return 0;
        }
        long ticksPassed = obj.ticksPassed();
        return ticksPassed > (long)maxTicks ? 0 : (int)((long)maxTicks - ticksPassed);
    }

    public double count(String name, int maxTicks) {
        return this.storage.computeIfAbsent(name, k -> new Storage()).count(1.0, maxTicks);
    }

    public double ratio(String name, int minimumTicks, int maxTicks) {
        double count;
        Storage obj = this.storage.computeIfAbsent(name, k -> new Storage());
        double ticksPassed = obj.ticksPassed();
        if (ticksPassed > (double)maxTicks) {
            obj.reset();
        }
        return (count = obj.increase(1.0)) >= (double)minimumTicks ? count / ticksPassed : 0.0;
    }

    public boolean canDo(String name) {
        return this.get(name) >= 0.0;
    }

    public void remove(String name) {
        this.storage.remove(name);
    }

    public void remove(String[] names) {
        for (String name : names) {
            this.storage.remove(name);
        }
    }

    public static class Storage {
        private double count;
        private long start;

        public Storage() {
            this.reset();
        }

        public long ticksPassed() {
            return NumberConversions.ceil((double)((double)(System.currentTimeMillis() - this.start) / 50.0));
        }

        public long ticksRemaining(int maxTicks) {
            return Math.max(0L, (long)maxTicks - this.ticksPassed());
        }

        public double set(double amount) {
            this.count = amount;
            return this.count;
        }

        public double increase(double amount) {
            this.count += amount;
            return this.count;
        }

        public double decrease(double amount) {
            if (this.count > amount) {
                this.count -= amount;
                return this.count;
            }
            this.count = 0.0;
            return 0.0;
        }

        public void reset() {
            this.count = 0.0;
            this.start = System.currentTimeMillis();
        }

        public double count(double amount, int maxTicks) {
            if (this.ticksPassed() > (long)maxTicks) {
                this.reset();
            }
            return this.increase(amount);
        }
    }

    public static class IndividualSimpleBuffer {
        public int count;
    }
}

