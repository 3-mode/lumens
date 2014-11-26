/*
 * Copyright Lumens Team, Inc. All Rights Reserved.
 */
package com.lumens.connector.txt;

import com.lumens.connector.Direction;
import com.lumens.connector.FormatBuilder;
import com.lumens.model.DataFormat;
import com.lumens.model.Format;
import com.lumens.model.Format.Form;
import com.lumens.model.Value;
import java.io.BufferedReader;
import java.io.InputStreamReader;
import java.util.Map;

import org.dom4j.io.SAXReader;
import org.dom4j.Attribute;
import org.dom4j.Document;
import org.dom4j.DocumentHelper;
import org.dom4j.Element;
import org.dom4j.io.OutputFormat;
import org.dom4j.io.SAXReader;
import org.dom4j.io.XMLWriter;
import org.dom4j.io.SAXValidator;
import org.dom4j.DocumentException;
import org.dom4j.util.XMLErrorHandler;
import java.io.File;
import java.util.HashMap;
import java.util.Iterator;
/**
 *
 * @author Xiaoxin(whiskeyfly@163.com)
 */
public class FormatFromXmlSchemaBuilder implements FormatBuilder{
    private String xmlSchemaPath;    
    private SAXReader schemaReader; 
    private Element schemaRoot;
    private String encoding;
    
    public FormatFromXmlSchemaBuilder(String schemaPath){
        xmlSchemaPath = schemaPath;
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
        if( rootName.toLowerCase() == "format" ){            
            Format rootFmt =  new DataFormat(rootName, Format.Form.STRUCT);
            fmtList.put(rootName, rootFmt);
            rootFmt.addChild(TextConstants.FIELDS, Form.STRUCT);
            rootFmt.setProperty(TextConstants.ENCODING, new Value(encoding));
            getFormat(rootFmt, null, direction);            
        }
        
        return fmtList;
    }

    @Override
    public Format getFormat(Format format, String path, Direction direction){
        if( format.getName().toLowerCase() == "format" ){
            Iterator itor = schemaRoot.elementIterator();
            Format fields = format.getChild(TextConstants.FIELDS);
            while( itor.hasNext() ){
                Element column = (Element)itor.next();
                String elemName = column.getName();
                if( elemName.toLowerCase() == "field" ){                    
                    Iterator attrItor = column.attributeIterator();
                    Format field = null;
                    while( attrItor.hasNext() ){
                        Attribute attr = (Attribute) attrItor.next();
                        String name = attr.getName();   
                        String value = attr.getValue();                        
                        
                        if( name.toLowerCase() == "name" && !value.isEmpty() ){
                            field = new DataFormat(value, Form.FIELD); 
                            fields.addChild(field);
                        }                        
                        if( field != null &&  name.toLowerCase() != "name"){   
                            field.setProperty(name, new Value(value));
                        }          
                    }
                }                    
            }     
        }
        
        return format;
    }
}
