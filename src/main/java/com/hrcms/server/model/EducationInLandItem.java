package com.hrcms.server.model;

import com.hrcms.server.dao.factory.Column;
import com.hrcms.server.dao.factory.Table;
import java.math.BigDecimal;

@Table(name = "学历学位")
public class EducationInLandItem {
    public static String SQL_ALL_EDUCATIONS = "select t1.全局编号, t1.职工号, t3.姓名, t1.最后修改时间, t4.名称 as 进修性质, t6.名称 as 学习方式, t1.学习起始年月, t1.学习终止年月, t1.总学时, t1.学习内容, t1.进修班名称, t1.主办单位, t7.名称 as 主办单位性质, t1.在学单位, t2.名称 as 在学单位类别, t5.名称 as 进修结果, t1.备注"
                                              + " from 国内进修培训 t1, dict_在学单位类别代码 t2, 个人概况 t3, dict_进修性质代码 t4, dict_教育结果代码 t5, dict_学习方式代码 t6, dict_单位性质代码 t7"
                                              + " where t1.在学单位类别 = t2.代码(+) and t1.职工号 = t3.职工号(+) and t1.进修性质 = t4.代码(+) and t1.进修结果 = t5.代码(+) and t1.学习方式 = t6.代码(+) and t1.主办单位性质 = t7.代码(+) order by t1.职工号";
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
