
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

public class TxtConnector implements Connector{
    private boolean isOpen = false;
    private Map<String, Format> formatListIn;
    private Map<String, Format> formatListOut;
    private FormatBuilder formatBuilder;
    private Map<String, Format> xmlFmt;
    private TxtClient xml;
    private Direction direction;
    Map<String, Value> propList;
        
    // xml properties 
    //private String version;
    //private String encoding;
    //private String xmlpath;    
    
    @Override
    public void setPropertyList(Map<String, Value> props){
        propList = props;
        /*if (props.containsKey(XmlConstants.VERSION))        
            version = props.get(XmlConstants.VERSION).getString();
        
        if (props.containsKey(XmlConstants.ENCODING))
            encoding = props.get(XmlConstants.ENCODING).getString();

        if (props.containsKey(XmlConstants.XMLPATH))
            xmlpath = props.get(XmlConstants.XMLPATH).getString();        
                */
    }
        
    @Override
    public boolean isOpen(){
        return isOpen;
    }            

    // To craete specific client in type of DOM or SAX 
    @Override
    public void open(){
        if ( xml == null) {
            xml = TxtClientFactory.createTXTClient(this);
            if (direction == direction.IN)
            {
                
            }else
            {
                //TODO: Open for write
                isOpen = true;
            }
        }        
    }

    @Override
    public void close(){
        isOpen = false;
    }

    @Override
    public Operation getOperation(){
        return new TxtOperation();
    }

    @Override
    public Map<String, Format> getFormatList(Direction direction){
        return xml.getFormatList(direction);

    }

    @Override
    public Format getFormat(Format format, String path, Direction direction){
        return xml.getFormat(format, path, direction);
    }     
}
