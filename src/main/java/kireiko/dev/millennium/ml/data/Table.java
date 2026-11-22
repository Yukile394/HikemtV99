/*
 * Decompiled with CFR 0.152.
 *
 * Could not load the following classes:
 *  lombok.Generated
 */
package kireiko.dev.millennium.ml.data;

import java.io.Serializable;
import java.util.ArrayList;
import java.util.List;
import kireiko.dev.millennium.ml.data.Pitch;
import kireiko.dev.millennium.ml.data.statistic.OtherStatisticTable;
import lombok.Generated;

public class Table
implements Serializable {
    private static final long serialVersionUID = 1L;
    private static final double[] SCALE = new double[]{0.5, 0.25, 0.125, 0.0625};
    private List<OtherStatisticTable> statisticTable = new ArrayList<OtherStatisticTable>();

    public Table(int stackSize) {
        for (i < 4; ++i) {
            this.statisticTable.add(new OtherStatisticTable(SCALE[i], stackSize));
        }
    }

    public List<Double> checkData(Pitch data) {
        ArrayList<Double> l = new ArrayList<Double>4;
        for (OtherStatisticTable ml : this.statisticTable) {
            l.add(ml.checkData(data));
        }
        return l;
    }

    public void pushData(Pitch data, boolean isMustBeBlocked) {
        for (OtherStatisticTable statisticML : this.statisticTable) {
            statisticML.pushData(data, isMustBeBlocked);
        }
    }

    @Generated
    public List<OtherStatisticTable> getStatisticTable() {
        return this.statisticTable;
    }

    @Generated
    public void setStatisticTable(List<OtherStatisticTable> statisticTable) {
    }

    @Generated
    public boolean equals(Object o) {
        if (o == this) {
            return true;
        }
        if (!(o instanceof Table)) {
            return false;
        }
        Table other = (Table)o;
        if (!other.canEqual(this)) {
            return false;
        }
        List<OtherStatisticTable> this$statisticTable = this.getStatisticTable();
        List<OtherStatisticTable> other$statisticTable = other.getStatisticTable();
        return !(this$statisticTable == null ? other$statisticTable != null : !((Object)this$statisticTable).equals(other$statisticTable));
    }

    @Generated
    protected boolean canEqual(Object other) {
        return other instanceof Table;
    }

    @Generated
    public int hashCode() {
        int PRIME = 59;
        int result = 1;
        List<OtherStatisticTable> $statisticTable = this.getStatisticTable();
        result = result * 59 + ($statisticTable == null ? 43 : (Object$statisticTable).hashCode());
        return result;
    }

    @Generated
    public String toString() {
        return "DataML(statisticTable=" + this.getStatisticTable() + ")";
    }
}

