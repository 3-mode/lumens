/*
 * To change this template, choose Tools | Templates
 * and open the template in the editor.
 */
package com.lumens.connector.webservice;

import com.lumens.connector.Operation;
import com.lumens.connector.OperationResult;
import com.lumens.connector.webservice.soap.SOAPClient;
import com.lumens.model.Element;
import org.apache.axiom.soap.SOAPEnvelope;

/**
 *
 * @author shaofeng wang
 */
public class WebServiceOperation implements Operation {
    private SOAPClient client;

    WebServiceOperation(SOAPClient client) {
        this.client = client;
    }

    @Override
    public OperationResult execute(Element input) throws Exception {
        SOAPEnvelope envelope = client.execute(input);
        return new WebServiceResult(envelope);
    }
}
