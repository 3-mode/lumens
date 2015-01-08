/* 
 * Application
 */
Lumens.controllers = angular.module("lumens-controllers", ["ngRoute"]);
Lumens.services = angular.module('lumens-services', ['ngResource']);
Lumens.directives = angular.module("lumens-directives", []);
Lumens.$ngApp = angular.module("lumens-app", ["lumens-directives", "lumens-services", "lumens-controllers", "ngRoute"]);
Lumens.$ngApp.config(function ($routeProvider) {
    $routeProvider
    .when("/id-dashboard-view", {
        template: '<div font-size: 18px;">DashboardViewCtrl</div>',
        controller: "DashboardViewCtrl"
    })
    .when("/id-management-view", {
        templateUrl: "app/templates/manage/manage_tmpl.html",
        controller: "ManageViewCtrl"
    })
    .when("/id-desinger-view", {
        template: "",
        controller: "DesignViewCtrl"
    });
});