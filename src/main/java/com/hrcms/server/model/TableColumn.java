package com.hrcms.server.model;

import com.hrcms.server.dao.factory.Column;
import com.hrcms.server.dao.factory.Table;

@Table(name = "USER_TAB_COLUMNS")
public class TableColumn {
    public static String TABLECOLUMNS = "SELECT t.COLUMN_NAME FROM USER_TAB_COLUMNS t WHERE t.TABLE_NAME = '%s'";
    @Column(name = "COLUMN_NAME")
    public String columnName;
}
