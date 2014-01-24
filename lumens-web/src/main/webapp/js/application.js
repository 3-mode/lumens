/* 
 * Application
 */
Lumens.Application = Class.$extend({
    __init__: function(id) {
        this.id = id;
    },
    getID: function() {
        return this.id;
    },
    run: function() {
        /*Load side bar*/
        $.get("html/navbar.html", function(data) {
            $("#navbar").html(data);
            $.get("html/sidebar_items.html", function(data) {
                $("#sidebar").html(data);
            });
        });
        /*Load content
         $.get("html/content.html", function(data) {
         $("#page-wrapper").html(data);
         });*/
    }
});

$(function() {
    var lumensApp = new Lumens.Application("#LumensApp");
    lumensApp.run();
});