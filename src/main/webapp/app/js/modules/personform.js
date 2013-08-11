$(function() {
    var I18N = Hrcms.I18N;
    Hrcms.PersonForm = {};
    Hrcms.PersonForm.create = function(config) {
        var tThis = {};
        var container = config.container;
        var formContainer = $('<div class="hrcms-form-container"/>').appendTo(container);
        var workspaceHeader = Hrcms.NavIndicator.create(formContainer);
        for (var i = 0; i < config.navigator.length; ++i)
            workspaceHeader.goTo(config.navigator[i]);
        workspaceHeader.configure({
            goBack: config.goBack
        });
        var mainForm = $(
        '<div class="hrcms-record-main-form">' +
        '</div>'
        ).appendTo(formContainer);

        var offsetHeight = config.offsetHeight ? config.offsetHeight : 0;
        var offsetWidth = config.offsetWidth ? config.offsetWidth : 0;
        function resize(event) {
            var w = container.width() - offsetWidth;
            var h = container.height() - offsetHeight;
            formContainer.css("width", w);
            formContainer.css("height", h);
            var size = workspaceHeader.size();
            mainForm.css("height", h - size.height);
        }
        container.bind("resize", resize);
        resize();
        // Utility function
        function loadGrid(formsHolder, formHolderId, gridHolderId, tableDefineUrl, gridDataUrl) {
            var formHolder = formsHolder.find(formHolderId);
            var dataGrid = Hrcms.DataGrid.create({
                container: formsHolder.find(gridHolderId)
            });
            $.ajaxSetup({cache: false});
            $.getJSON(tableDefineUrl,
            function(table) {
                dataGrid.configure({
                    columns: table.columns,
                    event: {
                        rowclick: function(event) {
                            var tdList = $(this).find("td");
                            tdList.each(function(index, td) {
                                var fieldName = $(td).attr("field-name");
                                formHolder.find('input[field-name="' + fieldName + '"]').val($(td).find("div").html());
                            });
                        }
                    }
                    // TODO add sort function
                });
                $.getJSON(gridDataUrl,
                function(record) {
                    dataGrid.data(record);
                });
            }).fail(function(result) {
                if (Hrcms.debugEnabled)
                    console.log(result);
            });
        }
        var formEntry;
        var SysModuleID = Hrcms.SysModuleID;
        // Member methods
        tThis.configure = function(config) {
            // TODO HTML template configuration here
            // TODO Form should be load when the accordion item is actived
            // TODO Form label should be load by activate call back
            formEntry = $(config.personFormTempl).appendTo(mainForm);
            Hrcms.TabPanel.create($("#basicInfoTab")).configure({
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
                        var list = Hrcms.List.create(formEntry.find("#basicInfo"));
                        list.configure({
                            IdList: [
                                SysModuleID.ContentNavMenu_InfoManage_Info_PersonNature,
                                SysModuleID.ContentNavMenu_InfoManage_Info_ContactInfo,
                                SysModuleID.ContentNavMenu_InfoManage_Info_Evaluation,
                                SysModuleID.ContentNavMenu_InfoManage_Info_Family
                            ],
                            formTitleList: [
                                I18N.ContentNavMenu.InfoManage_Info_PersonNature,
                                I18N.ContentNavMenu.InfoManage_Info_ContactInfo,
                                I18N.ContentNavMenu.InfoManage_Info_Evaluation,
                                I18N.ContentNavMenu.InfoManage_Info_Family
                            ],
                            formURLList: [
                                "app/profile/html/basic/nature.html",
                                "app/profile/html/basic/contactInfo.html",
                                "app/profile/html/basic/evaluate.html",
                                "app/profile/html/basic/family.html"
                            ],
                            activate: function(accordion, accordionTitle, isExpand) {
                                if (!isExpand)
                                    return;
                                if (accordionTitle.attr("id") === SysModuleID.ContentNavMenu_InfoManage_Info_PersonNature) {
                                    loadGrid(
                                    accordion,
                                    "#natureInfo", "#natureInfoGrid",
                                    "rest/tables/basic/person_nature",
                                    "data/test/person_nature_data.json");
                                }
                                else if (accordionTitle.attr("id") === SysModuleID.ContentNavMenu_InfoManage_Info_ContactInfo) {
                                    loadGrid(
                                    accordion,
                                    "#contactInfo", "#contactInfoGrid",
                                    "rest/tables/basic/person_contact",
                                    "data/test/person_contact_data.json");
                                }
                                else if (accordionTitle.attr("id") === SysModuleID.ContentNavMenu_InfoManage_Info_Evaluation) {
                                    loadGrid(
                                    accordion,
                                    "#evaluateInfo", "#evaluateGrid",
                                    "rest/tables/basic/person_evaluation",
                                    "data/test/person_evaluation_data.json");
                                }
                                else if (accordionTitle.attr("id") === SysModuleID.ContentNavMenu_InfoManage_Info_Family) {
                                    loadGrid(
                                    accordion,
                                    "#familyInfo", "#familyGrid",
                                    "rest/tables/basic/person_family",
                                    "data/test/person_family_data.json");
                                }
                            }
                        });
                    }
                    else if (id === "resumeInfo") {
                        var list = Hrcms.List.create(formEntry.find("#resumeInfo"));
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
                            formTitleList: [
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
                            formURLList: [
                                "app/profile/html/resume/info_before_join.html",
                                "app/profile/html/resume/degree.html",
                                "app/profile/html/resume/training_in_land.html",
                                "app/profile/html/resume/training_out_land.html",
                                "app/profile/html/resume/award.html",
                                "app/profile/html/resume/punishment.html",
                                "app/profile/html/resume/anomaly_in_college.html",
                                "app/profile/html/resume/info_join_college.html",
                                "app/profile/html/resume/quit_college.html"
                            ],
                            activate: function(accordion, accordionTitle, isExpand) {
                                if (!isExpand)
                                    return;
                                if (accordionTitle.attr("id") === SysModuleID.ContentNavMenu_InfoManage_Records_JobExperience) {
                                    loadGrid(
                                    accordion,
                                    "#resumeInfo", "#resumeInfoGrid",
                                    "rest/tables/resume/info_before_college",
                                    "data/test/info_before_college_data.json");
                                }
                                else if (accordionTitle.attr("id") === SysModuleID.ContentNavMenu_InfoManage_Records_Degree) {
                                    loadGrid(
                                    accordion,
                                    "#degreeInfo", "#degreeInfoGrid",
                                    "rest/tables/resume/education",
                                    "data/test/education_data.json");
                                }
                                else if (accordionTitle.attr("id") === SysModuleID.ContentNavMenu_InfoManage_Records_TrainingInLand) {
                                    loadGrid(
                                    accordion,
                                    "#trainInInfo", "#trainInInfoGrid",
                                    "rest/tables/resume/training_inland",
                                    "data/test/training_inland_data.json");
                                }
                                else if (accordionTitle.attr("id") === SysModuleID.ContentNavMenu_InfoManage_Records_TrainingOutLand) {
                                    loadGrid(
                                    accordion,
                                    "#trainOutInfo", "#trainOutInfoGrid",
                                    "rest/tables/resume/training_outland",
                                    "data/test/training_outland_data.json");
                                }
                                else if (accordionTitle.attr("id") === SysModuleID.ContentNavMenu_InfoManage_Records_Award) {
                                    loadGrid(
                                    accordion,
                                    "#awardInfo", "#awardInfoGrid",
                                    "rest/tables/resume/award",
                                    "data/test/award_data.json");
                                }
                                else if (accordionTitle.attr("id") === SysModuleID.ContentNavMenu_InfoManage_Records_Punishment) {
                                    loadGrid(
                                    accordion,
                                    "#punishmentInfo", "#punishmentInfoGrid",
                                    "rest/tables/resume/punishment",
                                    "data/test/punishment_data.json");
                                }
                                else if (accordionTitle.attr("id") === SysModuleID.ContentNavMenu_InfoManage_Records_AnomalyInCollege) {
                                    loadGrid(
                                    accordion,
                                    "#anomalyInfo", "#anomalyInfoGrid",
                                    "rest/tables/resume/anomaly",
                                    "data/test/anomaly_data.json");
                                }
                                else if (accordionTitle.attr("id") === SysModuleID.ContentNavMenu_InfoManage_Records_InfoToJoinCollege) {
                                    loadGrid(
                                    accordion,
                                    "#joinInfo", "#joinInfoGrid",
                                    "rest/tables/resume/info2joincollege",
                                    "data/test/info2joincollege_data.json");
                                }
                                else if (accordionTitle.attr("id") === SysModuleID.ContentNavMenu_InfoManage_Records_QuitCollege) {
                                    loadGrid(
                                    accordion,
                                    "#quitInfo", "#quitInfoGrid",
                                    "rest/tables/resume/quitcollege",
                                    "data/test/quitcollege_data.json");
                                }
                            }
                        });
                    }
                    else if (id === "takeJobInfo") {
                        var list = Hrcms.List.create(formEntry.find("#takeJobInfo"));
                        list.configure({
                            IdList: [
                                SysModuleID.ContentNavMenu_InfoManage_JobInfo_Unit,
                                SysModuleID.ContentNavMenu_InfoManage_JobInfo_Politics,
                                SysModuleID.ContentNavMenu_InfoManage_JobInfo_JobOfPolitics,
                                SysModuleID.ContentNavMenu_InfoManage_JobInfo_TechnicalTitles,
                                SysModuleID.ContentNavMenu_InfoManage_JobInfo_TechnicalLevel
                            ],
                            formTitleList: [
                                I18N.ContentNavMenu.InfoManage_JobInfo_Unit,
                                I18N.ContentNavMenu.InfoManage_JobInfo_Politics,
                                I18N.ContentNavMenu.InfoManage_JobInfo_JobOfPolitics,
                                I18N.ContentNavMenu.InfoManage_JobInfo_TechnicalTitles,
                                I18N.ContentNavMenu.InfoManage_JobInfo_TechnicalLevel
                            ],
                            formURLList: [
                                "app/profile/html/job/unit.html",
                                "app/profile/html/job/politics.html",
                                "app/profile/html/job/job_of_politics.html",
                                "app/profile/html/job/technical_titles.html",
                                "app/profile/html/job/technical_level.html"
                            ],
                            activate: function(accordion, accordionTitle, isExpand) {
                                if (!isExpand)
                                    return;
                                if (accordionTitle.attr("id") === SysModuleID.ContentNavMenu_InfoManage_JobInfo_Unit) {
                                    loadGrid(
                                    accordion,
                                    "#unitInfo", "#unitInfoGrid",
                                    "rest/tables/job/unit",
                                    "data/test/unit_data.json");
                                }
                                else if (accordionTitle.attr("id") === SysModuleID.ContentNavMenu_InfoManage_JobInfo_Politics) {
                                    loadGrid(
                                    accordion,
                                    "#politicsInfo", "#politicsInfoGrid",
                                    "rest/tables/job/politics",
                                    "data/test/dummy.json");
                                }
                                else if (accordionTitle.attr("id") === SysModuleID.ContentNavMenu_InfoManage_JobInfo_JobOfPolitics) {
                                    loadGrid(
                                    accordion,
                                    "#jobOfPoliticsInfo", "#jobOfPoliticsInfoGrid",
                                    "rest/tables/job/jobofpolitics",
                                    "data/test/dummy.json");
                                }
                                else if (accordionTitle.attr("id") === SysModuleID.ContentNavMenu_InfoManage_JobInfo_TechnicalTitles) {
                                    loadGrid(
                                    accordion,
                                    "#technicalTitleInfo", "#technicalTitleInfoGrid",
                                    "rest/tables/job/technicaltitles",
                                    "data/test/dummy.json");
                                }
                                else if (accordionTitle.attr("id") === SysModuleID.ContentNavMenu_InfoManage_JobInfo_TechnicalLevel) {
                                    loadGrid(
                                    accordion,
                                    "#technicalLevelInfo", "#technicalLevelInfoGrid",
                                    "rest/tables/job/technicallevel",
                                    "data/test/dummy.json");
                                }
                            }
                        });
                    }
                    else if (id === "qualificationInfo") {
                        var list = Hrcms.List.create(formEntry.find("#qualificationInfo"));
                        list.configure({
                            IdList: [
                                SysModuleID.ContentNavMenu_InfoManage_Qualification_PostdoctoralTeacher,
                                SysModuleID.ContentNavMenu_InfoManage_Qualification_VisitingScholarTeacher,
                                SysModuleID.ContentNavMenu_InfoManage_Qualification_ExpertJob,
                                SysModuleID.ContentNavMenu_InfoManage_Qualification_TalentsFunding
                            ],
                            formTitleList: [
                                I18N.ContentNavMenu.InfoManage_Qualification_PostdoctoralTeacher,
                                I18N.ContentNavMenu.InfoManage_Qualification_VisitingScholarTeacher,
                                I18N.ContentNavMenu.InfoManage_Qualification_ExpertJob,
                                I18N.ContentNavMenu.InfoManage_Qualification_TalentsFunding
                            ],
                            formURLList: [
                                "app/profile/html/qualification/postdocteacher.html",
                                "app/profile/html/qualification/visitteacher.html",
                                "app/profile/html/qualification/expertjob.html",
                                "app/profile/html/qualification/talentsfunding.html"
                            ],
                            activate: function(accordion, accordionTitle, isExpand) {

                            }
                        });
                    }
                    else if (id === "contractInfo") {
                        var list = Hrcms.List.create(formEntry.find("#contractInfo"));
                        list.configure({
                            IdList: [
                                SysModuleID.ContentNavMenu_InfoManage_Contract_EmploymentContract
                            ],
                            formTitleList: [
                                I18N.ContentNavMenu.InfoManage_Contract_EmploymentContract
                            ],
                            formURLList: [
                                "app/profile/html/job/technical_level.html"
                            ],
                            activate: function(accordion, accordionTitle, isExpand) {

                            }
                        });
                    }
                }
            });
            var reportFactory = Hrcms.ReportFactory.create();
            reportFactory.load({
                contentHolder: $('<div style="width: auto;"></div>').appendTo(formEntry.find("#personSummaryReport")),
                reportTemplURL: config.reportTemplURL,
                reportDataURL: config.reportDataURL
            });
        }
        tThis.remove = function() {
            formContainer.remove();
        }
        // end
        return tThis;
    }
});
