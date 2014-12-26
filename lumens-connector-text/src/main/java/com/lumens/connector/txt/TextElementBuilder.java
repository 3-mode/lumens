/*
 * Copyright Lumens Team, Inc. All Rights Reserved.
 */

package com.lumens.connector.txt;
import com.lumens.model.DataElement;
import com.lumens.model.Element;
import com.lumens.model.Format;
import com.lumens.model.Value;
import com.lumens.tool.RFC4180Parser;
import java.util.List;
import java.util.ArrayList;
import java.util.Arrays;
import java.util.regex.*;

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
            // TODO: support specific escape char quote char. Need to build customize pattern
            String pattern = RFC4180Parser.GetFieldPattern();
            try{
                values.addAll(RFC4180Parser.ParserField(line));
            }catch(PatternSyntaxException ex){
                throw ex;
            }          
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
