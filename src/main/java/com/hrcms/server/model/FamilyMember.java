package com.hrcms.server.model;

import com.hrcms.server.dao.factory.Column;
import com.hrcms.server.dao.factory.Table;

@Table(name = "家庭成员")
public class FamilyMember {
    public static final String SQL_ALL_MEMBER = "SELECT f.全局编号, f.职工号, g.姓名, f.主要成员姓名 AS 成员姓名, c.名称 AS 称谓, f.出生时间, f.工作单位, f.备注, f.最后修改时间 FROM 家庭成员 f, 个人概况 g,  DICT_称谓代码 c WHERE f.职工号 = g.职工号(+) AND f.称谓 = c.代码(+) ORDER BY f.职工号";
    @Column(name = "职工号")
    public String globalID;
    @Column(name = "姓名")
    public String name;
    @Column(name = "成员姓名")
    public String memberName;
    @Column(name = "称谓")
    public String relation;
    @Column(name = "出生时间")
    public String dateOfBirth;
    @Column(name = "工作单位")
    public String unit;
    @Column(name = "备注")
    public String remark;
    @Column(name = "最后修改时间")
    public String lastModifyTime;
}
