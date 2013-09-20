package com.hrcms.server.model;

import com.hrcms.server.dao.factory.Column;
import com.hrcms.server.dao.factory.Table;
import java.math.BigDecimal;

@Table(name = "来校前工作简历")
public class ResumeItem {
    public static String SQL_ALL_RESUMEITEMS = "SELECT t1.全局编号, t1.职工号, t2.姓名, t1.最后修改时间, t1.起始年月, t1.截止年月, t1.所在单位名称, t1.从事工作内容, t1.曾任党政职务, t1.曾任技术职务, t1.证明人, t1.备注 FROM 来校前工作简历 t1, 个人概况 t2 WHERE t1.职工号 = t2.职工号(+) ORDER BY t1.职工号";
    @Column(name = "全局编号")
    public String globalID;
    @Column(name = "职工号")
    public String employeeID;
    @Column(name = "姓名")
    public String name;
    @Column(name = "最后修改时间")
    public String lastModifyTime;
    @Column(name = "起始年月")
    public String startDate;
    @Column(name = "截止年月")
    public String endDate;
    @Column(name = "所在单位名称")
    public String unitName;
    @Column(name = "从事工作内容")
    public String jobContent;
    @Column(name = "曾任党政职务")
    public BigDecimal jobTitle;
    @Column(name = "曾任技术职务")
    public BigDecimal jobLevel;
    @Column(name = "证明人")
    public String certifier;
    @Column(name = "备注")
    public String remark;
}
