package com.hrcms.server.model;

import com.hrcms.server.dao.factory.Column;
import com.hrcms.server.dao.factory.Table;

@Table(name = "USER_TAB_COMMENTS")
public class DictListRecord {
    @Column(name = "TABLE_NAME")
    public String tableName;
    @Column(name = "COMMENTS")
    public String tableComments;
}
