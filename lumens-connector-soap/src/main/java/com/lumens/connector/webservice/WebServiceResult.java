/*
 * Copyright Lumens Team, Inc. All Rights Reserved.
 */
package com.lumens.connector.webservice;

import com.lumens.connector.OperationResult;
import com.lumens.connector.webservice.soap.ElementFromSOAPBuilder;
import com.lumens.model.Element;
import com.lumens.model.Format;
import java.util.ArrayList;
import java.util.List;
import org.apache.axiom.soap.SOAPEnvelope;

/**
 *
 * @author shaofeng wang (shaofeng.cjpw@gmail.com)
 */
public class WebServiceResult implements OperationResult {
    private ElementFromSOAPBuilder elementBuilder = new ElementFromSOAPBuilder();
    private SOAPEnvelope envelope;
    private Format resultFormat;

    WebServiceResult(Format resultFormat, SOAPEnvelope envelope) {
        this.resultFormat = resultFormat;
        this.envelope = envelope;
    }

    @Override
    public List<Element> getResult() {
        Element result = elementBuilder.buildElement(resultFormat, envelope);
        if (result != null) {
            List<Element> results = new ArrayList<>(1);
            results.add(result);
            return results;
        }
        return null;
    }
}
