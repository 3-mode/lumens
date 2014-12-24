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

import com.lumens.io.LocalFileNameFilter;
import com.lumens.model.Format;
import com.lumens.model.Element;
import com.lumens.model.Value;
import java.util.List;
import java.util.ArrayList;
import java.util.Map;
import java.io.File;


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
        String filter = param.getChild(TextConstants.FILE_FILTER) == null ? 
                propList.get(TextConstants.FILE_FILTER).toString()     : 
                param.getChild(TextConstants.FILE_FILTER).getValue().toString();        
        String delimiter = param.getChild(TextConstants.FILEDELIMITER) == null ?  
                propList.get(TextConstants.FILEDELIMITER).toString()          : 
                param.getChild(TextConstants.FILEDELIMITER).getValue().toString();
        String escape = param.getChild(TextConstants.ESCAPE_CHAR) == null ? 
                propList.get(TextConstants.ESCAPE_CHAR).toString()         : 
                param.getChild(TextConstants.ESCAPE_CHAR).getValue().toString();
        String quote = param.getChild(TextConstants.QUOTE_CHAR) == null ? 
                propList.get(TextConstants.QUOTE_CHAR).toString()         : 
                param.getChild(TextConstants.QUOTE_CHAR).getValue().toString();        
        boolean ignoreEmptyLine = param.getChild(TextConstants.OPTION_IGNORE_EMPTYLINE) == null ?
                propList.get(TextConstants.OPTION_IGNORE_EMPTYLINE).getBoolean():
                param.getChild(TextConstants.OPTION_IGNORE_EMPTYLINE).getValue().getBoolean();
        boolean formatAsTitle = param.getChild(TextConstants.OPTION_FORMAT_ASTITLE) == null
                ? propList.get(TextConstants.OPTION_FORMAT_ASTITLE).getBoolean()
                : param.getChild(TextConstants.OPTION_FORMAT_ASTITLE).getValue().getBoolean();        
        int maxLine = param.getChild(TextConstants.OPTION_MAXLINE) == null ?
                propList.get(TextConstants.OPTION_MAXLINE).getInt():
                param.getChild(TextConstants.OPTION_MAXLINE).getValue().getInt();
        
        if (delimiter.equals(escape)){
            throw new RuntimeException("Delimiter should not be equal to escape char.");
        }
        
        try {
            // Add file list
            List<File> files = new ArrayList();
            File fileOrDir = new File(path);
            if (!fileOrDir.exists()){
                throw new RuntimeException("Path not exist!");
            }
            if (fileOrDir.isFile()){
                files.add(new File(path));
            }else if (fileOrDir.isDirectory()){                
                for(File f : fileOrDir.listFiles(new LocalFileNameFilter(filter))){                       
                    if (f.isFile()) 
                        files.add(f);                   
                }                
            }
            
            for( File file: files){
                reader = new BufferedReader(new InputStreamReader(new FileInputStream(file), encoding));
                String line;
                while ((line = reader.readLine()) != null) {
                    if (maxLine > 0 && --maxLine <= 0) {
                        break;
                    }
                    if (line.isEmpty() && ignoreEmptyLine) {
                        continue;
                    }
                    
                    // TODO: deal with title while reading 
                    if (formatAsTitle){
                        
                    }
                    
                    Element build = TextElementBuilder.buildElement(fmt, line, delimiter, escape, quote);
                    result.add(build);
                }
            }
        } catch (Exception ex) {
            throw new RuntimeException(ex);
        }finally{
            try{
                reader.close();
            }catch (Exception ex) {                
            }
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
