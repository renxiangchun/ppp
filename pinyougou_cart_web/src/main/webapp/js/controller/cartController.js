 //控制层 
app.controller("cartController",function($scope,cartService,addressService,orderService){
    $scope.findCartList = function () {
        cartService.findCartList().success(function (response) {
            //返回的是购物车集合
            $scope.cartList = response;
            //求和计数对象
           $scope.totalValue=cartService.sum($scope.cartList );
        });
    }
    //订单商品数据的加/减
    $scope.addGoodsCartList = function (itemId,num) {
        cartService.addGoodsCartList(itemId,num).success(function (response) {
            if(response.success){
                //刷新页面
                $scope.findCartList();
            } else {
                alert(response.message)
            }
        });
    }
    //结算页初始化，根据当前登录者信息查询收货人列表
    $scope.findAddressByLoginUser=function () {
        addressService.findAddressByLoginUser().success(function (response) {
            $scope.addressList = response;
            //遍历这个集合把默认状态：1 赋值给$scope.address
            $scope.address = $scope.addressList[0];
            /*for(var i = 0; i < $scope.addressList.length; i++){
              if($scope.addressList[i].isDefault == 1){
                  $scope.address = $scope.addressList[i];
              }
          }*/
        });
    }
    //点击选中收货人地址
    $scope.selectAddress=function (address) {
        $scope.address=address;
    }
    //判断是否是当前选中的地址
    $scope.isSelectAddress=function (address) {
        if(address == $scope.address){
            return true
        }
        return false;
    }
    //初始化一个Order对象，提交订单到后台
    //初始化支付类型，在线支付
    $scope.order ={paymentType:1};

    //点击选择支付类型
    $scope.selectPayType=function (type) {
        $scope.order.paymentType=type;
    }
    //提交订单
    $scope.saveOrder=function () {
        //提交数据，收货人地址数据
        $scope.order.receiverAreaName = $scope.address.address;
        $scope.order.receiverMobile = $scope.address.mobile;
        $scope.order.receiver = $scope.address.contact;

        orderService.save($scope.order).success(function (response) {
            if(response.success){
                alert(response.message);
                //跳转到支付页面
                location.href="/pay.html";
            }else {
                alert(response.message);
            }
        });
    }



});	
