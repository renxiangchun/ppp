app.controller('sellerLoginController',function ($scope,$controller,sellerLoginService) {

    //读取商家登录人
    $scope.showLoginName = function () {
        sellerLoginService.sellerLoginName().success(function (response) {
            $scope.sellerLoginName = response.sellerLoginName;
        });
    }
});