/*
 * Copyright Lumens Team, Inc. All Rights Reserved.
 */
package com.lumens.sysdb;

import java.lang.reflect.Field;
import java.sql.ResultSet;
import java.sql.SQLException;

public class EntityFactory {

    public static <T> T createEntity(final Class<T> clazz, ResultSet rs) throws SQLException {
        try {
            T entity = (T) clazz.newInstance();
            Field[] fieldList = clazz.getDeclaredFields();
            int index = -1;
            for (int i = 0; i < fieldList.length; ++i) {
                Column column = fieldList[i].getAnnotation(Column.class);
                if (column == null)
                    continue;
                if (0 < (index = findColumnIndex(rs, column.name())))
                    fieldList[i].set(entity, rs.getObject(index));
            }
            return entity;
        } catch (InstantiationException | IllegalAccessException | SecurityException | SQLException | IllegalArgumentException ex) {
            throw new SQLException(ex);
        }
    }

    public static int findColumnIndex(ResultSet rs, String columnLabel) {
        try {
            return rs.findColumn(columnLabel);
        } catch (SQLException ex) {
            return -1;
        }
    }
}
