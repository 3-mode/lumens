package com.hrcms.server.dao.factory;

import com.hrcms.server.model.DictItem;
import com.hrcms.server.model.TableColumn;
import java.lang.reflect.Field;
import java.sql.ResultSet;
import java.sql.SQLException;
import java.util.List;
import java.util.Map.Entry;

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

    public static DictItem createDynamicDictEntity(List<TableColumn> cl, ResultSet rs) throws SQLException {
        try {
            DictItem entity = new DictItem();
            Field[] fieldList = entity.getClass().getDeclaredFields();
            for (int i = 0; i < fieldList.length; ++i) {
                Column column = fieldList[i].getAnnotation(Column.class);
                if (column != null && column.name().equals("HRCMS_DICT_DYNC_COLUMN")) {
                    for (TableColumn c : cl) {
                        entity.fields.put(c.columnName, rs.getObject(c.columnName));
                    }
                    break;
                }
            }
            return entity;
        } catch (Exception ex) {
            throw new SQLException(ex);
        }
    }

    public static <T> String createJsonFromEntity(T entity) throws IllegalArgumentException, IllegalAccessException {
        StringBuilder b = new StringBuilder();
        if (entity instanceof DictItem) {
            for (Entry<String, Object> entry : ((DictItem) entity).fields.entrySet()) {
                if (b.length() > 0) {
                    b.append(", ");
                }
                Object v = entry.getValue();
                b.append(String.format("\"%s\": \"%s\"", entry.getKey(), v == null ? "" : v.toString().replace("\"", "\\\"")));
            }
        } else {
            Class<T> clazz = (Class<T>) entity.getClass();
            Field[] fieldList = clazz.getDeclaredFields();
            for (int i = 0; i < fieldList.length; ++i) {
                Column column = fieldList[i].getAnnotation(Column.class);
                if (column == null) {
                    continue;
                }
                if (b.length() > 0) {
                    b.append(", ");
                }
                Object v = fieldList[i].get(entity);
                b.append(String.format("\"%s\": \"%s\"", column.name(), v == null ? "" : v.toString().replace("\"", "\\\"")));
            }
        }
        return String.format("{ %s }", b.toString());
    }
}