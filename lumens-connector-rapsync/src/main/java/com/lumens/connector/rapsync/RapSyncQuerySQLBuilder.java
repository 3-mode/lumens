/*
 * Copyright Lumens Team, Inc. All Rights Reserved.
 */
package com.lumens.connector.rapsync;

import com.lumens.connector.database.client.DBQuerySQLBuilder;
import com.lumens.model.Format;
import org.apache.commons.lang.StringUtils;

/**
 *
 * @author Xiaoxin(whiskeyfly@163.com)
 */
public class RapSyncQuerySQLBuilder extends DBQuerySQLBuilder implements RapSyncConstants {

    public RapSyncQuerySQLBuilder(Format output) {
        super(output);
    }

    @Override
    protected String generatePageSQL(Format table, String fields, String strWhere, String strOrderBy, String strGroupBy) {
        StringBuilder innerQuerySQL = new StringBuilder();
        innerQuerySQL.append("SELECT ").append(fields).append(" FROM ").append("V_$LOGMNR_CONTENTS");
        innerQuerySQL.append(" WHERE ( seg_type_name='TABLE' AND operation !='SELECT_FOR_UPDATE') ");
        if (StringUtils.isNotEmpty(strWhere) && StringUtils.isNotBlank(strWhere)) {
            innerQuerySQL.append(" AND ( ").append(strWhere.trim()).append(") ");
        }
        
        innerQuerySQL.append(" ORDER BY SCN ASC");  // Enforce to order by SCN as Oracle does not order by default        
        
        return innerQuerySQL.toString();
    }

    @Override
    public String generatePageSQL(String SQL, int start, int page) {
        return String.format(SQL, start + page, start);
    }
}
