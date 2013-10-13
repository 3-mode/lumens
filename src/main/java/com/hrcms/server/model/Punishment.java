package com.hrcms.server.model;

import com.hrcms.server.dao.factory.Column;
import com.hrcms.server.dao.factory.Table;

/**
 *
 * @author shaofeng.wang@outlook.com
 */
@Table(name = "惩处")
public class Punishment {
    @Column(name = "全局编号")
    public String globalID;
    @Column(name = "职工号")
    public String employeeID;
    @Column(name = "姓名")
    public String name;
    @Column(name = "惩处名称")
    public String punishmentName;
    @Column(name = "惩处原因")
    public String punishmentReason;
    @Column(name = "惩处内容")
    public String punishmentContent;
    @Column(name = "惩处单位")
    public String punishmentUnit;
    @Column(name = "惩处文号")
    public String punishmentDocID;
    @Column(name = "惩处日期")
    public String punishmentDate;
    @Column(name = "惩处撤销日期")
    public String removePunishmentDate;
    @Column(name = "惩处撤销文号")
    public String removePunishmentDocID;
    @Column(name = "惩处撤销原因")
    public String removePunishmentReason;
    @Column(name = "最后修改时间")
    public String lastModifyTime;
    @Column(name = "备注")
    public String remark;
}
