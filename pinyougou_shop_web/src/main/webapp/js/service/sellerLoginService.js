app.service('sellerLoginService',function ($http) {
    //读取商家登录人名称
    this.sellerLoginName = function () {
        return $http.get("../login/name")
    }
});