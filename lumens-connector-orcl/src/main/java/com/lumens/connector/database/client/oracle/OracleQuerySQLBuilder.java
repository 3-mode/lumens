/*
 * Copyright Lumens Team, Inc. All Rights Reserved.
 */
package com.lumens.connector.database.client.oracle;

import com.lumens.connector.database.client.DBQuerySQLBuilder;
import com.lumens.model.Format;
import org.apache.commons.lang.StringUtils;

/**
 *
 * @author Shaofeng Wang <shaofeng.wang@outlook.com>
 */
public class OracleQuerySQLBuilder extends DBQuerySQLBuilder {

    public OracleQuerySQLBuilder(Format output) {
        super(output);
    }

    @Override
    protected String generatePageSQL(Format table, String fields, String strWhere, String strOrderBy, String strGroupBy) {
        StringBuilder innerQuerySQL = new StringBuilder();
        innerQuerySQL.append("SELECT ").append(fields).append(", ROWNUM AS rowno").append(" FROM ").append(table.getName());
        if (StringUtils.isNotEmpty(strWhere) && StringUtils.isNotBlank(strWhere))
            innerQuerySQL.append(" WHERE (").append(strWhere.trim()).append(") AND ROWNUM < %d");
        else
            innerQuerySQL.append(" WHERE ").append("ROWNUM < %d");
        if (StringUtils.isNotEmpty(strOrderBy) && StringUtils.isNotBlank(strOrderBy))
            innerQuerySQL.append(" ORDER BY (").append(strOrderBy.trim()).append(")");
        if (StringUtils.isNotEmpty(strGroupBy) && StringUtils.isNotBlank(strGroupBy))
            innerQuerySQL.append(" GROUP BY (").append(strGroupBy.trim()).append(")");

        // Build final paging query SQL
        StringBuilder finalSQL = new StringBuilder();
        finalSQL.append("SELECT * FROM (");
        finalSQL.append(innerQuerySQL.toString()).append(") ").append(PAGEQUERY_TABLEALIAS).append(" WHERE ").append(PAGEQUERY_TABLEALIAS).append(".rowno >= %d");

        return finalSQL.toString();
    }

    @Override
    public String generatePageSQL(String SQL, int start, int page) {
        return String.format(SQL, start + page, start);
    }

}
