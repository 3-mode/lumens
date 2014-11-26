/*
 * Copyright Lumens Team, Inc. All Rights Reserved.
 */
package com.lumens.connector.txt;

import java.io.File;
import java.io.FileNotFoundException;  
import java.io.FileOutputStream;  
import java.io.FileInputStream;  
import java.io.IOException;  
import java.io.IOException;  
import java.io.InputStream;  
import java.io.OutputStream;  

import com.lumens.connector.Connector;
import com.lumens.connector.FormatBuilder;
import com.lumens.connector.Operation;
import com.lumens.connector.Direction;
import com.lumens.model.DataFormat;
import com.lumens.model.Format;
import com.lumens.model.Value;
import java.util.HashMap;
import java.util.Map;

/**
 *
 * @author Xiaoxin(whiskeyfly@163.com)
 */

public class TextConnector implements Connector{
    private boolean isOpen = false;
    private FormatBuilder formatBuilder;
    private TextClient textClient;
    Map<String, Value> propList;    
        
    // properties 
    private String encoding;
    private String path;  
    private String linedelimiter;  
    private String filedelimiter; 
    private String escapechar; 
    
    @Override
    public void setPropertyList(Map<String, Value> props){
        propList = props;
        if (props.containsKey(TextConstants.ENCODING))        
            encoding = props.get(TextConstants.ENCODING).getString();
        
        if (props.containsKey(TextConstants.PATH))
            path = props.get(TextConstants.PATH).getString();

        if (props.containsKey(TextConstants.LINEDELIMITER))
            linedelimiter = props.get(TextConstants.LINEDELIMITER).getString();        

        if (props.containsKey(TextConstants.FILEDELIMITER))
            filedelimiter = props.get(TextConstants.FILEDELIMITER).getString(); 
        
        if (props.containsKey(TextConstants.ESCAPECHAR))
            escapechar = props.get(TextConstants.ESCAPECHAR).getString();         
    }
        
    @Override
    public boolean isOpen(){
        return isOpen;
    }            

    // To craete specific client in type of DOM or SAX 
    @Override
    public void open(){
        if ( textClient == null) {
            textClient = new TextClient(this);
            formatBuilder = new FormatFromXmlSchemaBuilder(path);
            try{
                formatBuilder.initalize();
                isOpen = true;
            }catch(RuntimeException ex){
                throw ex;
            }
        }        
    }

    @Override
    public void close(){
        textClient = null;
        formatBuilder = null;
        isOpen = false;
    }

    @Override
    public Operation getOperation(){
        return new TextOperation();
    }

    @Override
    public Map<String, Format> getFormatList(Direction direction){
        return formatBuilder.getFormatList(direction);
    }

    @Override
    public Format getFormat(Format format, String path, Direction direction){
        return formatBuilder.getFormat(format, path, direction);
    }     
}
