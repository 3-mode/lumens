$(function() {
    if (!window.Hrcms)
        window.Hrcms = {};
    if (!Hrcms.I18N)
        Hrcms.I18N = {};
    if (!Hrcms.I18N.lang)
        Hrcms.I18N.lang = "zh_CN";
    var I18N = Hrcms.I18N;
    // System business moudles
    I18N.SystemTitle = "高校人力资源管理信息系统";
    // Toolbar module
    I18N.SysToolbar = {
        Home: "首页管理",
        Information: "信息管理",
        Statistic_Analysis: "统计分析管理",
        ApproveProgress: "审批管理",
        MessageCenter: "消息管理",
        SystemManage: "系统管理"
    };

    // Left nav module
    I18N.ContentNavMenu = {
        InfoManage_Info_Title: "基本信息类",
        InfoManage_Info_Person: "个人概况",
        InfoManage_Info_ContactInfo: "联系方式",
        InfoManage_Info_PersonNature: "人员性质",
        InfoManage_Info_Evaluation: "考核",
        InfoManage_Info_Family: "家庭成员",
        InfoManage_Records_Title: "履历类信息",
        InfoManage_Records_JobExperience: "来校前工作简历",
        InfoManage_Records_Degree: "学历学位",
        InfoManage_Records_TrainingInLand: "国内进修培训",
        InfoManage_Records_TrainingOutLand: "出国（境）进修",
        InfoManage_Records_Award: "荣誉性奖励",
        InfoManage_Records_Punishment: "惩处",
        InfoManage_Records_AnomalyInCollege: "校内异动",
        InfoManage_Records_InfoToJoinCollege: "来校信息",
        InfoManage_Records_LeaveOffice: "离校",
        InfoManage_JobInfo_Title: "任职类信息",
        InfoManage_JobInfo_Unit: "任职单位",
        InfoManage_JobInfo_Politics: "政治面貌",
        InfoManage_JobInfo_JobOfPolitics: "党政职务",
        InfoManage_JobInfo_TechnicalTitles: "专业技术职称",
        InfoManage_JobInfo_TechnicalLevel: "工人技术等级",
        InfoManage_Qualification_Title: "资格类信息",
        InfoManage_Qualification_PostdoctoralTeacher: "指导博士后",
        InfoManage_Qualification_VisitingScholarTeacher: "指导国内访问学者",
        InfoManage_Qualification_ExpertJob: "专家工作",
        InfoManage_Qualification_TalentsFunding: "人才项目资助",
        InfoManage_Contract_Title: "合同类信息",
        InfoManage_Contract_EmploymentContract: "聘用合同",
        StatisticAnalysisManage_PersonInfoStatistic_Title: "人员情况统计",
        StatisticAnalysisManage_PersonInfoStatistic_Report: "报表",
        StatisticAnalysisManage_PersonInfoReport_Title: "人员情况表",
        StatisticAnalysisManage_PersonInfoReport_RelationshipWithStudent: "学缘",
        StatisticAnalysisManage_PersonInfoReport_JobTitleInfo: "职称情况"
    };

});


