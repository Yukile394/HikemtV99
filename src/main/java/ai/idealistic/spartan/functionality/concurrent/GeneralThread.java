/*
 * Decompiled with CFR 0.152.
 */
package ai.idealistic.spartan.functionality.concurrent;

import ai.idealistic.spartan.functionality.concurrent.CheckThread;
import java.util.ArrayList;
import java.util.Collections;
import java.util.LinkedList;
import java.util.List;
import java.util.Map;
import java.util.concurrent.ConcurrentHashMap;

public class GeneralThread {
    private static boolean enabled = true;
    private static final Map<String, Boolean> threads = new ConcurrentHashMap<String, Boolean>();

    public static void disable() {
        enabled = false;
        CheckThread.shutdown();
    }

    public static boolean isKnown(Thread thread) {
        return threads.containsKey(thread.getName());
    }

    public static class ThreadPool {
        private final Thread thread;
        private final List<Runnable> runnables = Collections.synchronizedList(new LinkedList());
        private boolean pause = false;

        public ThreadPool(long refreshRateInMilliseconds) {
            this.thread = new Thread(() -> this.threadPoolInit(refreshRateInMilliseconds));
            threads.put(this.thread.getName(), true);
            this.thread.start();
        }

        private void threadPoolInit(long refreshRateInMilliseconds) {
            while (enabled) {
                Runnable runnable;
                if (this.pause) {
                    Thread.sleep(refreshRateInMilliseconds);
                    continue;
                }
                List<Runnable> list = this.runnables;
                synchronized (list) {
                    runnable = this.runnables.isEmpty() ? null : this.runnables.remove(0);
                }
                if (runnable != null) {
                    runnable.run();
                    continue;
                }
                Thread.sleep(refreshRateInMilliseconds);
            }
            this.runnables.clear();
            threads.remove(Thread.currentThread().getName());
        }

        public boolean executeIfFreeElseHere(Runnable runnable) {
            List<Runnable> list = this.runnables;
            synchronized (list) {
                if (this.runnables.isEmpty()) {
                    return this.runnables.add(runnable);
                }
                runnable.run();
                return false;
            }
        }

        public boolean executeIfFree(Runnable runnable) {
            List<Runnable> list = this.runnables;
            synchronized (list) {
                if (!this.runnables.isEmpty()) {
                    return false;
                }
                this.runnables.add(runnable);
                return true;
            }
        }

        public boolean execute(Runnable runnable) {
            List<Runnable> list = this.runnables;
            synchronized (list) {
                return this.runnables.add(runnable);
            }
        }

        public boolean executeIfUnknownThreadElseHere(Runnable runnable) {
            if (GeneralThread.isKnown(Thread.currentThread())) {
                runnable.run();
                return true;
            }
            List<Runnable> list = this.runnables;
            synchronized (list) {
                return this.runnables.add(runnable);
            }
        }

        public boolean executeWithPriority(Runnable runnable) {
            List<Runnable> list = this.runnables;
            synchronized (list) {
                if (this.runnables.isEmpty()) {
                    return this.runnables.add(runnable);
                }
                ArrayList<Runnable> runnables = new ArrayList<Runnable>(this.runnables.size() + 1);
                runnables.add(runnable);
                runnables.addAll(this.runnables);
                this.runnables.clear();
                return this.runnables.addAll(runnables);
            }
        }

        public boolean pause() {
            boolean pause = this.pause;
            this.pause = true;
            return !pause;
        }

        public boolean resume() {
            boolean pause = this.pause;
            this.pause = false;
            return pause;
        }

        public long getID() {
            return this.thread.getId();
        }
    }
}

