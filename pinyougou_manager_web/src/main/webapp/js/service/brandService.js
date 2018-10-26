//定义Angular前端框架中的service层
//两个参数：1.指定service服务层名称，2.方法集
app.service("brandService",function($http){
    this.findAll = function(){
        return $http.get("../brand/findAll");
    }
    this.findPage = function (pageNum, pageSize) {
        return $http.get("../brand/findPage?pageNum="+pageNum+"&pageSize="+pageSize);
    }
    this.update = function (entity) {
        return $http.post("../brand/update",entity);
    }
    this.add = function (entity) {
        return $http.post("../brand/add",entity);
    }
    this.dele = function (ids) {
        return $http.get("../brand/delete?ids="+ids);
    }
    this.findOne = function (id) {
        return $http.get("../brand/findOne?id="+id);
    }
    this.search = function (pageNum, pageSize, entity) {
        return $http.post("../brand/search?pageNum="+pageNum+"&pageSize="+pageSize,entity)
    }

    //品牌下列列表数据
    this.selectOptionList = function () {
        return $http.get("../brand/selectOptionList");
    }
});