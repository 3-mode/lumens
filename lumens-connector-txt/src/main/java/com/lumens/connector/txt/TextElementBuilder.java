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
    public static Element buildElement(Format fmt, String line) throws Exception {
        if( fmt == null || fmt.getChildren() == null || line == null )
            return null;
       
        String delimiter = fmt.getProperty(TextConstants.FILEDELIMITER).toString();
        String[] values = line.split(delimiter);
        Element elem = new DataElement(fmt);        
        List<Format> children = fmt.getChildren();
        int index = 0;
        for(Format child: children){
            elem.addChild(child.getName()).setValue(getValue(child, values[index++]));
        }
       
        return elem;        
     }
    
    private static Value getValue(Format fmt, String value) throws Exception {
        switch (fmt.getType()) {
            case BOOLEAN:
                return new Value(Boolean.parseBoolean(value));
            case BYTE:
            case SHORT:
            case INTEGER:
            case LONG:
                return new Value(Long.parseLong(value));
            case FLOAT:
            case DOUBLE:
                return new Value(Double.parseDouble(value));
            case STRING:
                return new Value(value);
            case DATE:                
                return new Value(new Date(value));
            default:
                throw new Exception("Not support data type");
        }
    }
}
