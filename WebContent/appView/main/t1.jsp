<%@ page language="java" contentType="text/html; charset=UTF-8" pageEncoding="UTF-8"%>
<%
  String path = request.getContextPath();
%>
<!DOCTYPE html PUBLIC "-//W3C//DTD HTML 4.01 Transitional//EN" "http://www.w3.org/TR/html4/loose.dtd">
<html>
	<head>
		<meta http-equiv="Content-Type" content="text/html; charset=utf-8">
		<title>t1</title>
		<script type="text/javascript" src="<%=path %>/resources/plugins/jquery/jquery-1.10.2.min.js"></script><!-- 引入jquery-->
		<script type="text/javascript" src="<%=path %>/resources/plugins/easyui-1.3.4/jquery.easyui.min.js"></script>
		<script type="text/javascript" src="<%=path %>/resources/plugins/easyui-1.3.4/locale/easyui-lang-zh_CN.js"></script>
		<link rel="stylesheet" type="text/css" href="<%=path %>/resources/plugins/easyui-1.3.4/themes/default/easyui.css"></link>
		<link rel="stylesheet" type="text/css" href="<%=path %>/resources/plugins/easyui-1.3.4/themes/icon.css"></link>
	</head>
	<body>
    <ul id="tt" >
	    <li>
		    <span>Folder</span>
		    <ul>
			    <li>
				    <span>Sub Folder 1</span>
				    <ul>
					    <li><span><a href="#">File 11</a></span></li>
					    <li><span>File 12</span></li>
					    <li><span>File 13</span></li>
				    </ul>
			    </li>
			    <li><span>File 2</span></li>
			    <li><span>File 3</span></li>
		    </ul>
	    </li>
	    <li><span>File21</span></li>
    </ul>
    <h1>——————————————————————————————————————</h1>
    <ul id="tt2" lines="true">
    </ul>
	</body>
	<script type="text/javascript">
	 $(function(){
		 $('#tt').tree({});
		 
	   $('#tt2').tree({
		   url:'tree_data.json'
     });
	 });
	</script>
</html>
