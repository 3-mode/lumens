/*
 * To change this license header, choose License Headers in Project Properties.
 * To change this template file, choose Tools | Templates
 * and open the template in the editor.
 */

package com.lumens.connector.xml;

import com.lumens.addin.AddinActivator;
import com.lumens.addin.AddinContext;
import com.lumens.connector.ConnectorFactory;
import com.lumens.descriptor.DescriptorUtils;
import com.lumens.connector.xml.xmlConnectorFactory;

/**
 *
 * @author whiskey
 */
public class Activator implements AddinActivator{    
    // Create factory and register service
    public void start(AddinContext ctx){
        
        ConnectorFactory fact = new xmlConnectorFactory();
        ctx.registerService(fact.getIdentifier(), fact, null);
    }

    public void stop(AddinContext ctx){       
    }
}
