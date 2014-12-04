/*
 * Copyright Lumens Team, Inc. All Rights Reserved.
 */
package com.lumens.connector.txt;

import com.lumens.connector.Direction;
import com.lumens.connector.FormatBuilder;
import com.lumens.model.DataFormat;
import com.lumens.model.Format;
import com.lumens.model.Type;
import com.lumens.model.Format.Form;
import com.lumens.model.Value;
import java.util.Map;

import org.dom4j.Attribute;
import org.dom4j.Document;
import org.dom4j.Element;
import org.dom4j.io.SAXReader;
import java.io.File;
import java.util.HashMap;
import java.util.Iterator;
/**
 *
 * @author Xiaoxin(whiskeyfly@163.com)
 */
public class TextFormatBuilder implements FormatBuilder{
    private String xmlSchemaPath;    
    private SAXReader schemaReader; 
    private Element schemaRoot;
    private String encoding;
    Map<String, Value> propList;
    
    public TextFormatBuilder(Map<String, Value> props){
        if( props.containsKey(TextConstants.SCHEMAPATH))
            xmlSchemaPath = props.get(TextConstants.SCHEMAPATH).toString();
        propList = props;
        schemaRoot = null;
    }
    
    @Override
    public void initalize(){        
        try{
           File file = new File(xmlSchemaPath);
           if( file.isFile() && file.exists() ){
               schemaReader = new SAXReader();
               Document doc = schemaReader.read(xmlSchemaPath);
               schemaRoot = doc.getRootElement(); 
               encoding = doc.getXMLEncoding();                 
           }           
        }catch (Exception ex) {  
            throw new RuntimeException(ex);
        }
     }

    @Override
    public Map<String, Format> getFormatList(Direction direction){
        Map<String, Format> fmtList = new HashMap<>();
        String rootName = schemaRoot.getName();
        if( rootName.equalsIgnoreCase( "format") ){            
            Format rootFmt =  new DataFormat(rootName, Format.Form.STRUCT);
            fmtList.put(rootName, rootFmt);
            rootFmt.setProperty(TextConstants.ENCODING, new Value(encoding));
            if (propList.containsKey(TextConstants.LINEDELIMITER))
                rootFmt.setProperty(TextConstants.LINEDELIMITER, new Value(propList.get(TextConstants.LINEDELIMITER).getString()));
            if (propList.containsKey(TextConstants.FILEDELIMITER))
                rootFmt.setProperty(TextConstants.FILEDELIMITER, new Value(propList.get(TextConstants.FILEDELIMITER).getString()));            
            Format fields = rootFmt.addChild(TextConstants.FIELDS, Form.STRUCT);            
            getFormat(fields, null, direction);            
        }
        
        return fmtList;
    }

    @Override
    public Format getFormat(Format format, String path, Direction direction){
        if( format.getName().equalsIgnoreCase("format") ){
            Iterator itor = schemaRoot.elementIterator();
            Format fields = format.getChild(TextConstants.FIELDS);
            while( itor.hasNext() ){
                Element column = (Element)itor.next();
                String elemName = column.getName();
                if( elemName.equalsIgnoreCase("field") ){                    
                    Iterator attrItor = column.attributeIterator();
                    Format field = null;
                    
                    //Note that to be simply coding, the name in schema MUST be the first property
                    while( attrItor.hasNext() ){
                        Attribute attr = (Attribute) attrItor.next();
                        String name = attr.getName();   
                        String value = attr.getValue();    
                                            
                        // create field element
                        if( name != null && name.equalsIgnoreCase("name") && !value.isEmpty() ){
                            field = new DataFormat(name, Form.FIELD);   // set type later
                            fields.addChild(field);
                        }else if( name != null && name.equalsIgnoreCase("key") && !value.isEmpty() ){                                                      
                            field.setType(Type.parseString(value));                           
                        }else if( name != null && name.equalsIgnoreCase("type") && !value.isEmpty() ){                                                      
                            field.setProperty(name, new Value(Type.parseString(value)));                           
                        }else if( name != null && name.equalsIgnoreCase("nullable") && !value.isEmpty() ){                                                      
                            field.setProperty(name, new Value(Type.parseString(value)) );                          
                        }else if( name != null && name.equalsIgnoreCase("pattern") && !value.isEmpty() ){    
                            field.setProperty(name, new Value(value));
                        }else if( name != null && name.equalsIgnoreCase("length") && !value.isEmpty() ){    
                            field.setProperty(name, new Value(Type.parseString(value)));
                        }else if( name != null && name.equalsIgnoreCase("comment") && !value.isEmpty() ){    
                            field.setProperty(name, new Value(value));
                        }else if( name != null && name.equalsIgnoreCase("precision") && !value.isEmpty() ){    
                            field.setProperty(name, new Value(Integer.parseInt(value)));
                        }     
                    }
                }                    
            }     
        }
        
        return format;
    }
}
