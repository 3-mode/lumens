/*
 * Copyright Lumens Team, Inc. All Rights Reserved.
 */

package com.lumens.connector.database.client.sqlserver.sql;

import static com.lumens.connector.database.DatabaseConstants.CONST_CNTR_SQLSERVER_CLAUSE;
import static com.lumens.connector.database.DatabaseConstants.CONST_CNTR_SQLSERVER_FIELDS;
import com.lumens.model.Element;
import com.lumens.model.Format;

/**
 *
 * @author Xiaoxin(whiskeyfly@163.com)
 */
public class SqlServerQuerySQLBuilder extends SqlServerSQLBuilder {
    private Format output;

    public SqlServerQuerySQLBuilder(Format output) {
        this.output = output;
    }

    @Override
    public String generateSelectSQL(Element input) {
        StringBuilder queryFields = new StringBuilder();
        String tableName = output.getName();
        Format fields = output.getChild(CONST_CNTR_SQLSERVER_FIELDS);
        if (fields == null) {
            return null;
        }
        for (Format f : fields.getChildren()) {
            if (queryFields.length() > 0) {
                queryFields.append(", ");
            }
            queryFields.append(f.getName());
        }
        StringBuilder querySQL = new StringBuilder();
        querySQL.append("SELECT ").append(queryFields.toString()).append(" FROM ").append(tableName);
        if (input != null) {
            Element clause = input.getChild(CONST_CNTR_SQLSERVER_CLAUSE);
            if (clause != null) {
                String sqlClause = clause.getValue().getString();
                if (sqlClause != null && !sqlClause.isEmpty()) {
                    querySQL.append(' ').append(sqlClause);
                }
            }
        }
        // TODO append query where clause, order by, desc, group by from input element
        return querySQL.toString();
    }
    
}
