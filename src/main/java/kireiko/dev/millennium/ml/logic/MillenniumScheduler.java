/*
 * Decompiled with CFR 0.152.
 *
 * Could not load the following classes:
 *  com.google.common.util.concurrent.ThreadFactoryBuilder
 *  lombok.Generated
 */
package kireiko.dev.millennium.ml.logic;

import ai.idealistic.spartan.utils.java.ThrowableIgnore;
import com.google.common.util.concurrent.ThreadFactoryBuilder;
import java.lang.reflect.Field;
import java.util.concurrent.Callable;
import java.util.concurrent.Executors;
import java.util.concurrent.Future;
import java.util.concurrent.ScheduledExecutorService;
import java.util.concurrent.ScheduledFuture;
import java.util.concurrent.TimeUnit;
import java.util.function.Function;
import java.util.logging.Logger;
import lombok.Generated;

public final class MillenniumScheduler {
    @Generated
    private static final Logger log = Logger.getLogger(MillenniumScheduler.class.getName());
    private static final char INNER_CLASS_SEPARATOR_CHAR = '$';
    public static int STOP_WATCH_TIME_MILLIS = 2500;
    private static ScheduledExecutorService scheduler = Executors.newScheduledThreadPool(20, new ThreadFactoryBuilder().setNameFormat("Millennium/ML Thread %d").build());

    private MillenniumScheduler() {
    }

    public static void shutdown() {
        ThrowableIgnore.ignore(() -> scheduler.shutdownNow());
    }

    public static Future<?> run(Runnable runnable) {
        return scheduler.submit(new DecoratedRunnable(runnable));
    }

    public static <T> Future<T> run(Callable<T> callable) {
        return scheduler.submit(new DecoratedCallable<T>callable);
    }

    public static ScheduledFuture<?> later(Runnable runnable, long delay, TimeUnit time) {
        return scheduler.schedule(new DecoratedRunnable(runnable), delay, time);
    }

    public static ScheduledFuture<?> timer(Runnable runnable, long delay, long period, TimeUnit time) {
        return scheduler.scheduleAtFixedRate(new DecoratedRunnable(runnable), delay, period, time);
    }

    public static void cancel(ScheduledFuture<?> timer) {
        try {
            if (timer != null) {
                timer.cancel(true);
            }
        }
        catch (Exception exception) {
            // empty catch block
        }
    }

    public static String toString(Object object) {
        if (object == null) {
            return "null";
        }
        Class<?> clazz = object.getClass();
        StringBuilder sb = new StringBuilder(clazz.getSimpleName() + "{");
        Field[] fields = clazz.getDeclaredFields();
        for (i < fields.length; ++i) {
            Field field = fields[i];
            field.setAccessible(true);
            try {
                if (field.getName().indexOf(36) != -1) {
                    sb.append(field.getName()).append("=");
                    Object value = field.get(object);
                    sb.append(value == null ? "null" : value.toString());
                }
            }
            catch (IllegalAccessException e) {
                sb.append(field.getName()).append("=<access denied>");
            }
            if (i >= fields.length - 1) continue;
            sb.append(", ");
        }
        sb.append("}");
        return sb.toString();
    }

    @Generated
    public static ScheduledExecutorService getScheduler() {
        return scheduler;
    }

    public static class DecoratedRunnable
    implements Runnable {
        private static Function<Runnable, Runnable> hotfixDecorator = runnable -> runnable;
        private final Runnable originalRunnable;
        private final Runnable decoratedRunnable;

        public DecoratedRunnable(Runnable originalRunnable) {
            this.decoratedRunnable = hotfixDecorator.apply(originalRunnable);
        }

        @Override
        public void run() {
            long start = System.currentTimeMillis();
            try {
                this.decoratedRunnable.run();
            }
            catch (Throwable e) {
                log.severe("Error during execution of asynchronous task " + MillenniumScheduler.toString(this.originalRunnable));
                e.printStackTrace();
                if (e instanceof InterruptedException) {
                    throw e;
                }
            }
            finally {
                long after = System.currentTimeMillis() - start;
                if (after > (long)STOP_WATCH_TIME_MILLIS) {
                    return;
                }
            }
        }

        @Generated
        public String toString() {
            return "MillenniumScheduler.DecoratedRunnable(originalRunnable=" + this.originalRunnable + ", decoratedRunnable=" + this.decoratedRunnable + ")";
        }

        @Generated
        public static void setHotfixDecorator(Function<Runnable, Runnable> hotfixDecorator) {
            DecoratedRunnable.hotfixDecorator = hotfixDecorator;
        }
    }

    public static class DecoratedCallable<T>
    implements Callable<T> {
        private static Function<Callable<?>, Callable<?>> hotfixDecorator = callable -> callable;
        private final Callable<T> originalCallable;
        private final Callable<T> decoratedCallable;

        public DecoratedCallable(Callable<T> originalCallable) {
            this.decoratedCallable = hotfixDecorator.apply(originalCallable);
        }

        @Override
        public T call() throws Exception {
            long start = System.currentTimeMillis();
            try {
                T t = this.decoratedCallable.call();
                return t;
            }
            catch (Throwable e) {
                log.severe("Error while accepting to call method: " + MillenniumScheduler.toString(this.originalCallable));
                e.printStackTrace();
                throw e;
            }
            finally {
                long after = System.currentTimeMillis() - start;
                if (after > (long)STOP_WATCH_TIME_MILLIS) {
                    // empty if block
                }
            }
        }

        @Generated
        public String toString() {
            return "MillenniumScheduler.DecoratedCallable(originalCallable=" + this.originalCallable + ", decoratedCallable=" + this.decoratedCallable + ")";
        }

        @Generated
        public static void setHotfixDecorator(Function<Callable<?>, Callable<?>> hotfixDecorator) {
            DecoratedCallable.hotfixDecorator = hotfixDecorator;
        }
    }
}

