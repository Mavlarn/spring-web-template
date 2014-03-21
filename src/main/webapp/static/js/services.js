angular.module('guahao.services', [])

/**
 * A simple example service that returns some data.
 */
.factory('$RegService', function() {
  var billList = [
      {id:0, place: "四季民福", members: [{id:0, pay: 158/4}, {id:1, pay: 158/4}, {id:2, pay: 158/4}, {id:6, pay: 158/4}], amount: 158, date: new Date()},
      {id:1, place: "宏状元",   members: [{id:0, pay: 158/5}, {id:1, pay: 158/5}, {id:2, pay: 158/5}, {id:3, pay: 158/5}, {id:5, pay: 158/5}], amount: 168, date: new Date()},
      {id:2, place: "永和大王", members: [{id:0, pay: 148/4}, {id:1, pay: 148/4}, {id:3, pay: 148/4}, {id:6, pay: 148/4} ], amount: 148, date: new Date()},
      {id:3, place: "金草帽",   members: [{id:0, pay: 158/4}, {id:2, pay: 158/4}, {id:4, pay: 158/4}, {id:6, pay: 158/4} ], amount: 158, date: new Date()},
      {id:4, place: "眉州东坡", members: [{id:0, pay: 170/5}, {id:1, pay: 170/5}, {id:2, pay: 170/5}, {id:3, pay: 170/5}, {id:4, pay: 170/5}, {id:5, pay: 170/6}], amount: 170, date: new Date()}
  ];

  return {
    all: function() {
    },
    get: function(billId) {
    },
    addBill: function(newBill) {
    },
    updateBill: function(changedBill) {
    }
  };
})

// user service
.factory('$UserService', function() {
  var friendList = [
    { id: 0, name: '小张', phone: '13800138001', email: "XiaoZhang@163.com" },
    { id: 1, name: '小明', phone: '13800138002', email: "XiaoMing@163.com" },
    { id: 2, name: '小王', phone: '13800138003', email: "XiaoWang@163.com" },
    { id: 3, name: '小周', phone: '13800138004', email: "XiaoZhou@163.com" },
    { id: 4, name: '小陈', phone: '13800138005', email: "XiaoChen@163.com" },
    { id: 5, name: '小杨', phone: '13800138006', email: "XiaoYang@163.com" },
    { id: 6, name: '小李', phone: '13800138006', email: "XiaoYang@163.com" }
  ];

  var maxUserId = 5;

  return {
    signup: function(user) {
      window.localStorage['aa.user.name'] = user.name;
      window.localStorage['aa.user.email'] = user.email;
      window.localStorage['aa.user.phone'] = user.phone;
      console.log("sign up with:" + user.name + ", " + user.email + ", " + user.phone);
      return true;
    },
    signin: function(user) {
      window.localStorage['aa.user.name'] = user.name;
      window.localStorage['aa.user.email'] = user.email;
      window.localStorage['aa.user.phone'] = user.phone;
      return true;
    },
    allFriends: function() {

    },
    getFriendInfoByName: function(userName) {
      return _.find(friendList, function(dd) {dd.name === userName});
    },
    getFriendInfoByIds: function(paymembers) {
      var friends = [];
      _.each(friendList, function(friend) {
        _.each(paymembers, function(paymember) {
          if (paymember.id === friend.id) {
            paymember.name = friend.name;
            friends.push(paymember);
          }
        });
      });
      return friends;
    },
    deleteFriend: function(userName) {

    },
    addFriend: function(newFriend) {
      newFriend.id = maxUserId + 1;
      friendList.push(newFriend);
      maxUserId = maxUserId + 1;
    },
    saveFriend: function (friend) {
      var oldFriendInfo = _.find(friendList, function(dd) {dd.id == friend.id});
      oldFriendInfo.name = friend.name;
      oldFriendInfo.phone = friend.phone;
      oldFriendInfo.email = friend.email;
    }
  };
})
.factory('$SettingService', function() {
  var config = {a_key: "a_value"};

  return {
    getProperty: function(key) {
      return config[key];
    },
    setProperty: function(aKey, aValue) {
      config[aKey] = aValue;
    }
  };
});
