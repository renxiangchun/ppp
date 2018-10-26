 //控制层 
app.controller('userController' ,function($scope,$controller   ,userService){	
	
	$controller('baseController',{$scope:$scope});//继承
	
    //读取列表数据绑定到表单中  
	$scope.findAll=function(){
		userService.findAll().success(
			function(response){
				$scope.list=response;
			}			
		);
	}    
	
	//分页
	$scope.findPage=function(page,rows){			
		userService.findPage(page,rows).success(
			function(response){
				$scope.list=response.rows;	
				$scope.paginationConf.totalItems=response.total;//更新总记录数
			}			
		);
	}
	
	//查询实体 
	$scope.findOne=function(id){				
		userService.findOne(id).success(
			function(response){
				$scope.entity= response;					
			}
		);				
	}
	
	//保存 
	$scope.save=function(){

		//判断密码是否一致
		if($scope.entity.password != $scope.password){
			alert("密码不一致，请重新输入...")
            $scope.entity.password = "";
            $scope.password = "";
            return;
		}
		var serviceObject;//服务层对象  				
		if($scope.entity.id!=null){//如果有ID
			serviceObject=userService.update( $scope.entity ); //修改  
		}else{
			serviceObject=userService.add( $scope.entity,$scope.smsCode);//增加
		}				
		serviceObject.success(
			function(response){
				if(response.success){
					//注册成功后，跳转到登录页面
					location.href = "/home-index.html";
				}else{
					alert(response.message);
				}
			}		
		);				
	}
	
	 
	//批量删除 
	$scope.dele=function(){			
		//获取选中的复选框			
		userService.dele( $scope.selectIds ).success(
			function(response){
				if(response.success){
					$scope.reloadList();//刷新列表
				}						
			}		
		);				
	}
	
	$scope.searchEntity={};//定义搜索对象 
	
	//搜索
	$scope.search=function(page,rows){			
		userService.search(page,rows,$scope.searchEntity).success(
			function(response){
				$scope.list=response.rows;	
				$scope.paginationConf.totalItems=response.total;//更新总记录数
			}			
		);
	}

	//发送验证码
	$scope.entity = {};
	$scope.sendCode=function () {
		if($scope.entity.phone == null){
			alert("请输入手机号");
			return;
		}
		userService.sendCode($scope.entity.phone).success(function (response) {
			if(response.success){
                alert(response.message);
            } else {
                alert(response.message);
			}
        });
    }
    //用户首页初始化，获取当前登录者信息
	$scope.showName = function () {
		userService.showName().success(function (response) {
			$scope.userMap = response;
        });

    }
    
});	
