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
    protected String TEXTDATA;
    protected String ESCAPE;
    protected String NON_ESCAPE;
    protected String FIELD = String.format("%s|%s", ESCAPE, NON_ESCAPE);
    protected Pattern defaultPattern;

    public PatternParser(String text, String escape, String nonEscape){
        TEXTDATA = text;
        ESCAPE = escape;
        NON_ESCAPE = nonEscape;
        defaultPattern = Pattern.compile(FIELD);
    }
    
    public String GetTextDataPattern() {
        return TEXTDATA;
    }

    public String GetEscapePattern() {
        return ESCAPE;
    }

    public String GetNonEscapePattern() {
        return NON_ESCAPE;
    }

    public String GetFieldPattern() {
        return FIELD;
    }

    // PatternSyntaxException throw if Pattern parse error
    public List<String> ParseField(String pattern, String field) throws PatternSyntaxException {
        Pattern p = pattern.equals(FIELD) ? defaultPattern : Pattern.compile(pattern);
        Matcher m = p.matcher(field);

        List<String> list = new ArrayList();
        while (m.find()) {
            list.add(m.group());
        }

        return list;
    }

    public List<String> ParseField(String field) {
        return ParseField(FIELD, field);
    }
}
