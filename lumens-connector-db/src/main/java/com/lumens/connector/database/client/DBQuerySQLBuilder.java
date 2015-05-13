/*
 * Copyright Lumens Team, Inc. All Rights Reserved.
 */
package com.lumens.connector.database.client;

import com.lumens.connector.database.DBConstants;
import com.lumens.model.Element;
import com.lumens.model.Format;
import com.lumens.model.ModelUtils;

/**
 *
 * @author shaofeng.wang@outlook.com
 */
public abstract class DBQuerySQLBuilder extends DBSQLBuilder implements DBConstants {

    private final Format output;

    public DBQuerySQLBuilder(Format output) {
        this.output = output;
    }

    @Override
    public Format getFormat() {
        return output;
    }

    @Override
    public String generateSelectSQL(Element input) {
        StringBuilder queryFields = new StringBuilder();
        String tableName = null;
        if (output != null) {
            for (Format child : output.getChildren()) {
                if (SQLPARAMS.equals(child.getName()))
                    continue;
                if (queryFields.length() > 0)
                    queryFields.append(", ");
                queryFields.append(child.getName());
            }
        } else {
            throw new RuntimeException("Error no output format");
        }
        String strWhere = null, strOrderBy = null, strGroupBy = null;
        if (input != null) {
            Element sqlParams = input.getChild(SQLPARAMS);
            if (sqlParams != null) {
                Element whereElem = sqlParams.getChild(WHERE);
                if (ModelUtils.isNotNullValue(whereElem))
                    strWhere = whereElem.getValue().getString();
                Element orderByElem = sqlParams.getChild(ORDERBY);
                if (ModelUtils.isNotNullValue(orderByElem))
                    strOrderBy = orderByElem.getValue().getString();
                Element groupByElem = sqlParams.getChild(GROUPBY);
                if (ModelUtils.isNotNullValue(groupByElem))
                    strOrderBy = groupByElem.getValue().getString();
            }
        }
        return generatePageSQL(output, queryFields.toString(), strWhere, strOrderBy, strGroupBy);
    }

    public abstract String generatePageSQL(String SQL, int start, int page);

    protected abstract String generatePageSQL(Format table, String fields, String strWhere, String strOrderBy, String strGroupBy);
}
