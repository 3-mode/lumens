/*
 * Copyright Lumens Team, Inc. All Rights Reserved.
 */

package com.lumens.connector.txt;

import java.io.FileReader;
import java.io.BufferedReader;
import java.io.InputStreamReader;
import java.io.FileInputStream;
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
    boolean bInit = false;
    
    public TextClient(String path){
        filepath = path;
    }
    
    public void init()
    {                
            File file = new File(filepath);
            if( file.isFile() && file.exists() ){                
                bInit = true;
            }
    }
           
    public List<Element> read(Format fmt){      
        List<Element> result = new ArrayList();
        String encoding = fmt.getProperty(TextConstants.ENCODING).toString();

        try{           
            reader = new BufferedReader(new InputStreamReader(new FileInputStream(filepath), encoding));
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
    
    public void write(OutputStream ous){
        
    }      
}
