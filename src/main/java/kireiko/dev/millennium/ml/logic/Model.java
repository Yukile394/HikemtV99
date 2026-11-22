/*
 * Decompiled with CFR 0.152.
 */
package kireiko.dev.millennium.ml.logic;

import java.util.List;
import kireiko.dev.millennium.ml.data.Pitch;
import kireiko.dev.millennium.ml.data.Class2;

public interface Model {
    public Class2 checkData(List<Pitch> var1);

    public void learnByData(List<Pitch> var1, boolean var2);

    public void saveToFile(String var1);

    public int parameters();
}

