/*
 * To change this license header, choose License Headers in Project Properties.
 * To change this template file, choose Tools | Templates
 * and open the template in the editor.
 */

package com.lumens.connector.xml;

import com.lumens.connector.Connector;
import com.lumens.connector.ConnectorFactory;

/**
 *
 * @author whiskey
 */
public class xmlConnectorFactory implements ConnectorFactory {
    public String getIdentifier(){
        return "id-xml";
    }

    public Connector createConnector(){
        return new xmlConnector();
    }
}
