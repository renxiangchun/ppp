app.service("payService",function ($http) {

    //生成支付二维码
    this.createNative = function () {
        //r表示随机数，避开缓存
        return $http.get("pay/createNative.do?r="+Math.random());
    }

    //查询支付状态
    this.queryPayStatus = function (out_trade_no) {
        return $http.get("pay/queryPayStatus?out_trade_no="+out_trade_no);
    }
});