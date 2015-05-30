/*
 * Copyright Lumens Team, Inc. All Rights Reserved.
 */
package com.lumens.connector.rapidsync;

import static com.lumens.connector.database.DBConstants.PAGEQUERY_TABLEALIAS;
import com.lumens.connector.database.client.DBQuerySQLBuilder;
import com.lumens.model.Format;
import org.apache.commons.lang.StringUtils;

/**
 *
 * @author Xiaoxin(whiskeyfly@163.com)
 */
public class RapidSyncQuerySQLBuilder extends DBQuerySQLBuilder implements RapidSyncConstants {

    public RapidSyncQuerySQLBuilder(Format output) {
        super(output);
    }

    @Override
    protected String generatePageSQL(Format table, String fields, String strWhere, String strOrderBy, String strGroupBy) {
        StringBuilder innerQuerySQL = new StringBuilder();
        innerQuerySQL.append("SELECT ").append(fields).append(" FROM ").append(table.getName());
        innerQuerySQL.append(" WHERE ( seg_type_name='TABLE' AND operation !='SELECT_FOR_UPDATE') ");
        if (StringUtils.isNotEmpty(strWhere) && StringUtils.isNotBlank(strWhere)) {
            innerQuerySQL.append(" AND ( ").append(strWhere.trim()).append(") ");
        }

        if (StringUtils.isNotEmpty(strOrderBy) && StringUtils.isNotBlank(strOrderBy)) {
            innerQuerySQL.append(" ORDER BY ").append(strOrderBy.trim());
        }
        if (StringUtils.isNotEmpty(strGroupBy) && StringUtils.isNotBlank(strGroupBy)) {
            innerQuerySQL.append(" GROUP BY ").append(strGroupBy.trim());
        }

        return innerQuerySQL.toString();
    }

    @Override
    public String generatePageSQL(String SQL, int start, int page) {
        return String.format(SQL, start + page, start);
    }
}
