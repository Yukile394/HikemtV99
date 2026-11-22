/*
 * Decompiled with CFR 0.152.
 *
 * Could not load the following classes:
 *  lombok.Generated
 *  org.bukkit.ChatColor
 */
package ai.idealistic.spartan.utils.java;

import java.nio.charset.Charset;
import java.nio.charset.StandardCharsets;
import java.util.ArrayList;
import java.util.Arrays;
import java.util.Base64;
import java.util.Collection;
import java.util.List;
import lombok.Generated;
import org.bukkit.ChatColor;

public final class StringUtils {
    private static final Charset UTF_8 = StandardCharsets.UTF_8;

    public static String newStringUtf8(byte[] var) {
        return new String(var, UTF_8);
    }

    public static String decodeBase64(String s) {
        try {
            return StringUtils.newStringUtf8(Base64.getDecoder().decode(s));
        }
        catch (IllegalArgumentException e) {
            return s;
        }
    }

    public static String encodeBase64(String s) {
        return Base64.getEncoder().encodeToString(s.getBytes());
    }

    public static String replaceBars(String s, String color) {
        return (color == null ? "" : "\u00a7" + color) + s.replace("_", " ");
    }

    public static String replaceBars2(String s, String color) {
        return (color == null ? "" : "\u00a7" + color) + s.replace("_", "-");
    }

    public static String replaceSpaces(String s) {
        return ChatColor.stripColor((String)s.replace(" ", "_"));
    }

    public static String reverseBars(String s) {
        return ChatColor.stripColor(Strings.replace("-", "_"));
    }

    public static String getClearColorSyntaxString(String s) {
        return s.replaceAll("&[a-z,-9]", "");
    }

    public static String getMultipleArgs(String[] args, int start) {
        StringBuilder s = new StringBuilder();
        for (int i = start; i < args.length; ++i) {
            s.append(args[i]).append(" ");
        }
        return s.substring(0, s.length() - 1);
    }

    public static List<String> constructDescription(String string, List<String> array, boolean space) {
        if (array == null) {
            array = new ArrayList<String>(40);
        }
        if (!string.isEmpty()) {
            boolean hasColor;
            String color = string.startsWith("\u00a7") ? string.substring(0, 2) : "";
            hasColor = !color.isEmpty();
            if (hasColor) {
                string = string.substring(2);
            }
            if (!string.isEmpty()) {
                if (space) {
                    array.add("");
                }
                int add = 40 + (hasColor ? 2 : 0);
                for (int level = 0; level <= string.length(); level += add) {
                    array.add(color + StringUtils.substring(string, level, level + add));
                }
            }
        }
        return array;
    }

    public static String toString(Object[] a, String separator) {
        int iMax = a.length - 1;
        if (iMax == -1) {
            return "";
        }
        StringBuilder b = new StringBuilder();
        for (int i = 0; i < a.length; ++i) {
            if (a[i] == null) continue;
            b.append(a[i]);
            if (i == iMax) {
                return b.toString();
            }
            b.append(separator);
        }
        return b.toString();
    }

    public static <E> String toString(Collection<E> a, String separator) {
        int iMax = a.size() - 1;
        if (iMax == -1) {
            return "";
        }
        StringBuilder b = new StringBuilder();
        int i = 0;
        for (E o : a) {
            if (o == null) continue;
            b.append(o);
            if (i == iMax) break;
            b.append(separator);
            ++i;
        }
        return b.toString();
    }

    public static String rearrange(String s) {
        char[] list = s.toCharArray();
        Arrays.sort(list);
        return new String(list);
    }

    public static String substring(String string, int start, int end) {
        int length = string.length();
        return start > length || start > end || start == end ? string : (end > length ? string.substring(start, length) : string.substring(start, end));
    }

    public static String escapeMetaCharacters(String inputString) {
        String[] metaCharacters;
        for (String metaCharacter : metaCharacters = new String[]{"\\", "^", "$", "{", "}", "[", "]", "(", ")", ".", "*", , "?", "|", "<", ">", "-", "&", "%"}) {
            if (!inputString.contains(metaCharacter)) continue;
            inputString = inputString.replace(metaCharacter, "\\" + metaCharacter);
        }
        return inputString;
    }

    public static boolean stringContainsPartOfArray(String[] array, String storedKey) {
        for (String toFind : array) {
            if (!storedKey.contains(toFind)) continue;
            return true;
        }
        return false;
    }

    public static int countMatches(String str, String sub) {
        int count = 0;
        int idx = 0;
        while ((idx = str.indexOf(sub, idx)) != -1) {
            ++count;
            idx += sub.length();
        }
        return count;
    }

    public static boolean isNumeric(char c) {
        return c == '0' || c == '1' || c == '2' || c == '3' || c == '4' || c == '5' || c == '6' || c == '7' || c == '8' || c == '9';
    }

    @Generated
    private StringUtils() {
        throw new UnsupportedOperationException("This is a utility class and cannot be instantiated");
    }
}

