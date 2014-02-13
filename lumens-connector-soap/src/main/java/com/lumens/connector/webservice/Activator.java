package com.lumens.connector.webservice;

import com.lumens.connector.ConnectorFactory;
import com.lumens.model.LumensException;
import java.io.IOException;
import java.io.InputStream;
import java.util.Dictionary;
import java.util.Hashtable;
import org.apache.commons.io.IOUtils;
import org.osgi.framework.BundleActivator;
import org.osgi.framework.BundleContext;

public class Activator implements BundleActivator {

    private BundleContext m_context = null;

    @Override
    public void start(BundleContext context) {
        m_context = context;
        @SuppressWarnings("UseOfObsoleteCollectionType")
        Dictionary<String, Object> dict = new Hashtable<>();
        dict.put(ConnectorFactory.NAME_PROPERTY, "SOAP");
        try (InputStream in = this.getClass().getResourceAsStream("img/Web.png")) {
            dict.put(ConnectorFactory.INSTANCE_ICON_PROPERTY, IOUtils.toByteArray(in));
        } catch (IOException ex) {
            throw new LumensException(ex);
        }
        m_context.registerService(ConnectorFactory.class.getName(), new SoapConnectorFactory(), dict);
    }

    @Override
    public void stop(BundleContext context) {
    }
}