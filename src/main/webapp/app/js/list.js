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
            accordion.find("ul").toggle(300);
            accordion.find(".hrcms-accordion-icon")
            .toggleClass("hrcms-accordion-icon-expand")
            .toggleClass("hrcms-accordion-icon-collapse");
        }
        function loadForm(config, accordion) {
            if (config.activate)
                config.activate(accordion, accordion.find(".hrcms-accordion-title"), accordion.find(".hrcms-accordion-icon").hasClass("hrcms-accordion-icon-expand"));
            
            /*$.ajaxSetup({cache: false});
            $.ajax({
                type: "GET",
                url: config.formURLList[i],
                dataType: "html",
                success: function(formTempl) {
                    $(formTempl).appendTo(accordion.find("li"));
                    // Load data

                }
            });*/
        }
        function addAccordion(config, i) {
            var accordion = $(sectionTempl).appendTo(accordionHolder);
            accordion.find("b").html(config.titleList[i]);
            var accordionTitle = accordion.find(".hrcms-accordion-title");
            var form = accordion.find("ul");
            form.toggle(100);
            accordionTitle.attr("id", config.IdList[i])
            .click(function(event) {
                doExpandCollapse(accordion);
                // Load data
                if (accordion.find("li").children().length === 0 && config.activate) {
                    if (config.activate)
                        config.activate(accordion, accordion.find(".hrcms-accordion-title"),
                                        accordion.find(".hrcms-accordion-icon").hasClass("hrcms-accordion-icon-expand"));
                }

            });
        }
        tThis.configure = function(config) {
            for (var i = 0; i < config.titleList.length; ++i) {
                addAccordion(config, i);
            }
            var accordions = accordionHolder.find(".hrcms-accordion-item");
            if (accordions.length) {
                var accordion = $(accordions[0]);
                doExpandCollapse($(accordions[0]));
                if (config.activate)
                    config.activate(accordion, accordion.find(".hrcms-accordion-title"),
                                    accordion.find(".hrcms-accordion-icon").hasClass("hrcms-accordion-icon-expand"));

            }
        }
        // end
        return tThis;
    }
});


