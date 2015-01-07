/*
 * Copyright Lumens Team, Inc. All Rights Reserved.
 */

package com.lumens.connector.txt;

import java.util.ArrayList;
import java.util.List;
import java.util.regex.Matcher;
import java.util.regex.Pattern;
import java.util.regex.PatternSyntaxException;

/**
 *
 * @author Xiaoxin(whiskeyfly@163.com)
 */
public abstract class PatternParser {
    protected static String TEXTDATA = "[\\x20-\\x21]|[\\x23-\\x2B]|[\\x2D-\\x7E]";
    protected static String ESCAPE = String.format("\"(?:%s+|,|\\r|\\n|\"\")+\"", TEXTDATA);
    protected static String NON_ESCAPE = String.format("(?:%s)+", TEXTDATA);
    protected static String FIELD = String.format("%s|%s", ESCAPE, NON_ESCAPE);
    protected static Pattern defaultPattern = Pattern.compile(FIELD);

    public PatternParser(String text, String escape, String nonEscape){
        TEXTDATA = text;
        ESCAPE = escape;
        NON_ESCAPE = nonEscape;
    }
    
    public static String GetTextDataPattern() {
        return TEXTDATA;
    }

    public static String GetEscapePattern() {
        return ESCAPE;
    }

    public static String GetNonEscapePattern() {
        return NON_ESCAPE;
    }

    public static String GetFieldPattern() {
        return FIELD;
    }

    // PatternSyntaxException throw if Pattern parse error
    public static List<String> ParseField(String pattern, String field) throws PatternSyntaxException {
        Pattern p = pattern.equals(FIELD) ? defaultPattern : Pattern.compile(pattern);
        Matcher m = p.matcher(field);

        List<String> list = new ArrayList();
        while (m.find()) {
            list.add(m.group());
        }

        return list;
    }

    public static List<String> ParseField(String field) {
        return ParseField(FIELD, field);
    }
}
