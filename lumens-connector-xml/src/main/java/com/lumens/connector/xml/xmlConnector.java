/*
 * To change this license header, choose License Headers in Project Properties.
 * To change this template file, choose Tools | Templates
 * and open the template in the editor.
 */

package com.lumens.connector.xml;

import com.lumens.connector.Connector;
import com.lumens.connector.FormatBuilder;
import com.lumens.connector.Operation;
import com.lumens.connector.Direction;
import com.lumens.model.Format;
import com.lumens.model.Value;
import java.util.Map;



/**
 *
 * @author whiskey
 */
public class xmlConnector implements Connector{
    private boolean isOpen = false;
    private Map<String, Format> formatListIn;
    private Map<String, Format> formatListOut;
    private FormatBuilder formatBuilder;
    private Map<String, Format> xmlFmt;
    private xmlClient xml;
    
    // xml properties 
    private String version;
    private String encoding;
    private String path;
    
    public void setPropertyList(Map<String, Value> props){
        if (props.containsKey(xmlConstants.VERSION))        
            version = props.get(xmlConstants.VERSION).getString();
        
        if (props.containsKey(xmlConstants.ENCODING))
            encoding = props.get(xmlConstants.ENCODING).getString();

        if (props.containsKey(xmlConstants.PATH))
            path = props.get(xmlConstants.PATH).getString();        
    }
        
    public boolean isOpen(){
        return isOpen;
    }            

    // To craete specific client in type of DOM or SAX 
    public void open(){
        if ( xml == null) {
            xml = new xmlClient(this);            
        }
        isOpen = true;
    }

    public void close(){
        isOpen = false;
    }

    public Operation getOperation(){
        return new xmlOperation();
    }

    public Map<String, Format> getFormatList(Direction direction){
        return null;
    }

    public Format getFormat(Format format, String path, Direction direction){
        return null;
    }   
    
    // xml connector special mathods

}
