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
        function loadFormAndGrid(formsHolder, formHolderId, gridHolderId, formTemplUrl, tableDefineUrl, gridDataUrl) {
            $.ajaxSetup({cache: false});
            $.ajax({
                type: "GET",
                url: formTemplUrl,
                dataType: "html",
                success: function(formTempl) {
                    $(formTempl).appendTo(formsHolder.find("li")).find("input").attr("disabled", true);
                    // Load data
                    var formHolder = formsHolder.find(formHolderId);
                    var dataGrid = Hrcms.DataGrid.create({
                        container: formsHolder.find(gridHolderId)
                    });
                    $.ajaxSetup({cache: false});
                    $.getJSON(tableDefineUrl,
                    function(table) {
                        for (var i = 0; i < table.columns.length; ++i) {
                            var column = table.columns[i];
                            var fieldLabel = formHolder.find('td[field-label="' + column.field + '"]');
                            if (fieldLabel.length > 0)
                                fieldLabel.html(column.label);
                        }
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
                            },
                            header_filter: function(column) {
                                // filter the ex field out, the "ex" fields like "pk", "fk" and etc.
                                if (column.field_type === "ex")
                                    return false;
                                return true;
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
                                    loadFormAndGrid(
                                    accordion,
                                    "#natureInfo", "#natureInfoGrid",
                                    "app/profile/html/basic/nature.html",
                                    "rest/tables/basic/person_nature",
                                    "data/test/person_nature_data.json");
                                }
                                else if (accordionTitle.attr("id") === SysModuleID.ContentNavMenu_InfoManage_Info_ContactInfo) {
                                    loadFormAndGrid(
                                    accordion,
                                    "#contactInfo", "#contactInfoGrid",
                                    "app/profile/html/basic/contactInfo.html",
                                    "rest/tables/basic/person_contact",
                                    "data/test/person_contact_data.json");
                                }
                                else if (accordionTitle.attr("id") === SysModuleID.ContentNavMenu_InfoManage_Info_Evaluation) {
                                    loadFormAndGrid(
                                    accordion,
                                    "#evaluateInfo", "#evaluateGrid",
                                    "app/profile/html/basic/evaluate.html",
                                    "rest/tables/basic/person_evaluation",
                                    "data/test/person_evaluation_data.json");
                                }
                                else if (accordionTitle.attr("id") === SysModuleID.ContentNavMenu_InfoManage_Info_Family) {
                                    loadFormAndGrid(
                                    accordion,
                                    "#familyInfo", "#familyGrid",
                                    "app/profile/html/basic/family.html",
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
                                    loadFormAndGrid(
                                    accordion,
                                    "#resumeInfo", "#resumeInfoGrid",
                                    "app/profile/html/resume/info_before_join.html",
                                    "rest/tables/resume/info_before_college",
                                    "data/test/info_before_college_data.json");
                                }
                                else if (accordionTitle.attr("id") === SysModuleID.ContentNavMenu_InfoManage_Records_Degree) {
                                    loadFormAndGrid(
                                    accordion,
                                    "#degreeInfo", "#degreeInfoGrid",
                                    "app/profile/html/resume/education.html",
                                    "rest/tables/resume/education",
                                    "data/test/education_data.json");
                                }
                                else if (accordionTitle.attr("id") === SysModuleID.ContentNavMenu_InfoManage_Records_TrainingInLand) {
                                    loadFormAndGrid(
                                    accordion,
                                    "#trainInInfo", "#trainInInfoGrid",
                                    "app/profile/html/resume/training_in_land.html",
                                    "rest/tables/resume/training_inland",
                                    "data/test/training_inland_data.json");
                                }
                                else if (accordionTitle.attr("id") === SysModuleID.ContentNavMenu_InfoManage_Records_TrainingOutLand) {
                                    loadFormAndGrid(
                                    accordion,
                                    "#trainOutInfo", "#trainOutInfoGrid",
                                    "app/profile/html/resume/training_out_land.html",
                                    "rest/tables/resume/training_outland",
                                    "data/test/training_outland_data.json");
                                }
                                else if (accordionTitle.attr("id") === SysModuleID.ContentNavMenu_InfoManage_Records_Award) {
                                    loadFormAndGrid(
                                    accordion,
                                    "#awardInfo", "#awardInfoGrid",
                                    "app/profile/html/resume/award.html",
                                    "rest/tables/resume/award",
                                    "data/test/award_data.json");
                                }
                                else if (accordionTitle.attr("id") === SysModuleID.ContentNavMenu_InfoManage_Records_Punishment) {
                                    loadFormAndGrid(
                                    accordion,
                                    "#punishmentInfo", "#punishmentInfoGrid",
                                    "app/profile/html/resume/punishment.html",
                                    "rest/tables/resume/punishment",
                                    "data/test/punishment_data.json");
                                }
                                else if (accordionTitle.attr("id") === SysModuleID.ContentNavMenu_InfoManage_Records_AnomalyInCollege) {
                                    loadFormAndGrid(
                                    accordion,
                                    "#anomalyInfo", "#anomalyInfoGrid",
                                    "app/profile/html/resume/anomaly_in_college.html",
                                    "rest/tables/resume/anomaly",
                                    "data/test/anomaly_data.json");
                                }
                                else if (accordionTitle.attr("id") === SysModuleID.ContentNavMenu_InfoManage_Records_InfoToJoinCollege) {
                                    loadFormAndGrid(
                                    accordion,
                                    "#joinInfo", "#joinInfoGrid",
                                    "app/profile/html/resume/info_join_college.html",
                                    "rest/tables/resume/info2joincollege",
                                    "data/test/info2joincollege_data.json");
                                }
                                else if (accordionTitle.attr("id") === SysModuleID.ContentNavMenu_InfoManage_Records_QuitCollege) {
                                    loadFormAndGrid(
                                    accordion,
                                    "#quitInfo", "#quitInfoGrid",
                                    "app/profile/html/resume/quit_college.html",
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
                                    loadFormAndGrid(
                                    accordion,
                                    "#unitInfo", "#unitInfoGrid",
                                    "app/profile/html/job/unit.html",
                                    "rest/tables/job/unit",
                                    "data/test/unit_data.json");
                                }
                                else if (accordionTitle.attr("id") === SysModuleID.ContentNavMenu_InfoManage_JobInfo_Politics) {
                                    loadFormAndGrid(
                                    accordion,
                                    "#politicsInfo", "#politicsInfoGrid",
                                    "app/profile/html/job/politics.html",
                                    "rest/tables/job/politics",
                                    "data/test/dummy.json");
                                }
                                else if (accordionTitle.attr("id") === SysModuleID.ContentNavMenu_InfoManage_JobInfo_JobOfPolitics) {
                                    loadFormAndGrid(
                                    accordion,
                                    "#jobOfPoliticsInfo", "#jobOfPoliticsInfoGrid",
                                    "app/profile/html/job/job_of_politics.html",
                                    "rest/tables/job/jobofpolitics",
                                    "data/test/dummy.json");
                                }
                                else if (accordionTitle.attr("id") === SysModuleID.ContentNavMenu_InfoManage_JobInfo_TechnicalTitles) {
                                    loadFormAndGrid(
                                    accordion,
                                    "#technicalTitleInfo", "#technicalTitleInfoGrid",
                                    "app/profile/html/job/technical_titles.html",
                                    "rest/tables/job/technicaltitles",
                                    "data/test/dummy.json");
                                }
                                else if (accordionTitle.attr("id") === SysModuleID.ContentNavMenu_InfoManage_JobInfo_TechnicalLevel) {
                                    loadFormAndGrid(
                                    accordion,
                                    "#technicalLevelInfo", "#technicalLevelInfoGrid",
                                    "app/profile/html/job/technical_level.html",
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
                                    loadFormAndGrid(
                                    accordion,
                                    "#postDocInfo", "#postDocInfoGrid",
                                    "app/profile/html/qualification/postdocteacher.html",
                                    "rest/tables/qualification/postdoctoralteacher",
                                    "data/test/dummy.json");
                                }
                                else if (accordionTitle.attr("id") === SysModuleID.ContentNavMenu_InfoManage_Qualification_VisitingScholarTeacher) {
                                    loadFormAndGrid(
                                    accordion,
                                    "#visitTeacherInfo", "#visitTeacherInfoGrid",
                                    "app/profile/html/qualification/visitteacher.html",
                                    "rest/tables/qualification/visitingscholarteacher",
                                    "data/test/dummy.json");
                                }
                                else if (accordionTitle.attr("id") === SysModuleID.ContentNavMenu_InfoManage_Qualification_ExpertJob) {
                                    loadFormAndGrid(
                                    accordion,
                                    "#expertJobInfo", "#expertJobInfoGrid",
                                    "app/profile/html/qualification/expertjob.html",
                                    "rest/tables/qualification/expertjob",
                                    "data/test/dummy.json");
                                }
                                else if (accordionTitle.attr("id") === SysModuleID.ContentNavMenu_InfoManage_Qualification_TalentsFunding) {
                                    loadFormAndGrid(
                                    accordion,
                                    "#talentsFundingInfo", "#talentsFundingInfoGrid",
                                    "app/profile/html/qualification/talentsfunding.html",
                                    "rest/tables/qualification/talentsfunding",
                                    "data/test/dummy.json");
                                }
                            }
                        });
                    }
                    else if (id === "contractInfo") {
                        var list = Hrcms.List.create(formEntry.find("#contractInfo"));
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
                                    loadFormAndGrid(
                                    accordion,
                                    "#contactInfo", "#contactInfoGrid",
                                    "app/profile/html/contact/employee_contact.html",
                                    "rest/tables/contact/employmentcontract",
                                    "data/test/dummy.json");
                                }
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
