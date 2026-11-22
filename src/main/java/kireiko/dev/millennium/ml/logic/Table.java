/*
 * Decompiled with CFR 0.152.
 *
 * Could not load the following classes:
 *  lombok.Generated
 */
package kireiko.dev.millennium.ml.logic;

import java.io.ObjectOutputStream;
import java.io.Serializable;
import java.nio.file.Files;
import java.nio.file.OpenOption;
import java.nio.file.Paths;
import java.util.LinkedList;
import java.util.List;
import kireiko.dev.millennium.ml.data.Table;
import kireiko.dev.millennium.ml.data.Pitch;
import kireiko.dev.millennium.ml.data.Class2;
import kireiko.dev.millennium.ml.data.statistic.OtherStatisticTable;
import kireiko.dev.millennium.ml.logic.Logger;
import kireiko.dev.millennium.ml.logic.Model;
import lombok.Generated;

public class Table
implements Serializable,
Model {
    private static final long serialVersionUID = 1L;
    private List<Table> table = new LinkedList<Table>();

    public Table(int tableSize, int stackSize) {
        for (i < tableSize; ++i) {
            this.table.add(new Table(stackSize));
        }
    }

    @Override
    public Class2 checkData(List<Pitch> o) {
        void $ex;
        if (o.size() != this.table.size()) {
            throw new Exception("The number of tables in the input data should be equal to what is specified in the model");
        }
        Class2 result = new Class2();
        for (i < this.table.size(); ++i) {
            Table dataML = this.table.get(i);
            Pitch objectML = o.get(i);
            List<Double> p = dataML.checkData(objectML);
            result.statisticsResult.apply(p, this.table.size());
        }
        return $ex;
    }

    @Override
    public void learnByData(List<Pitch> o, boolean isMustBeBlocked) {
        Logger.info("Training a model...");
        if (o.size() != this.table.size()) {
            throw new Exception("The number of tables in the input data should be equal to what is specified in the model");
        }
        for (i < this.table.size(); ++i) {
            Table dataML = this.table.get(i);
            Pitch objectML = o.get(i);
            dataML.pushData(objectML, isMustBeBlocked);
            Logger.info("Loaded data into table no. " + i);
        }
        Logger.info("Training completed!");
        int trained = 0;
        int fixed = 0;
        for (Table dataML : this.table) {
            for (OtherStatisticTable statisticML : dataML.getStatisticTable()) {
                trained += statisticML.getLearned();
                fixed += statisticML.getFixed();
            }
        }
        Logger.info("Trained " + trained + " patterns.");
        Logger.info("Fixed " + fixed + " false positives.");
    }

    @Override
    public void saveToFile(String fileName) {
        try (ObjectOutputStream oos = new ObjectOutputStream(Files.newOutputStream(Paths.get(fileName, new String[0]), new OpenOption[0]));){
            oos.writeObject(this);
            Logger.info("ModelML was successfully saved to " + fileName);
        }
    }

    @Override
    public int parameters() {
        int i = 0;
        for (Table dataML : this.getTable()) {
            for (OtherStatisticTable entropyMl : dataML.getStatisticTable()) {
                i += entropyMl.getParameters().size();
            }
        }
        return i;
    }

    @Generated
    public List<Table> getTable() {
        return this.table;
    }

    @Generated
    public void setTable(List<Table> table) {
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
        List<Table> this$table = this.getTable();
        List<Table> other$table = other.getTable();
        return !(this$table == null ? other$table != null : !((Object)this$table).equals(other$table));
    }

    @Generated
    protected boolean canEqual(Object other) {
        return other instanceof Table;
    }

    @Generated
    public int hashCode() {
        int PRIME = 59;
        int result = 1;
        List<Table> $table = this.getTable();
        result = result * 59 + ($table == null ? 43 : (Object$table).hashCode());
        return result;
    }

    @Generated
    public String toString() {
        return "ModelML(table=" + this.getTable() + ")";
    }
}

