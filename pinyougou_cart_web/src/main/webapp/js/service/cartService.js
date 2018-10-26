//服务层
app.service("cartService",function($http){
    this.findCartList = function () {
        return $http.get("../cart/findCartList");
    }
    
    this.addGoodsCartList = function (itemId,num) {
        return $http.get("../cart/addGoodsCartList?itemId="+itemId+"&num="+num);
    }
    this.sum=function (cartList) {
        //设置和计数对象
        var totalValue = {totalNum:0,totalMoney:0.00};
        for(var i=0;i<cartList.length;i++){
            var cart = cartList[i];
            for (var j=0;j<cart.orderItemList.length;j++){
                //订单明细对象
                var orderItem = cart.orderItemList[j];
                totalValue.totalNum += orderItem.num;
                totalValue.totalMoney += orderItem.totalFee;
            }
        }
        return totalValue;
    }
});
