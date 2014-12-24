/*
 * Copyright Lumens Team, Inc. All Rights Reserved.
 */

package com.lumens.connector.txt;
import com.lumens.model.DataElement;
import com.lumens.model.Element;
import com.lumens.model.Format;
import com.lumens.model.Value;
import java.util.List;
import java.util.ArrayList;
import java.util.Arrays;
/**
 *
 * @author Xiaoxin(whiskeyfly@163.com)
 */
public class TextElementBuilder implements TextConstants{
    public static Element buildElement(Format fmt, String line, String delimiter, String escape, String quote) throws Exception {
        if( fmt == null || fmt.getChildren() == null || line == null )
            return null;
               
        boolean bEscape = (escape != null) && (!escape.isEmpty());
        
        List<String> values = new ArrayList();
        if (!bEscape){            
            values.addAll(Arrays.asList(line.split(delimiter)));
        }else{
            int len = line.length();
            int index = 0;
            int first = 0;
            while (index++ < len - 1){
                char current = line.charAt(index);
                char next = line.charAt(index+1);
                char quoteChar = quote.charAt(0);
                if (current == quoteChar && next != quoteChar ){
                    // Deal with first 
                }                
                // Deal with delimiter
            }
            // Add last string
            values.add(line.substring(first, index));
        }
        
        
        Element elem = new DataElement(fmt);   
             
        List<Format> children = fmt.getChildren();
        int index = 0;
        for(Format child: children){
            if(!child.getName().equalsIgnoreCase(TextConstants.FORMAT_PARAMS))
                elem.addChild(child.getName()).setValue(new Value(child.getType(), values.get(index++)));
        }
       
        return elem;        
     }   
}
