/*
 * Copyright Lumens Team, Inc. All Rights Reserved.
 */
package com.lumens.connector.database.client;

import com.lumens.model.ModelUtils;
import com.lumens.connector.ElementChunk;

import com.lumens.connector.Operation;
import com.lumens.connector.OperationResult;
import com.lumens.connector.database.Client;
import com.lumens.connector.database.DBConstants;
import com.lumens.connector.database.DBUtils;
import com.lumens.model.DataElement;
import com.lumens.model.Element;
import com.lumens.model.Format;
import java.util.ArrayList;
import java.util.List;

public abstract class DBOperation implements Operation, DBConstants {

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
            String strAction = getSQLAction(dataList.get(input.getStart()));
            if (DBUtils.isQuery(strAction)) {
                if(output == null)
                    throw new UnsupportedOperationException("The query output format can't be empty!");
                return new DBQueryResult(this, getQuerySQLBuilder(output), input);
            } else if (DBUtils.isWrite(strAction)) {
                List<Element> outList = processWrite(dataList, output);
                if (input.isLast())
                    client.commit();
                // Except query, for update, delete no result for output
                return new DBWriteResult(input, outList);
            }
        }
        throw new UnsupportedOperationException("Error, the input data can not be empty !");
    }

    private List<Element> processWrite(List<Element> inList, Format output) {
        List<Element> outList = new ArrayList<>();
        for (Element elem : inList) {
            String strAction = getSQLAction(elem);
            if (DBUtils.isInsert(strAction)) {
                client.execute(getWriteSQLBuilder().generateInsertSQL(elem));
            } else if (DBUtils.isUpdate(strAction)) {
                client.execute(getWriteSQLBuilder().generateUpdateSQL(elem));
            } else if (DBUtils.isDelete(strAction)) {
                client.execute(getWriteSQLBuilder().generateDeleteSQL(elem));
            } else if (DBUtils.isUpdateElseInsert(strAction)) {
                if (client.hasRecord(getWriteSQLBuilder().generateSelectSQL(elem)))
                    client.execute(getWriteSQLBuilder().generateUpdateSQL(elem));
                else
                    client.execute(getWriteSQLBuilder().generateInsertSQL(elem));
            } else if (DBUtils.isQuery(strAction)) {
                // If i < input.getStart then there are some input alreadly handled as update or insert
                // No such useful business logic for this behavior so rollback and throw exception
                client.rollback();
                throw new RuntimeException("Not supported behavior, the 'Operation' can't be mixed with 'INSERT', 'DELETE' or 'UPDATE'");
            } else if (DBUtils.isUpdateElseInsert(strAction)) {
                throw new RuntimeException("'UPDATE_ELSE_INSERT' are not supported now!");
            } else {
                client.rollback();
                throw new RuntimeException("'" + strAction + "' are not supported now!");
            }
            if (output != null)
                outList.add(new DataElement(output));
        }
        return outList;
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
