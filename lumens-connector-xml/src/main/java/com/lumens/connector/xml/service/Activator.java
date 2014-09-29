/*
 * To change this license header, choose License Headers in Project Properties.
 * To change this template file, choose Tools | Templates
 * and open the template in the editor.
 */

package com.lumens.connector.xml.service;

import com.lumens.addin.AddinActivator;
import com.lumens.addin.AddinContext;
import com.lumens.connector.ConnectorFactory;
import com.lumens.descriptor.DescriptorUtils;
import com.lumens.connector.xml.XmlConnectorFactory;

/**
 *
 * @author whiskey
 */
public class Activator implements AddinActivator{    
    // Create factory and register service
    @Override 
    public void start(AddinContext ctx){
        
        ConnectorFactory factory = new XmlConnectorFactory();
        ctx.registerService(factory.getIdentifier(), factory, DescriptorUtils.processDescriptor(Activator.class, "xml", factory.getIdentifier()));
    }

    @Override 
    public void stop(AddinContext ctx){       
    }
}
