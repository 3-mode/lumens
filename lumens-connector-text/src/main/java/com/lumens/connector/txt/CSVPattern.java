/*
 * Copyright Lumens Team, Inc. All Rights Reserved.
 */

package com.lumens.connector.txt;

import java.util.ArrayList;
import java.util.Map;
import java.util.HashMap;
import java.util.List;
import java.util.regex.Matcher;
import java.util.regex.Pattern;
import java.util.regex.PatternSyntaxException;
import com.lumens.model.Value;

/**
 *
 * @author Xiaoxin(whiskeyfly@163.com)
 */
public abstract class CSVPattern {
    protected String TEXTDATA;
    protected String ESCAPE;
    protected String NON_ESCAPE;
    protected String FIELD;
    protected Pattern defaultPattern;
    protected Map<String, Value> options = new HashMap();

    public CSVPattern(String text, String escape, String nonEscape, String field){
        TEXTDATA = text;
        ESCAPE = escape;
        NON_ESCAPE = nonEscape;
        FIELD = String.format("%s|%s", ESCAPE, NON_ESCAPE);
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
        String group;
        
        boolean bTrim = options.containsKey(TextConstants.OPTION_TRIM_SPACE) && options.get(TextConstants.OPTION_TRIM_SPACE).getBoolean();
        while (m.find()) {
            group = m.group();
            if (bTrim)
                group = group.trim();
            list.add(group);
        }

        return list;
    }

    public List<String> ParseField(String field) {
        return ParseField(FIELD, field);
    }
    
    public void SetOption(String key, Value v){
        Value val;
        if ((val = options.get(key)) != null){
            val.set(v);
        }else{
            options.put(key, v);
        }
    }
}
