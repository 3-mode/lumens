/*
 * Copyright Lumens Team, Inc. All Rights Reserved.
 */
package com.lumens.connector.database.sqlserver;

import com.lumens.connector.database.client.DBQuerySQLBuilder;
import com.lumens.model.Format;
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
    protected String generatePageSQL(String tableName, String fields, String strWhere, String strOrderBy, String strGroupBy) {
        // TODO how paging in SQL Server
        StringBuilder innerQuerySQL = new StringBuilder();
        innerQuerySQL.append("SELECT ").append(fields).append("ROWNUM AS rowno").append(" FROM ").append(tableName);
        if (StringUtils.isNotEmpty(strWhere) && StringUtils.isNotBlank(strWhere))
            innerQuerySQL.append(" WHERE (").append(strWhere.trim()).append(") AND ROWNUM < %d");
        else
            innerQuerySQL.append(" WHERE ").append("ROWNUM < %d");
        if (StringUtils.isNotEmpty(strOrderBy) && StringUtils.isNotBlank(strOrderBy))
            innerQuerySQL.append(" ORDERBY (").append(strOrderBy.trim()).append(")");
        if (StringUtils.isNotEmpty(strGroupBy) && StringUtils.isNotBlank(strGroupBy))
            innerQuerySQL.append(" GROUPBY (").append(strGroupBy.trim()).append(")");

        // Build final paging query SQL
        StringBuilder finalSQL = new StringBuilder();
        String aliasName = tableName + "_Alias";
        finalSQL.append("SELECT * FROM (");
        finalSQL.append(innerQuerySQL.toString()).append(") ").append(aliasName).append(" WHERE ").append(aliasName).append(".rowno >= %d");

        return finalSQL.toString();
    }

}
