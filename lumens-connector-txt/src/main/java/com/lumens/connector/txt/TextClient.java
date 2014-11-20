/*
 * To change this license header, choose License Headers in Project Properties.
 * To change this template file, choose Tools | Templates
 * and open the template in the editor.
 */

package com.lumens.connector.txt;

import com.lumens.connector.Connector;
import com.lumens.connector.Direction;
import com.lumens.model.Format;
import java.io.InputStream;
import java.io.OutputStream;
import java.util.Map;

/**
 *
 * @author whiskey
 */
public class TextClient {
    protected TextConnector xmlCntr;     
    
    public TextClient(TextConnector cnt){
        xmlCntr = cnt;
    }
    
    public void init()
    {
        
    }
           
    public void read(InputStream ins){
        
    }            
    
    public void write(OutputStream ous){
        
    }
        
    public Map<String, Format> getFormatList(Direction direction){
        return null;
    }
    
    public Format getFormat(Format format, String path, Direction direction){
        return null;
    }
}
