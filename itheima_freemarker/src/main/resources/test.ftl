<html>
<head>
    <meta charset="utf-8">
    <title>Freemarker 入门小 DEMO </title>
</head>
<body>0
<#--我只是一个注释，我不会有任何输出  -->
${name},你好。${message}
<br>
<#assign user={"name":"张三丰","age":"100"}>
姓名:${user.name},年龄:${user.age}
<br>
<#--在页面中可以引入其他页面-->
<#include "head.ftl">
<br>
<#if success== true>
    传递success是true
<#else>
    否则传递的是false
</#if>
<br>
<#list goodsList as goods>
    索引:${goods_index + 1} --- 商品名称:${goods.name},价格:${goods.price}
    <br>
</#list>
商品总数:${goodsList?size}
<br>
<#--把String字符串转JSON对象-->
<#assign userInfo="{'name':'赵敏','age':'18'}">
信息:${userInfo}
<br>
<#assign u=userInfo?eval>
姓名:${u.name};年龄:${u.age}

<br>
当前日期:${date?date}<br>
当前时间:${date?time}<br>
当前日期+时间:${date?datetime}<br>
日期格式化:${date?string("yyyy-MM-dd HH:mm:ss SSS")}<br>

将数字转换为字符串:${goodsId?c}
<br>
<#if aaa??>
 这个值有值${aaa}
<#else>
 这个值没有值
</#if>
<#-- 叹号：作为变量值的判断；如果有值输出；如果妹纸输出后面的数据 -->
<br>
${usernames!666}

</body>
</html>