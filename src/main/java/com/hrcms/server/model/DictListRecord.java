package com.hrcms.server.model;

import com.hrcms.server.dao.factory.Column;
import com.hrcms.server.dao.factory.Table;

@Table(name = "USER_TAB_COMMENTS")
public class DictListRecord {
    public static final String TABLENAMES = "SELECT t.TABLE_NAME,t.COMMENTS FROM USER_TAB_COMMENTS t WHERE t.TABLE_NAME LIKE 'DICT_%'";
    @Column(name = "TABLE_NAME")
    public String tableName;
    @Column(name = "COMMENTS")
    public String tableComments;
}
