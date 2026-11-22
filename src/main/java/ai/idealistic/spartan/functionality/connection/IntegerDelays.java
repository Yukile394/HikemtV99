/*
 * Decompiled with CFR 0.152.
 *
 * Could not load the following classes:
 *  org.bukkit.Location
 */
package ai.idealistic.spartan.functionality.connection;

import ai.idealistic.spartan.utils.java.OverflowMap;
import java.util.ArrayList;
import java.util.Collections;
import java.util.LinkedList;
import java.util.List;
import java.util.Map;
import java.util.concurrent.ConcurrentHashMap;
import org.bukkit.Location;

public class IntegerDelays {
    private static final Map<Integer, LinkedList<Location>> cache = new OverflowMap<Integer, LinkedList<Location>>(new ConcurrentHashMap(), 1024);
    private static final Map<Integer, Long> lastPacket = new OverflowMap<Integer, Long>(new ConcurrentHashMap(), 1024);
    private static final Map<Integer, List<Integer>> delays = new OverflowMap<Integer, List<Integer>>(new ConcurrentHashMap(), 1024);

    public static void add(int id, Location location) {
        cache.computeIfAbsent(id, k -> new LinkedList()).addFirst(location);
        List delaysList = delays.computeIfAbsent(id, k -> Collections.synchronizedList(new LinkedList()));
        delaysList.add((int)(System.currentTimeMillis() - lastPacket.getOrDefault(id, System.currentTimeMillis())));
        lastPacket.put(id, System.currentTimeMillis());
        IntegerDelays.newPacket(id);
        if (delaysList.size() == 10) {
            List<Integer> l = Collections.synchronizedList(new LinkedList());
            l.add(IntegerCollections.max(delaysList) / 2);
            delays.put(id, l);
        }
    }

    public static void newPacket(int id) {
        lastPacket.put(id, System.currentTimeMillis());
    }

    public static Location getLocationWithTickRollback(int id, int ticks) {
        LinkedList<Location> locationStack = cache.get(id);
        if (locationStack == null || locationStack.isEmpty()) {
            return null;
        }
        return locationStack.get(ticks >= locationStack.size() ? locationStack.size() - 1 : ticks);
    }

    public static int getPlayerTicksDelay(int id) {
        List<Integer> delaysList = delays.get(id);
        if (delaysList != null) {
            ArrayList<Integer> snapshot;
            synchronized (delaysList) {
                snapshot = new ArrayList<Integer>(delaysList);
            }
            return Collections.max(snapshot) / 50;
        }
        return 0;
    }
}

