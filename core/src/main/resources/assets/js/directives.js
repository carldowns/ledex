'use strict';

angular.module("ledex.directives", [])

/**
 * Directive to set focus to specified element. Allows for delayed focus too -- set the attribute to {{ variable }}
 */
    .directive('setFocus', function () {
        return function (scope, element, attrs) {
            scope.$watch(attrs.setFocus,
                function (newValue) {
                    if (newValue) {
                        element[0].focus()
                    }
                })
        }
    })

;