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
    public static final String SQL_ALL = "SELECT 职工号, 姓名 FROM 个人概况";
    // Fields
    @Column(name = "职工号")
    private String employeeID;
    @Column(name = "姓名")
    private String employeeName;
    private String sex;
    private Date birthday;
    private String idCard;
    private String nation;
    private String nativePlace;
    private Date dateToJoinColleage;
    private Date dateToHaveJob;
    private String health;
    private String alienInfo;

    /**
     * @return the employeeID
     */
    public String getEmployeeID() {
        return employeeID;
    }

    /**
     * @param employeeID the employeeID to set
     */
    public void setEmployeeID(String employeeID) {
        this.employeeID = employeeID;
    }

    /**
     * @return the employeeName
     */
    public String getEmployeeName() {
        return employeeName;
    }

    /**
     * @param employeeName the employeeName to set
     */
    public void setEmployeeName(String employeeName) {
        this.employeeName = employeeName;
    }
}
