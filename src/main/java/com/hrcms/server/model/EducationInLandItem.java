package com.hrcms.server.model;

import com.hrcms.server.dao.factory.Column;
import com.hrcms.server.dao.factory.Table;
import java.math.BigDecimal;

@Table(name = "学历学位")
public class EducationInLandItem {
    @Column(name = "全局编号")
    public String globalID;
    @Column(name = "职工号")
    public String employeeID;
    @Column(name = "姓名")
    public String name;
    @Column(name = "进修性质")
    public String educationNature;
    @Column(name = "学习方式")
    public String educationMethod;
    @Column(name = "学习起始年月")
    public String dateIn;
    @Column(name = "总学时")
    public BigDecimal educationTotalTime;
    @Column(name = "学习终止年月")
    public String dateOut;
    @Column(name = "学习内容")
    public String educationContent;
    @Column(name = "进修班名称")
    public String className;
    @Column(name = "主办单位")
    public String unitProvideEducation;
    @Column(name = "主办单位性质")
    public String unitNature;
    @Column(name = "在学单位")
    public String unitForEducation;
    @Column(name = "在学单位类别")
    public String unitForEducationClass;
    @Column(name = "进修结果")
    public String educationResult;
    @Column(name = "备注")
    public String remark;
    @Column(name = "最后修改时间")
    public String lastModifyTime;
}
