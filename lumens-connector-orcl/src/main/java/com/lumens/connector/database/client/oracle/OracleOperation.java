/*
 * Copyright Lumens Team, Inc. All Rights Reserved.
 */
package com.lumens.connector.database.client.oracle;

import org.apache.logging.log4j.Logger;

import com.lumens.connector.database.client.oracle.sql.OracleWriteSQLBuilder;
import com.lumens.connector.database.client.oracle.sql.OracleQuerySQLBuilder;
import com.lumens.connector.Operation;
import com.lumens.connector.OperationResult;
import com.lumens.model.Element;
import com.lumens.model.Format;
import java.util.ArrayList;
import java.util.List;
import org.apache.logging.log4j.LogManager;

public class OracleOperation implements Operation, OracleConstants {

    private static final Logger log = LogManager.getLogger(OracleOperation.class);
    private final OracleClient client;
    private final OracleElementBuilder elementBuilder;

    public OracleOperation(OracleClient client) {
        this.client = client;
        this.elementBuilder = new OracleElementBuilder();
    }

    @Override
    public OperationResult execute(List<Element> input, Format output) throws Exception {
        if (input != null && !input.isEmpty()) {
            List<Element> results = new ArrayList<>();
            for (Element elem : input) {
                Element action = elem.getChild(SQLPARAMS).getChild(ACTION);
                if (action == null || action.getValue() == null) {
                    throw new Exception("'action' is mandatory");
                }
                String operation = action.getValue().getString();
                if (SELECT.equalsIgnoreCase(operation)) {
                    OracleQuerySQLBuilder sql = new OracleQuerySQLBuilder(output);
                    String SQL = sql.generateSelectSQL(elem);
                    List<Element> result = client.executeQuery(SQL, elementBuilder, output);
                    if (result != null && !result.isEmpty())
                        results.addAll(result);
                } else if (INSERT.equalsIgnoreCase(operation)) {
                    OracleWriteSQLBuilder sql = new OracleWriteSQLBuilder();
                    String SQL = sql.generateInsertSQL(elem);
                    client.execute(SQL);
                } else if (UPDATE.equalsIgnoreCase(operation)) {
                    OracleWriteSQLBuilder sql = new OracleWriteSQLBuilder();
                    String SQL = sql.generateUpdateSQL(elem);
                    client.execute(SQL);
                }
            }
            return new OracleOperationResult(results);
        }
        throw new UnsupportedOperationException("Not supported yet.");
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
