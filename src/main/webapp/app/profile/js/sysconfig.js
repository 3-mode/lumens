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
    Hrcms.NavMenu_Config = {
        event_callback: null,
        settings:
        [
            {
                title: I18N.ContentNavMenu.Info_Title,
                items: [
                    {
                        title: I18N.ContentNavMenu.Info_Person,
                        module_id: "ContentNavMenu.Info_Person"
                    },
                    {
                        title: I18N.ContentNavMenu.Info_ContactInfo,
                        module_id: "ContentNavMenu.Info_ContactInfo"
                    },
                    {
                        title: I18N.ContentNavMenu.Info_PersonNature,
                        module_id: "ContentNavMenu.Info_PersonNature"
                    },
                    {
                        title: I18N.ContentNavMenu.Info_Evaluation,
                        module_id: "ContentNavMenu.Info_Evaluation"
                    },
                    {
                        title: I18N.ContentNavMenu.Info_Family,
                        module_id: "ContentNavMenu.Info_Family"
                    }
                ]
            },
            {
                title: I18N.ContentNavMenu.Records_Title,
                items: [
                    {
                        title: I18N.ContentNavMenu.Records_JobExperience,
                        module_id: "ContentNavMenu.Records_JobExperience"
                    },
                    {
                        title: I18N.ContentNavMenu.Records_Degree,
                        module_id: "ContentNavMenu.Records_Degree"
                    },
                    {
                        title: I18N.ContentNavMenu.Records_TrainingInLand,
                        module_id: "ContentNavMenu.Records_TrainingInLand"
                    },
                    {
                        title: I18N.ContentNavMenu.Records_TrainingOutLand,
                        module_id: "ContentNavMenu.Records_TrainingOutLand"
                    },
                    {
                        title: I18N.ContentNavMenu.Records_Award,
                        module_id: "ContentNavMenu.Records_Award"
                    },
                    {
                        title: I18N.ContentNavMenu.Records_Punishment,
                        module_id: "ContentNavMenu.Records_Punishment"
                    },
                    {
                        title: I18N.ContentNavMenu.Records_AnomalyInCollege,
                        module_id: "ContentNavMenu.Records_AnomalyInCollege"
                    },
                    {
                        title: I18N.ContentNavMenu.Records_InfoToJoinCollege,
                        module_id: "ContentNavMenu.Records_InfoToJoinCollege"
                    },
                    {
                        title: I18N.ContentNavMenu.Records_LeaveOffice,
                        module_id: "ContentNavMenu.Records_LeaveOffice"
                    }
                ]
            },
            {
                title: I18N.ContentNavMenu.JobInfo_Title,
                items: [
                    {
                        title: I18N.ContentNavMenu.JobInfo_Unit,
                        module_id: "ContentNavMenu.JobInfo_Unit"
                    },
                    {
                        title: I18N.ContentNavMenu.JobInfo_Politics,
                        module_id: "ContentNavMenu.JobInfo_Politics"
                    },
                    {
                        title: I18N.ContentNavMenu.JobInfo_JobOfPolitics,
                        module_id: "ContentNavMenu.JobInfo_JobOfPolitics"
                    },
                    {
                        title: I18N.ContentNavMenu.JobInfo_TechnicalTitles,
                        module_id: "ContentNavMenu.JobInfo_TechnicalTitles"
                    },
                    {
                        title: I18N.ContentNavMenu.JobInfo_TechnicalLevel,
                        module_id: "ContentNavMenu.JobInfo_TechnicalLevel"
                    }
                ]
            },
            {
                title: I18N.ContentNavMenu.Qualification_Title,
                items: [
                    {
                        title: I18N.ContentNavMenu.Qualification_PostdoctoralTeacher,
                        module_id: "ContentNavMenu.Qualification_PostdoctoralTeacher"
                    },
                    {
                        title: I18N.ContentNavMenu.Qualification_VisitingScholarTeacher,
                        module_id: "ContentNavMenu.Qualification_VisitingScholarTeacher"
                    },
                    {
                        title: I18N.ContentNavMenu.Qualification_ExpertJob,
                        module_id: "ContentNavMenu.Qualification_ExpertJob"
                    },
                    {
                        title: I18N.ContentNavMenu.Qualification_TalentsFunding,
                        module_id: "ContentNavMenu.Qualification_TalentsFunding"
                    }
                ]
            },
            {
                title: I18N.ContentNavMenu.Contract_Title,
                items: [
                    {
                        title: I18N.ContentNavMenu.Contract_EmploymentContract,
                        module_id: "ContentNavMenu.Contract_EmploymentContract"
                    }
                ]
            }
        ]
    };
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
                title: I18N.Toolbar.WorkflowCheck,
                module_id: "Toolbar.WorkflowCheck"
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
});