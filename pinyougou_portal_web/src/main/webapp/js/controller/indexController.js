//广告控制层（运营商后台）
app.controller('indexController',function ($scope, indexService) {
    $scope.contentList= [];//广告集合
    $scope.findContentByCategoryId=function (categoryId) {
        indexService.findContentByCategoryId(categoryId).success(function (response) {
            $scope.contentList[categoryId]=response;
        });
    }

    //点击搜索按钮去搜索工程查询
    $scope.keywords = "";
    $scope.search=function () {
        //页面传递参数可以直接在后面追加：#?
        location.href="http://search.pinyougou.com/search.html#?keywords="+$scope.keywords;
    }


});