package com.hrcms.server.model;

import com.hrcms.server.dao.factory.Column;

/**
 *
 * @author shaofeng.wang@outlook.com
 */
public class AwardItem {
    @Column(name = "职工号")
    public String employeeID;
    @Column(name = "姓名")
    public String name;
    @Column(name = "获奖项目")
    public String award;
    @Column(name = "奖励级别")
    public String awardLevel;
    @Column(name = "获奖类别")
    public String awardClass;
    @Column(name = "奖励方式")
    public String awardMethod;
    @Column(name = "奖励名称")
    public String awardName;
    @Column(name = "奖励名称分类")
    public String awardType;
    @Column(name = "颁奖单位")
    public String awardUnit;
    @Column(name = "颁奖日期")
    public String awardDate;
    @Column(name = "荣誉称号")
    public String awardTitle;
    @Column(name = "获奖角色")
    public String awardRole;
    @Column(name = "备注")
    public String remark;
    @Column(name = "最后修改时间")
    public String lastModifytime;
}
