package com.lumens.connector.database.client.oracle.service;

import com.lumens.LumensException;
import com.lumens.addin.AddinActivator;
import com.lumens.addin.AddinContext;
import com.lumens.connector.ConnectorFactory;
import com.lumens.connector.database.client.oracle.OracleConnector;
import java.io.IOException;
import java.io.InputStream;
import java.util.HashMap;
import java.util.Map;
import org.apache.commons.codec.binary.Base64;
import org.apache.commons.io.IOUtils;

public class Activator implements AddinActivator {

    private AddinContext addinContext;

    @Override
    public void start(AddinContext context) {
        addinContext = context;
        Map<String, Object> props = new HashMap<>();
        props.put(ConnectorFactory.ID_PROPERTY, OracleConnector.CONNECTOR_ID);
        props.put(ConnectorFactory.NAME_PROPERTY, "Oracle");
        props.put(ConnectorFactory.CLASS_NAME_PROPERTY, OracleConnector.class.getName());
        try (InputStream in = Activator.class.getClassLoader().getResourceAsStream("img/Oracle64x64.png")) {
            props.put(ConnectorFactory.INSTANCE_ICON_PROPERTY, Base64.encodeBase64String(IOUtils.toByteArray(in)));
        } catch (IOException ex) {
            throw new LumensException(ex);
        }
        try (InputStream in = Activator.class.getClassLoader().getResourceAsStream("img/Oracle24x24.png")) {
            props.put(ConnectorFactory.CATALOG_ICON_PROPERTY, Base64.encodeBase64String(IOUtils.toByteArray(in)));
        } catch (IOException ex) {
            throw new LumensException(ex);
        }
        addinContext.registerService(OracleConnector.class.getName(), new OracleConnectorFactory(), props);
    }

    @Override
    public void stop(AddinContext context) {
    }
}