/* 
 * Application
 */
Lumens.$ngApp = angular.module("lumens-app", ["lumens-directives", "lumens-controllers", "ngRoute"]);
Lumens.$ngApp.config(function($routeProvider) {
    $routeProvider
    .when("/id-dashboard-view", {
        template: "",
        controller: "DashboardViewCtrl"
    })
    .when("/id-management-view", {
        template: "",
        controller: "ManageViewCtrl"
    })
    .when("/id-desinger-view", {
        template: "",
        controller: "DesignViewCtrl"
    });
});