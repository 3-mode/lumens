/*
 * To change this template, choose Tools | Templates
 * and open the template in the editor.
 */
package com.lumens.connector.database;

import com.lumens.connector.Operation;
import com.lumens.connector.OperationResult;
import com.lumens.model.Element;
import com.lumens.model.Format;

/**
 *
 * @author shaofeng wang
 */
public class DatabaseOperation implements Operation {
    DatabaseOperation(Client dbClient) {
    }

    @Override
    public OperationResult execute(Element input, Format output) throws Exception {
        throw new UnsupportedOperationException("Not supported yet.");
    }
}
