/*
 * To change this license header, choose License Headers in Project Properties.
 * To change this template file, choose Tools | Templates
 * and open the template in the editor.
 */

package com.lumens.connector.xml;

import java.io.InputStream;
import com.lumens.model.serializer.FormatSerializer;

/**
 *
 * @author whiskey
 */
public class xmlClient {
    xmlConnector xml;
    
    xmlClient(xmlConnector cnt){
        xml = cnt;
    }
        
    String getVersion(){ return null;};
    //void open();
    //void close();
    
    public void init()
    {
    }
    
    public void parseXml(InputStream in)
    {
    }        
}
