package com.hrcms.server.dao.factory;

import java.lang.reflect.Field;
import java.sql.ResultSet;
import java.sql.SQLException;

public class EntityFactory<T> {
    public static <T> T createEntity(Class<T> clazz, ResultSet rs) throws SQLException {
        try {
            T entity = (T) clazz.newInstance();
            Field[] fieldList = clazz.getDeclaredFields();
            for (int i = 0; i < fieldList.length; ++i) {
                Column column = fieldList[i].getAnnotation(Column.class);
                if (column == null) {
                    continue;
                }
                fieldList[i].set(entity, rs.getObject(column.name()));
            }
            return entity;
        } catch (Exception ex) {
            throw new SQLException(ex);
        }
    }

    public static <T> String createJsonFromEntity(T entity) throws IllegalArgumentException, IllegalAccessException {
        Class<T> clazz = (Class<T>) entity.getClass();
        Field[] fieldList = clazz.getDeclaredFields();
        StringBuilder b = new StringBuilder();
        for (int i = 0; i < fieldList.length; ++i) {
            Column column = fieldList[i].getAnnotation(Column.class);
            if (column == null) {
                continue;
            }
            if (b.length() > 0) {
                b.append(", ");
            }
            Object v = fieldList[i].get(entity);
            b.append(String.format("\"%s\": \"%s\"", column.name(), v == null ? "" : v.toString()));
        }
        return String.format("{ %s }", b.toString());
    }
}