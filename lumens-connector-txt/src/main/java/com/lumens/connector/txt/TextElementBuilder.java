/*
 * Copyright Lumens Team, Inc. All Rights Reserved.
 */

package com.lumens.connector.txt;
import com.lumens.model.DataElement;
import com.lumens.model.Element;
import com.lumens.model.Format;
import com.lumens.model.Type;
import com.lumens.model.Value;
import java.sql.ResultSet;
import java.util.ArrayList;
import java.util.Date;
import java.util.List;
/**
 *
 * @author Xiaoxin(whiskeyfly@163.com)
 */
public class TextElementBuilder implements TextConstants{
    public static Element buildElement(Format fmt, String line, String delimiter) throws Exception {
        if( fmt == null || fmt.getChildren() == null || line == null )
            return null;
               
        String[] values = line.split(delimiter);
        Element elem = new DataElement(fmt);   
        Element fields;        
        if( fmt.getChild(TextConstants.FORMAT_FIELDS) == null )
            throw new Exception("Missing fields");
        
        fields = elem.addChild(TextConstants.FORMAT_FIELDS);        
        List<Format> children = fmt.getChild(TextConstants.FORMAT_FIELDS).getChildren();
        int index = 0;
        for(Format child: children){
            fields.addChild(child.getName()).setValue(new Value(child.getType(), values[index++]));
        }
       
        return elem;        
     }   
}
