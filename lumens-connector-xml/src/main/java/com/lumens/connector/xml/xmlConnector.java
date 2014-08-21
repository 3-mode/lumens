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
    private boolean isOpen;
    private Map<String, Format> formatListIn;
    private Map<String, Format> formatListOut;
    private FormatBuilder formatBuilder;
    private Map<String, Format> xmlProps;
    private xmlClient xml;
    
    public void setPropertyList(Map<String, Value> params){
        
    }
    
    
    public boolean isOpen(){
        return isOpen;
    }            

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
        return xmlProps;
    }

    public Format getFormat(Format format, String path, Direction direction){
        return null;
    }   
}
