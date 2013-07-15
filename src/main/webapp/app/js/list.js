$(function() {
    Hrcms.List = {};
    Hrcms.List.create = function(container) {
        var tThis = {};
        var accordionHolder = $('<div class="hrcms-accordion"></div>').appendTo(container);
        var sectionTempl =
        '<div>' +
        '  <div class="hrcms-accordion-title">' +
        '    <div style="padding-top: 4px; padding-left: 10px;">' +
        '      <div class="ui-icon hrcms-accordion-icon hrcms-accordion-icon-expand"></div>' +
        '      <b></b>' +
        '    </div>' +
        '  </div>' +
        '  <ul class="hrcms-secondary-submenu"><li></li></ul>' +
        '</div>'
        function loadForm(event, formTitleList, formURLList, i) {
            $.ajaxSetup({cache: false});
            $.ajax({
                type: "GET",
                url: formURLList[i],
                dataType: "html",
                success: function(formTempl) {
                    var section = $(sectionTempl).appendTo(accordionHolder);
                    $(formTempl).appendTo(section.find("li"));
                    var form = section.find("ul");
                    section.find("b").html(formTitleList[i]);
                    section.find(".hrcms-accordion-title").click(function(event) {
                        form.toggle(300);
                        var icon = $(this).find(".hrcms-accordion-icon");
                        icon.toggleClass("hrcms-accordion-icon-expand");
                        icon.toggleClass("hrcms-accordion-icon-collapse");
                    });
                    ++i;
                    if (i < formURLList.length)
                        accordionHolder.trigger("AccordionFormLoad", [formTitleList, formURLList, i]);
                }
            });
        }
        accordionHolder.on("AccordionFormLoad", loadForm);
        tThis.configure = function(config) {
            var formURLList = config.formURLList;
            var i = 0;
            accordionHolder.trigger("AccordionFormLoad", [config.formTitleList, formURLList, i]);
        }
        // end
        return tThis;
    }
});


