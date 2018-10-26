//控制层
app.controller('goodsController' ,function($scope,$controller ,itemCatService,uploadService ,typeTemplateService ,goodsService){

    $controller('baseController',{$scope:$scope});//继承

    //读取列表数据绑定到表单中
    $scope.findAll=function(){
        goodsService.findAll().success(
            function(response){
                $scope.list=response;
            }
        );
    }

    //分页
    $scope.findPage=function(page,rows){
        goodsService.findPage(page,rows).success(
            function(response){
                $scope.list=response.rows;
                $scope.paginationConf.totalItems=response.total;//更新总记录数
            }
        );
    }

    //查询实体
    $scope.findOne=function(id){
        goodsService.findOne(id).success(
            function(response){
                $scope.entity= response;
            }
        );
    }

    //保存
    $scope.save=function(){
        //在添加之前获取商品介绍中的数据
        $scope.entity.goodsDesc.introduction = editor.html();

        var serviceObject;//服务层对象
        if($scope.entity.goods.id !=null){//如果有ID
            serviceObject=goodsService.update( $scope.entity ); //修改
        }else{
            serviceObject=goodsService.add( $scope.entity  );//增加
        }
        serviceObject.success(
            function(response){
                if(response.success){
                    //把对象清空
                    $scope.entity={};
                    editor.html('');
                }else{
                    alert(response.message);
                }
            }
        );
    }


    //批量删除
    $scope.dele=function(){
        //获取选中的复选框
        goodsService.dele( $scope.selectIds ).success(
            function(response){
                if(response.success){
                    $scope.reloadList();//刷新列表
                }
            }
        );
    }

    $scope.searchEntity={};//定义搜索对象
    $scope.statusList =["未审核","已审核","审核通过","关闭"];//商品状态

    //搜索
    $scope.search=function(page,rows){
        goodsService.search(page,rows,$scope.searchEntity).success(
            function(response){
                $scope.list=response.rows;
                $scope.paginationConf.totalItems=response.total;//更新总记录数
            }
        );
    }

    $scope.entity={goods:{},goodsDesc:{itemImages:[],specificationItems:[]}};


    //页面初始化的时候，查询商品一级分类列表
    $scope.findItemCat1List=function () {
        itemCatService.findByParentId(0).success(function (response) {
            $scope.itemCat1List = response;
        })
    }

    //监听方法：$watch : 监控变量值，发生变化了，触发的事件
    //参数说明：1、监听哪个变量；2、执行的方法；方法中两个参数：1、变化的新值值；2、之前的值
    $scope.$watch("entity.goods.category1Id",function(newValue, oldValue){
        itemCatService.findByParentId(newValue).success(function (response) {
            $scope.itemCat2List = response;
        });
    });

    $scope.$watch("entity.goods.category2Id",function(newValue, oldValue){
        itemCatService.findByParentId(newValue).success(function (response) {
            $scope.itemCat3List = response;
        });
    });

    $scope.$watch("entity.goods.category3Id",function(newValue, oldValue){
        itemCatService.findOne(newValue).success(function (response) {
            $scope.entity.goods.typeTemplateId = response.typeId;
        });
    });

    $scope.$watch("entity.goods.typeTemplateId",function(newValue, oldValue){
        //查询模板对象
        typeTemplateService.findOne(newValue).success(function (response) {
            $scope.typeTemplate = response;
            //把brandIDS这个String类型的字符串属性转成JSON对象
            $scope.typeTemplate.brandIds = JSON.parse($scope.typeTemplate.brandIds);
            //获取自定义扩展属性：String类型字符串：需要转成JSON对象
            $scope.entity.goodsDesc.customAttributeItems = JSON.parse($scope.typeTemplate.customAttributeItems);
        });
        //查询了规格及规格选项列表
        typeTemplateService.findMapList(newValue).success(function (response) {
            //想要结果：[{"id":27,"text":"网络",”options”:[“移动3G”,”移动4G”…]},{"id":32,"text":"机身内存",options”:[“4g”,”8g”…]}]
            $scope.specList = response;
        });
    });


    //上传图片方法
    $scope.uploadFile=function(){
        uploadService.uploadFile().success(function (response) {
            if(response.success){
                $scope.image_entity.url=response.message;
            }
        })
    }

    //添加图片到图片结合中
    $scope.addItemImages=function(){
        $scope.entity.goodsDesc.itemImages.push($scope.image_entity);
    }
    //删除图片
    $scope.deleItemImages=function(index){
        $scope.entity.goodsDesc.itemImages.splice(index,1);
    }

    //查询规格集合中是否存在规格名
    $scope.selectAttribute=function(list,key,value){
        for(var i = 0; i < list.length; i++){
            if(list[i][key] == value){
                //返回的对象:{"attributeValue":["移动3G"],"attributeName":"网络"}
                return list[i];
            }
        }
        return null;
    }

    //点击规格选项，存储你选中的数据
    //思路:把选中的规格及规格选项放在数组中存储起来
    //[{attributeName:"网络",attributeValue:["规格选项"]}]
    //attributeName:规格名  attributeValue:规格选项名
    //$scope.entity.goodsDesc.specificationItems:[{"attributeValue":["移动3G","移动4G"],"attributeName":"网络"},{"attributeValue":["16G","32G"],"attributeName":"机身内存"}]
    $scope.updateSpecAttribute=function($event,attributeName,attributeValue){
        //先来根据你传递过来的AttributeName得值查询，如果这个值在集合中的对象中，那么对象就把值push，Value值
        //判断选中的规格选项是否已经在对象中了，根据什么来查询呢？根据规格名来查询
        var object  = $scope.selectAttribute($scope.entity.goodsDesc.specificationItems,"attributeName",attributeName);
        if(object != null){
            if($event.target.checked){//选中
                object.attributeValue.push(attributeValue);
            } else{
                //删除
                object.attributeValue.splice(object.attributeValue.indexOf(attributeValue),1);
                if(object.attributeValue.length == 0){
                    $scope.entity.goodsDesc.specificationItems.splice(
                        $scope.entity.goodsDesc.specificationItems.indexOf(object),1
                    )
                }
            }

        } else{
            $scope.entity.goodsDesc.specificationItems.push({"attributeName":attributeName,"attributeValue":[attributeValue]});
        }
    }


    //创建SKU列表
    $scope.createItemList=function(){
        $scope.entity.itemList=[{spec:{},price:0,num:99999,status:'0',isDefault:'0' } ];//初始数组
        var items=  $scope.entity.goodsDesc.specificationItems;
        for(var i=0;i< items.length;i++){
            // items[i].attributeName:“网络” items[i].attributeValue：["移动3g","移动4g"]
            $scope.entity.itemList = addColumn( $scope.entity.itemList,items[i].attributeName,items[i].attributeValue);
        }
    }
    //添加列值
    addColumn=function(list,columnName,conlumnValues){
        var newList=[];//新的集合
        for(var i=0;i<list.length;i++){
            var oldRow= list[i];
            for(var j=0;j<conlumnValues.length;j++){
                var newRow= JSON.parse( JSON.stringify( oldRow )  );//深克隆
                newRow.spec[columnName]=conlumnValues[j];
                newList.push(newRow);
            }
        }
        //newList=[{spec:{"网络":"移动3G"},price:0,num:99999,status:'0',isDefault:'0' }，{spec:{"网络":"移动4G"},price:0,num:99999,status:'0',isDefault:'0' }]
        return newList;
    }

    $scope.itemCatList=[];//商品分类列表
    //加载商品分类列表//初始化，获取商品分类把分类得ID和name放入数组
    $scope.findItemCatList = function () {
        itemCatService.findAll().success(function (response) {
            for (var i=0;i<response.length;i++){
                $scope.itemCatList[response[i].id] = response[i].name;
             }
        });
    }

    $scope.maketableStatusList = ["已下架","已上架"];
    //更改商品上下架
    $scope.maketableStatus = function (status) {
        goodsService.maketableStatus($scope.selectIds,status).success(function (response) {
            if (response.success){
                //修改成功
                $scope.selectIds=[];
                alert(response.message);
                $scope.reloadList();
            } else {
                //修改不成功
                alert(response.message);
            }
        });
    }

});	
