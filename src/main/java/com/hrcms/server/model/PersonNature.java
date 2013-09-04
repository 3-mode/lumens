package com.hrcms.server.model;

import com.hrcms.server.dao.factory.Table;
import com.hrcms.server.dao.factory.Column;
import java.sql.Date;

/**
 *
 * @author shaofeng.wang@outlook.com
 */
@Table(name = "人员性质")
public class PersonNature {
    public static String SQL_ALL = "SELECT 职工号, 现记录, 调整时间, 编制代码, 岗位代码, 人员类别, 人员状态, 统计分类, 备注 FROM 人员性质";
    public static String SQL_BY_EMPLOYEEID = "SELECT 职工号, 现记录, 调整时间, 编制代码, 岗位代码, 人员类别, 人员状态, 统计分类, 备注 FROM 人员性质 WHERE 职工号='%s'";
    @Column(name = "职工号")
    private String employeeID;
    @Column(name = "现记录")
    private boolean registered;
    @Column(name = "调整时间")
    private Date changeTime;
    @Column(name = "编制代码")
    private Integer registerType;
    @Column(name = "岗位代码")
    private Integer post;
    @Column(name = "人员类别")
    private Integer staffType;
    @Column(name = "人员状态")
    private Integer staffStatus;
    @Column(name = "统计分类")
    private Integer classification;
    @Column(name = "备注")
    private String remark;

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
     * @return the registered
     */
    public boolean isRegistered() {
        return registered;
    }

    /**
     * @param registered the registered to set
     */
    public void setRegistered(boolean registered) {
        this.registered = registered;
    }

    /**
     * @return the changeTime
     */
    public Date getChangeTime() {
        return changeTime;
    }

    /**
     * @param changeTime the changeTime to set
     */
    public void setChangeTime(Date changeTime) {
        this.changeTime = changeTime;
    }

    /**
     * @return the registerType
     */
    public Integer getRegisterType() {
        return registerType;
    }

    /**
     * @param registerType the registerType to set
     */
    public void setRegisterType(Integer registerType) {
        this.registerType = registerType;
    }

    /**
     * @return the post
     */
    public Integer getPost() {
        return post;
    }

    /**
     * @param post the post to set
     */
    public void setPost(Integer post) {
        this.post = post;
    }

    /**
     * @return the staffType
     */
    public Integer getStaffType() {
        return staffType;
    }

    /**
     * @param staffType the staffType to set
     */
    public void setStaffType(Integer staffType) {
        this.staffType = staffType;
    }

    /**
     * @return the staffStatus
     */
    public Integer getStaffStatus() {
        return staffStatus;
    }

    /**
     * @param staffStatus the staffStatus to set
     */
    public void setStaffStatus(Integer staffStatus) {
        this.staffStatus = staffStatus;
    }

    /**
     * @return the classification
     */
    public Integer getClassification() {
        return classification;
    }

    /**
     * @param classification the classification to set
     */
    public void setClassification(Integer classification) {
        this.classification = classification;
    }

    /**
     * @return the remark
     */
    public String getRemark() {
        return remark;
    }

    /**
     * @param remark the remark to set
     */
    public void setRemark(String remark) {
        this.remark = remark;
    }
}
