/*
 * Copyright Lumens Team, Inc. All Rights Reserved.
 */
package com.lumens.engine;

import com.lumens.connector.Operation;
import com.lumens.connector.OperationResult;
import com.lumens.model.Element;

/**
 *
 * @author shaofeng wang (shaofeng.cjpw@gmail.com)
 */
public class MockOperation implements Operation
{
    @Override
    public OperationResult execute(Element input) throws Exception
    {
        throw new UnsupportedOperationException("Not supported yet.");
    }
}
