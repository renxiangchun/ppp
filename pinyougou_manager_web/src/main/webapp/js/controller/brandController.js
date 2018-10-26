app.controller("brandController",function($scope,$controller,brandService){

    //用BrandController继承BaseController，那么$scope域就一个域了！
    //参数哦说明：1、指定继承的Controller的名称；3、指定继承的数据
    $controller("baseController",{$scope:$scope});

    //Java代码中服务层是Service，调用Mapper获取数据
    //前端的服务层是Service，调用java后台接口来获取数据的

    //查询品牌列表的方法
    $scope.findAll=function(){
        brandService.findAll().success(function(response){
            $scope.list = response;
        });
    }

    //分页查询的方法
    $scope.findPage=function(pageNum,pageSize){
        brandService.findPage(pageNum,pageSize).success(function (response) {
            $scope.list = response.rows;
            $scope.paginationConf.totalItems = response.total;
        })
    }

    //保存品牌
    $scope.save=function(){
        var objectName = "";
        if($scope.entity.id != null){
            objectName = brandService.update($scope.entity);
        } else{
            objectName = brandService.add($scope.entity);
        }
        objectName.success(function (response) {
            if(response.success){
                //成功了
                $scope.reloadList();
            } else{
                alert(response.message);
            }
        })
    }
    //定义一个根据品牌ID查询的对象
    $scope.findOne=function(id){
        brandService.findOne(id).success(function (response) {
            $scope.entity = response;
        })
    }

    //删除方法
    $scope.dele=function(){
        brandService.dele($scope.selectIds).success(function (response) {
            if(response.success){
                //刷新页面
                $scope.selectIds=[];
                $scope.reloadList();
            } else{
                alert(response.message);
            }
        })
    };

    //初始化一个对象为空
    $scope.searchEntity={};
    //定义搜索方法
    $scope.search=function(pageNum,pageSize){
        brandService.search(pageNum,pageSize,$scope.searchEntity).success(function (response) {
            $scope.list = response.rows;
            $scope.paginationConf.totalItems = response.total;
        })
    }
});