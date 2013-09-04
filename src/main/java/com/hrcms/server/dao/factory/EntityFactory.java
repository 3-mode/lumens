package com.hrcms.server.dao.factory;

import java.beans.PropertyDescriptor;
import java.lang.reflect.Field;
import java.lang.reflect.Method;
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
                PropertyDescriptor pd = new PropertyDescriptor(fieldList[i].getName(), clazz);
                Method wM = pd.getWriteMethod();//获得写方法
                wM.invoke(entity, rs.getObject(column.name()));
            }
            return entity;
        } catch (Exception ex) {
            throw new SQLException(ex);
        }
    }
}