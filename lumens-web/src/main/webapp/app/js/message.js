/* 
 * Copyright Lumens Team, Inc. All Rights Reserved.
 */

Lumens.Message = Class.$extend({
    __init__: function(TemplateLoader) {
        var __this = this;
        TemplateLoader.success.get(function(successTmpl) {
            __this.successTmpl = successTmpl;
        });
        TemplateLoader.warning.get(function(warningTmpl) {
            __this.warningTmpl = warningTmpl;
        });
        TemplateLoader.error.get(function(errorTmpl) {
            __this.errorTmpl = errorTmpl;
        });
        this.fadeOut = {
            duration: 3000,
            complete: function() {
                $(this).remove();
            }
        }
    },
    showSuccess: function(message, $parent) {
        $(this.successTmpl).appendTo($parent)
        .zIndex(5000)
        .fadeOut(this.fadeOut)
        .find("#message").html(message);
    },
    showWarning: function(message, $parent) {
        $(this.warningTmpl).appendTo($parent)
        .zIndex(5000)
        .fadeOut(this.fadeOut)
        .find("#message").html(message);
    },
    showError: function(message, $parent) {
        $(this.errorTmpl).appendTo($parent)
        .zIndex(5000)
        .fadeOut(this.fadeOut)
        .find("#message").html(message);
    }
});
