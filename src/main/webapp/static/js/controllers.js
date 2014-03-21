angular.module('guahao.controllers', [])

.controller('AppCtrl', function($scope, $RegService, $window) {


  $scope.back = function() {
    $window.history.back();
  };


})
.controller('UserCtrl', function($scope, $SettingService, $window) {



});
