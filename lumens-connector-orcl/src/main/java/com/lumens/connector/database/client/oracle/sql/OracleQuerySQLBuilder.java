package com.lumens.connector.database.client.oracle.sql;

import static com.lumens.connector.database.client.oracle.OracleConstants.CLAUSE;
import static com.lumens.connector.database.client.oracle.OracleConstants.FIELDS;
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
        Format fields = output.getChild(FIELDS);
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
            Element clause = input.getChild(CLAUSE);
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
