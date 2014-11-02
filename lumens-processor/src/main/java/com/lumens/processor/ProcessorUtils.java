package com.lumens.processor;

/**
 *
 * @author shaofeng wang
 */
public class ProcessorUtils {

    public static boolean isPathFormat(String input) {
        // TODO need to enhance the algorithm for path format pattern
        if (input != null && !input.isEmpty()) {
            // TODO it is a temp way to check @a.b.c + "test"
            String[] tokens = input.split(" ");
            if (tokens.length > 1)
                return false;
            if (tokens[0].charAt(0) == '@')
                return true;
        }
        return false;
    }

    public static String getAccessPath(String input) {
        if (isPathFormat(input))
            return input.substring(1);
        return null;
    }
}
