package com.hrcms.server.model;

import com.hrcms.server.dao.factory.Column;
import com.hrcms.server.dao.factory.Table;
import java.math.BigDecimal;

@Table(name = "出国进修")
public class EducationOutLandItem {
    @Column(name = "职工号")
    public String employeeID;
    @Column(name = "姓名")
    public String name;
    @Column(name = "出国日期")
    public String outDate;
    @Column(name = "出国目的")
    public String outGoal;
    @Column(name = "出国国别")
    public String country;
    @Column(name = "所去单位英文名称")
    public String unitEnglishName;
    @Column(name = "所去单位中文名称")
    public String unitChineseName;
    @Column(name = "指导教师")
    public String yourTeacher;
    @Column(name = "指导教师email")
    public String teacherEmail;
    @Column(name = "派出单位")
    public String unitFrom;
    @Column(name = "经费项目渠道")
    public String fundingChannel;
    @Column(name = "出国经费来源")
    public String fundingSource;
    @Column(name = "审批单位")
    public String authorityUnit;
    @Column(name = "审批日期")
    public String authorityDate;
    @Column(name = "审批文号")
    public String authorityID;
    @Column(name = "批准期限")
    public String deadLine;
    @Column(name = "批延期限")
    public String extensionDate;
    @Column(name = "学习工作内容")
    public String learningContent;
    @Column(name = "学习工作业绩")
    public String achievements;
    @Column(name = "学习方式")
    public String learningMode;
    @Column(name = "应归日期")
    public String returnDate;
    @Column(name = "状态")
    public String status;
    @Column(name = "相应日期")
    public String correspondingDate;
    @Column(name = "经济关系")
    public BigDecimal economicRelation;
    @Column(name = "停薪年月")
    public String salaryFreezeDate;
    @Column(name = "恢复薪金年月")
    public String salaryRecoverDate;
    @Column(name = "担保人")
    public String bondsman;
    @Column(name = "担保人所在单位")
    public BigDecimal bondsmanUnit;
    @Column(name = "回国入境日期")
    public String entryDate;
    @Column(name = "备注")
    public String remark;
    @Column(name = "最后修改时间")
    public String lastModifyTime;
}
