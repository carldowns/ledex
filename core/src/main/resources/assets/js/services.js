'use strict';

angular.module("ledex.services", ['ngResource'])

    // Ledex service endpoint
    .service("ledex", ['$http', '$q',
        function(http, q) {
            return {

                /////////////////////
                // API
                /////////////////////

                search: function(term) {
                    var names = q.defer();
                    http.get("/app/emo/_tables/search/" + term).then(function(data) {
                        names.resolve(data);
                    }, function(err) {
                        names.reject(err);
                    });
                    return names.promise;
                },

                getEmoTablesByClient: function(clientName, page) {
                    var names = q.defer();
                    http.get("/app/emo/_tables/client/" + clientName + "?page=" + page).then(function(data) {
                        names.resolve(data);
                    }, function(err) {
                        names.reject(err);
                    });
                    return names.promise;
                }
            }
        }
    ])

;
