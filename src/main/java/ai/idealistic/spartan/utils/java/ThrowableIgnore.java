/*
 * Decompiled with CFR 0.152.
 *
 * Could not load the following classes:
 *  lombok.Generated
 */
package ai.idealistic.spartan.utils.java;

import java.util.Optional;
import java.util.function.Consumer;
import java.util.function.Predicate;
import lombok.Generated;

public final class ThrowableIgnore {
    public static ThrowableHandler throwableHandler = Throwable::printStackTrace;

    public static <false unchecked(Interface<T> supplier) {
        try {
            return supplier.get();
        }
        catch (Exception e) {
            ThrowableIgnore.doThrow0(e);
            throw new AssertionError();
        }
    }

    public static void unchecked(RunnableThrows runnable) {
        try {
            runnable.run();
        }
        catch (Exception e) {
            ThrowableIgnore.doThrow0(e);
            throw new AssertionError();
        }
    }

    public static <T> Predicate<T> unchecked(PredicateThrows<T> predicate) {
        return t -> {
            try {
                return predicate.test(t);
            }
            catch (Exception e) {
                ThrowableIgnore.doThrow0(e);
                throw new AssertionError();
            }
        };
    }

    public static <false ignore(Interface<T> supplier, T def) {
        try {
            return supplier.get();
        }
        catch (Throwable e) {
            throwableHandler.handle(e);
            return def;
        }
    }

    public static Optional<Throwable> ignore(RunnableThrows runnable) {
        try {
            runnable.run();
        }
        catch (Throwable e) {
            throwableHandler.handle(e);
            return Optional.of(e);
        }
        return Optional.empty();
    }

    public static Optional<Throwable> ignore(RunnableThrows runnable, Consumer<Throwable> consumer) {
        try {
            runnable.run();
        }
        catch (Throwable e) {
            consumer.accept(e);
            throwableHandler.handle(e);
            return Optional.of(e);
        }
        return Optional.empty();
    }

    public static <T> Predicate<T> ignore(PredicateThrows<T> predicate, boolean def) {
        return t -> {
            try {
                return predicate.test(t);
            }
            catch (Throwable e) {
                throwableHandler.handle(e);
                return def;
            }
        };
    }

    public static <E extends Throwable> void doThrow0(Throwable e) throws E {
        throw e;
    }

    @Generated
    private ThrowableIgnore() {
        throw new UnsupportedOperationException("This is a utility class and cannot be instantiated");
    }

    public static interface Interface<T> {
        public T get() throws Exception;
    }

    public static interface RunnableThrows {
        public void run() throws Exception;
    }

    public static interface PredicateThrows<T> {
        public boolean test(T var1) throws Exception;
    }

    public static interface ThrowableHandler {
        public void handle(Throwable var1);
    }
}

