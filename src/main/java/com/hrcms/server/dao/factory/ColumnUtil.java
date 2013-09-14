package com.hrcms.server.dao.factory;

public class ColumnUtil {
    public static String getColumnName(Object object, String field) throws NoSuchFieldException {
        return object.getClass().getField(field).getAnnotation(Column.class).name();
    }
}
