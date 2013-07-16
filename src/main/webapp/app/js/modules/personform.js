$(function() {
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
        var offsetHeight = config.offsetHeight ? config.offsetHeight : 0;
        var offsetWidth = config.offsetWidth ? config.offsetWidth : 0;
        function resize(event) {
            formContainer.css("width", container.width() - offsetWidth);
            formContainer.css("height", container.height() - offsetHeight);
        }
        container.bind("resize", resize);
        resize();
        var mainForm = $(
        '<div class="hrcms-record-main-form">' +
        '</div>'
        ).appendTo(formContainer);
        var formEntry;

        // Member methods
        tThis.configure = function(config) {
            // TODO HTML template configuration here
            formEntry = $(config.personFormTempl).appendTo(mainForm);
            formEntry.find("#tabs").tabs();
            var reportFactory = Hrcms.ReportFactory.create();
            reportFactory.load({
                contentHolder: formEntry.find("#personSummaryReport").find(".hrcms-tab-content"),
                reportTemplURL: config.reportTemplURL,
                reportDataURL: config.reportDataURL
            });
            var list = Hrcms.List.create(formEntry.find("#basicInfo"));
            list.configure({
                formTitleList: ["联系方式", "综合信息"],
                formURLList: [
                    "app/profile/html/contractInfo.html",
                    "app/profile/html/personSummary.html"
                ]
            });
        }
        tThis.remove = function() {
            formContainer.remove();
        }
        // end
        return tThis;
    }
});
