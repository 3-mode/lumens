/* 
 * Application
 */
Lumens.$ngApp = angular.module("lumens-app", ["lumens-directives", "lumens-services", "lumens-controllers", "ngRoute"]);
Lumens.controllers = angular.module("lumens-controllers", ["ngRoute"]);
Lumens.$ngApp.config(function ($routeProvider) {
    window.LumensLog = console && console.log ? console : {
        log: function () {
        }
    };
    $routeProvider
    .when("/id-dashboard-view", {
        template: '<div font-size: 18px;">DashboardViewCtrl</div>',
        controller: "DashboardViewCtrl"
    })
    .when("/id-management-view", {
        templateUrl: 'app/templates/manage/manage_tmpl.html',
        controller: "ManageViewCtrl"
    })
    .when("/id-desinger-view", {
        template: "",
        controller: "DesignViewCtrl"
    });
});