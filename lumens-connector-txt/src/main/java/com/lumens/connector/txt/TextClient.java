/*
 * Copyright Lumens Team, Inc. All Rights Reserved.
 */

package com.lumens.connector.txt;

import java.io.BufferedInputStream;
import java.io.BufferedReader;
import java.io.File;
import java.io.FileInputStream;
import java.io.InputStreamReader;

import com.lumens.connector.Connector;
import com.lumens.connector.Direction;
import com.lumens.model.Format;
import java.io.InputStream;
import java.io.OutputStream;
import java.util.Map;

/**
 *
 * @author Xiaoxin(whiskeyfly@163.com)
 */
public class TextClient {
    protected TextConnector xmlCntr;     
    
    public TextClient(TextConnector cnt){
        xmlCntr = cnt;
    }
    
    public void init()
    {        
    }
           
    public void read(InputStream ins, String encoding){        

    }            
    
    public void write(OutputStream ous){
        
    }      
}
