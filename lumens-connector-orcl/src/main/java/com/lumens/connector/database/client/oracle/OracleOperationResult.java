/*
 * Copyright Lumens Team, Inc. All Rights Reserved.
 */
package com.lumens.connector.database.client.oracle;

import com.lumens.connector.OperationResult;
import com.lumens.model.Element;
import java.util.List;

/**
 *
 * @author shaofeng.wang@outlook.com
 */
public class OracleOperationResult implements OperationResult {

    private final List<Element> result;

    public OracleOperationResult(List<Element> result) {
        this.result = result;
    }

    @Override
    public List<Element> getResult() {
        return result;
    }
}
