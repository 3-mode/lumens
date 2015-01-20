/*
 * Copyright Lumens Team, Inc. All Rights Reserved.
 */
package com.lumens.connector.database.client;

import com.lumens.connector.database.DBConstants;
import com.lumens.model.Utils;
import com.lumens.model.Element;
import com.lumens.model.Value;
import org.apache.commons.lang.StringUtils;

/**
 *
 * @author shaofeng.wang@outlook.com
 */
public class DBWriteSQLBuilder extends DBSQLBuilder {

    @Override
    public String generateInsertSQL(Element input) {
        String tableName = input.getFormat().getName();
        StringBuilder sql = new StringBuilder();
        StringBuilder fields = new StringBuilder();
        StringBuilder values = new StringBuilder();
        for (Element e : input.getChildren()) {
            if (DBConstants.SQLPARAMS.equals(e.getFormat().getName()))
                continue;
            Value v = e.getValue();
            if (v != null) {
                if (fields.length() > 0)
                    fields.append(", ");
                fields.append(e.getFormat().getName());
                if (values.length() > 0)
                    values.append(", ");
                if (v.isString() || v.isDate())
                    values.append("'").append(v.isNull() ? "" : v.getString()).append("'");
                else
                    values.append(v.isNull() ? "" : v.getString());
            }
        }
        return sql.append("INSERT INTO ").append(tableName).append(" (").append(fields.toString()).append(") VALUES (").append(values.toString()).append(")").toString();
    }

    @Override
    public String generateUpdateSQL(Element input) {
        String tableName = input.getFormat().getName();
        StringBuilder sql = new StringBuilder();
        StringBuilder values = new StringBuilder();
        for (Element e : input.getChildren()) {
            if (DBConstants.SQLPARAMS.equals(e.getFormat().getName()))
                continue;
            if (values.length() > 0) {
                values.append(", ");
            }
            values.append(e.getFormat().getName()).append('=');
            Value v = e.getValue();
            if (v.isString()) {
                values.append("'").append(v.getString()).append("'");
            } else {
                values.append(v.getString());
            }
        }
        sql.append("UPDATE ").append(tableName).append(" SET ").append(values.toString());
        if (input != null) {
            Element whereElem = input.getChild(DBConstants.WHERE);
            if (!Utils.isNullValue(whereElem)) {
                String strWhere = whereElem.getValue().getString();
                if (StringUtils.isNotEmpty(strWhere) && StringUtils.isNotBlank(strWhere)) {
                    sql.append(' ').append(strWhere);
                }
            }
        }
        return sql.toString();
    }
}
