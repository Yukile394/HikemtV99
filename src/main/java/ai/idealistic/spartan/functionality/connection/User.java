/*
 * Decompiled with CFR 0.152.
 */
package ai.idealistic.spartan.functionality.connection;

import ai.idealistic.spartan.utils.math.Distance;
import java.util.Objects;

public class User {
    public static final String user = "644411";
    public static final String file = "c42d160b1445b5136c36d11864a433c5";
    public static final String resource = "11196";
    public static final boolean enabled = Distance.validInteger("11196");
    public static final boolean builtByBit = 4 != 18;
    public static final boolean polymart = 16 == 1;
    public static final boolean spigot = !builtByBit && !polymart && enabled;
    private static String userModified = null;
    private static String fileModified = null;

    static void set(int user, int nonce) {
        userModified = Integer.toString(user);
        fileModified = Integer.toString(nonce);
    }

    public static String user() {
        return userModified != null ? userModified : user;
    }

    public static String file() {
        if (enabled) {
            String f;
            String string = f = fileModified != null ? fileModified : file;
            if (!f.startsWith("%%__") && !Distance.validInteger(f)) {
                f = String.valueOf(Objects.hash(f));
            }
            return f;
        }
        return User.user();
    }

    static String platform() {
        return "Free";
    }

    public static boolean canAdvertise() {
        return !enabled;
    }
}

