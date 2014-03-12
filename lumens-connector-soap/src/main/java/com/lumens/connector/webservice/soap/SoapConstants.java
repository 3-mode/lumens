package com.lumens.connector.webservice.soap;

/**
 *
 * @author shaofng.wang
 */
public interface SoapConstants {

    public static final String WSDL = "WSDL";
    public static final String USER = "User";
    public static final String PASSWORD = "Password";
    public static final String PROXY_ADDR = "ProxyAddress";
    public static final String PROXY_PORT = "ProxyPort";
    public static final String PROXY_USER = "ProxyUser";
    public static final String PROXY_PASSWORD = "ProxyPassword";
    // Protocol
    public int SOAP11 = 11;
    public int SOAP12 = 12;
    public int SOAPMESSAGE_IN = 0;
    public int SOAPMESSAGE_OUT = 1;
    public String SOAPSERVICES = "SOAPServices";
    public String SOAPMESSAGE = "SOAPMessage";
    public String SOAPENDPOINT = "SOAPAddress";
    public String SOAPACTION = "SOAPAction";
    public String SOAPATTRIBUTE = "SOAPAttribute";
    public String BINDINGINPUT = "BindingInput";
    public String BINDINGOUTPUT = "BindingOutput";
    public String NAMESPACEPREFIX = "ns";
    public String NAMESPACE = "Namespace";
    public String TARGETNAMESPACE = "targetNamespace";
    public String XMLSCHEMAXSD = "http://www.w3.org/2001/XMLSchema";
    public String EMPTY_STRING = "";
}
