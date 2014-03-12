package com.lumens.connector.webservice.soap;

import com.lumens.connector.Operation;
import com.lumens.connector.OperationResult;
import com.lumens.connector.webservice.soap.SoapClient;
import com.lumens.model.Element;
import com.lumens.model.Format;
import org.apache.axiom.soap.SOAPEnvelope;

/**
 *
 * @author shaofeng wang
 */
public class SoapOperation implements Operation {

    private SoapClient client;

    public SoapOperation(SoapClient client) {
        this.client = client;
    }

    @Override
    public OperationResult execute(Element input, Format output) throws Exception {
        SOAPEnvelope envelope = client.execute(input);
        return new SoapResult(output, envelope);
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
