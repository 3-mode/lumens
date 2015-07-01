/*
 * Copyright Lumens Team, Inc. All Rights Reserved.
 */
package com.lumens.connector.rapsync;

import com.lumens.connector.ElementChunk;
import com.lumens.connector.Operation;
import com.lumens.connector.OperationResult;
import static com.lumens.connector.database.DBConstants.ACTION;
import static com.lumens.connector.database.DBConstants.SQLPARAMS;
import com.lumens.connector.rapsync.api.LogMiner;
import com.lumens.connector.rapsync.api.RedoValue;
import com.lumens.model.Element;
import com.lumens.model.Format;
import com.lumens.model.ModelUtils;
import com.lumens.model.Value;
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
                List<Element> outList = processSync(dataList, input.getData().get(0).getFormat());
                return new RapSyncWriteResult(input, outList);
            } else {
                throw new UnsupportedOperationException("Error, not supported action : " + strOper);
            }
        }
        throw new UnsupportedOperationException("Error, the input data can not be empty !");
    }

    private List<Element> processSync(List<Element> inList, Format output) throws Exception {
        List<Element> outList = new ArrayList<>();
        // Check before sync
        if (inList.size() > 0) {
            Element firstElem = inList.get(0);
            if (checkMandatoryField(firstElem, COLUMN_REDO)) {
                throw new RuntimeException(COLUMN_REDO + " has not been defined. It is what to be synced to destination database.");
            }
            if (checkMandatoryField(firstElem, COLUMN_OPERATION)) {
                throw new RuntimeException(COLUMN_OPERATION + " has not been defined. It is necessary to filter redundant records");
            }
            if (checkMandatoryField(firstElem, COLUMN_SCN)) {
                throw new RuntimeException(COLUMN_SCN + " has not been defined. It is necessary to perform incremental sync.");
            }
        }

        for (Element elem : inList) {
            RedoValue value = new RedoValue();
            value.SCN = elem.getChild(COLUMN_SCN).getValue().getInt();
            value.SQL_REDO = elem.getChild(COLUMN_REDO).getValue().toString();
            value.OPERATION = elem.getChild(COLUMN_OPERATION).getValue().toString();
            value.SEG_OWNER = getRequiredFieldValue(elem, COLUMN_SEG_OWNER).getString();
            value.TABLE_NAME = getRequiredFieldValue(elem, COLUMN_TABLE_NAME).getString();
            if (value.OPERATION.equalsIgnoreCase("ddl")) {
                miner.buildDictionary();
            }
            miner.sync(value);
        }
        return outList;
    }

    private boolean checkMandatoryField(Element parent, String fieldName) {
        return parent.getChild(fieldName) == null || parent.getChild(fieldName).getValue().getString().isEmpty();
    }

    private Value getRequiredFieldValue(Element elem, String fieldName) {
        return elem.getChild(fieldName) != null ? elem.getChild(fieldName).getValue() : new Value("");
    }
}
