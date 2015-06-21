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
import com.lumens.model.DataElement;
import com.lumens.model.Element;
import com.lumens.model.Format;
import java.util.ArrayList;
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
            String strOper = getSQLAction(dataList.get(input.getStart()));
            if (strOper == null || SELECT.equalsIgnoreCase(strOper))
                return new DBQueryResult(this, getQuerySQLBuilder(output), input);
            else if (INSERT_ONLY.equalsIgnoreCase(strOper) || UPDATE_ONLY.equalsIgnoreCase(strOper)) {
                List<Element> outList = new ArrayList<>();
                for (Element elem : dataList) {
                    processWrite(elem);
                    outList.add(new DataElement(output));
                }
                if (input.isLast())
                    client.commit();
                // Except query, for update, delete no result for output
                return new DBWriteResult(input, outList);
            }
        }
        throw new UnsupportedOperationException("Error, the input data can not be empty !");
    }

    private void processWrite(Element elem) {
        String strOper = getSQLAction(elem);
        if (INSERT_ONLY.equalsIgnoreCase(strOper)) {
            client.execute(getWriteSQLBuilder().generateInsertSQL(elem));
        } else if (UPDATE_ONLY.equalsIgnoreCase(strOper)) {
            client.execute(getWriteSQLBuilder().generateUpdateSQL(elem));
        } else if (UPDATE_OR_INSERT.equalsIgnoreCase(strOper) || DELETE_ONLY.equalsIgnoreCase(strOper)) {
            client.rollback();
            throw new RuntimeException("'Update_Or_Insert' and 'Delete_Only' are not supported now!");
        } else if (strOper == null || SELECT.equalsIgnoreCase(strOper)) {
            // If i < input.getStart then there are some input alreadly handled as update or insert
            // No such useful business logic for this behavior so rollback and throw exception
            client.rollback();
            throw new RuntimeException("Not supported behavior, the 'Query' can't be mixed with 'Insert' or 'Update'");
        }
    }

    private String getSQLAction(Element elem) {
        Element sqlParams = elem.getChild(SQLPARAMS);
        Element action = null;
        if (sqlParams != null)
            action = sqlParams.getChild(ACTION);
        return ModelUtils.isNullValue(action) ? null : action.getValue().getString();
    }

    protected DBWriteSQLBuilder getWriteSQLBuilder() {
        return new DBWriteSQLBuilder();
    }

    protected abstract DBQuerySQLBuilder getQuerySQLBuilder(Format output);
}
