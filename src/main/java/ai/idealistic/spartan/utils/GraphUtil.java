/*
 * Decompiled with CFR 0.152.
 *
 * Could not load the following classes:
 *  lombok.Generated
 *  org.bukkit.ChatColor
 */
package ai.idealistic.spartan.utils;

import java.util.List;
import lombok.Generated;
import org.bukkit.ChatColor;

public final class GraphUtil {
    public static GraphResult getGraph(List<Double> values) {
        StringBuilder graph = new StringBuilder();
        double largest = 0.0;
        for (double value : values) {
            if (value <= largest) continue;
            largest = value;
        }
        int GRAPH_HEIGHT = 2;
        int positives = 0;
        int negatives = 0;
        for (int i = GRAPH_HEIGHT - 1; i > 0; --i) {
            StringBuilder sb = new StringBuilder();
            for (double index : values) {
                double value = (double)GRAPH_HEIGHT * index / largest;
                if (value > (double)i && value < (double)(i + 1)) {
                    ++positives;
                    sb.append(String.format("%s+", ChatColor.GREEN));
                    continue;
                }
                ++negatives;
                sb.append(String.format("%s-", ChatColor.RED));
            }
            graph.append((CharSequence)sb);
        }
        return new GraphResult(graph.toString(), positives, negatives);
    }

    public static class GraphResult {
        private final String graph;
        private final int positives;
        private final int negatives;

        @Generated
        public String getGraph() {
            return this.graph;
        }

        @Generated
        public int getPositives() {
            return this.positives;
        }

        @Generated
        public int getNegatives() {
            return this.negatives;
        }

        @Generated
        public GraphResult(String graph, int positives, int negatives) {
        }
    }
}

