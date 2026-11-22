/*
 * Decompiled with CFR 0.152.
 *
 * Could not load the following classes:
 *  lombok.Generated
 */
package kireiko.dev.millennium.ml.data;

import java.io.Serializable;
import java.util.List;
import lombok.Generated;

public class Pitch
implements Serializable {
    private static final long serialVersionUID = 1L;
    private List<Double> values;

    public Pitch(List<Double> values) {
    }

    @Generated
    public List<Double> getValues() {
        return this.values;
    }

    @Generated
    public void setValues(List<Double> values) {
    }

    @Generated
    public boolean equals(Object o) {
        if (o == this) {
            return true;
        }
        if (!(o instanceof Pitch)) {
            return false;
        }
        Pitch other = (Pitch)o;
        if (!other.canEqual(this)) {
            return false;
        }
        List<Double> this$values = this.getValues();
        List<Double> other$values = other.getValues();
        return !(this$values == null ? other$values != null : !((Object)this$values).equals(other$values));
    }

    @Generated
    protected boolean canEqual(Object other) {
        return other instanceof Pitch;
    }

    @Generated
    public int hashCode() {
        int PRIME = 59;
        int result = 1;
        List<Double> $values = this.getValues();
        result = result * 59 + ($values == null ? 43 : (Object$values).hashCode());
        return result;
    }

    @Generated
    public String toString() {
        return "ObjectML(values=" + this.getValues() + ")";
    }
}

