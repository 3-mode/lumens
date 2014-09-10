/*
 * To change this license header, choose License Headers in Project Properties.
 * To change this template file, choose Tools | Templates
 * and open the template in the editor.
 */

package com.lumens.connector.xml;

/**
 *
 * @author whiskey
 */
public class xmlClientFactory {
    xmlClient createDOMClient(xmlConnector cnt){
        return new DOMClient();
    }
    
    xmlClient createSAXClient(xmlConnector cnt){
        return new SAXClient();
    }
}
