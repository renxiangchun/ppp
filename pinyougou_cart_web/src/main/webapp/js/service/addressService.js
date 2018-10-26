app.service("addressService",function ($http) {
    this.findAddressByLoginUser=function () {
        return $http.get("../address/findAddressByLoginUser");
    }
});