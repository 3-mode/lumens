/*
 * Copyright Lumens Team, Inc. All Rights Reserved.
 */
package com.lumens.connector.database.client;

import com.lumens.connector.database.client.oracle.OracleConstants;
import com.lumens.model.Element;
import com.lumens.model.Format;

/**
 *
 * @author shaofeng.wang@outlook.com
 */
public abstract class DBQuerySQLBuilder extends DBSQLBuilder implements OracleConstants {

    private final Format output;

    public DBQuerySQLBuilder(Format output) {
        this.output = output;
    }

    public static String generatePageSQL(String SQL, int start, int page) {
        return String.format(SQL, start + page, start);
    }

    @Override
    public String generateSelectSQL(Element input) {
        StringBuilder queryFields = new StringBuilder();
        String tableName = null;
        if (output != null) {
            tableName = output.getName();
            for (Format child : output.getChildren()) {
                if (SQLPARAMS.equals(child.getName()))
                    continue;
                queryFields.append(child.getName()).append(", ");
            }
        } else {
            throw new RuntimeException("Error no output format");
        }
        String strWhere = null, strOrderBy = null, strGroupBy = null;
        if (input != null) {
            Element sqlParams = input.getChild(SQLPARAMS);
            if (sqlParams != null) {
                Element whereElem = sqlParams.getChild(WHERE);
                if (whereElem != null)
                    strWhere = whereElem.getValue().getString();
                Element orderByElem = sqlParams.getChild(ORDERBY);
                if (orderByElem != null)
                    strOrderBy = orderByElem.getValue().getString();
                Element groupByElem = sqlParams.getChild(GROUPBY);
                if (groupByElem != null)
                    strOrderBy = groupByElem.getValue().getString();
            }
        }
        return generatePageSQL(tableName, queryFields.toString(), strWhere, strOrderBy, strGroupBy);
    }

    protected abstract String generatePageSQL(String tableName, String fields, String strWhere, String strOrderBy, String strGroupBy);
}
