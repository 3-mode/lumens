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
    public String employeeID;
    @Column(name = "现记录")
    public boolean registered;
    @Column(name = "调整时间")
    public Date changeTime;
    @Column(name = "编制代码")
    public Integer registerType;
    @Column(name = "岗位代码")
    public Integer post;
    @Column(name = "人员类别")
    public Integer staffType;
    @Column(name = "人员状态")
    public Integer staffStatus;
    @Column(name = "统计分类")
    public Integer classification;
    @Column(name = "备注")
    public String remark;
}
