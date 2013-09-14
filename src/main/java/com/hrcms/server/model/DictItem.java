package com.hrcms.server.model;

import com.hrcms.server.dao.factory.Column;

public class DictItem {
    public static final String SQL_ALL = "SELECT t.代码, t.名称 FROM %s t";
    @Column(name = "代码")
    public String code;
    @Column(name = "名称")
    public String name;
}
