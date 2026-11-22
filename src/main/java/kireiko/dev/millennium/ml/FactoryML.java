/*
 * Decompiled with CFR 0.152.
 *
 * Could not load the following classes:
 *  lombok.Generated
 */
package kireiko.dev.millennium.ml;

import ai.idealistic.spartan.Register;
import java.io.File;
import java.io.InputStream;
import java.io.ObjectInputStream;
import java.nio.file.Files;
import java.nio.file.OpenOption;
import java.nio.file.Path;
import java.util.Map;
import java.util.concurrent.ConcurrentHashMap;
import kireiko.dev.millennium.ml.logic.Logger;
import kireiko.dev.millennium.ml.logic.Model;
import kireiko.dev.millennium.ml.logic.Table;
import kireiko.dev.millennium.ml.logic.ModelVer;
import lombok.Generated;

public final class FactoryML {
    private static final Map<Integer, Model> runnableML = new ConcurrentHashMap<Integer, Model>();

    public static void createModel(int id, int tableSize, ModelVer modelVer) {
        FactoryML.createModel(id, tableSize, 10, modelVer);
    }

    public static Model createModel(int id, int tableSize, int stackSize, ModelVer modelVer) {
        switch (modelVer) {
            // Empty switch
        }
        Table millennium = new Table(tableSize, stackSize);
        runnableML.put(id, millennium);
        return millennium;
    }

    public static Model getModel(int id) {
        return runnableML.get(id);
    }

    public static void removeModel(int id) {
        runnableML.remove(id);
    }

    public static Model loadFromFile(int id, String fileName, int tableSize, int stackSize, ModelVer modelVer) {
        Model millennium;
        File dataFolder = Register.plugin.getDataFolder();
        File modelsFolder = new File(dataFolder, "models");
        if (!modelsFolder.exists()) {
            modelsFolder.mkdirs();
        }
        File modelFile = new File(modelsFolder, fileName);
        Path modelPath = modelFile.toPath();
        ObjectInputStream ois = new ObjectInputStream(Files.newInputStream(modelPath, new OpenOption[0]));
        try {
            Model model = (Model)ois.readObject();
            runnableML.put(id, model);
            Logger.info("ModelML was successfully loaded from " + modelFile.getPath() + " (" + model.parameters() + " parameters)");
            millennium = model;
        }
        catch (Throwable throwable) {
            try {
                try {
                    ois.close();
                }
                catch (Throwable throwable2) {
                    throwable.addSuppressed(throwable2);
                }
                throw throwable;
            }
            catch (Exception e) {
                FactoryML.createModel(id, tableSize, stackSize, modelVer);
                FactoryML.getModel(id).saveToFile(modelFile.getAbsolutePath());
                Logger.error(e.getMessage() + " | created new model!");
                return null;
            }
        }
        ois.close();
        return millennium;
    }

    public static Model loadFromResources(int id, String fileName, int tableSize, int stackSize, ModelVer modelVer) {
        String resourcePath = "/ml/" + fileName;
        try (InputStream resourceAsStream =  Register.class.getResourceAsStream(resourcePath);){
            Model millennium;
            try (ObjectInputStream ois = new ObjectInputStream(is);){
                Model model = (Model)ois.readObject();
                runnableML.put(id, model);
                Logger.info("ModelML was successfully loaded from " + resourcePath + " (" + model.parameters() + " parameters)");
                millennium = model;
            }
            return millennium;
        }
        catch (Exception e) {
            File dataFolder = new File("data");
            File modelsFolder = new File(dataFolder, "models");
            if (!modelsFolder.exists()) {
                modelsFolder.mkdirs();
            }
            File modelFile = new File(modelsFolder, fileName);
            Logger.error("Failed to load model: " + e.getMessage() + " | Well, created new one!");
            Model newModel = FactoryML.createModel(id, tableSize, stackSize, modelVer);
            newModel.saveToFile(modelFile.getAbsolutePath());
            return newModel;
        }
    }

    @Generated
    private FactoryML() {
        throw new UnsupportedOperationException("This is a utility class and cannot be instantiated");
    }
}

