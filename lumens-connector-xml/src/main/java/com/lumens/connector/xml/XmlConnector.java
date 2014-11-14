/**
 * @author whiskey
 * Features:
 *   1, Abort on execute error
 * Properities:
 *   1, File path for query
 *   2, Query by xPath for loop
 * Types:
 *   1, String
 *   2, Object
 *   3, Byte|byte|byte[] *   
 *   4, Char|character
 *   5, Date
 *   6, double | Double
 *   7, Int
 * Parsing Modes:
 *   1, Dom4j: fast, memory consumed
 *   2, Xerces: Memory-consuming
 *   3, SAX: Fast and less memory consumed
 * Enconding:
 *   1, ISO-8859-1
 *   2, UTF-8
 *   3, Custom
 */
package com.lumens.connector.xml;

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

public class XmlConnector implements Connector{
    private boolean isOpen = false;
    private Map<String, Format> formatListIn;
    private Map<String, Format> formatListOut;
    private FormatBuilder formatBuilder;
    private Map<String, Format> xmlFmt;
    private XmlClient xml;
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
            xml = XmlClientFactory.createSAXClient(this);
            if (direction == direction.IN)
            {
                try{
                    // TODO: check key existing outside
                    InputStream ins = new FileInputStream(propList.get(XmlConstants.XMLPATH).getString());
                    xml.read(ins);
                    isOpen = true;
                }catch(FileNotFoundException e){
                    e.printStackTrace();
                }
                
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
        return new XmlOperation();
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
