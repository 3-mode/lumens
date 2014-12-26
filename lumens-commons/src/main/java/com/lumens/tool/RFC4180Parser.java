/*
 * Copyright Lumens Team, Inc. All Rights Reserved.
 */

package com.lumens.tool;
import java.util.regex.Matcher;
import java.util.regex.Pattern;
import java.util.ArrayList;
import java.util.List;
import java.util.regex.PatternSyntaxException;

/**
 *
 * @author Xiaoxin(whiskeyfly@163.com)
 */
// Refer to RFC4180 code: http://tools.ietf.org/html/rfc4180#page-2 
public class RFC4180Parser {
    // [\x20-\x21]|[\x23-\x2B]|[\x2D-\x7E]
    private final static String TEXTDATA  = "[\\x20-\\x21]|[\\x23-\\x2B]|[\\x2D-\\x7E]";       
    // "([\x20-\x21]|[\x23-\x2B]|[\x2D-\x7E]|,|\r|\n|"")+"
    private final static String ESCAPE = String.format("\"(?:%s+|,|\\r|\\n|\"\")+\"", TEXTDATA);
    // (?:[\x20-\x21]|[\x23-\x2B]|[\x2D-\x7E])+
    private final static String NON_ESCAPE = String.format("(?:%s)+", TEXTDATA);
    // "([\x20-\x21]|[\x23-\x2B]|[\x2D-\x7E]|,|\r|\n|"")+"|(?:[\x20-\x21]|[\x23-\x2B]|[\x2D-\x7E])+
    private final static String FIELD = String.format("%s|%s", ESCAPE, NON_ESCAPE);
       
    public static String GetTextDataPattern(){
        return TEXTDATA;
    }
    
    public static String GetEscapePattern(){
        return ESCAPE;
    }
    
    public static String GetNonEscapePattern(){
        return NON_ESCAPE;
    }
    
    public static String GetFieldPattern(){
        return FIELD;
    }
    
    // PatternSyntaxException throw if Pattern parse error
    public static List<String> ParserField(String pattern, String field) throws PatternSyntaxException{        
        Pattern p = Pattern.compile(field);
        Matcher m = p.matcher(field);
        
        List<String> list = new ArrayList();
        while (m.find()){                    
            list.add(m.group());
        }
        
        return list;
    }    
    
    public static List<String> ParserField(String field){   
        return ParserField(FIELD, field);
    }
}
