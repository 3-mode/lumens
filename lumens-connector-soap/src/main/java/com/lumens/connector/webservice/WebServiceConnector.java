/*
 * Copyright Lumens Team, Inc. All Rights Reserved.
 */
package com.lumens.connector.webservice;

import com.lumens.connector.Connector;
import com.lumens.connector.FormatBuilder;
import com.lumens.connector.Operation;
import com.lumens.connector.Direction;
import com.lumens.connector.webservice.soap.SOAPClient;
import com.lumens.model.Format;
import com.lumens.model.Value;
import java.util.Map;

/**
 * TODO need to enhance to handle complex xsd element
 *
 * @author shaofeng wang
 */
public class WebServiceConnector implements Connector, WebServiceConstants {

    private SOAPClient soapClient;
    private FormatBuilder formatBuilder;
    private String wsdlURL;
    private String user;
    private String password;
    private String proxyAddr;
    private int proxyPort = 80;
    private String proxyUser;
    private String proxyPassword;
    private Map<String, Format> formatListIn;
    private Map<String, Format> formatListOut;

    @Override
    public void open() {
        if (soapClient == null) {
            soapClient = new SOAPClient(this);
            soapClient.open();
            formatBuilder = soapClient.getFormatBuilder();
        }
    }

    @Override
    public void close() {
        if (soapClient != null) {
            soapClient.close();
        }
        formatListIn = null;
        formatListOut = null;
        soapClient = null;
    }

    @Override
    public Map<String, Format> getFormatList(Direction direction) {
        if (direction == Direction.IN) {
            if (formatListIn != null)
                return formatListIn;
            formatListIn = formatBuilder.getFormatList(direction);
            return formatListIn;
        } else if (direction == Direction.OUT) {
            if (formatListOut != null)
                return formatListOut;
            formatListOut = formatBuilder.getFormatList(direction);
            return formatListOut;
        }
        throw new RuntimeException("Not support direction !");
    }

    @Override
    public Format getFormat(Format format, String path, Direction direction) {
        return formatBuilder.getFormat(format, path, direction);
    }

    @Override
    public void setPropertyList(Map<String, Value> propertyList) {
        if (propertyList.containsKey(WSDL)) {
            wsdlURL = propertyList.get(WSDL).getString();
        }
        if (propertyList.containsKey(USER)) {
            user = propertyList.get(USER).getString();
        }
        if (propertyList.containsKey(PASSWORD)) {
            password = propertyList.get(PASSWORD).getString();
        }
        if (propertyList.containsKey(PROXY_ADDR)) {
            proxyAddr = propertyList.get(PROXY_ADDR).getString();
        }
        if (propertyList.containsKey(PROXY_PORT)) {
            proxyPort = propertyList.get(PROXY_PORT).getInt();
        }
        if (propertyList.containsKey(PROXY_USER)) {
            proxyUser = propertyList.get(PROXY_USER).getString();
        }
        if (propertyList.containsKey(PROXY_PASSWORD)) {
            proxyPassword = propertyList.get(PROXY_PASSWORD).getString();
        }
    }

    @Override
    public Operation getOperation() {
        return new WebServiceOperation(getClient());
    }

    protected SOAPClient getClient() {
        return soapClient;
    }

    public String getProxyAddr() {
        return proxyAddr;
    }

    public int getProxyPort() {
        return proxyPort;
    }

    public String getProxyUser() {
        return proxyUser;
    }

    public String getProxyPassword() {
        return proxyPassword;
    }

    /**
     * @return the wsdlURL
     */
    public String getWsdlURL() {
        return wsdlURL;
    }

    /**
     * @return the user
     */
    public String getUser() {
        return user;
    }

    /**
     * @return the password
     */
    public String getPassword() {
        return password;
    }
}
