/* 
 * Application
 */
Lumens.$ngApp = angular.module("lumens-app", ["lumens-directives", "lumens-services", "lumens-controllers", "ngRoute"]);
Lumens.$ngApp.config(function($routeProvider) {
    $routeProvider
    .when("/id-dashboard-view", {
        template: '<div font-size: 18px;">DashboardViewCtrl</div>',
        controller: "DashboardViewCtrl"
    })
    .when("/id-management-view", {
        template: '<div font-size: 18px;">ManageViewCtrl</div>',
        controller: "ManageViewCtrl"
    })
    .when("/id-desinger-view", {
        template: "",
        controller: "DesignViewCtrl"
    });
});