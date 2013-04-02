/*
 * To change this template, choose Tools | Templates
 * and open the template in the editor.
 */
package com.lumens.processor;

/**
 *
 * @author shaofeng wang
 */
public class ProcessorUtils
{
    public static boolean isPathFormat(String input)
    {
        if (input != null && !input.isEmpty() && input.charAt(0) == '@')
            return true;
        return false;
    }

    public static String getAccessPath(String input)
    {
        if (isPathFormat(input))
            return input.substring(1);
        return null;
    }
}
