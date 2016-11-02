package seedu.todo.commons.util;

import com.google.common.base.CharMatcher;
import org.apache.commons.lang3.StringUtils;

import java.io.PrintWriter;
import java.io.StringWriter;
import java.util.Arrays;
import java.util.List;
import java.util.StringJoiner;

/**
 * Helper functions for handling strings.
 */
public class StringUtil {
    /**
     * Returns a detailed message of the t, including the stack trace.
     */
    public static String getDetails(Throwable t){
        assert t != null;
        StringWriter sw = new StringWriter();
        t.printStackTrace(new PrintWriter(sw));
        return t.getMessage() + "\n" + sw.toString();
    }

    /**
     * Returns true if s represents an unsigned integer e.g. 1, 2, 3, ... <br>
     *   Will return false for null, empty string, "-1", "0", "+1", and " 2 " (untrimmed) "3 0" (contains whitespace).
     * @param s Should be trimmed.
     */
    public static boolean isUnsignedInteger(String s){
        return s != null && s.matches("^0*[1-9]\\d*$");
    }

    //@@author A0135817B
    /**
     * Returns true if the string is null, of length zero, or contains only whitespace 
     */
    public static boolean isEmpty(String s) {
        return s == null || s.length() == 0 || CharMatcher.whitespace().matchesAllOf(s);
    }

    //@@author A0135805H
    /**
     * Partitions the string into three parts:
     *      string[0 .. position - 1], string[position], string[position + 1 .. length - 1], all index inclusive
     * @param string to be partitioned
     * @param position location where the string should be partitioned
     * @return a String array containing the three elements stated above,
     *         where each element must not be null, but can have empty string.
     */
    public static String[] partitionStringAtPosition(String string, int position) {
        String[] stringArray = new String[3];
        if (string == null || string.isEmpty() || position < 0 || position >= string.length()) {
            stringArray[0] = "";
            stringArray[1] = "";
            stringArray[2] = "";
        } else {
            stringArray[0] = string.substring(0, position);
            stringArray[1] = string.substring(position, position + 1);
            stringArray[2] = string.substring(position + 1, string.length());
        }
        return stringArray;
    }

    /**
     * Splits string at only space and comma.
     * @return Returns a String array with all the split components of the string.
     */
    public static String[] splitString(String string) {
        if (string == null || string.isEmpty()) {
            return new String[0];
        } else {
            return string.trim().split("[,\\s]+");
        }
    }

    /**
     * Given a string list, gets the text from the list in the following manner:
     *      apple, pear, pineapple
     */
    public static String convertListToString(String[] stringList) {
        if (stringList == null || stringList.length == 0) {
            return "";
        }
        StringJoiner stringJoiner = new StringJoiner(", ");
        for (String string : stringList) {
            stringJoiner.add(string);
        }
        return stringJoiner.toString();
    }

    /**
     * Given an iterable, join them together with comma as shown:
     *      apple, pear, pineapple
     */
    public static String convertIterableToString( Iterable<?> iterable) {
        if (iterable == null) {
            return "";
        }
        return StringUtils.join(iterable, ", ");
    }

    //@@author A0139021U
    /**
     * Calculates the levenstein distance between the two strings and returns
     * their closeness in a percentage score.
     * @param s1 The first string
     * @param s2 The second string
     * @return The percentage score of their closeness
     */
    public static double calculateClosenessScore(String s1, String s2) {
        // empty string, not close at all
        if (isEmpty(s1) || isEmpty(s2)) {
            return 0d;
        }

        s1 = s1.toLowerCase();
        s2 = s2.toLowerCase();
        int distance = StringUtils.getLevenshteinDistance(s1, s2);
        double ratio = ((double) distance) / (Math.max(s1.length(), s2.length()));
        return 100 - ratio * 100;
    }
}
