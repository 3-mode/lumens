package com.lumens.connector.webservice.soap;

import com.lumens.connector.Operation;
import com.lumens.connector.OperationResult;
import com.lumens.model.Element;
import com.lumens.model.Format;
import java.util.List;
import org.apache.axiom.soap.SOAPEnvelope;

/**
 *
 * @author shaofeng wang
 */
public class SoapOperation implements Operation {

    private final SoapClient client;

    public SoapOperation(SoapClient client) {
        this.client = client;
    }

    @Override
    public OperationResult execute(List<Element> input, Format output) throws Exception {
        // TODO need to handle chunk
        SOAPEnvelope envelope = null;
        if (!input.isEmpty())
            envelope = client.execute(input.get(0));
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
