/*
 * Copyright Lumens Team, Inc. All Rights Reserved.
 */
package com.lumens.connector.rapsync;

import com.lumens.connector.ElementChunk;
import com.lumens.connector.Operation;
import com.lumens.connector.OperationResult;
import static com.lumens.connector.database.DBConstants.ACTION;
import static com.lumens.connector.database.DBConstants.SQLPARAMS;
import com.lumens.connector.database.client.DBWriteResult;
import com.lumens.connector.rapsync.api.LogMiner;
import com.lumens.connector.rapsync.api.RedoValue;
import com.lumens.model.Element;
import com.lumens.model.Format;
import com.lumens.model.ModelUtils;
import java.util.ArrayList;
import java.util.List;

/**
 *
 * @author Xiaoxin(whiskeyfly@163.com)
 */
public class RapSyncOperation implements Operation, RapSyncConstants {

    private LogMiner miner = null;
    private int pageSize = 1000;

    public RapSyncOperation(LogMiner miner) {
        this.miner = miner;
    }

    public Operation setPageSize(int size) {
        pageSize = size;

        return this;
    }

    public int getPageSize() {
        return pageSize;
    }

    @Override
    public OperationResult execute(ElementChunk input, Format output) throws Exception {
        List<Element> dataList = input == null ? null : input.getData();
        if (dataList != null && !dataList.isEmpty() && input != null) {
            Element elem = dataList.get(input.getStart());
            Element action = elem.getChild(SQLPARAMS).getChild(ACTION);
            String strOper = ModelUtils.isNullValue(action) ? null : action.getValue().getString();
            if (strOper == null || QUERY.equalsIgnoreCase(strOper)) {
                return new RapSyncQueryResult(this, miner, new RapSyncQuerySQLBuilder(output), input);
            } else if (SYNC.equalsIgnoreCase(strOper)) { // sync here
                List<Element> outList = processSync(dataList, output);
                return new DBWriteResult(input, outList);
            } else {
                throw new UnsupportedOperationException("Error, not supported action : " + strOper);
            }
        }
        throw new UnsupportedOperationException("Error, the input data can not be empty !");
    }

    private List<Element> processSync(List<Element> inList, Format output) throws Exception {
        List<Element> outList = new ArrayList<>();
        for (Element elem : inList) {
            RedoValue value = new RedoValue();
            value.SCN = elem.getChildByPath(COLUMN_SCN).getValue().getInt();
            value.SQL_REDO = elem.getChildByPath(COLUMN_REDO).getValue().toString();
            value.OPERATION = elem.getChildByPath(COLUMN_OPERATION).getValue().toString();
            value.SEG_OWNER = elem.getChildByPath(COLUMN_SEG_OWNER).getValue().toString();
            value.TABLE_NAME = elem.getChildByPath(COLUMN_TABLE_NAME).getValue().toString();
            if (value.OPERATION.equalsIgnoreCase("ddl")) {
                miner.buildDictionary();
            }
            miner.sync(value);
        }
        return outList;
    }
}
