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
public class TextFormatBuilder implements FormatBuilder, TextConstants {
    private final String xmlSchemaPath;
    private SAXReader schemaReader;
    private Element schemaRoot;
    private String encoding;
    private boolean isInitalized = false;

    public TextFormatBuilder(String schemaPath) {
        xmlSchemaPath = schemaPath;
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
        if (!isInitalized) {
            throw new RuntimeException("TextFormatBuilder not initalized.");
        }
        Map<String, Format> fmtList = new HashMap<>();
        String rootName = schemaRoot.getName();
        Attribute nameAttribute = schemaRoot.attribute(FORMAT_NAME);
        String formatName = nameAttribute == null ? rootName : nameAttribute.getValue();

        if (rootName.equalsIgnoreCase(FORMAT_FORMAT)) {
            Format rootFmt = new DataFormat(formatName, Form.STRUCT);
            fmtList.put(formatName, rootFmt);

            // This encoding is for schema file itself rather than processing encoding
            rootFmt.setProperty(SCHEMA_ENCODING, new Value(encoding));
            Format params = rootFmt.addChild(FORMAT_PARAMS, Form.STRUCT);            
            params.addChild(PATH, Form.FIELD, Type.STRING);
            params.addChild(OPERATION, Form.FIELD, Type.STRING);

            getFormat(rootFmt, null, direction);
        }

        return fmtList;
    }

    @Override
    public Format getFormat(Format format, String path, Direction direction) {
        if (!isInitalized) {
            throw new RuntimeException("TextFormatBuilder not initalized.");
        }
        Iterator fieldItor = schemaRoot.elementIterator();
        while (fieldItor.hasNext()) {
            Element columnNode = (Element) fieldItor.next();
            String node = columnNode.getName();
            if (FORMAT_FIELD.equalsIgnoreCase(node)) {
                Format field = null;
                Iterator attrItor = columnNode.attributeIterator();
                //Note that to be simply coding, the name in schema MUST be the first property
                while (attrItor.hasNext()) {
                    Attribute attr = (Attribute) attrItor.next();
                    String attrName = attr.getName();
                    String attrValue = attr.getValue();
                    // create field element
                    if (!attrValue.isEmpty()) {
                        if (FORMAT_NAME.equalsIgnoreCase(attrName))
                            field = format.addChild(attrValue, Form.FIELD); // Set type later
                        if ( field == null)
                            throw new RuntimeException("First format element must be " + FORMAT_NAME);
                        if (FORMAT_KEY.equalsIgnoreCase(attrName))
                            field.setProperty(attrName, new Value(attrValue));
                        else if (FORMAT_TYPE.equalsIgnoreCase(attrName))
                            field.setType(Type.parseString(attrValue));
                        else if (FORMAT_NULLABLE.equalsIgnoreCase(attrName))
                            field.setProperty(attrName, new Value(attrValue));
                        else if (FORMAT_PATTERN.equalsIgnoreCase(attrName))
                            field.setProperty(attrName, new Value(attrValue));
                        else if (FORMAT_LENGTH.equalsIgnoreCase(attrName))
                            field.setProperty(attrName, new Value(Integer.parseInt(attrValue)));
                        else if (FORMAT_COMMENT.equalsIgnoreCase(attrName))
                            field.setProperty(attrName, new Value(attrValue));
                        else if (FORMAT_PRECISION.equalsIgnoreCase(attrName))
                            field.setProperty(attrName, new Value(Integer.parseInt(attrValue)));
                    }
                }
            }
        }

        return format;
    }
}
