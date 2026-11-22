/*
 * Decompiled with CFR 0.152.
 *
 * Could not load the following classes:
 *  lombok.Generated
 */
package ai.idealistic.spartan.utils.java;

import java.io.File;
import java.lang.reflect.Field;
import java.lang.reflect.Method;
import java.net.URL;
import java.net.URLClassLoader;
import lombok.Generated;

public final class ReflectionUtils {
    public static Class<?> getClass(String s) {
        try {
            return Class.forName(s);
        }
        catch (Exception e) {
            return null;
        }
    }

    public static boolean classExists(String s) {
        try {
            Class.forName(s);
        }
        catch (ClassNotFoundException e) {
            return false;
        }
        return true;
    }

    public static Object getFieldFromJar(String jarFilePath, String className, String fieldName) {
        Object object;
        File jarFile = new File(jarFilePath);
        URL jarURL = jarFile.toURI().toURL();
        URLClassLoader classLoader = new URLClassLoader(new URL[]{jarURL});
        try {
            Class<?> clazz = classLoader.loadClass(className);
            Field field = clazz.getDeclaredField(fieldName);
            field.setAccessible(true);
            object = field.get(null);
        }
        catch (Throwable throwable) {
            try {
                try {
                    classLoader.close();
                }
                catch (Throwable throwable2) {
                    throwable.addSuppressed(throwable2);
                }
                throw throwable;
            }
            catch (Exception e) {
                return null;
            }
        }
        classLoader.close();
        return object;
    }

    public static Object invokeMethodFromJar(String jarFilePath, String className, String methodName, Class<?>[] parameterTypes, Object[] args) {
        Object object;
        File jarFile = new File(jarFilePath);
        URL jarURL = jarFile.toURI().toURL();
        URLClassLoader classLoader = new URLClassLoader(new URL[]{jarURL});
        try {
            Class<?> clazz = classLoader.loadClass(className);
            Method method = clazz.getDeclaredMethod(methodName, parameterTypes);
            method.setAccessible(true);
            object = method.invoke(null, args);
        }
        catch (Throwable throwable) {
            try {
                try {
                    classLoader.close();
                }
                catch (Throwable throwable2) {
                    throwable.addSuppressed(throwable2);
                }
                throw throwable;
            }
            catch (Exception e) {
                return null;
            }
        }
        classLoader.close();
        return object;
    }

    @Generated
    private ReflectionUtils() {
        throw new UnsupportedOperationException("This is a utility class and cannot be instantiated");
    }
}

