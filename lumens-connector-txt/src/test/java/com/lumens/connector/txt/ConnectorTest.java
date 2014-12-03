/*
 * Copyright Lumens Team, Inc. All Rights Reserved.
 */

package com.lumens.connector.txt;

import com.lumens.connector.Direction;
import com.lumens.model.Format;
import com.lumens.model.Value;
import java.io.BufferedReader;
import java.io.File;
import java.io.FileInputStream;
import java.io.InputStreamReader;
import java.util.HashMap;
import java.util.Map;

import org.junit.Test;

/**
 *
 * @author Xiaoxin(whiskeyfly@163.com)
 */
public class ConnectorTest 
{
    private String path = getClass().getResource("/delimited/incsv.csv").getFile();
    private String schemaPath = getClass().getResource("/delimited/incsv_schema.xml").getFile();
    
    @Test
    public void testConnection() {
        try{
            File file = new File(path);
            if( file.isFile() && file.exists() ){
                InputStreamReader reader = new InputStreamReader(new FileInputStream(path), "UTF-8");
                BufferedReader bufReader = new BufferedReader(reader);
                String lineTxt = null;
                while((lineTxt = bufReader.readLine()) != null ){
                    System.out.println(lineTxt);                    
                }
            }     
        }catch (Exception e) {
            System.out.println("Error on reading file content.");
            e.printStackTrace();
        }
    }    
    
    @Test
    public void testConnectorRead(){
        
    }
    
    @Test
    public void testSchemaRead(){
        File schema = new File(schemaPath);
        Map<String, Value> propList = new HashMap<>();
        propList.put(TextConstants.ENCODING, new Value("UTF-8"));
        propList.put(TextConstants.ESCAPECHAR, new Value("\\"));
        propList.put(TextConstants.FILEDELIMITER, new Value(","));
        propList.put(TextConstants.SCHEMAPATH, new Value(schemaPath));
        propList.put(TextConstants.MAXLINE, new Value(1000));
        propList.put(TextConstants.ENCODING, new Value("UTF-8"));
        if( schema.isFile() && schema.exists() ){
            TextFormatBuilder xsdReader = new TextFormatBuilder(propList);
            xsdReader.initalize();
            Map<String, Format> formats = xsdReader.getFormatList(Direction.IN);
        }       
    }
    
    @Test
    public void testClientRead(){    
        
    }
    
    @Test
    public void testClientWrite(){    
        
    }
    
    @Test
    public void testElementBuilder(){
        
    }
}
