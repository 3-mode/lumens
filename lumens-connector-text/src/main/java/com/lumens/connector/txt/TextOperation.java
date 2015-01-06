/*
 * Copyright Lumens Team, Inc. All Rights Reserved.
 */
package com.lumens.connector.txt;

import com.lumens.connector.ElementChunk;
import com.lumens.connector.Operation;
import com.lumens.connector.OperationResult;
import com.lumens.model.Element;
import com.lumens.model.Format;
import java.util.ArrayList;
import java.util.List;

/**
 *
 * @author Xiaoxin(whiskeyfly@163.com)
 */
public class TextOperation implements Operation {
    private final TextClient client;

    TextOperation(TextClient cli) {
        client = cli;
    }

    @Override
    public OperationResult execute(ElementChunk input, Format fmt) throws Exception {
        List<Element> result = new ArrayList<>();
        List<Element> elementList = input.getData();
        if (elementList != null && !elementList.isEmpty()) {
            for (Element elem : elementList) {
                Element params = elem.getChild(TextConstants.FORMAT_PARAMS);
                if (params == null)
                    break;
                Element oper = params.getChild(TextConstants.OPERATION);
                if (oper == null || oper.getValue() == null)
                    throw new Exception("'operation' is mandatory");

                String operation = oper.getValue().toString();
                if (TextConstants.OPERATION_READ.equals(operation)) {
                    result.addAll(client.read(elem, fmt));
                } else if (TextConstants.OPERATION_APPEND.equals(operation)) {
                    client.write(elem, true);
                }
                if (TextConstants.OPERATION_OVERWRITE.equals(operation)) {
                    client.write(elem, false);
                }
            }
        }
        return new TextOperationResult(result);
    }
}
