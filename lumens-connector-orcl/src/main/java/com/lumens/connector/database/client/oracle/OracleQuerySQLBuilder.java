package com.lumens.connector.database.client.oracle;

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
        String tableName = output.getName();
        for (Format f : output.getChildren()) {
            if (queryFields.length() > 0) {
                queryFields.append(", ");
            }
            queryFields.append(f.getName());
        }
        StringBuilder querySQL = new StringBuilder();
        querySQL.append("SELECT ").append(queryFields.toString()).append(" FROM ").append(tableName);
        if (input != null) {
            String sqlClause = input.getValue().getString();
            if (sqlClause != null && !sqlClause.isEmpty()) {
                querySQL.append(' ').append(sqlClause);
            }
        }
        // TODO append query where clause, order by, desc, group by from input element
        return querySQL.toString();
    }
}
