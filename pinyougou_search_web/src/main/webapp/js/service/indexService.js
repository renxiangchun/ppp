app.service('indexService',function ($http) {

    //根据分类ID查询广告列表
    this.findContentByCategoryId=function (categoryId) {
        return $http.get("../content/findContentByCategoryId?categoryId="+categoryId);
    }
});