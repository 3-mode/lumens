package com.lumens.processor;

/**
 *
 * @author shaofeng wang
 */
public class ProcessorUtils {

    public static boolean isPathFormat(String input) {
        // TODO need to enhance the algorithm for path format pattern
        if (input != null && !input.isEmpty() && input.charAt(0) == '@')
            return true;
        return false;
    }

    public static String getAccessPath(String input) {
        if (isPathFormat(input))
            return input.substring(1);
        return null;
    }
}
