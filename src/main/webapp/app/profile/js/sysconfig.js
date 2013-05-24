$(function() {
    if (!window.Hrcms)
        window.Hrcms = {};
    var Demo = function() {
    };
    Hrcms.debugEnabled = false;
    Hrcms.SyncGet = function(settings) {
        var result = null;
        $.ajax({
            async: false,
            cache: settings.cache ? settings.cache : true,
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
    Hrcms.SysToolbar_Config = {
        event_callback: null,
        settings:
        [
            {
                title: I18N.Toolbar.Home,
                module_id: "Toolbar.Home"
            },
            {
                title: I18N.Toolbar.Information,
                module_id: "Toolbar.Information"
            },
            {
                title: I18N.Toolbar.Statistic_Analysis,
                module_id: "Toolbar.Statistic_Analysis"
            },
            {
                title: I18N.Toolbar.ApproveProgress,
                module_id: "Toolbar.ApproveProgress"
            },
            {
                title: I18N.Toolbar.MessageCenter,
                module_id: "Toolbar.MessageCenter"
            },
            {
                title: I18N.Toolbar.SystemManage,
                module_id: "Toolbar.SystemManage"
            }
        ]
    };
    Hrcms.NavMenu_InfoManage_Config = {
        event_callback: null,
        settings:
        [
            {
                title: I18N.ContentNavMenu.InfoManage_Info_Title,
                items: [
                    {
                        title: I18N.ContentNavMenu.InfoManage_Info_Person,
                        module_id: "ContentNavMenu.InfoManage_Info_Person"
                    },
                    {
                        title: I18N.ContentNavMenu.InfoManage_Info_ContactInfo,
                        module_id: "ContentNavMenu.InfoManage_Info_ContactInfo"
                    },
                    {
                        title: I18N.ContentNavMenu.InfoManage_Info_PersonNature,
                        module_id: "ContentNavMenu.InfoManage_Info_PersonNature"
                    },
                    {
                        title: I18N.ContentNavMenu.InfoManage_Info_Evaluation,
                        module_id: "ContentNavMenu.InfoManage_Info_Evaluation"
                    },
                    {
                        title: I18N.ContentNavMenu.InfoManage_Info_Family,
                        module_id: "ContentNavMenu.InfoManage_Info_Family"
                    }
                ]
            },
            {
                title: I18N.ContentNavMenu.InfoManage_Records_Title,
                items: [
                    {
                        title: I18N.ContentNavMenu.InfoManage_Records_JobExperience,
                        module_id: "ContentNavMenu.InfoManage_Records_JobExperience"
                    },
                    {
                        title: I18N.ContentNavMenu.InfoManage_Records_Degree,
                        module_id: "ContentNavMenu.InfoManage_Records_Degree"
                    },
                    {
                        title: I18N.ContentNavMenu.InfoManage_Records_TrainingInLand,
                        module_id: "ContentNavMenu.InfoManage_Records_TrainingInLand"
                    },
                    {
                        title: I18N.ContentNavMenu.InfoManage_Records_TrainingOutLand,
                        module_id: "ContentNavMenu.InfoManage_Records_TrainingOutLand"
                    },
                    {
                        title: I18N.ContentNavMenu.InfoManage_Records_Award,
                        module_id: "ContentNavMenu.InfoManage_Records_Award"
                    },
                    {
                        title: I18N.ContentNavMenu.InfoManage_Records_Punishment,
                        module_id: "ContentNavMenu.InfoManage_Records_Punishment"
                    },
                    {
                        title: I18N.ContentNavMenu.InfoManage_Records_AnomalyInCollege,
                        module_id: "ContentNavMenu.InfoManage_Records_AnomalyInCollege"
                    },
                    {
                        title: I18N.ContentNavMenu.InfoManage_Records_InfoToJoinCollege,
                        module_id: "ContentNavMenu.InfoManage_Records_InfoToJoinCollege"
                    },
                    {
                        title: I18N.ContentNavMenu.InfoManage_Records_LeaveOffice,
                        module_id: "ContentNavMenu.InfoManage_Records_LeaveOffice"
                    }
                ]
            },
            {
                title: I18N.ContentNavMenu.InfoManage_JobInfo_Title,
                items: [
                    {
                        title: I18N.ContentNavMenu.InfoManage_JobInfo_Unit,
                        module_id: "ContentNavMenu.InfoManage_JobInfo_Unit"
                    },
                    {
                        title: I18N.ContentNavMenu.InfoManage_JobInfo_Politics,
                        module_id: "ContentNavMenu.InfoManage_JobInfo_Politics"
                    },
                    {
                        title: I18N.ContentNavMenu.InfoManage_JobInfo_JobOfPolitics,
                        module_id: "ContentNavMenu.InfoManage_JobInfo_JobOfPolitics"
                    },
                    {
                        title: I18N.ContentNavMenu.InfoManage_JobInfo_TechnicalTitles,
                        module_id: "ContentNavMenu.InfoManage_JobInfo_TechnicalTitles"
                    },
                    {
                        title: I18N.ContentNavMenu.InfoManage_JobInfo_TechnicalLevel,
                        module_id: "ContentNavMenu.InfoManage_JobInfo_TechnicalLevel"
                    }
                ]
            },
            {
                title: I18N.ContentNavMenu.InfoManage_Qualification_Title,
                items: [
                    {
                        title: I18N.ContentNavMenu.InfoManage_Qualification_PostdoctoralTeacher,
                        module_id: "ContentNavMenu.InfoManage_Qualification_PostdoctoralTeacher"
                    },
                    {
                        title: I18N.ContentNavMenu.InfoManage_Qualification_VisitingScholarTeacher,
                        module_id: "ContentNavMenu.InfoManage_Qualification_VisitingScholarTeacher"
                    },
                    {
                        title: I18N.ContentNavMenu.InfoManage_Qualification_ExpertJob,
                        module_id: "ContentNavMenu.InfoManage_Qualification_ExpertJob"
                    },
                    {
                        title: I18N.ContentNavMenu.InfoManage_Qualification_TalentsFunding,
                        module_id: "ContentNavMenu.InfoManage_Qualification_TalentsFunding"
                    }
                ]
            },
            {
                title: I18N.ContentNavMenu.InfoManage_Contract_Title,
                items: [
                    {
                        title: I18N.ContentNavMenu.InfoManage_Contract_EmploymentContract,
                        module_id: "ContentNavMenu.InfoManage_Contract_EmploymentContract"
                    }
                ]
            }
        ]
    };

    Hrcms.NavMenu_StatisticAnalysisManage_Config = {
        event_callback: null,
        settings:
        [
            {
                title: I18N.ContentNavMenu.StatisticAnalysisManage_PersonInfoStatistic_Title,
                items: [
                    {
                        title: I18N.ContentNavMenu.StatisticAnalysisManage_PersonInfoStatistic_Report,
                        module_id: "ContentNavMenu.StatisticAnalysisManage_PersonInfoStatistic_Report"
                    }
                ]
            },
            {
                title: I18N.ContentNavMenu.StatisticAnalysisManage_PersonInfoReport_Title,
                items: [
                    {
                        title: I18N.ContentNavMenu.StatisticAnalysisManage_PersonInfoReport_RelationshipWithStudent,
                        module_id: "ContentNavMenu.StatisticAnalysisManage_PersonInfoReport_RelationshipWithStudent"
                    },
                    {
                        title: I18N.ContentNavMenu.StatisticAnalysisManage_PersonInfoReport_JobTitleInfo,
                        module_id: "ContentNavMenu.StatisticAnalysisManage_PersonInfoReport_JobTitleInfo"
                    }
                ]
            }
        ]
    };

});