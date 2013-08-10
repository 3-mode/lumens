$(function() {
    Hrcms.List = {};
    Hrcms.List.create = function(container) {
        var tThis = {};
        var accordionHolder = $('<div class="hrcms-accordion"></div>').appendTo(container);
        var sectionTempl =
        '<div class="hrcms-accordion-item">' +
        '  <div class="hrcms-accordion-title">' +
        '    <div style="padding-top: 5px; padding-left: 10px;">' +
        '      <div class="ui-icon hrcms-accordion-icon hrcms-accordion-icon-collapse"></div>' +
        '      <b></b>' +
        '    </div>' +
        '  </div>' +
        '  <ul class="hrcms-accordion-content"><li></li></ul>' +
        '</div>';

        function doExpandCollapse(accordion) {
            accordion.find("ul").toggle(500);
            accordion.find(".hrcms-accordion-icon")
            .toggleClass("hrcms-accordion-icon-expand")
            .toggleClass("hrcms-accordion-icon-collapse");
        }
        function loadForm(config, accordion, i) {
            $.ajaxSetup({cache: false});
            $.ajax({
                type: "GET",
                url: config.formURLList[i],
                dataType: "html",
                success: function(formTempl) {
                    $(formTempl).appendTo(accordion.find("li"));
                    // Load data
                    if (config.activate)
                        config.activate(accordion, accordion.find(".hrcms-accordion-title"), accordion.find(".hrcms-accordion-icon").hasClass("hrcms-accordion-icon-expand"));
                }
            });
        }
        function addAccordion(config, i) {
            var accordion = $(sectionTempl).appendTo(accordionHolder);
            accordion.find("b").html(config.formTitleList[i]);
            var accordionTitle = accordion.find(".hrcms-accordion-title");
            var form = accordion.find("ul");
            form.toggle(100);
            accordionTitle.attr("id", config.IdList[i])
            .click(function(event) {
                doExpandCollapse(accordion);
                // Load data
                if (accordion.find("li").children().length === 0 && config.activate) {
                    loadForm(config, accordion, i);
                }

            });
        }
        tThis.configure = function(config) {
            for (var i = 0; i < config.formTitleList.length; ++i) {
                addAccordion(config, i);
            }
            var accordions = accordionHolder.find(".hrcms-accordion-item");
            if (accordions.length) {
                doExpandCollapse($(accordions[0]));
                loadForm(config, $(accordions[0]), 0);
            }
        }
        // end
        return tThis;
    }
});


