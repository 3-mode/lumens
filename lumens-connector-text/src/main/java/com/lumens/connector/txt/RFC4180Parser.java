/*
 * Copyright Lumens Team, Inc. All Rights Reserved.
 */
package com.lumens.connector.txt;

/**
 *
 * @author Xiaoxin(whiskeyfly@163.com)
 */
// Refer to RFC4180 code: http://tools.ietf.org/html/rfc4180#page-2 
public class RFC4180Parser  extends CSVPattern{
    // [\x20-\x21]|[\x23-\x2B]|[\x2D-\x7E]
    private final static String TEXTDATA = "[\\x20-\\x21]|[\\x23-\\x2B]|[\\x2D-\\x7E]";
    // "([\x20-\x21]|[\x23-\x2B]|[\x2D-\x7E]|,|\r|\n|"")+"
    private final static String ESCAPE = String.format("\"(?:%s+|,|\\r|\\n|\"\")+\"", TEXTDATA);
    // (?:[\x20-\x21]|[\x23-\x2B]|[\x2D-\x7E])+
    private final static String NON_ESCAPE = String.format("(?:%s)+", TEXTDATA);
    // "([\x20-\x21]|[\x23-\x2B]|[\x2D-\x7E]|,|\r|\n|"")+"|(?:[\x20-\x21]|[\x23-\x2B]|[\x2D-\x7E])+ 
    private final static String FIELD = String.format("%s|%s", ESCAPE, NON_ESCAPE); 
    
    public RFC4180Parser(){
        super(TEXTDATA, ESCAPE, NON_ESCAPE, FIELD);       
    }
}
