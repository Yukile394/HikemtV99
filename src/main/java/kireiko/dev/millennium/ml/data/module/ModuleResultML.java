/*
 * Decompiled with CFR 0.152.
 *
 * Could not load the following classes:
 *  lombok.Generated
 */
package kireiko.dev.millennium.ml.data.module;

import kireiko.dev.millennium.ml.data.module.FlagType;
import lombok.Generated;

public class ModuleResultML {
    private int priority;
    private FlagType type;
    private String info;

    @Generated
    public ModuleResultML(int priority, FlagType type, String info) {
    }

    @Generated
    public int getPriority() {
        return this.priority;
    }

    @Generated
    public FlagType getType() {
        return this.type;
    }

    @Generated
    public String getInfo() {
        return this.info;
    }

    @Generated
    public boolean equals(Object o) {
        if (o == this) {
            return true;
        }
        if (!(o instanceof ModuleResultML)) {
            return false;
        }
        ModuleResultML other = (ModuleResultML)o;
        if (!other.canEqual(this)) {
            return false;
        }
        if (this.getPriority() != other.getPriority()) {
            return false;
        }
        FlagType this$type = this.getType();
        FlagType other$type = other.getType();
        if (this$type == null ? other$type != null : !((Object)this$type).equals((Object)other$type)) {
            return false;
        }
        String this$info = this.getInfo();
        String other$info = other.getInfo();
        return !(this$info == null ? other$info != null : !this$info.equals(other$info));
    }

    @Generated
    protected boolean canEqual(Object other) {
        return other instanceof ModuleResultML;
    }

    @Generated
    public int hashCode() {
        int PRIME = 59;
        result = result * 59 + this.getPriority();
        FlagType $type = this.getType();
        result = result * 59 + ($type == null ? 43 : (Object$type).hashCode());
        String $info = this.getInfo();
        result = result * 59 + ($info == null ? 43 : $info.hashCode());
        return result;
    }
}

