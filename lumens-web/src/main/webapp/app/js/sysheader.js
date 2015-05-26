/* 
 * Copyright Lumens Team, Inc. All Rights Reserved.
 */

Lumens.Header = Class.$extend({
    __init__: function (containerObj) {
        this.$header = $('<div class="lumens-header"/>').appendTo(containerObj);
        this.$header.append('<div class="lumens-title-layout"><table><tr><td><div class="lumens-logo"/></td><td></td></tr></table></div>');
        //this.$sysTitle = $("#lumens-system-title");
    },
    setSysTitle: function (text) {
        //this.$sysTitle.html(text);
        return this;
    },
    getElement: function () {
        return this.$header;
    }
});
