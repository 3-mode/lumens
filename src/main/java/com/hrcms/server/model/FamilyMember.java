package com.hrcms.server.model;

import com.hrcms.server.dao.factory.Column;
import com.hrcms.server.dao.factory.Table;

@Table(name = "家庭成员")
public class FamilyMember {
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
