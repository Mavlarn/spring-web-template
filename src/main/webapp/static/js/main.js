// angular.module is a global place for creating, registering and retrieving Angular modules
// 'starter' is the name of this angular module example (also set in a <body> attribute in index.html)
// the 2nd parameter is an array or 'requires'
// 'starter.services' is found in services.js
// 'starter.controllers' is found in controllers.js
angular.module('guahao', ['ngAnimate', 'ui.router', 'guahao.services', 'guahao.controllers'])

.run(function($rootScope) {
  console.log("AA app running...");

})

.config(function($stateProvider, $urlRouterProvider) {
  $stateProvider
    .state('home', {
      url: '/home',
      templateUrl: 'home/home.html'
    })
    .state('home', {
      url: '/home',
      templateUrl: 'home/list.html'
    })
    .state('user', {
      url: '/user',
      templateUrl: 'secure/user/home.html',
      controller: 'UserCtrl'
    })
    .state('user.message', {
      url: '/user',
      templateUrl: 'secure/user/message.html',
      controller: 'UserCtrl'
    })

  $urlRouterProvider.otherwise("/home");
})

.config(function ($compileProvider){
  // Needed for routing to work
  $compileProvider.aHrefSanitizationWhitelist(/^\s*(https?|ftp|mailto|file|tel):/);
});

