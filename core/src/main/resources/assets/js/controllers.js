'use strict';

angular.module('ledex.controllers', ['ledex.services', 'ngRoute'])

    // unused, example
    .controller('GoogleAddressController', ['$scope', '$http',

        function ($scope, Emoji, $http) {

            $scope.getLocation = function (val) {
                return $http.get('http://maps.googleapis.com/maps/api/geocode/json', {
                    params: {
                        address: val,
                        sensor: false
                    }
                }).then(function (response) {
                    return response.data.results.map(function (item) {
                        return item.formatted_address;
                    });
                });
            };
        }
    ])

    // unused, example
    .controller('GoogleInfiniteAddressController', ['$scope', '$http',

        function ($scope, Emoji, $http) {

            $scope.getLocation = function (val) {
                return $http.get('http://maps.googleapis.com/maps/api/geocode/json', {
                    params: {
                        address: val,
                        sensor: false
                    }
                }).then(function (response) {
                    var addresses = [];
                    angular.forEach(response.data.results, function (item) {
                        addresses.push(item.formatted_address);
                    });

                    return addresses;
                });
            };
        }
    ])
;

