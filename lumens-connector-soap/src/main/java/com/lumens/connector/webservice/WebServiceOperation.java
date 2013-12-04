package com.lumens.connector.webservice;

import com.lumens.connector.Operation;
import com.lumens.connector.OperationResult;
import com.lumens.connector.webservice.soap.SOAPClient;
import com.lumens.model.Element;
import com.lumens.model.Format;
import org.apache.axiom.soap.SOAPEnvelope;

/**
 *
 * @author shaofeng wang
 */
public class WebServiceOperation implements Operation {
    private SOAPClient client;

    public WebServiceOperation(SOAPClient client) {
        this.client = client;
    }

    @Override
    public OperationResult execute(Element input, Format output) throws Exception {
        SOAPEnvelope envelope = client.execute(input);
        return new WebServiceResult(output, envelope);
    }

    @Override
    public void begin() {
    }

    @Override
    public void end() {
    }

    @Override
    public void commit() {
    }
}
