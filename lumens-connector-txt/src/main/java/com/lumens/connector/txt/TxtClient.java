/*
 * To change this license header, choose License Headers in Project Properties.
 * To change this template file, choose Tools | Templates
 * and open the template in the editor.
 */

package com.lumens.connector.txt;

import com.lumens.connector.Direction;
import com.lumens.model.Format;
import java.io.InputStream;
import java.io.OutputStream;
import java.util.Map;

/**
 *
 * @author whiskey
 */
public abstract class TxtClient implements TxtInterface{
    protected TxtConnector xmlCntr;     
    
    abstract public void init();
           
    abstract public void read(InputStream ins);    
    
    abstract public void write(OutputStream ous);
    
    abstract public boolean validate(String xsdFileName);
        
    abstract public Map<String, Format> getFormatList(Direction direction); 
    
    abstract public Format getFormat(Format format, String path, Direction direction);
}
