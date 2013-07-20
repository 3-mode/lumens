$(function() {
    Hrcms.List = {};
    Hrcms.List.create = function(container) {
        var tThis = {};
        var accordionHolder = $('<div class="hrcms-accordion"></div>').appendTo(container);
        var sectionTempl =
        '<div>' +
        '  <div class="hrcms-accordion-title">' +
        '    <div style="padding-top: 5px; padding-left: 10px;">' +
        '      <div class="ui-icon hrcms-accordion-icon hrcms-accordion-icon-expand"></div>' +
        '      <b></b>' +
        '    </div>' +
        '  </div>' +
        '  <ul class="hrcms-accordion-content"><li></li></ul>' +
        '</div>'
        function loadForm(event, config, i) {
            $.ajaxSetup({cache: false});
            $.ajax({
                type: "GET",
                url: config.formURLList[i],
                dataType: "html",
                success: function(formTempl) {
                    var section = $(sectionTempl).appendTo(accordionHolder);
                    $(formTempl).appendTo(section.find("li"));
                    var form = section.find("ul");
                    section.find("b").html(config.formTitleList[i]);
                    section.find(".hrcms-accordion-title").click(function(event) {
                        form.toggle(500);
                        var icon = $(this).find(".hrcms-accordion-icon");
                        icon.toggleClass("hrcms-accordion-icon-expand");
                        icon.toggleClass("hrcms-accordion-icon-collapse");
                    });
                    ++i;
                    if (i < config.formURLList.length)
                        accordionHolder.trigger("AccordionFormLoad", [config, i]);
                    else if (config.callback)
                        config.callback(accordionHolder);
                }
            });
        }
        accordionHolder.on("AccordionFormLoad", loadForm);
        tThis.configure = function(config) {
            var i = 0;
            accordionHolder.trigger("AccordionFormLoad", [config, i]);
        }
        // end
        return tThis;
    }
});


