package com.hrcms.server.model;

import com.hrcms.server.dao.factory.Table;
import com.hrcms.server.dao.factory.Column;
import java.sql.Date;

/**
 *
 * @author shaofeng.wang@outlook.com
 */
@Table(name = "个人概况")
public class PersonSummaryListRecord {
    public static final String SQL_ALL_SUMMARY = "SELECT A.职工号,A.姓名, A.性别, A.出生日期, A.身份证号, B.名称 AS 民族, A.来校年月, A.参加工作年月, A.健康状况, A.外侨情况, A.婚姻状况, A.来源类别, A. 编制类别, A.人员类别 FROM 个人概况 A, DICT_民族名称代码 B WHERE A.民族＝B.代码(+)";
    public static final String SQL_BY_EMPLOYEEID = "SELECT 职工号, 姓名 FROM 个人概况 WHERE 职工号='%s'";
    // Fields
    @Column(name = "职工号")
    public String employeeID;
    @Column(name = "姓名")
    public String employeeName;
    public String sex;
    public Date birthday;
    public String idCard;
    public String nation;
    public String nativePlace;
    public Date dateToJoinColleage;
    public Date dateToHaveJob;
    public String health;
    public String alienInfo;
}
