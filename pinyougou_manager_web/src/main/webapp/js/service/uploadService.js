app.service("uploadService",function ($http) {

    this.uploadFile = function () {
        //定义一个form表单
        var formData = new FormData;
        enctype="multipart/form-data";
        //在这个表单中追加了一个file,file:js的服务，获取input框中type=file的所有对象；获取第1个
        //如果HTML中有多个type=file那么从上往下一个一个标记索引
        formData.append('file',file.files[0]);
        return $http({
            method:"POST",
            url:"../upload",
            data:formData,
            headers:{'content-Type':undefined},
            transformRequest:angular.identity
        });

    }
});