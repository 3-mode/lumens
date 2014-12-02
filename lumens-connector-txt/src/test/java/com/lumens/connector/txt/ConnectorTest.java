/*
 * Copyright Lumens Team, Inc. All Rights Reserved.
 */

package com.lumens.connector.txt;

import com.lumens.connector.Direction;
import java.io.BufferedReader;
import java.io.File;
import java.io.FileInputStream;
import java.io.InputStreamReader;

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
    public void testSchema(){
        File schema = new File(schemaPath);
        if( schema.isFile() && schema.exists() ){
            TextFormatBuilder xsdReader = new TextFormatBuilder(schemaPath);
            xsdReader.initalize();
            xsdReader.getFormatList(Direction.IN);
        }       
    }
}
