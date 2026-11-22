/*
 * Decompiled with CFR 0.152.
 *
 * Could not load the following classes:
 *  lombok.Generated
 *  org.bukkit.OfflinePlayer
 *  org.bukkit.inventory.ItemStack
 */
package ai.idealistic.spartan.abstraction.profiling;

import ai.idealistic.spartan.abstraction.check.Check;
import ai.idealistic.spartan.abstraction.check.CheckEnums;
import ai.idealistic.spartan.abstraction.check.CheckRunner;
import ai.idealistic.spartan.abstraction.profiling.MiningHistory;
import ai.idealistic.spartan.abstraction.profiling.ProfileContinuity;
import ai.idealistic.spartan.abstraction.protocol.PlayerProtocol;
import ai.idealistic.spartan.functionality.server.PluginBase;
import ai.idealistic.spartan.utils.java.ConcurrentList;
import ai.idealistic.spartan.utils.minecraft.inventory.InventoryUtils;
import java.util.ArrayList;
import java.util.Collections;
import java.util.Iterator;
import java.util.List;
import java.util.Map;
import java.util.concurrent.ConcurrentHashMap;
import lombok.Generated;
import org.bukkit.OfflinePlayer;
import org.bukkit.inventory.ItemStack;

public class PlayerProfile {
    public static final String activeFor = " was active for: ";
    public final String name;
    private final MiningHistory[] miningHistory;
    private ItemStack skull;
    private OfflinePlayer offlinePlayer;
    private Check.DataType lastDataType;
    private final ProfileContinuity continuity;
    private final Map<String, List<Long>>[][] data;

    public PlayerProfile(String name) {
        this.skull = null;
        this.miningHistory = new MiningHistory[MiningHistory.MiningOre.values().length];
        this.lastDataType = Check.DataType.JAVA;
        this.continuity = new ProfileContinuity(this);
        this.data = new Map[CheckEnums.HackType.values().length][Check.DataType.values().length];
        for (CheckEnums.HackType hackType : CheckEnums.HackType.values()) {
            for (Check.DataType dataType : Check.DataType.values()) {
                this.data[hackType.ordinal()][dataType.ordinal()] = new ConcurrentHashMap<String, List<Long>>1;
            }
        }
        for (Enum enum_ : MiningHistory.MiningOre.values()) {
            this.miningHistory[enum_.ordinal()] = new MiningHistory(this, (MiningHistory.MiningOre)enum_);
        }
        PlayerProtocol protocol = PluginBase.getProtocol(name);
        this.offlinePlayer = protocol != null ? protocol.bukkit() : null;
    }

    public PlayerProfile(PlayerProtocol protocol) {
        this.name = protocol.bukkit().getName();
        this.offlinePlayer = protocol.bukkit();
        this.skull = null;
        this.miningHistory = new MiningHistory[MiningHistory.MiningOre.values().length];
        this.lastDataType = protocol.getDataType();
        this.continuity = new ProfileContinuity(this);
        this.data = new Map[CheckEnums.HackType.values().length][Check.DataType.values().length];
        for (CheckEnums.HackType hackType : CheckEnums.HackType.values()) {
            for (Check.DataType dataType : Check.DataType.values()) {
                this.data[hackType.ordinal()][dataType.ordinal()] = new ConcurrentHashMap<String, List<Long>>1;
            }
        }
        for (Enum enum_ : MiningHistory.MiningOre.values()) {
            this.miningHistory[enum_.ordinal()] = new MiningHistory(this, (MiningHistory.MiningOre)enum_);
        }
    }

    public void update(PlayerProtocol protocol) {
        this.offlinePlayer = protocol.bukkit();
        this.lastDataType = protocol.getDataType();
    }

    PlayerProtocol protocol() {
        return PluginBase.getAnyCaseProtocol(this.name);
    }

    public CheckRunner getRunner(CheckEnums.HackType hackType) {
        PlayerProtocol protocol = this.protocol();
        return protocol == null ? null : protocol.getRunner(hackType);
    }

