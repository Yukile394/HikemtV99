/*
 * Decompiled with CFR 0.152.
 *
 * Could not load the following classes:
 *  lombok.Generated
 */
package ai.idealistic.spartan.utils.java;

import lombok.Generated;

public final class TimeUtils {
    public static String convertMilliseconds(long milliseconds) {
        long millisecondsPerSecond = 1000L;
        long millisecondsPerMinute = 60000L;
        long millisecondsPerHour = 3600000L;
        long millisecondsPerDay = 86400000L;
        long millisecondsPerWeek = 604800000L;
        double daysPerMonth = 30.44;
        double daysPerYear = 365.25;
        long millisecondsPerMonth = 2630016000L;
        long millisecondsPerYear = 31557600000L;
        long remainingMilliseconds = milliseconds;
        long years = remainingMilliseconds / 31557600000L;
        long months = (remainingMilliseconds %= 31557600000L) / 2630016000L;
        long weeks = (remainingMilliseconds %= 2630016000L) / 604800000L;
        long days = (remainingMilliseconds %= 604800000L) / 86400000L;
        long hours = (remainingMilliseconds %= 86400000L) / 3600000L;
        long minutes = (remainingMilliseconds %= 3600000L) / 60000L;
        long seconds = (remainingMilliseconds %= 60000L) / 1000L;
        StringBuilder timeBuilder = new StringBuilder();
        if (years > 0L) {
            timeBuilder.append(years).append("y ");
        }
        if (months > 0L) {
            timeBuilder.append(months).append("m ");
        }
        if (weeks > 0L) {
            timeBuilder.append(weeks).append("w ");
        }
        if (days > 0L) {
            timeBuilder.append(days).append("d ");
        }
        if (hours > 0L) {
            timeBuilder.append(hours).append("h ");
        }
        if (minutes > 0L) {
            timeBuilder.append(minutes).append("m ");
        }
        if (seconds > 0L || timeBuilder.length() == 0) {
            timeBuilder.append(seconds).append("s ");
        }
        return timeBuilder.toString().trim();
    }

    @Generated
    private TimeUtils() {
        throw new UnsupportedOperationException("This is a utility class and cannot be instantiated");
    }
}

