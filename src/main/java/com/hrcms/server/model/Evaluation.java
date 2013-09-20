package com.hrcms.server.model;

import com.hrcms.server.dao.factory.Column;
import com.hrcms.server.dao.factory.Table;
import java.math.BigDecimal;

@Table(name = "考核")
public class Evaluation {
    public static final String SQL_ALL_EVALUATION = "SELECT k.全局编号, k.职工号, g.姓名, lb.名称 AS 考核类别, k.考核岗位, k.考核内容, dj.名称 AS 考核等级, jg.名称 AS 考核结果, k.考核时间, k.备注, k.最后修改时间 FROM 考核 k, 个人概况 g, DICT_考核类别代码 lb, DICT_考核等级代码 dj, DICT_考核结果代码 jg  WHERE k.职工号 = g.职工号(+) AND  k.考核类别 = lb.代码(+) AND k.考核等级 = dj.代码(+) AND k.考核结果 = jg.代码(+) ORDER BY k.职工号";
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
