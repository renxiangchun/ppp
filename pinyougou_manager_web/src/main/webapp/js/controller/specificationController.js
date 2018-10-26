app.controller("specificationController",function ($scope,$controller,specificationService) {

    //继承
    $controller("baseController",{$scope:$scope});

    //初始化一个对象为空
    $scope.searchEntity = {};
    //定义搜索方法
    $scope.search = function (pageNum, pageSize) {
        specificationService.search(pageNum,pageSize,$scope.searchEntity).success(function (response) {
            $scope.list = response.rows;
            $scope.paginationConf.totalItems = response.total;
        });
    }

    //新增规格选项
    $scope.addTableRow = function () {
        $scope.entity.specificationOptionList.push({})
    }
    //删除行
    $scope.deleTableRow = function (index) {
        $scope.entity.specificationOptionList.splice(index,1)
    }

    $scope.save = function () {

        var objectName = "";
        if($scope.entity.specification.id != null){
            objectName = specificationService.update($scope.entity);
        } else {
            objectName = specificationService.add($scope.entity);
        }
        objectName.success(function (response) {
            if (response.success){
                //刷新页面
                $scope.reloadList();
            } else {
                alert(response.message);
            }
        })
    }
    $scope.findOne = function (id) {
        specificationService.findOne(id).success(function (response) {
            $scope.entity = response;

        });
    }

    $scope.dele = function () {
        specificationService.dele($scope.selectIds).success(function (response) {
            if (response.success){
                $scope.reloadList();
            } else {
                alert(response.message);
            }
        })
    }

});