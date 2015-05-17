/*
 * Copyright Lumens Team, Inc. All Rights Reserved.
 */
package com.lumens.connector.database.client;

import com.lumens.model.ModelUtils;
import com.lumens.connector.ElementChunk;
import org.apache.logging.log4j.Logger;

import com.lumens.connector.Operation;
import com.lumens.connector.OperationResult;
import com.lumens.connector.database.Client;
import com.lumens.connector.database.DBConstants;
import com.lumens.model.Element;
import com.lumens.model.Format;
import java.util.List;
import org.apache.logging.log4j.LogManager;

public abstract class DBOperation implements Operation, DBConstants {

    private static final Logger log = LogManager.getLogger(DBOperation.class);
    private final Client client;

    public DBOperation(Client client) {
        this.client = client;
    }

    public Client getClient() {
        return client;
    }

    @Override
    public OperationResult execute(ElementChunk input, Format output) throws Exception {
        List<Element> dataList = input == null ? null : input.getData();
        if (dataList != null && !dataList.isEmpty()) {
            for (int i = input.getStart(); i < dataList.size(); ++i) {
                Element elem = dataList.get(i);
                Element action = elem.getChild(SQLPARAMS).getChild(ACTION);
                String strOper = ModelUtils.isNullValue(action) ? null : action.getValue().getString();
                if (strOper == null || SELECT.equalsIgnoreCase(strOper)) {
                    // If i < input.getStart then there are some input handled as update or insert need to commit before
                    // exeute the executeNext query, use this way to handle the mixed operation with select, update or insert
                    if (input.getStart() < i) {
                        client.commit();
                        input.setStart(i);
                    }
                    return new DBQueryResult(this, getQuerySQLBuilder(output), input);
                } else if (INSERT_ONLY.equalsIgnoreCase(strOper)) {
                    client.execute(getWriteSQLBuilder().generateInsertSQL(elem));
                } else if (UPDATE_ONLY.equalsIgnoreCase(strOper)) {
                    client.execute(getWriteSQLBuilder().generateUpdateSQL(elem));
                } else {
                    // TODO UPDATE_OR_INSERT.equalsIgnoreCase(operation)
                    throw new RuntimeException("Not supported now");
                }
                input.setStart(i);
            }
            if (input.isLast())
                client.commit();
            // Except query, for update, delete no result for output
            return null;
        }
        throw new UnsupportedOperationException("Error, the input data can not be empty !");
    }

    protected DBWriteSQLBuilder getWriteSQLBuilder() {
        return new DBWriteSQLBuilder();
    }

    protected abstract DBQuerySQLBuilder getQuerySQLBuilder(Format output);

}
