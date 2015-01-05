/*
 * Copyright Lumens Team, Inc. All Rights Reserved.
 */
package com.lumens.connector.database.client.oracle.sql;

import static com.lumens.connector.database.client.oracle.OracleConstants.CLAUSE;
import com.lumens.model.Element;
import com.lumens.model.Format;

/**
 *
 * @author shaofeng.wang@outlook.com
 */
public class OracleQuerySQLBuilder extends OracleSQLBuilder {

    private Format output;

    public OracleQuerySQLBuilder(Format output) {
        this.output = output;
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
                if (queryFields.length() > 0) {
                    queryFields.append(", ");
                }
                queryFields.append(child.getName());
            }
        } else {
            tableName = input.getFormat().getName();
            queryFields.append("*");
        }
        StringBuilder querySQL = new StringBuilder();
        querySQL.append("SELECT ").append(queryFields.toString()).append(" FROM ").append(tableName);
        if (input != null) {
            Element sqlParams = input.getChild(SQLPARAMS);
            if (sqlParams != null) {
                Element clause = sqlParams.getChild(CLAUSE);
                if (clause != null) {
                    String sqlClause = clause.getValue().getString();
                    if (sqlClause != null && !sqlClause.isEmpty()) {
                        querySQL.append(' ').append(sqlClause);
                    }
                }
            }
        }
        // TODO append query where clause, order by, desc, group by from input element
        return querySQL.toString();
    }
}
