$(function() {
    var Demo = function() {
    };
    Hrcms.debugEnabled = true;
    Hrcms.SyncGet = function(settings) {
        var result = null;
        $.ajax({
            async: false,
            cache: settings.cache ? settings.cache : false,
            url: "app/profile/" + settings.url,
            dataType: settings.dataType,
            type: "GET",
            success: function(data) {
                result = data;
            }
        });
        return result;
    };

    var I18N = Hrcms.I18N;
    var SysModuleID = Hrcms.SysModuleID = {
        // <ModuleName>_<SubModuleName>_<...>_<ItemName>
        SysToolbar_Home: "SysToolbar_Home",
        SysToolbar_Information: "SysToolbar_Information",
        SysToolbar_Statistic_Analysis: "SysToolbar_Statistic_Analysis",
        SysToolbar_ApproveProgress: "SysToolbar_ApproveProgress",
        SysToolbar_MessageCenter: "SysToolbar_MessageCenter",
        SysToolbar_SystemManage: "SysToolbar_SystemManage",
        ContentNavMenu_InfoManage_Info_Person: "ContentNavMenu_InfoManage_Info_Person",
        ContentNavMenu_InfoManage_Info_ContactInfo: "ContentNavMenu_InfoManage_Info_ContactInfo",
        ContentNavMenu_InfoManage_Info_PersonNature: "ContentNavMenu_InfoManage_Info_PersonNature",
        ContentNavMenu_InfoManage_Info_Evaluation: "ContentNavMenu_InfoManage_Info_Evaluation",
        ContentNavMenu_InfoManage_Info_Family: "ContentNavMenu_InfoManage_Info_Family",
        ContentNavMenu_InfoManage_Records_JobExperience: "ContentNavMenu_InfoManage_Records_JobExperience",
        ContentNavMenu_InfoManage_Records_Degree: "ContentNavMenu_InfoManage_Records_Degree",
        ContentNavMenu_InfoManage_Records_TrainingInLand: "ContentNavMenu_InfoManage_Records_TrainingInLand",
        ContentNavMenu_InfoManage_Records_TrainingOutLand: "ContentNavMenu_InfoManage_Records_TrainingOutLand",
        ContentNavMenu_InfoManage_Records_Award: "ContentNavMenu_InfoManage_Records_Award",
        ContentNavMenu_InfoManage_Records_Punishment: "ContentNavMenu_InfoManage_Records_Punishment",
        ContentNavMenu_InfoManage_Records_AnomalyInCollege: "ContentNavMenu_InfoManage_Records_AnomalyInCollege",
        ContentNavMenu_InfoManage_Records_InfoToJoinCollege: "ContentNavMenu_InfoManage_Records_InfoToJoinCollege",
        ContentNavMenu_InfoManage_Records_LeaveOffice: "ContentNavMenu_InfoManage_Records_LeaveOffice",
        ContentNavMenu_InfoManage_JobInfo_Unit: "ContentNavMenu_InfoManage_JobInfo_Unit",
        ContentNavMenu_InfoManage_JobInfo_Politics: "ContentNavMenu_InfoManage_JobInfo_Politics",
        ContentNavMenu_InfoManage_JobInfo_JobOfPolitics: "ContentNavMenu_InfoManage_JobInfo_JobOfPolitics",
        ContentNavMenu_InfoManage_JobInfo_TechnicalTitles: "ContentNavMenu_InfoManage_JobInfo_TechnicalTitles",
        ContentNavMenu_InfoManage_JobInfo_TechnicalLevel: "ContentNavMenu_InfoManage_JobInfo_TechnicalLevel",
        ContentNavMenu_InfoManage_Qualification_PostdoctoralTeacher: "ContentNavMenu_InfoManage_Qualification_PostdoctoralTeacher",
        ContentNavMenu_InfoManage_Qualification_ExpertJob: "ContentNavMenu_InfoManage_Qualification_ExpertJob",
        ContentNavMenu_InfoManage_Qualification_TalentsFunding: "ContentNavMenu_InfoManage_Qualification_TalentsFunding",
        ContentNavMenu_InfoManage_Contract_EmploymentContract: "ContentNavMenu_InfoManage_Contract_EmploymentContract",
        ContentNavMenu_StatisticAnalysisManage_PersonInfoStatistic_Report: "ContentNavMenu_StatisticAnalysisManage_PersonInfoStatistic_Report",
        ContentNavMenu_StatisticAnalysisManage_PersonInfoReport_RelationshipWithStudent: "ContentNavMenu_StatisticAnalysisManage_PersonInfoReport_RelationshipWithStudent",
        ContentNavMenu_StatisticAnalysisManage_PersonInfoReport_JobTitleInfo: "ContentNavMenu_StatisticAnalysisManage_PersonInfoReport_JobTitleInfo"
    };

    Hrcms.SysToolbar_Config = {
        eventType: "SysToolbar",
        buttons:
        [
            {
                title: I18N.SysToolbar.Home,
                moduleID: SysModuleID.SysToolbar_Home
            },
            {
                title: I18N.SysToolbar.Information,
                moduleID: SysModuleID.SysToolbar_Information
            },
            {
                title: I18N.SysToolbar.Statistic_Analysis,
                moduleID: SysModuleID.SysToolbar_Statistic_Analysis
            },
            {
                title: I18N.SysToolbar.ApproveProgress,
                moduleID: SysModuleID.SysToolbar_ApproveProgress
            },
            {
                title: I18N.SysToolbar.MessageCenter,
                moduleID: SysModuleID.SysToolbar_MessageCenter
            },
            {
                title: I18N.SysToolbar.SystemManage,
                moduleID: SysModuleID.SysToolbar_SystemManage
            }
        ]
    };
    Hrcms.NavMenu_InfoManage_Config = {
        eventType: "NavMenu_InfoManage",
        sections:
        [
            {
                title: I18N.ContentNavMenu.InfoManage_Info_Title,
                items: [
                    {
                        title: I18N.ContentNavMenu.InfoManage_Info_Person,
                        moduleID: SysModuleID.ContentNavMenu_InfoManage_Info_Person
                    },
                    {
                        title: I18N.ContentNavMenu.InfoManage_Info_ContactInfo,
                        moduleID: SysModuleID.ContentNavMenu_InfoManage_Info_ContactInfo
                    },
                    {
                        title: I18N.ContentNavMenu.InfoManage_Info_PersonNature,
                        moduleID: SysModuleID.ContentNavMenu_InfoManage_Info_PersonNature
                    },
                    {
                        title: I18N.ContentNavMenu.InfoManage_Info_Evaluation,
                        moduleID: SysModuleID.ContentNavMenu_InfoManage_Info_Evaluation
                    },
                    {
                        title: I18N.ContentNavMenu.InfoManage_Info_Family,
                        moduleID: SysModuleID.ContentNavMenu_InfoManage_Info_Family
                    }
                ]
            },
            {
                title: I18N.ContentNavMenu.InfoManage_Records_Title,
                items: [
                    {
                        title: I18N.ContentNavMenu.InfoManage_Records_JobExperience,
                        moduleID: SysModuleID.ContentNavMenu_InfoManage_Records_JobExperience
                    },
                    {
                        title: I18N.ContentNavMenu.InfoManage_Records_Degree,
                        moduleID: SysModuleID.ContentNavMenu_InfoManage_Records_Degree
                    },
                    {
                        title: I18N.ContentNavMenu.InfoManage_Records_TrainingInLand,
                        moduleID: SysModuleID.ContentNavMenu_InfoManage_Records_TrainingInLand
                    },
                    {
                        title: I18N.ContentNavMenu.InfoManage_Records_TrainingOutLand,
                        moduleID: SysModuleID.ContentNavMenu_InfoManage_Records_TrainingOutLand
                    },
                    {
                        title: I18N.ContentNavMenu.InfoManage_Records_Award,
                        moduleID: SysModuleID.ContentNavMenu_InfoManage_Records_Award
                    },
                    {
                        title: I18N.ContentNavMenu.InfoManage_Records_Punishment,
                        moduleID: SysModuleID.ContentNavMenu_InfoManage_Records_Punishment
                    },
                    {
                        title: I18N.ContentNavMenu.InfoManage_Records_AnomalyInCollege,
                        moduleID: SysModuleID.ContentNavMenu_InfoManage_Records_AnomalyInCollege
                    },
                    {
                        title: I18N.ContentNavMenu.InfoManage_Records_InfoToJoinCollege,
                        moduleID: SysModuleID.ContentNavMenu_InfoManage_Records_InfoToJoinCollege
                    },
                    {
                        title: I18N.ContentNavMenu.InfoManage_Records_LeaveOffice,
                        moduleID: SysModuleID.ContentNavMenu_InfoManage_Records_LeaveOffice
                    }
                ]
            },
            {
                title: I18N.ContentNavMenu.InfoManage_JobInfo_Title,
                items: [
                    {
                        title: I18N.ContentNavMenu.InfoManage_JobInfo_Unit,
                        moduleID: SysModuleID.ContentNavMenu_InfoManage_JobInfo_Unit
                    },
                    {
                        title: I18N.ContentNavMenu.InfoManage_JobInfo_Politics,
                        moduleID: SysModuleID.ContentNavMenu_InfoManage_JobInfo_Politics
                    },
                    {
                        title: I18N.ContentNavMenu.InfoManage_JobInfo_JobOfPolitics,
                        moduleID: SysModuleID.ContentNavMenu_InfoManage_JobInfo_JobOfPolitics
                    },
                    {
                        title: I18N.ContentNavMenu.InfoManage_JobInfo_TechnicalTitles,
                        moduleID: SysModuleID.ContentNavMenu_InfoManage_JobInfo_TechnicalTitles
                    },
                    {
                        title: I18N.ContentNavMenu.InfoManage_JobInfo_TechnicalLevel,
                        moduleID: SysModuleID.ContentNavMenu_InfoManage_JobInfo_TechnicalLevel
                    }
                ]
            },
            {
                title: I18N.ContentNavMenu.InfoManage_Qualification_Title,
                items: [
                    {
                        title: I18N.ContentNavMenu.InfoManage_Qualification_PostdoctoralTeacher,
                        moduleID: SysModuleID.ContentNavMenu_InfoManage_Qualification_PostdoctoralTeacher
                    },
                    {
                        title: I18N.ContentNavMenu.InfoManage_Qualification_VisitingScholarTeacher,
                        moduleID: SysModuleID.ContentNavMenu_InfoManage_Qualification_VisitingScholarTeacher
                    },
                    {
                        title: I18N.ContentNavMenu.InfoManage_Qualification_ExpertJob,
                        moduleID: SysModuleID.ContentNavMenu_InfoManage_Qualification_ExpertJob
                    },
                    {
                        title: I18N.ContentNavMenu.InfoManage_Qualification_TalentsFunding,
                        moduleID: SysModuleID.ContentNavMenu_InfoManage_Qualification_TalentsFunding
                    }
                ]
            },
            {
                title: I18N.ContentNavMenu.InfoManage_Contract_Title,
                items: [
                    {
                        title: I18N.ContentNavMenu.InfoManage_Contract_EmploymentContract,
                        moduleID: SysModuleID.ContentNavMenu_InfoManage_Contract_EmploymentContract
                    }
                ]
            }
        ]
    };

    Hrcms.NavMenu_StatisticAnalysisManage_Config = {
        eventType: "NavMenu_StatisticAnalysisManage",
        sections:
        [
            {
                title: I18N.ContentNavMenu.StatisticAnalysisManage_PersonInfoStatistic_Title,
                items: [
                    {
                        title: I18N.ContentNavMenu.StatisticAnalysisManage_PersonInfoStatistic_Report,
                        moduleID: SysModuleID.ContentNavMenu_StatisticAnalysisManage_PersonInfoStatistic_Report
                    }
                ]
            },
            {
                title: I18N.ContentNavMenu.StatisticAnalysisManage_PersonInfoReport_Title,
                items: [
                    {
                        title: I18N.ContentNavMenu.StatisticAnalysisManage_PersonInfoReport_RelationshipWithStudent,
                        moduleID: SysModuleID.ContentNavMenu_StatisticAnalysisManage_PersonInfoReport_RelationshipWithStudent
                    },
                    {
                        title: I18N.ContentNavMenu.StatisticAnalysisManage_PersonInfoReport_JobTitleInfo,
                        moduleID: SysModuleID.ContentNavMenu_StatisticAnalysisManage_PersonInfoReport_JobTitleInfo
                    }
                ]
            }
        ]
    };
});