    public Check.DataType getOppositeDataType() {
        return this.lastDataType == Check.DataType.JAVA ? Check.DataType.BEDROCK : Check.DataType.JAVA;
    }

    public ItemStack getSkull() {
        if (this.skull == null) {
            if (this.offlinePlayer == null) {
                return InventoryUtils.getSkull(null, this.name, false);
            }
            this.skull = InventoryUtils.getSkull(this.offlinePlayer, this.name, false);
            return this.skull;
        }
        return this.skull;
    }

    public MiningHistory getMiningHistory(MiningHistory.MiningOre ore) {
        return this.miningHistory[ore.ordinal()];
    }

    public List<Long> getTimeDifferences(CheckEnums.HackType hackType, Check.DataType dataType, String detection) {
        List<Long> data = this.data[hackType.ordinal()][dataType.ordinal()].get(detection);
        if (data != null) {
            if (!data.isEmpty()) {
                for (Long time : data) {
                    if (time >= 0L) continue;
                    data.remove(time);
                    data.add(Math.abs(time));
                }
            }
            return data;
        }
        return new ArrayList<Long>0;
    }

    public double getAverageTimeDifference(CheckEnums.HackType hackType, Check.DataType dataType, String detection) {
        List<Long> data = this.data[hackType.ordinal()][dataType.ordinal()].get(detection);
        if (data != null && !data.isEmpty()) {
            double total = 0.0;
            for (Long time : data) {
                if (time < 0L) {
                    long absTime = Math.abs(time);
                    data.remove(time);
                    data.add(absTime);
                    total += (double)absTime;
                    continue;
                }
                total += (double)time.(long)Value();
            }
            if (total > 0.0) {
                return total / (double)data.size();
            }
        }
        return 0.0;
    }

    public void setTimeDifferences(CheckEnums.HackType hackType, Check.DataType dataType, String detection, List<Long> data) {
        List list = this.data[hackType.ordinal()][dataType.ordinal()].computeIfAbsent(detection, k -> new ConcurrentList());
        list.clear();
        list.addAll(data);
    }

    public void addTimeDifference(CheckEnums.HackType hackType, Check.DataType dataType, String detection, long time) {
        List list = this.data[hackType.ordinal()][dataType.ordinal()].computeIfAbsent(detection, k -> new ConcurrentList());
        int size = list.size() - 1024;
        if (size > 0) {
            Iterator iterator = list.iterator();
            while (iterator.hasNext() && size > 0) {
                if (!list.remove(iterator.next())) continue;
                --size;
            }
        }
        list.add(time);
    }

    public void clearTimeDifferences(CheckEnums.HackType hackType, Check.DataType dataType, String detection) {
        List<Long> list = this.data[hackType.ordinal()][dataType.ordinal()].get(detection);
        if (list != null) {
            list.clear();
        }
    }

    public final void sortTimeDifferences(CheckEnums.HackType hackType, String detection) {
        for (Check.DataType dataType : Check.DataType.values()) {
            List<Long> data = this.data[hackType.ordinal()][dataType.ordinal()].get(detection);
            if (data == null || data.isEmpty()) continue;
            Collections.sort(data);
            Iterator<Long> iterator = data.iterator();
            if (!iterator.hasNext()) continue;
            ArrayList<Long> differences = new ArrayList<Long>(data.size() - 1);
            long previous = iterator.next();
            while (iterator.hasNext()) {
                long current = iterator.next();
                if (current >= 0L) {
                    if (previous < 0L) continue;
                    if (this.getContinuity().wasOnline(current, previous)) {
                        differences.add(current - previous);
                    }
                    previous = current;
                    continue;
                }
                differences.add(Math.abs(current));
            }
            this.setTimeDifferences(hackType, dataType, detection, differences);
        }
    }

    @Generated
    public Check.DataType getLastDataType() {
        return this.lastDataType;
    }

    @Generated
    public void setLastDataType(Check.DataType lastDataType) {
    }

    @Generated
    public ProfileContinuity getContinuity() {
        return this.continuity;
    }
}

