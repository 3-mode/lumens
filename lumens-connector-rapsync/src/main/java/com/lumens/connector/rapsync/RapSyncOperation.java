/*
 * Copyright Lumens Team, Inc. All Rights Reserved.
 */
package com.lumens.connector.rapsync;

import com.lumens.connector.ElementChunk;
import com.lumens.connector.Operation;
import com.lumens.connector.OperationResult;
import static com.lumens.connector.database.DBConstants.ACTION;
import static com.lumens.connector.database.DBConstants.SQLPARAMS;
import com.lumens.connector.database.client.DBElementBuilder;
import com.lumens.connector.rapsync.api.LogMiner;
import com.lumens.connector.rapsync.api.RedoValue;
import com.lumens.model.Element;
import com.lumens.model.Format;
import com.lumens.model.ModelUtils;
import java.sql.ResultSet;
import java.util.List;
import java.util.ArrayList;

/**
 *
 * @author Xiaoxin(whiskeyfly@163.com)
 */
public class RapSyncOperation implements Operation, RapSyncConstants {

    private LogMiner miner = null;

    public RapSyncOperation(LogMiner miner) {
        this.miner = miner;
    }

    @Override
    public OperationResult execute(ElementChunk input, Format output) throws Exception {
        // check parameter availablity
        // check soruce db as well as minger db with sufficient priviledge 
        // start log analysis
        // start processing redo log sql: query and process each sql

        List<Element> dataList = input == null ? null : input.getData();
        List<Element> resultList = new ArrayList();
        if (dataList != null && !dataList.isEmpty() && input != null) {
            for (int i = input.getStart(); i < dataList.size(); i++) {
                Element elem = dataList.get(i);
                Element action = elem.getChild(SQLPARAMS).getChild(ACTION);
                String strOper = ModelUtils.isNullValue(action) ? null : action.getValue().getString();
                if (strOper == null || QUERY.equalsIgnoreCase(strOper)) {
                    // TODO: implementing paging
                    ResultSet result = miner.query(new RapSyncQuerySQLBuilder(output).generateSelectSQL(elem));
                    resultList.addAll(new DBElementBuilder().buildElement(output, result));
                } else if (SYNC.equalsIgnoreCase(strOper)) { // sync here
                    RedoValue value = new RedoValue();
                    value.SCN = elem.getChildByPath(COLUMN_SCN).getValue().getInt();
                    value.SQL_REDO = elem.getChildByPath(COLUMN_REDO).getValue().toString();
                    value.OPERATION = elem.getChildByPath(COLUMN_OPERATION).getValue().toString();
                    value.SEG_OWNER = elem.getChildByPath(COLUMN_SEG_OWNER).getValue().toString();
                    value.TABLE_NAME = elem.getChildByPath(COLUMN_TABLE_NAME).getValue().toString();
                    if (value.OPERATION.equalsIgnoreCase("DDL")) {
                        miner.buildDictionary();
                    }

                    miner.sync(value);
                } else {
                    throw new UnsupportedOperationException("Error, not supported action : " + strOper);
                }
            }
            return new RapSyncResult(resultList);
        }
        throw new UnsupportedOperationException("Error, the input data can not be empty !");
    }
}
