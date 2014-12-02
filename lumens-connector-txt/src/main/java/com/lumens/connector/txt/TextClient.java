/*
 * Copyright Lumens Team, Inc. All Rights Reserved.
 */

package com.lumens.connector.txt;

import java.io.FileReader;
import java.io.BufferedReader;
import java.io.BufferedWriter;
import java.io.InputStreamReader;
import java.io.OutputStreamWriter;
import java.io.FileInputStream;
import java.io.FileOutputStream;
import java.io.File;

import com.lumens.model.Format;
import com.lumens.model.Element;
import java.io.OutputStream;
import java.util.List;
import java.util.ArrayList;

/**
 *
 * @author Xiaoxin(whiskeyfly@163.com)
 */
public class TextClient {    
    String filepath; 
    BufferedReader reader;
    BufferedWriter writer;
    boolean bInit = false;
    
    public TextClient(){       
    }    
           
    public List<Element> read(Format fmt){      
        List<Element> result = new ArrayList();
        String encoding = fmt.getProperty(TextConstants.ENCODING).toString();
        String path = fmt.getProperty(TextConstants.PATH).toString();

        try{           
            reader = new BufferedReader(new InputStreamReader(new FileInputStream(path), encoding));
            String line;
            while((line = reader.readLine())!=null){
                Element elem = TextElementBuilder.buildElement(fmt, line);
                result.add(elem);
            }
        }catch (Exception ex) {            
            throw new RuntimeException(ex);
        }

        return result;
    }            
    
    public void write(Element elem){
        try{
            Format fmt = elem.getFormat();
            String encoding = fmt.getProperty(TextConstants.ENCODING).toString();
            String path = fmt.getProperty(TextConstants.PATH).toString();
            writer  = new BufferedWriter(new OutputStreamWriter(new FileOutputStream(path), encoding ));
            Element fields;
            if( fmt.getChild(TextConstants.FIELDS) != null ){
                fields = elem.getChild(TextConstants.FIELDS);
            }
            else{
               fields = elem;
            }
            
            for( Element child: fields.getChildren()){
            }
            
            writer.flush();
            writer.close();
        }catch( Exception ex){
            throw new RuntimeException(ex);
        }
    }      
}
