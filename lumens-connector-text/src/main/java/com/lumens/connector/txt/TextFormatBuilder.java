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
    private Map<String, Value> propList;
    private boolean isInitalized = false;

    public TextFormatBuilder(Map<String, Value> props) {
        if (props.containsKey(TextConstants.SCHEMA_PATH))
            xmlSchemaPath = props.get(TextConstants.SCHEMA_PATH).toString();
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
                isInitalized = true;
            }
        } catch (Exception ex) {
            throw new RuntimeException(ex);
        }       
    }

    @Override
    public Map<String, Format> getFormatList(Direction direction) {
        if (!isInitalized){
            throw new RuntimeException("TextFormatBuilder not initalized.");
        }
        Map<String, Format> fmtList = new HashMap<>();        
        String rootName = schemaRoot.getName();
        Attribute nameAttribute = schemaRoot.attribute(TextConstants.FORMAT_NAME);
        String formatName = nameAttribute == null ? rootName : nameAttribute.getValue();        
        
        if (rootName.equalsIgnoreCase(TextConstants.FORMAT_FORMAT)) {
            Format rootFmt = new DataFormat(formatName, Form.STRUCT);
            fmtList.put(formatName, rootFmt);
            
            // This encoding is for schema file itself rather than processing encoding
            rootFmt.setProperty(TextConstants.SCHEMA_ENCODING, new Value(encoding));
           
            Format params = rootFmt.addChild(TextConstants.FORMAT_PARAMS, Form.STRUCT);
            params.addChild(TextConstants.ENCODING, Form.FIELD, Type.STRING);
            params.addChild(TextConstants.PATH, Form.FIELD, Type.STRING);
            params.addChild(TextConstants.LINEDELIMITER, Form.FIELD, Type.STRING);
            params.addChild(TextConstants.FILEDELIMITER, Form.FIELD, Type.STRING);
            params.addChild(TextConstants.ESCAPE_CHAR, Form.FIELD, Type.STRING);
            params.addChild(TextConstants.OPTION_MAXLINE, Form.FIELD, Type.STRING);
            params.addChild(TextConstants.OPERATION, Form.FIELD, Type.STRING);
            
            getFormat(rootFmt, null, direction);
        }

        return fmtList;
    }

    @Override
    public Format getFormat(Format format, String path, Direction direction) {   
        if (!isInitalized){
            throw new RuntimeException("TextFormatBuilder not initalized.");
        }        
        Iterator fieldItor = schemaRoot.elementIterator();
        while (fieldItor.hasNext()) {
            Element columnNode = (Element) fieldItor.next();
            String node = columnNode.getName();
            if (node.equalsIgnoreCase(TextConstants.FORMAT_FIELD)) {
                Format field = null;
                Iterator attrItor = columnNode.attributeIterator();

                //Note that to be simply coding, the name in schema MUST be the first property
                while (attrItor.hasNext()) {
                    Attribute attr = (Attribute) attrItor.next();
                    String name = attr.getName();
                    String value = attr.getValue();

                    // create field element
                    if (name != null && name.equalsIgnoreCase(TextConstants.FORMAT_NAME) && !value.isEmpty()) {
                        field = new DataFormat(value, Form.FIELD);   // set type later
                        format.addChild(field);
                    } else if (name != null && name.equalsIgnoreCase(TextConstants.FORMAT_KEY) && !value.isEmpty()) {
                        field.setProperty(name, new Value(value));
                    } else if (name != null && name.equalsIgnoreCase(TextConstants.FORMAT_TYPE) && !value.isEmpty()) {
                        field.setType(Type.parseString(value));
                    } else if (name != null && name.equalsIgnoreCase(TextConstants.FORMAT_NULLABLE) && !value.isEmpty()) {
                        field.setProperty(name, new Value(value));
                    } else if (name != null && name.equalsIgnoreCase(TextConstants.FORMAT_PATTERN) && !value.isEmpty()) {
                        field.setProperty(name, new Value(value));
                    } else if (name != null && name.equalsIgnoreCase(TextConstants.FORMAT_LENGTH) && !value.isEmpty()) {
                        field.setProperty(name, new Value(Integer.parseInt(value)));
                    } else if (name != null && name.equalsIgnoreCase(TextConstants.FORMAT_COMMENT) && !value.isEmpty()) {
                        field.setProperty(name, new Value(value));
                    } else if (name != null && name.equalsIgnoreCase(TextConstants.FORMAT_PRECISION) && !value.isEmpty()) {
                        field.setProperty(name, new Value(Integer.parseInt(value)));
                    }
                }
            }
        }        

        return format;
    }
}
