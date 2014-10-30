/*
 * To change this license header, choose License Headers in Project Properties.
 * To change this template file, choose Tools | Templates
 * and open the template in the editor.
 */

package com.lumens.connector.database.client.sqlserver.sql;

import static com.lumens.connector.database.DatabaseConstants.CONST_CNTR_SQLSERVER_FIELDS;
import com.lumens.model.Element;
import com.lumens.model.Value;

public class SqlServerWriteSQLBuilder extends SqlServerSQLBuilder{
    @Override
    public String generateInsertSQL(Element input) {
        String tableName = input.getFormat().getName();
        Element fieldList = input.getChild(CONST_CNTR_SQLSERVER_FIELDS);
        if (fieldList == null) {
            return null;
        }
        StringBuilder sql = new StringBuilder();
        StringBuilder fields = new StringBuilder();
        StringBuilder values = new StringBuilder();
        for (Element e : fieldList.getChildren()) {

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
        Element fieldList = input.getChild(CONST_CNTR_SQLSERVER_FIELDS);
        if (fieldList == null) {
            return null;
        }
        StringBuilder sql = new StringBuilder();
        StringBuilder values = new StringBuilder();
        for (Element e : fieldList.getChildren()) {
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
            Element clause = input.getChild(CONST_CNTR_SQLSERVER_CLAUSE);
            if (clause != null) {
                String sqlClause = clause.getValue().getString();
                if (sqlClause != null && !sqlClause.isEmpty()) {
                    sql.append(' ').append(sqlClause);
                }
            }
        }
        return sql.toString();
    }    
}
