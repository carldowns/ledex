'use strict';

angular.module('ledex', ['ngRoute', 'ledex.controllers', 'ledex.directives']).
    config(['$routeProvider', function ($routeProvider) {
        $routeProvider.when('/hello', {templateUrl: '/assets/hello.html'});
        $routeProvider.when('/blank7', {templateUrl: '/assets/page_misc_blank.7.html'});
        $routeProvider.when('/blank', {templateUrl: '/assets/page_misc_blank.html'});
        $routeProvider.otherwise({redirectTo: '/blank'});
    }]);