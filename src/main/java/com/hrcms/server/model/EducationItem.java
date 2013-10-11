package com.hrcms.server.model;

import com.hrcms.server.dao.factory.Column;
import com.hrcms.server.dao.factory.Table;
import java.math.BigDecimal;

@Table(name = "学历学位")
public class EducationItem {
    @Column(name = "全局编号")
    public String globalID;
    @Column(name = "职工号")
    public String employeeID;
    @Column(name = "姓名")
    public String name;
    @Column(name = "学历")
    public String education;
    @Column(name = "所学专业")
    public String subject;
    @Column(name = "入学年月")
    public String dateIn;
    @Column(name = "学习形式")
    public String learningMode;
    @Column(name = "学习方式")
    public String learningMethod;
    @Column(name = "学制")
    public BigDecimal plan;
    @Column(name = "毕业年月")
    public String dateOut;
    @Column(name = "毕肄业学校或单位")
    public String college;
    @Column(name = "学位")
    public String degree;
    @Column(name = "学位授予日期")
    public String dateToGetDegree;
    @Column(name = "学位授予国家")
    public String country;
    @Column(name = "学位授予单位")
    public String unit;
    @Column(name = "备注")
    public String remark;
    @Column(name = "最后修改时间")
    public String lastModifyTime;
}
