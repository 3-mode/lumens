$(function() {
    var I18N = Hrcms.I18N;
    Hrcms.CheckboxSet = {};
    Hrcms.CheckboxSet.create = function(parentObj) {
        /*
         * config = {
         * html: "xxxxxx ...",
         * id: "cell-border",
         * classNames: ["xxxxx"],
         * buttons: [
         *   {
         *     classNames: ["xxxxx"],
         *     value: v1,
         *     text: "xxxx",
         *     click: function(evt) {}
         *   },
         *   {
         *     classNames: ["xxxxx"],
         *     value: v2,
         *     text: "xxxx",
         *     click: function(evt) {}
         *   }]
         * };
         */
        var tThis = {};
        var parent = parentObj ? parentObj : $(document.body);
        var checkboxSet = $('<div class="hrcms-checkbox-set"/>').appendTo(parent);
        var checkboxValues = {};
        function checkBoxClick(event) {
            if (Hrcms.debugEnabled)
                console.log(this);
            var value = $(this).attr("value");
            if (checkboxValues[value])
                checkboxValues[value] = undefined;
            else
                checkboxValues[value] = value;
            $(this).toggleClass("hrcms-checkbox-selected");
        }
        tThis.configure = function(config) {
            if (config.html) {
                // TODO
            }
            else {
                var buttons = config.buttons;
                for (var i = 0; i < buttons.length; ++i) {
                    var checkbox = $('<div/>').appendTo(checkboxSet);
                    checkbox.addClass("hrcms-checkbox");
                    checkbox.attr("value", buttons[i].value);
                    checkbox.click(checkBoxClick);
                    if (buttons[i].classNames) {
                        for (var j = 0; j < buttons[i].classNames.length; ++j)
                            checkbox.addClass(buttons[i].classNames[j] + '-' + config.id);
                    }
                }
                var classNames = config.classNames;
                if (classNames)
                    for (var i = 0; classNames && i < classNames.length; ++i)
                        checkboxSet.find(".hrcms-checkbox").addClass(classNames[i] + '-' + config.id);
            }
            return tThis;
        }
        tThis.values = function(values) {
            if (values) {
                checkboxValues = values;
                var buttons = checkboxSet.find(".hrcms-checkbox");
                for (var i = 0; i < buttons.length; ++i) {
                    var jbutton = $(buttons[i]);
                    var v = jbutton.attr("value");
                    if (values[v])
                        jbutton.toggleClass("hrcms-checkbox-selected");
                }
            }
            return checkboxValues;
        }
        // end
        return tThis;
    }

    Hrcms.CellStyleChechboxSet = {};
    Hrcms.CellStyleChechboxSet.create = function(parentObj) {
        var tThis = Hrcms.CheckboxSet.create(parentObj);
        var __superConfigure = tThis.configure;
        tThis.configure = function(config) {
            var defaultConfig = config ? config : {
                id: "cell-border",
                classNames: ["hrcms-checkbox-size"],
                buttons: [
                    {
                        classNames: ["hrcms-report-table-left"],
                        value: "Left"
                    },
                    {
                        classNames: ["hrcms-report-table-right"],
                        value: "Right"
                    },
                    {
                        classNames: ["hrcms-report-table-bottom"],
                        value: "Bottom"
                    },
                    {
                        classNames: ["hrcms-report-table-top"],
                        value: "Top"
                    }]
            };
            __superConfigure(defaultConfig);
            tThis.values({
                Left: "Left",
                Right: "Right",
                Bottom: "Bottom",
                Top: "Top"
            })
        }
        // end
        return tThis;
    }
});