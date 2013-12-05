/*
 * Copyright Lumens Team, Inc. All Rights Reserved.
 */
package com.lumens.connector.webservice.soap;

import com.lumens.connector.FormatBuilder;
import com.lumens.connector.webservice.WebServiceConnector;
import com.lumens.model.Element;
import com.lumens.model.Format;
import com.lumens.model.Value;
import java.util.ArrayList;
import java.util.List;
import javax.xml.namespace.QName;
import org.apache.axiom.soap.SOAPEnvelope;
import org.apache.axis2.AxisFault;
import org.apache.axis2.addressing.EndpointReference;
import org.apache.axis2.client.OperationClient;
import org.apache.axis2.client.Options;
import org.apache.axis2.client.ServiceClient;
import org.apache.axis2.context.MessageContext;
import org.apache.axis2.transport.http.HTTPConstants;
import org.apache.axis2.transport.http.HttpTransportProperties;
import org.apache.axis2.transport.http.HttpTransportProperties.Authenticator;
import org.apache.axis2.transport.http.HttpTransportProperties.ProxyProperties;
import org.apache.axis2.wsdl.WSDLConstants;

/**
 *
 * @author shaofeng wang
 */
public class SOAPClient implements SOAPConstants {

    private FormatFromWSDLBuilder formatBuilder;
    private SOAPMessageBuilder soapBuilder;
    private Authenticator basicAuth;
    private ServiceClient client;
    private WebServiceConnector connector;

    public SOAPClient(WebServiceConnector connector) {
        this.connector = connector;
    }

    public void open() {
        // TODO need to handle SSL
        formatBuilder = new FormatFromWSDLBuilder(connector.getWsdlURL());
        soapBuilder = new SOAPMessageBuilder();
        try {
            client = new ServiceClient();
            Options options = client.getOptions();
            String user = connector.getUser();
            if (user != null) {
                basicAuth = new HttpTransportProperties.Authenticator();
                List auth = new ArrayList();
                auth.add(Authenticator.BASIC);
                basicAuth.setAuthSchemes(auth);
                basicAuth.setUsername(user);
                if (connector.getPassword() != null) {
                    basicAuth.setPassword(connector.getPassword());
                }
                basicAuth.setPreemptiveAuthentication(true);
                options.setProperty(HTTPConstants.AUTHENTICATE, basicAuth);
            }
            options.setProperty(HTTPConstants.REUSE_HTTP_CLIENT, true);
            ProxyProperties pp = new ProxyProperties();
            String proxyAddr = connector.getProxyAddr();
            if (proxyAddr != null && !proxyAddr.isEmpty()) {
                pp.setProxyName(proxyAddr);
                pp.setProxyPort(connector.getProxyPort());
                if (connector.getProxyUser() != null) {
                    pp.setUserName(connector.getProxyUser());
                    if (connector.getProxyPassword() != null) {
                        pp.setPassWord(connector.getProxyPassword());
                    }
                }
                options.setProperty(HTTPConstants.PROXY, pp);
                System.setProperty("http.useProxy", Boolean.toString(true));
                System.setProperty("http.proxyHost", pp.getProxyHostName());
                System.setProperty("http.proxyPort", Integer.toString(pp.
                getProxyPort()));
            } else {
                System.setProperty("http.useProxy", Boolean.toString(false));
            }

            formatBuilder.loadWSDL();
        } catch (AxisFault ex) {
            throw new RuntimeException(ex);
        }
    }

    public void close() {
        formatBuilder = null;
        if (client != null) {
            try {
                client.cleanup();
            } catch (AxisFault ex) {//Ignore exception when close
            }
        }
    }

    public FormatBuilder getFormatBuilder() {
        return formatBuilder;
    }

    public SOAPEnvelope execute(Element requestElement) throws Exception {
        Format format = requestElement.getFormat();
        SOAPEnvelope reqEnvelop = soapBuilder.buildSOAPMessage(requestElement);
        Options options = client.getOptions();
        Value soapAction = format.getProperty(SOAPACTION);
        if (soapAction != null && !soapAction.isNull()) {
            options.setAction(soapAction.getString());
        }
        Value soapEndPoint = format.getProperty(SOAPENDPOINT);
        if (soapEndPoint != null && !soapEndPoint.isNull()) {
            options.setTo(new EndpointReference(soapEndPoint.getString()));
        }

        Object bindingIn = format.getProperty(BINDINGINPUT);
        Object bindingOut = format.getProperty(BINDINGOUTPUT);
        QName operationQName = null;
        if (bindingIn != null && bindingOut != null) {
            operationQName = ServiceClient.ANON_OUT_IN_OP;
        } else if (bindingIn != null) {
            operationQName = ServiceClient.ANON_OUT_ONLY_OP;
        }

        MessageContext msgctx = new MessageContext();
        msgctx.setEnvelope(reqEnvelop);
        OperationClient opClient = client.createClient(operationQName);
        opClient.addMessageContext(msgctx);
        // Do executing
        opClient.execute(true);

        MessageContext responseMsgCtx = opClient.getMessageContext(
        WSDLConstants.MESSAGE_LABEL_IN_VALUE);
        return responseMsgCtx.getEnvelope();
    }
}
