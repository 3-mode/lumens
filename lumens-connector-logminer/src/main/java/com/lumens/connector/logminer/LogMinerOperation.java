/*
 * Copyright Lumens Team, Inc. All Rights Reserved.
 */
package com.lumens.connector.logminer;

import com.lumens.connector.ElementChunk;
import com.lumens.connector.Operation;
import com.lumens.connector.OperationResult;
import static com.lumens.connector.database.DBConstants.ACTION;
import static com.lumens.connector.database.DBConstants.SELECT;
import static com.lumens.connector.database.DBConstants.SQLPARAMS;
import com.lumens.connector.database.client.DBElementBuilder;
import com.lumens.connector.logminer.api.LogMiner;
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
public class LogMinerOperation implements Operation, LogMinerConstants {

    private LogMiner miner = null;

    public LogMinerOperation(LogMiner miner) {
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
        if (dataList != null && !dataList.isEmpty()) {
            for (int i = input.getStart(); i < dataList.size(); i++) {
                Element elem = dataList.get(i);
                Element action = elem.getChild(SQLPARAMS).getChild(ACTION);                
                String strOper = ModelUtils.isNullValue(action) ? null : action.getValue().getString();
                if (strOper == null || SELECT.equalsIgnoreCase(strOper)) {
                    // TODO: implementing paging
                    ResultSet result = miner.query(new LogMinerQuerySQLBuilder(output).generateSelectSQL(elem));
                    resultList.addAll(new DBElementBuilder().buildElement(output, result));
                } else {  // sync here
                    String scn = elem.getChildByPath(COLUMN_SCN).getValue().toString();
                    String redo = elem.getChildByPath(COLUMN_REDO).getValue().toString();
                    miner.sync(scn, redo);
                }
            }
            return new LogMinerResult(resultList);
        }
        throw new UnsupportedOperationException("Error, the input data can not be empty !");
    }
}
