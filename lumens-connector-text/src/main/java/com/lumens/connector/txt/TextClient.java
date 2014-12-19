/*
 * Copyright Lumens Team, Inc. All Rights Reserved.
 */
package com.lumens.connector.txt;

import com.lumens.model.DataElement;
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

    public List<Element> read(Element elem, Format fmt) {
        List<Element> result = new ArrayList();        
        Element param = elem.getChild(TextConstants.FORMAT_PARAMS);
        String encoding = param.getChild(TextConstants.ENCODING) == null ? 
                propList.get(TextConstants.ENCODING).toString()         : 
                param.getChild(TextConstants.ENCODING).getValue().toString();        
        String path = param.getChild(TextConstants.PATH) == null ? 
                propList.get(TextConstants.PATH).toString()     : 
                param.getChild(TextConstants.PATH).getValue().toString();
        String delimiter = param.getChild(TextConstants.FILEDELIMITER) == null ?  
                propList.get(TextConstants.FILEDELIMITER).toString()          : 
                param.getChild(TextConstants.FILEDELIMITER).getValue().toString();
        String escape = param.getChild(TextConstants.ESCAPECHAR) == null ? 
                propList.get(TextConstants.ESCAPECHAR).toString()         : 
                param.getChild(TextConstants.ESCAPECHAR).getValue().toString();
        boolean ignoreEmptyLine = param.getChild(TextConstants.OPTION_IGNORE_EMPTYLINE) == null ?
                propList.get(TextConstants.OPTION_IGNORE_EMPTYLINE).getBoolean():
                param.getChild(TextConstants.OPTION_IGNORE_EMPTYLINE).getValue().getBoolean();
        
        int maxLine = param.getChild(TextConstants.OPTION_MAXLINE) == null ?
                propList.get(TextConstants.OPTION_MAXLINE).getInt():
                param.getChild(TextConstants.OPTION_MAXLINE).getValue().getInt();
        
        try {
            reader = new BufferedReader(new InputStreamReader(new FileInputStream(path), encoding));
            String line;
            while ((line = reader.readLine()) != null) {
                if (maxLine > 0 && --maxLine <= 0)
                    break;
                if (line.isEmpty() && ignoreEmptyLine)
                    continue;
                Element build = TextElementBuilder.buildElement(fmt, line, delimiter);                
                result.add(build);
            }
        } catch (Exception ex) {
            throw new RuntimeException(ex);
        }

        return result;
    }

    public void write(Element elem, boolean append) {
        Element param = elem.getChild(TextConstants.FORMAT_PARAMS);        
        String encoding = param.getChild(TextConstants.ENCODING) == null
                ? propList.get(TextConstants.ENCODING).toString()
                : param.getChild(TextConstants.ENCODING).getValue().toString();
        String path = param.getChild(TextConstants.PATH) == null
                ? propList.get(TextConstants.PATH).toString()
                : param.getChild(TextConstants.PATH).getValue().toString();
        String delimiter = param.getChild(TextConstants.FILEDELIMITER) == null
                ? propList.get(TextConstants.FILEDELIMITER).toString()
                : param.getChild(TextConstants.FILEDELIMITER).getValue().toString();
        String linedelimter = param.getChild(TextConstants.LINEDELIMITER) == null
                ? propList.get(TextConstants.LINEDELIMITER).toString()
                : param.getChild(TextConstants.LINEDELIMITER).getValue().toString();
        boolean formatAsTitle = param.getChild(TextConstants.OPTION_FORMAT_ASTITLE) == null
                ? propList.get(TextConstants.OPTION_FORMAT_ASTITLE).getBoolean()
                : param.getChild(TextConstants.OPTION_FORMAT_ASTITLE).getValue().getBoolean();

        int maxline = param.getChild(TextConstants.OPTION_MAXLINE) == null ?
                propList.get(TextConstants.OPTION_MAXLINE).getInt():
                param.getChild(TextConstants.OPTION_MAXLINE).getValue().getInt();
        
        try {
            writer = new BufferedWriter(new OutputStreamWriter(new FileOutputStream(path, append), encoding));
            
            String line = "";
            String title = "";
            for (Element field : elem.getChildren()) {
                if (field.getFormat().getName().equalsIgnoreCase(TextConstants.FORMAT_PARAMS)){
                    continue;
                }
                    
                if (formatAsTitle && !title.isEmpty()) {
                    title += delimiter;
                }                
                if (formatAsTitle) {
                    title += field.getFormat().getName();
                }

                if (!line.isEmpty()) {
                    line += delimiter;
                }
                line += field.getValue().toString();
            }
            if (formatAsTitle) {
                writer.write(title);
                if (linedelimter.isEmpty()) {
                    writer.newLine();
                } else {
                    writer.write(linedelimter);
                }
                formatAsTitle = false;
            }

            writer.write(line);
            if (linedelimter.isEmpty()) {
                writer.newLine();
            } else {
                writer.write(linedelimter);
            }
            
            writer.flush();
            writer.close();
        } catch (Exception ex) {
            throw new RuntimeException(ex);
        }
    }
}
