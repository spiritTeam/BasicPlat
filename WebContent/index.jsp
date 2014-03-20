<%@ page language="java" contentType="text/html; charset=UTF-8" pageEncoding="UTF-8"%>
<%String path = request.getContextPath();%>
<!DOCTYPE html PUBLIC "-//W3C//DTD HTML 4.01 Transitional//EN" "http://www.w3.org/TR/html4/loose.dtd">
<html>
<head>
<meta http-equiv="Content-Type" content="text/html; charset=ISO-8859-1">
<title>jQuery easy-ui</title>
<script type="text/javascript" src="<%=path %>/js/jquery-easyui-1.3.5/jquery.min.js"></script><!-- 引入jquery-->
<script type="text/javascript" src="<%=path %>/js/jquery-easyui-1.3.5/jquery.easyui.min.js"></script>
<script type="text/javascript" src="<%=path %>/js/jquery-easyui-1.3.5/locale/easyui-lang-zh_CN.js"></script>
<link rel="stylesheet" type="text/css" href="<%=path %>/js/jquery-easyui-1.3.5/themes/default/easyui.css"></link>
<link rel="stylesheet" type="text/css" href="<%=path %>/js/jquery-easyui-1.3.5/themes/icon.css"></link>
<script type="text/javascript" charset="utf-8">
  var loginAndRegDialog;
  $(function (){
	  //dialog继承与panel，很多属性和方法可以从panel中获取
	  loginAndRegDialog = $('#loginAndRegDialog').dialog({
		closable:false,
		modal:true,
		buttons : [{
			//要想显示value，前面要加text，绑定事件handler：function
			text:'注册',
			handler:function(){
			}
		},{
			text:'登录',
			handler:function(){
				//console.info('点击我登录了');
				$.ajax({
					url:'<%=path %>/ajaxUpload.do',
					data:{
						name:$('#loginInputForm input[name=name]').val(),
						password:$('#loginInputForm input[name=password]').val()
					},
					cache:false,
					dataType:'json',
					sucess:function(r){
						console.info(r.msg);
					}
					
				});
			}
		}]
	  });
  });
</script>
</head>
<body>
    <!-- 用dialog实现登录 -->
    <div  id="loginAndRegDialog" title="用户登录" style="width:250px;height:200px;">
      <form id="loginInputForm" method="post" action="">
	      <table>
	        <tr>
	            <th align="right">用户名:</th>
	            <td><input name="name"/></td>
	        </tr>
	        <tr>
	            <th align="right">密码：</th>
	            <td><input type="password" name="password"/></td>
	        </tr>
	      </table>
      </form>      
    </div>
</body>
</html>