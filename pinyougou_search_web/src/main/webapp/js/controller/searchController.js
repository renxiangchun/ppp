//控制层
app.controller("searchController",function ($scope,$location,searchService) {

    //初始化一个searchMap对象
    // 定义封装搜索参数对象，封装所有参与搜索参数
    // 1,主查询条件 （关键词搜索条件）
    // 2,分类查询参数
    // 3，品牌参数
    // 4,规格属性参数
    // 5,价格参数
    // 6,排序
    // 7,分页
    $scope.searchMap = {
        "keywords" : "",
        "category" : "",
        "brand" : "",
        "spec" : {},
        "price" : "",
        "sort" : "DESC",
        "sortField" : "price",
        "page" : 1,
        "pageSize" : 20
    };
    $scope.search = function () {
        searchService.search($scope.searchMap).success(function (response) {
            //返回的数据建议用Map返回，因为后期你不确定是不是只返回一种数据，用Map来封装比较好！
            $scope.resultMap = response;
            $scope.list = $scope.resultMap.rows;

            //分页
            buildPageLabel();
        });
    }

    //定义方法，获取门户系统参数，实现搜索
    $scope.loadSearch = function () {
        //$location.search():获取所有参数
        // 获取门户系统跨系统传递的参数
        // $location.search():是angularjs提供内置方法
        // 1,即可以接受跨域参数
        // 2,也可以接受本地参数
        // 接受参数语法：
        // 1,$location.search()["参数名称"]
        // 2,$location.search().keywords;
        var keywords = $location.search()["keywords"];
        $scope.searchMap.keywords = keywords;
        //执行查询
        $scope.search();
    }

    // 定义条件过滤搜索参数封装方法
    $scope.addFilterCondition = function (key, value) {
        if(key == "category" || key == "brand" || key == "price"){
            $scope.searchMap[key] = value;
        } else {
            $scope.searchMap.spec[key] = value;
        }
        //查询
        $scope.search();
    }

    //取消面包屑数据
    $scope.removeSearchItem = function (key) {
        if(key == "category" || key == "brand" || key == "price"){
            $scope.searchMap[key]="";
        } else {
            $scope.searchMap.spec = {};
        }
    }

    //排序查询
    $scope.sortSearch = function (field, sort) {
        $scope.searchMap.sortField = field;
        $scope.searchMap.sort = sort;
        //查询
        $scope.search();
    }

    //构建分页标签(totalPages为总页数)
    buildPageLabel=function(){
        $scope.pageLabel=[];//新增分页栏属性集合
        var maxPageNo= $scope.resultMap.totalPages;//得到最后页码 13
        var firstPage=1;//开始页码
        var lastPage=maxPageNo;//截止页码
        $scope.firstDot=true;//前面有点
        $scope.lastDot=true;//后边有点
        if($scope.resultMap.totalPages > 5){  //如果总页数大于5页,显示部分页码
            if($scope.searchMap.page<=3){//如果当前页小于等于3
                lastPage=5; //前5页
                $scope.firstDot=false;
            }else if( $scope.searchMap.page>=lastPage-2  ){//如果当前页大于等于最大页码-2
                firstPage= maxPageNo-4;		 //后5页
                $scope.lastDot= false;
            }else{ //显示当前页为中心的5页
                firstPage=$scope.searchMap.page-2;
                lastPage=$scope.searchMap.page+2;
                $scope.firstDot=true;//前面有点
                $scope.lastDot=true;//后边有点
            }
        }
        //循环产生页码标签
        for(var i=firstPage;i<=lastPage;i++){
            $scope.pageLabel.push(i);
        }
    }

    //根据当前页搜索
    $scope.pageSearch=function(page){

        if(page<1 || page>$scope.resultMap.totalPages){
            return;
        }
        $scope.searchMap.page = page;
        //执行查询
        $scope.search();
    }
});