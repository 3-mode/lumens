package com.lumens.connector.webservice.soap;

import com.lumens.connector.ElementChunk;
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
    public OperationResult execute(ElementChunk input, Format output) throws Exception {
        List<Element> dataList = input.getData();
        // TODO need to handle chunk
        SOAPEnvelope envelope = null;
        if (!dataList.isEmpty())
            envelope = client.execute(dataList.get(0));
        return new SoapResult(output, envelope);
    }
}
