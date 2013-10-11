package com.hrcms.server.model;

import com.hrcms.server.dao.factory.Column;
import com.hrcms.server.dao.factory.Table;

@Table(name = "考核")
public class Evaluation {
    @Column(name = "职工号")
    public String employeeID;
    @Column(name = "姓名")
    public String name;
    @Column(name = "考核类别")
    public String evaluationClass;
    @Column(name = "考核岗位")
    public String station;
    @Column(name = "考核内容")
    public String content;
    @Column(name = "考核等级")
    public String rank;
    @Column(name = "考核结果")
    public String result;
    @Column(name = "考核时间")
    public String date;
    @Column(name = "备注")
    public String remark;
}
