/*
 * Copyright Lumens Team, Inc. All Rights Reserved.
 */
package com.lumens.connector.txt;

import java.io.BufferedReader;
import java.io.BufferedWriter;
import java.io.InputStreamReader;
import java.io.OutputStreamWriter;
import java.io.FileInputStream;
import java.io.FileOutputStream;

import com.lumens.model.Format;
import com.lumens.model.Element;
import com.lumens.model.Value;
import java.util.List;
import java.util.ArrayList;
import java.util.Map;



/**
 *
 * @author Xiaoxin(whiskeyfly@163.com)
 */
public class TextClient {
    String filepath;
    BufferedReader reader;
    BufferedWriter writer;
    boolean bInit = false;
    Map<String, Value> propList;

    public TextClient(Map<String, Value> props) {
        propList = props;
    }

    public List<Element> read(Element elem) {
        List<Element> result = new ArrayList();
        String encoding = elem.getChild(TextConstants.ENCODING) == null ? 
                propList.get(TextConstants.ENCODING).toString()         : 
                elem.getChild(TextConstants.ENCODING).getValue().toString();
        String path = elem.getChild(TextConstants.PATH) == null ? 
                propList.get(TextConstants.PATH).toString()     : 
                elem.getChild(TextConstants.PATH).getValue().toString();
        String delimiter = elem.getChild(TextConstants.FILEDELIMITER) == null ?  
                propList.get(TextConstants.FILEDELIMITER).toString()          : 
                elem.getChild(TextConstants.FILEDELIMITER).getValue().toString();

        try {
            reader = new BufferedReader(new InputStreamReader(new FileInputStream(path), encoding));
            String line;
            while ((line = reader.readLine()) != null) {
                Element build = TextElementBuilder.buildElement(elem.getFormat(), line, delimiter);
                result.add(build);
            }
        } catch (Exception ex) {
            throw new RuntimeException(ex);
        }

        return result;
    }

    public void write(Element elem, boolean append) {
        try {
            String encoding = elem.getChild(TextConstants.ENCODING) == null ? 
                    propList.get(TextConstants.ENCODING).toString()     :
                    elem.getChild(TextConstants.ENCODING).getValue().toString();
            String path = elem.getChild(TextConstants.PATH) == null ? 
                    propList.get(TextConstants.PATH).toString()     :
                    elem.getChild(TextConstants.PATH).getValue().toString();
            String delimiter = elem.getChild(TextConstants.FILEDELIMITER) == null ? 
                    propList.get(TextConstants.FILEDELIMITER).toString()     :
                    elem.getChild(TextConstants.FILEDELIMITER).getValue().toString();
            String linedelimter = elem.getChild(TextConstants.LINEDELIMITER) == null ? 
                    propList.get(TextConstants.LINEDELIMITER).toString()     :
                    elem.getChild(TextConstants.LINEDELIMITER).getValue().toString();
            writer = new BufferedWriter(new OutputStreamWriter(new FileOutputStream(path, append), encoding));
            for (Element child : elem.getChildren()) {
                Element fields;
                if (elem.getChild(TextConstants.FORMAT_FIELDS) != null) {
                    fields = child.getChild(TextConstants.FORMAT_FIELDS);
                } else {
                    fields = child;
                }
                String line = "";
                for (Element field : fields.getChildren()) {
                    line = delimiter + field.getValue().toString();
                }
                writer.write(line);
                if (linedelimter.isEmpty())
                    writer.newLine();
                else
                    writer.write(linedelimter);
            }
            writer.flush();
            writer.close();
        } catch (Exception ex) {
            throw new RuntimeException(ex);
        }
    }
}
