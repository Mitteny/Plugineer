package top.shjibi.plugineer.util;

import org.jetbrains.annotations.NotNull;

import java.util.List;
import java.util.regex.Pattern;

/**
 * A utility class for {@link String}
 */
public final class StringUtil {

    private StringUtil() {
    }

    private static final Pattern colorPattern = Pattern.compile("&([\\da-fk-orx])");
    private static final Pattern stripPattern = Pattern.compile("[&§]([\\da-fk-orx])");

    /**
     * Colors the message
     *
     * @param s The message to color
     * @return The colored message
     */
    @NotNull
    public static String color(@NotNull String s) {
        return colorPattern.matcher(s).replaceAll("§$1");
    }

    /**
     * Strips the message
     *
     * @param s The message to strip
     * @return the striped message
     */
    @NotNull
    public static String strip(@NotNull String s) {
        return stripPattern.matcher(s).replaceAll("");
    }

    /**
     * Removes a string from the given string
     *
     * @param s      The string to remove from
     * @param remove the strings to remove
     * @return The modified string
     */
    @NotNull
    public static String remove(@NotNull String s, @NotNull String... remove) {
        for (String r : remove) {
            s = s.replace(r, "");
        }
        return s;
    }

    /**
     * Joins a string between each of the elements
     *
     * @param s        string to join
     * @param elements the elements
     * @return the final string
     */
    @NotNull
    public static String join(@NotNull String s, @NotNull String... elements) {
        return join(s, 0, elements);
    }

    /**
     * Joins a string between each of the elements
     *
     * @param s        string to join
     * @param elements the elements
     * @return the final string
     */
    @NotNull
    public static String join(@NotNull String s, @NotNull List<String> elements) {
        return join(s, elements.toArray(new String[0]));
    }

    /**
     * Joins a string between each of the elements, starting from a specific index
     *
     * @param s        string to join between the elements
     * @param start    index to start from
     * @param elements the elements
     * @return the final string
     */
    @NotNull
    public static String join(@NotNull String s, int start, @NotNull String... elements) {
        if (start >= elements.length) start = 0;
        StringBuilder builder = new StringBuilder();

        for (int i = start; i < elements.length; i++) {
            builder.append(elements[i]).append(s);
        }

        return builder.substring(0, builder.length() - 1);
    }

}
