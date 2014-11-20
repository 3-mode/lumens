/*
 * Copyright Lumens Team, Inc. All Rights Reserved.
 */

package com.lumens.connector.txt;

import com.lumens.processor.script.JavaScriptContext;
import java.io.BufferedInputStream;
import java.io.BufferedReader;
import java.io.File;
import java.io.FileInputStream;
import java.io.InputStreamReader;

import java.io.Reader;
import org.junit.After;
import org.junit.AfterClass;
import org.junit.Before;
import org.junit.BeforeClass;
import org.junit.Test;

/**
 *
 * @author Xiaoxin(whiskeyfly@163.com)
 */
public class ConnectorTest 
{
    private String path = "C:\\demo\\in\\incsv.csv";
    
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
}
