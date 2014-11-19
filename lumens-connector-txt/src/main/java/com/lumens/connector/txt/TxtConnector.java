
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
    private Map<String, Format> txtFmt;
    private TxtClient txtCnt;
    private Direction direction;
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
        if (props.containsKey(TxtConstants.ENCODING))        
            encoding = props.get(TxtConstants.ENCODING).getString();
        
        if (props.containsKey(TxtConstants.PATH))
            path = props.get(TxtConstants.PATH).getString();

        if (props.containsKey(TxtConstants.LINEDELIMITER))
            linedelimiter = props.get(TxtConstants.LINEDELIMITER).getString();        

        if (props.containsKey(TxtConstants.FILEDELIMITER))
            filedelimiter = props.get(TxtConstants.FILEDELIMITER).getString(); 
        
        if (props.containsKey(TxtConstants.ESCAPECHAR))
            escapechar = props.get(TxtConstants.ESCAPECHAR).getString();         
    }
        
    @Override
    public boolean isOpen(){
        return isOpen;
    }            

    // To craete specific client in type of DOM or SAX 
    @Override
    public void open(){
        if ( txtCnt == null) {
            txtCnt = new TxtClient(this);
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
        return txtCnt.getFormatList(direction);

    }

    @Override
    public Format getFormat(Format format, String path, Direction direction){
        return txtCnt.getFormat(format, path, direction);
    }     
}
