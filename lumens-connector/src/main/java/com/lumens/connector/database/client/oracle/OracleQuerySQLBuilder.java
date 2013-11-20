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
            queryFields.append("t1.").append(f.getName());
        }
        StringBuilder querySQL = new StringBuilder();
        querySQL.append("SELECT ").append(queryFields.toString()).append(" FROM ").append(tableName).append(" t1");
        // TODO append query where clause, order by, desc, group by from input element
        return querySQL.toString();
    }
}
