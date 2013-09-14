$(function() {
    var I18N = Hrcms.I18N;
    var SysModuleID = Hrcms.SysModuleID;
    Hrcms.PersonForm = {};
    Hrcms.PersonForm.create = function(config) {
        var tThis = Hrcms.BaseForm.create(config);
        // Utility function
        var mainForm = tThis.getMainForm();
        var buildFormGrid = tThis.buildFormGrid;
        // Member methods
        tThis.configure = function(config) {
            // TODO HTML template configuration here
            // TODO Form should be load when the accordion item is actived
            // TODO Form label should be load by activate call back
            mainForm.append(config.personFormTempl);
            Hrcms.TabPanel.create(mainForm.find("#basicInfoTab")).configure({
                tab: [
                    {id: "personSummaryReport", label: I18N.ContentNavMenu.InfoManage_Summary_Info},
                    {id: "basicInfo", label: I18N.ContentNavMenu.InfoManage_Info_Title},
                    {id: "resumeInfo", label: I18N.ContentNavMenu.InfoManage_Records_Title},
                    {id: "takeJobInfo", label: I18N.ContentNavMenu.InfoManage_JobInfo_Title},
                    {id: "qualificationInfo", label: I18N.ContentNavMenu.InfoManage_Qualification_Title},
                    {id: "contractInfo", label: I18N.ContentNavMenu.InfoManage_Contract_Title}
                ],
                activate: function(header, content) {
                    if (Hrcms.debugEnabled) {
                        console.log(header);
                        console.log(content);
                    }
                    if (content.children().length > 0)
                        return;
                    var id = content.attr("id");
                    if (id === "basicInfo") {
                        var list = Hrcms.List.create(mainForm.find("#basicInfo"));
                        list.configure({
                            IdList: [
                                SysModuleID.ContentNavMenu_InfoManage_Info_PersonNature,
                                SysModuleID.ContentNavMenu_InfoManage_Info_ContactInfo,
                                SysModuleID.ContentNavMenu_InfoManage_Info_Evaluation,
                                SysModuleID.ContentNavMenu_InfoManage_Info_Family
                            ],
                            titleList: [
                                I18N.ContentNavMenu.InfoManage_Info_PersonNature,
                                I18N.ContentNavMenu.InfoManage_Info_ContactInfo,
                                I18N.ContentNavMenu.InfoManage_Info_Evaluation,
                                I18N.ContentNavMenu.InfoManage_Info_Family
                            ],
                            activate: function(accordion, accordionTitle, isExpand) {
                                if (!isExpand)
                                    return;
                                if (accordionTitle.attr("id") === SysModuleID.ContentNavMenu_InfoManage_Info_PersonNature) {
                                    buildFormGrid(accordion,
                                    "#natureInfo", "app/html/basic/nature.html",
                                    "#natureInfoGrid", "rest/tables/basic/person_nature", "data/test/person_nature_data.json");
                                }
                                else if (accordionTitle.attr("id") === SysModuleID.ContentNavMenu_InfoManage_Info_ContactInfo) {
                                    buildFormGrid(accordion,
                                    "#contactInfo", "app/html/basic/contactInfo.html",
                                    "#contactInfoGrid", "rest/tables/basic/person_contact", "data/test/person_contact_data.json");
                                }
                                else if (accordionTitle.attr("id") === SysModuleID.ContentNavMenu_InfoManage_Info_Evaluation) {
                                    buildFormGrid(accordion,
                                    "#evaluateInfo", "app/html/basic/evaluate.html",
                                    "#evaluateGrid", "rest/tables/basic/person_evaluation", "data/test/person_evaluation_data.json");
                                }
                                else if (accordionTitle.attr("id") === SysModuleID.ContentNavMenu_InfoManage_Info_Family) {
                                    buildFormGrid(accordion,
                                    "#familyInfo", "app/html/basic/family.html",
                                    "#familyGrid", "rest/tables/basic/person_family", "data/test/person_family_data.json");
                                }
                            }
                        });
                    }
                    else if (id === "resumeInfo") {
                        var list = Hrcms.List.create(mainForm.find("#resumeInfo"));
                        list.configure({
                            IdList: [
                                SysModuleID.ContentNavMenu_InfoManage_Records_JobExperience,
                                SysModuleID.ContentNavMenu_InfoManage_Records_Degree,
                                SysModuleID.ContentNavMenu_InfoManage_Records_TrainingInLand,
                                SysModuleID.ContentNavMenu_InfoManage_Records_TrainingOutLand,
                                SysModuleID.ContentNavMenu_InfoManage_Records_Award,
                                SysModuleID.ContentNavMenu_InfoManage_Records_Punishment,
                                SysModuleID.ContentNavMenu_InfoManage_Records_AnomalyInCollege,
                                SysModuleID.ContentNavMenu_InfoManage_Records_InfoToJoinCollege,
                                SysModuleID.ContentNavMenu_InfoManage_Records_QuitCollege
                            ],
                            titleList: [
                                I18N.ContentNavMenu.InfoManage_Records_JobExperience,
                                I18N.ContentNavMenu.InfoManage_Records_Degree,
                                I18N.ContentNavMenu.InfoManage_Records_TrainingInLand,
                                I18N.ContentNavMenu.InfoManage_Records_TrainingOutLand,
                                I18N.ContentNavMenu.InfoManage_Records_Award,
                                I18N.ContentNavMenu.InfoManage_Records_Punishment,
                                I18N.ContentNavMenu.InfoManage_Records_AnomalyInCollege,
                                I18N.ContentNavMenu.InfoManage_Records_InfoToJoinCollege,
                                I18N.ContentNavMenu.InfoManage_Records_QuitCollege
                            ],
                            activate: function(accordion, accordionTitle, isExpand) {
                                if (!isExpand)
                                    return;
                                if (accordionTitle.attr("id") === SysModuleID.ContentNavMenu_InfoManage_Records_JobExperience) {
                                    buildFormGrid(accordion,
                                    "#resumeInfo", "app/html/resume/info_before_join.html",
                                    "#resumeInfoGrid", "rest/tables/resume/info_before_college", "data/test/info_before_college_data.json");
                                }
                                else if (accordionTitle.attr("id") === SysModuleID.ContentNavMenu_InfoManage_Records_Degree) {
                                    buildFormGrid(
                                    accordion,
                                    "#degreeInfo", "app/html/resume/education.html",
                                    "#degreeInfoGrid", "rest/tables/resume/education", "data/test/education_data.json");
                                }
                                else if (accordionTitle.attr("id") === SysModuleID.ContentNavMenu_InfoManage_Records_TrainingInLand) {
                                    buildFormGrid(accordion,
                                    "#trainInInfo", "app/html/resume/training_in_land.html",
                                    "#trainInInfoGrid", "rest/tables/resume/training_inland", "data/test/training_inland_data.json");
                                }
                                else if (accordionTitle.attr("id") === SysModuleID.ContentNavMenu_InfoManage_Records_TrainingOutLand) {
                                    buildFormGrid(accordion,
                                    "#trainOutInfo", "app/html/resume/training_out_land.html",
                                    "#trainOutInfoGrid", "rest/tables/resume/training_outland", "data/test/training_outland_data.json");
                                }
                                else if (accordionTitle.attr("id") === SysModuleID.ContentNavMenu_InfoManage_Records_Award) {
                                    buildFormGrid(accordion,
                                    "#awardInfo", "app/html/resume/award.html",
                                    "#awardInfoGrid", "rest/tables/resume/award", "data/test/award_data.json");
                                }
                                else if (accordionTitle.attr("id") === SysModuleID.ContentNavMenu_InfoManage_Records_Punishment) {
                                    buildFormGrid(accordion,
                                    "#punishmentInfo", "app/html/resume/punishment.html",
                                    "#punishmentInfoGrid", "rest/tables/resume/punishment", "data/test/punishment_data.json");
                                }
                                else if (accordionTitle.attr("id") === SysModuleID.ContentNavMenu_InfoManage_Records_AnomalyInCollege) {
                                    buildFormGrid(accordion,
                                    "#anomalyInfo", "app/html/resume/anomaly_in_college.html",
                                    "#anomalyInfoGrid", "rest/tables/resume/anomaly", "data/test/anomaly_data.json");
                                }
                                else if (accordionTitle.attr("id") === SysModuleID.ContentNavMenu_InfoManage_Records_InfoToJoinCollege) {
                                    buildFormGrid(accordion,
                                    "#joinInfo", "app/html/resume/info_join_college.html",
                                    "#joinInfoGrid", "rest/tables/resume/info2joincollege", "data/test/info2joincollege_data.json");
                                }
                                else if (accordionTitle.attr("id") === SysModuleID.ContentNavMenu_InfoManage_Records_QuitCollege) {
                                    buildFormGrid(accordion,
                                    "#quitInfo", "app/html/resume/quit_college.html",
                                    "#quitInfoGrid", "rest/tables/resume/quitcollege", "data/test/quitcollege_data.json");
                                }
                            }
                        });
                    }
                    else if (id === "takeJobInfo") {
                        var list = Hrcms.List.create(mainForm.find("#takeJobInfo"));
                        list.configure({
                            IdList: [
                                SysModuleID.ContentNavMenu_InfoManage_JobInfo_Unit,
                                SysModuleID.ContentNavMenu_InfoManage_JobInfo_Politics,
                                SysModuleID.ContentNavMenu_InfoManage_JobInfo_JobOfPolitics,
                                SysModuleID.ContentNavMenu_InfoManage_JobInfo_TechnicalTitles,
                                SysModuleID.ContentNavMenu_InfoManage_JobInfo_TechnicalLevel
                            ],
                            titleList: [
                                I18N.ContentNavMenu.InfoManage_JobInfo_Unit,
                                I18N.ContentNavMenu.InfoManage_JobInfo_Politics,
                                I18N.ContentNavMenu.InfoManage_JobInfo_JobOfPolitics,
                                I18N.ContentNavMenu.InfoManage_JobInfo_TechnicalTitles,
                                I18N.ContentNavMenu.InfoManage_JobInfo_TechnicalLevel
                            ],
                            activate: function(accordion, accordionTitle, isExpand) {
                                if (!isExpand)
                                    return;
                                if (accordionTitle.attr("id") === SysModuleID.ContentNavMenu_InfoManage_JobInfo_Unit) {
                                    buildFormGrid(accordion,
                                    "#unitInfo", "app/html/job/unit.html",
                                    "#unitInfoGrid", "rest/tables/job/unit", "data/test/unit_data.json");
                                }
                                else if (accordionTitle.attr("id") === SysModuleID.ContentNavMenu_InfoManage_JobInfo_Politics) {
                                    buildFormGrid(accordion,
                                    "#politicsInfo", "app/html/job/politics.html",
                                    "#politicsInfoGrid", "rest/tables/job/politics", "data/test/dummy.json");
                                }
                                else if (accordionTitle.attr("id") === SysModuleID.ContentNavMenu_InfoManage_JobInfo_JobOfPolitics) {
                                    buildFormGrid(accordion,
                                    "#jobOfPoliticsInfo", "app/html/job/job_of_politics.html",
                                    "#jobOfPoliticsInfoGrid", "rest/tables/job/jobofpolitics", "data/test/dummy.json");
                                }
                                else if (accordionTitle.attr("id") === SysModuleID.ContentNavMenu_InfoManage_JobInfo_TechnicalTitles) {
                                    buildFormGrid(accordion,
                                    "#technicalTitleInfo", "app/html/job/technical_titles.html",
                                    "#technicalTitleInfoGrid", "rest/tables/job/technicaltitles", "data/test/dummy.json");
                                }
                                else if (accordionTitle.attr("id") === SysModuleID.ContentNavMenu_InfoManage_JobInfo_TechnicalLevel) {
                                    buildFormGrid(accordion,
                                    "#technicalLevelInfo", "app/html/job/technical_level.html",
                                    "#technicalLevelInfoGrid", "rest/tables/job/technicallevel", "data/test/dummy.json");
                                }
                            }
                        });
                    }
                    else if (id === "qualificationInfo") {
                        var list = Hrcms.List.create(mainForm.find("#qualificationInfo"));
                        list.configure({
                            IdList: [
                                SysModuleID.ContentNavMenu_InfoManage_Qualification_PostdoctoralTeacher,
                                SysModuleID.ContentNavMenu_InfoManage_Qualification_VisitingScholarTeacher,
                                SysModuleID.ContentNavMenu_InfoManage_Qualification_ExpertJob,
                                SysModuleID.ContentNavMenu_InfoManage_Qualification_TalentsFunding
                            ],
                            titleList: [
                                I18N.ContentNavMenu.InfoManage_Qualification_PostdoctoralTeacher,
                                I18N.ContentNavMenu.InfoManage_Qualification_VisitingScholarTeacher,
                                I18N.ContentNavMenu.InfoManage_Qualification_ExpertJob,
                                I18N.ContentNavMenu.InfoManage_Qualification_TalentsFunding
                            ],
                            activate: function(accordion, accordionTitle, isExpand) {
                                if (!isExpand)
                                    return;
                                if (accordionTitle.attr("id") === SysModuleID.ContentNavMenu_InfoManage_Qualification_PostdoctoralTeacher) {
                                    buildFormGrid(accordion,
                                    "#postDocInfo", "app/html/qualification/postdocteacher.html",
                                    "#postDocInfoGrid", "rest/tables/qualification/postdoctoralteacher", "data/test/dummy.json");
                                }
                                else if (accordionTitle.attr("id") === SysModuleID.ContentNavMenu_InfoManage_Qualification_VisitingScholarTeacher) {
                                    buildFormGrid(accordion,
                                    "#visitTeacherInfo", "app/html/qualification/visitteacher.html",
                                    "#visitTeacherInfoGrid", "rest/tables/qualification/visitingscholarteacher", "data/test/dummy.json");
                                }
                                else if (accordionTitle.attr("id") === SysModuleID.ContentNavMenu_InfoManage_Qualification_ExpertJob) {
                                    buildFormGrid(accordion,
                                    "#expertJobInfo", "app/html/qualification/expertjob.html",
                                    "#expertJobInfoGrid", "rest/tables/qualification/expertjob", "data/test/dummy.json");
                                }
                                else if (accordionTitle.attr("id") === SysModuleID.ContentNavMenu_InfoManage_Qualification_TalentsFunding) {
                                    buildFormGrid(accordion,
                                    "#talentsFundingInfo", "app/html/qualification/talentsfunding.html",
                                    "#talentsFundingInfoGrid", "rest/tables/qualification/talentsfunding", "data/test/dummy.json");
                                }
                            }
                        });
                    }
                    else if (id === "contractInfo") {
                        var list = Hrcms.List.create(mainForm.find("#contractInfo"));
                        list.configure({
                            IdList: [
                                SysModuleID.ContentNavMenu_InfoManage_Contract_EmploymentContract
                            ],
                            titleList: [
                                I18N.ContentNavMenu.InfoManage_Contract_EmploymentContract
                            ],
                            activate: function(accordion, accordionTitle, isExpand) {
                                if (!isExpand)
                                    return;
                                if (accordionTitle.attr("id") === SysModuleID.ContentNavMenu_InfoManage_Contract_EmploymentContract) {
                                    buildFormGrid(accordion,
                                    "#contactInfo", "app/html/contact/employee_contact.html",
                                    "#contactInfoGrid", "rest/tables/contact/employmentcontract", "data/test/dummy.json");
                                }
                            }
                        });
                    }
                }
            });
            var reportFactory = Hrcms.ReportFactory.create();
            reportFactory.load({
                contentHolder: $('<div style="width: auto;"></div>').appendTo(mainForm.find("#personSummaryReport")),
                reportTemplURL: config.reportTemplURL,
                reportDataURL: config.reportDataURL
            });
        }
        // end
        return tThis;
    }
});
