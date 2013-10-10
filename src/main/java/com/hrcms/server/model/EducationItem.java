package com.hrcms.server.model;

import com.hrcms.server.dao.factory.Column;
import com.hrcms.server.dao.factory.Table;
import java.math.BigDecimal;

@Table(name = "学历学位")
public class EducationItem {
    public static String SQL_ALL_EDUCATIONS = "select t1.全局编号, t1.职工号, t6.姓名, t1.学历, t1.所学专业, t1.入学年月, t2.名称 as 学习形式, t3.名称 as 学习方式, t1.学制, "
                                              + "t1.毕业年月, t1.毕肄业学校或单位, t4.名称 as 学位, t1.学位授予日期, t5.名称 as 学位授予国家, t1.学位授予单位, t1.备注, t1.最后修改时间 "
                                              + "from 学历学位 t1, DICT_学习形式代码 t2, DICT_学习方式代码 t3, DICT_学位代码 t4, DICT_国家代码 t5, 个人概况 t6 "
                                              + "where t1.学习形式 = t2.代码(+) and t1.学习方式 = t3.代码(+) and t1.学位 = t4.代码(+) and t1.学位授予国家 = t5.代码(+) and t1.职工号 = t6.职工号(+) "
                                              + "order by t1.职工号";
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
