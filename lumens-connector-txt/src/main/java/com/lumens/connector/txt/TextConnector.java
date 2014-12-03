/*
 * Copyright Lumens Team, Inc. All Rights Reserved.
 */
package com.lumens.connector.txt;

import com.lumens.connector.Connector;
import com.lumens.connector.FormatBuilder;
import com.lumens.connector.Operation;
import com.lumens.connector.Direction;
import com.lumens.model.Format;
import com.lumens.model.Value;
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
           
    @Override
    public void setPropertyList(Map<String, Value> props){
        propList = props;
        // TODO:validate props     
    }
        
    @Override
    public boolean isOpen(){
        return isOpen;
    }            

    // To craete specific client in type of DOM or SAX 
    @Override
    public void open(){
        if ( textClient == null) {
            textClient = new TextClient();
            formatBuilder = new TextFormatBuilder(propList);
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
        return new TextOperation(textClient);
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
