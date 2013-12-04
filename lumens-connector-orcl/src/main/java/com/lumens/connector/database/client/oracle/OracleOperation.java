package com.lumens.connector.database.client.oracle;

import org.apache.logging.log4j.Logger;

import com.lumens.connector.database.client.oracle.sql.OracleWriteSQLBuilder;
import com.lumens.connector.database.client.oracle.sql.OracleQuerySQLBuilder;
import com.lumens.connector.Operation;
import com.lumens.connector.OperationResult;
import com.lumens.model.Element;
import com.lumens.model.Format;
import java.util.List;
import org.apache.logging.log4j.LogManager;

public class OracleOperation implements Operation, OracleConstants {
    private static Logger log = LogManager.getLogger(OracleOperation.class);
    private OracleClient client;
    private ElementFromDbBuilder elementBuilder;

    public OracleOperation(OracleClient client) {
        this.client = client;
        this.elementBuilder = new ElementFromDbBuilder();
    }

    @Override
    public OperationResult execute(Element input, Format output) throws Exception {
        if (input != null) {
            Element oper = input.getChild(OPERATION);
            if (oper == null || oper.getValue() == null)
                throw new Exception("'operation' is mandatory");
            String operation = oper.getValue().getString();
            if (SELECT.equalsIgnoreCase(operation)) {
                OracleQuerySQLBuilder sql = new OracleQuerySQLBuilder(output);
                String SQL = sql.generateSelectSQL(input);
                List<Element> result = client.executeQuery(SQL, elementBuilder, output);
                return new OracleOperationResult(result);
            } else if (INSERT.equalsIgnoreCase(operation)) {
                OracleWriteSQLBuilder sql = new OracleWriteSQLBuilder();
                String SQL = sql.generateInsertSQL(input);
                client.execute(SQL);
                return null;
            } else if (UPDATE.equalsIgnoreCase(operation)) {
                OracleWriteSQLBuilder sql = new OracleWriteSQLBuilder();
                String SQL = sql.generateUpdateSQL(input);
                client.execute(SQL);
                return null;
            }
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
