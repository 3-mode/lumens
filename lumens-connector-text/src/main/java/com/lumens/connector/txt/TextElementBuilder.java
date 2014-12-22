/*
 * Copyright Lumens Team, Inc. All Rights Reserved.
 */

package com.lumens.connector.txt;
import com.lumens.model.DataElement;
import com.lumens.model.Element;
import com.lumens.model.Format;
import com.lumens.model.Value;
import java.util.List;
/**
 *
 * @author Xiaoxin(whiskeyfly@163.com)
 */
public class TextElementBuilder implements TextConstants{
    public static Element buildElement(Format fmt, String line, String delimiter, String escape) throws Exception {
        if( fmt == null || fmt.getChildren() == null || line == null )
            return null;
               
        boolean bEscape = (escape != null) && (!escape.isEmpty());
        if (bEscape){            
        }
        
        String[] values = line.split(delimiter);
        Element elem = new DataElement(fmt);   
             
        List<Format> children = fmt.getChildren();
        int index = 0;
        for(Format child: children){
            if(!child.getName().equalsIgnoreCase(TextConstants.FORMAT_PARAMS))
                elem.addChild(child.getName()).setValue(new Value(child.getType(), values[index++]));
        }
       
        return elem;        
     }   
}
