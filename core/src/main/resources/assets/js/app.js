'use strict';

angular.module('ledex', ['ngRoute', 'ledex.controllers', 'ledex.directives']).
    config(['$routeProvider', function ($routeProvider) {
        $routeProvider.when('/hello', {templateUrl: '/assets/hello.html'});
        $routeProvider.otherwise({redirectTo: '/hello'});
    }]);