/*
 * Copyright Lumens Team, Inc. All Rights Reserved.
 */
package com.lumens.connector.database.sqlserver;

import com.lumens.connector.database.client.DBQuerySQLBuilder;
import static com.lumens.connector.database.sqlserver.SQLServerConstants.SQLSERVER_PK;
import com.lumens.model.Format;
import com.lumens.model.Value;
import org.apache.commons.lang.StringUtils;

/**
 *
 * @author Shaofeng Wang <shaofeng.wang@outlook.com>
 */
public class SQLServerQuerySQLBuilder extends DBQuerySQLBuilder {

    public SQLServerQuerySQLBuilder(Format output) {
        super(output);
    }

    @Override
    protected String generatePageSQL(Format table, String fields, String strWhere, String strOrderBy, String strGroupBy) {
        StringBuilder innerQuerySQL = new StringBuilder();
        innerQuerySQL.append("SELECT TOP %d row_number() OVER ");
        Value primaryKeys = table.getProperty(SQLSERVER_PK);

        if (StringUtils.isNotEmpty(strOrderBy) && StringUtils.isNotBlank(strOrderBy))
            innerQuerySQL.append("(ORDER BY (").append(strOrderBy.trim()).append(")) rowno, ");
        else if (primaryKeys != null)
            innerQuerySQL.append("(ORDER BY (").append(primaryKeys.getString()).append(")) rowno, ");

        innerQuerySQL.append(fields).append(" FROM ").append(table.getName());

        if (StringUtils.isNotEmpty(strWhere) && StringUtils.isNotBlank(strWhere))
            innerQuerySQL.append(" WHERE (").append(strWhere.trim()).append(")");
        if (StringUtils.isNotEmpty(strGroupBy) && StringUtils.isNotBlank(strGroupBy))
            innerQuerySQL.append(" GROUP BY (").append(strGroupBy.trim()).append(")");

        // Build final paging query SQL
        StringBuilder finalSQL = new StringBuilder();
        finalSQL.append("SELECT * FROM (");
        finalSQL.append(innerQuerySQL.toString()).append(") ").append(PAGEQUERY_TABLEALIAS);
        finalSQL.append(" WHERE ").append(PAGEQUERY_TABLEALIAS).append(".rowno >= %d").append(" ORDER BY ").append(PAGEQUERY_TABLEALIAS).append(".rowno");

        return finalSQL.toString();
    }

    @Override
    public String generatePageSQL(String SQL, int start, int page) {
        return String.format(SQL, start + page - 1, start);
    }
}
