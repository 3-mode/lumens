package com.lumens.connector.webservice.service;

import com.lumens.LumensException;
import com.lumens.addin.AddinActivator;
import com.lumens.addin.AddinContext;
import com.lumens.connector.ConnectorFactory;
import com.lumens.connector.webservice.WebServiceConnector;
import java.io.IOException;
import java.io.InputStream;
import java.util.HashMap;
import java.util.Map;
import org.apache.commons.io.IOUtils;

public class Activator implements AddinActivator {

    private AddinContext addinContext = null;

    @Override
    public void start(AddinContext ctx) {
        addinContext = ctx;
        Map<String, Object> props = new HashMap<>();
        props.put(ConnectorFactory.NAME_PROPERTY, "SOAP");
        try (InputStream in = this.getClass().getResourceAsStream("img/Web.png")) {
            props.put(ConnectorFactory.INSTANCE_ICON_PROPERTY, IOUtils.toByteArray(in));
        } catch (IOException ex) {
            throw new LumensException(ex);
        }
        addinContext.registerService(WebServiceConnector.class.getName(), new SoapConnectorFactory(), props);
    }

    @Override
    public void stop(AddinContext ctx) {
    }
}