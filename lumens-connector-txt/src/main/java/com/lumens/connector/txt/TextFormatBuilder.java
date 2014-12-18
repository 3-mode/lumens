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
public class TextFormatBuilder implements FormatBuilder {
    private String xmlSchemaPath;
    private SAXReader schemaReader;
    private Element schemaRoot;
    private String encoding;
    Map<String, Value> propList;

    public TextFormatBuilder(Map<String, Value> props) {
        if (props.containsKey(TextConstants.SCHEMAPATH))
            xmlSchemaPath = props.get(TextConstants.SCHEMAPATH).toString();
        propList = props;
        schemaRoot = null;
    }

    @Override
    public void initalize() {
        try {
            File file = new File(xmlSchemaPath);
            if (file.isFile() && file.exists()) {
                schemaReader = new SAXReader();
                Document doc = schemaReader.read(xmlSchemaPath);
                schemaRoot = doc.getRootElement();
                encoding = doc.getXMLEncoding();
            }
        } catch (Exception ex) {
            throw new RuntimeException(ex);
        }
    }

    @Override
    public Map<String, Format> getFormatList(Direction direction) {
        Map<String, Format> fmtList = new HashMap<>();
        String rootName = schemaRoot.getName();
        if (rootName.equalsIgnoreCase(TextConstants.FORMAT_FORMAT)) {
            Format rootFmt = new DataFormat(rootName, Format.Form.STRUCT);
            fmtList.put(rootName, rootFmt);
            rootFmt.setProperty(TextConstants.ENCODING, new Value(encoding));
           
            getFormat(rootFmt, null, direction);
        }

        return fmtList;
    }

    @Override
    public Format getFormat(Format format, String path, Direction direction) {
        if (format.getName().equalsIgnoreCase(TextConstants.FORMAT_FORMAT)) {
            Iterator fieldItor = schemaRoot.elementIterator();
   
            Iterator p = fieldItor;
            Format fmt = format;
            int level = 0;
            while( p.hasNext() ){
                Element columnNode = (Element)p.next();
                String node = columnNode.getName();
                if( node.equalsIgnoreCase(TextConstants.FORMAT_FIELD) ){   
                    Format field = null;
                    Iterator attrItor = columnNode.attributeIterator();     

                    //Note that to be simply coding, the name in schema MUST be the first property
                    while (attrItor.hasNext()) {
                        Attribute attr = (Attribute) attrItor.next();
                        String name = attr.getName();
                        String value = attr.getValue();

                        // create field element
                        if( name != null && name.equalsIgnoreCase(TextConstants.FORMAT_NAME) && !value.isEmpty() ){
                            field = new DataFormat(value, Form.FIELD);   // set type later
                            fmt.addChild(field);
                        }else if( name != null && name.equalsIgnoreCase(TextConstants.FORMAT_KEY) && !value.isEmpty() ){                                                      
                            field.setProperty(name, new Value(value));                           
                        }else if( name != null && name.equalsIgnoreCase(TextConstants.FORMAT_TYPE) && !value.isEmpty() ){                                                      
                            field.setType(Type.parseString(value));                           
                        }else if( name != null && name.equalsIgnoreCase(TextConstants.FORMAT_NULLABLE) && !value.isEmpty() ){                                                      
                            field.setProperty(name, new Value(value) );                          
                        }else if( name != null && name.equalsIgnoreCase(TextConstants.FORMAT_PATTERN) && !value.isEmpty() ){    
                            field.setProperty(name, new Value(value));
                        }else if( name != null && name.equalsIgnoreCase(TextConstants.FORMAT_LENGTH) && !value.isEmpty() ){    
                            field.setProperty(name, new Value(Integer.parseInt(value)));
                        }else if( name != null && name.equalsIgnoreCase(TextConstants.FORMAT_COMMENT) && !value.isEmpty() ){    
                            field.setProperty(name, new Value(value));
                        } else if (name != null && name.equalsIgnoreCase(TextConstants.FORMAT_PRECISION) && !value.isEmpty()) {
                            field.setProperty(name, new Value(Integer.parseInt(value)));
                        }
                    }
                }else if ( node.equalsIgnoreCase(TextConstants.FORMAT_PARAMS) ){
                    Iterator paramItor = columnNode.elementIterator();
                    if( paramItor.hasNext() ){
                        p = paramItor;
                        fmt = fmt.addChild(TextConstants.FORMAT_PARAMS, Form.STRUCT, Type.STRING);
                        level++;
                    }
                }
                
                if( !p.hasNext() && level > 0){
                    p = fieldItor;
                    fmt = fmt.getParent();
                    level--;
                }
            }
        }

        return format;
    }
}
