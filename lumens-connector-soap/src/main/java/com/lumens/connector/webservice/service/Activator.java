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
import org.apache.commons.codec.binary.Base64;
import org.apache.commons.io.IOUtils;

public class Activator implements AddinActivator {

    private AddinContext addinContext = null;

    @Override
    public void start(AddinContext ctx) {
        addinContext = ctx;
        Map<String, Object> props = new HashMap<>();
        props.put(ConnectorFactory.ID_PROPERTY, "SOAP_connector");
        props.put(ConnectorFactory.NAME_PROPERTY, "SOAP");
        props.put(ConnectorFactory.CLASS_NAME_PROPERTY, WebServiceConnector.class.getName());
        try (InputStream in = Activator.class.getClassLoader().getResourceAsStream("img/Web64x64.png")) {
            props.put(ConnectorFactory.INSTANCE_ICON_PROPERTY, Base64.encodeBase64String(IOUtils.toByteArray(in)));
        } catch (IOException ex) {
            throw new LumensException(ex);
        }
        try (InputStream in = Activator.class.getClassLoader().getResourceAsStream("img/Web24x24.png")) {
            props.put(ConnectorFactory.CATALOG_ICON_PROPERTY, Base64.encodeBase64String(IOUtils.toByteArray(in)));
        } catch (IOException ex) {
            throw new LumensException(ex);
        }

        addinContext.registerService(WebServiceConnector.class.getName(), new SoapConnectorFactory(), props);
    }

    @Override
    public void stop(AddinContext ctx) {
    }
}