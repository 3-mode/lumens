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
public class WebServiceConnector implements Connector
{
    public static final String WSDL = "WSDL";
    public static final String USER = "User";
    public static final String PASSWORD = "Password";
    public static final String PROXY_ADDR = "ProxyAddress";
    public static final String PROXY_PORT = "ProxyPort";
    public static final String PROXY_USER = "ProxyUser";
    public static final String PROXY_PASSWORD = "ProxyPassword";
    private SOAPClient soapClient;
    private FormatBuilder formatBuilder;
    private String wsdlURL;
    private String user;
    private String password;
    private String proxyAddr;
    private int proxyPort = 80;
    private String proxyUser;
    private String proxyPassword;

    @Override
    public void open()
    {
        soapClient = new SOAPClient(this);
        soapClient.open();
        formatBuilder = soapClient.getFormatBuilder();
    }

    @Override
    public void close()
    {
        if (soapClient != null)
            soapClient.close();
        soapClient = null;
    }

    @Override
    public Map<String, Format> getFormatList(Direction direction)
    {
        return formatBuilder.getFormatList(direction);
    }

    @Override
    public Format getFormat(Format format, String path, Direction direction)
    {
        return formatBuilder.getFormat(format, path, direction);
    }

    @Override
    public void setPropertyList(Map<String, Value> propertyList)
    {
        if (propertyList.containsKey(WSDL))
            wsdlURL = propertyList.get(WSDL).getString();
        if (propertyList.containsKey(USER))
            user = propertyList.get(USER).getString();
        if (propertyList.containsKey(PASSWORD))
            password = propertyList.get(PASSWORD).getString();
        if (propertyList.containsKey(PROXY_ADDR))
            proxyAddr = propertyList.get(PROXY_ADDR).getString();
        if (propertyList.containsKey(PROXY_PORT))
            proxyPort = propertyList.get(PROXY_PORT).getInt();
        if (propertyList.containsKey(PROXY_USER))
            proxyUser = propertyList.get(PROXY_USER).getString();
        if (propertyList.containsKey(PROXY_PASSWORD))
            proxyPassword = propertyList.get(PROXY_PASSWORD).getString();
    }

    @Override
    public Operation getOperation()
    {
        return new WebServiceOperation(getClient());
    }

    protected SOAPClient getClient()
    {
        return soapClient;
    }

    public String getProxyAddr()
    {
        return proxyAddr;
    }

    public int getProxyPort()
    {
        return proxyPort;
    }

    public String getProxyUser()
    {
        return proxyUser;
    }

    public String getProxyPassword()
    {
        return proxyPassword;
    }

    /**
     * @return the wsdlURL
     */
    public String getWsdlURL()
    {
        return wsdlURL;
    }

    /**
     * @return the user
     */
    public String getUser()
    {
        return user;
    }

    /**
     * @return the password
     */
    public String getPassword()
    {
        return password;
    }
}
