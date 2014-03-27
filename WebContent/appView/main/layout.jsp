<%@ page language="java" contentType="text/html; charset=UTF-8" pageEncoding="UTF-8"%>
<%
  String path = request.getContextPath();
%>
<!DOCTYPE html PUBLIC "-//W3C//DTD HTML 4.01 Transitional//EN" "http://www.w3.org/TR/html4/loose.dtd">
<html>
<head>
<meta http-equiv="Content-Type" content="text/html; charset=ISO-8859-1">
<title>jQuery easy-ui</title>
<script type="text/javascript" src="<%=path %>/resources/plugins/jquery/jquery-1.10.2.min.js"></script><!-- 引入jquery-->
<script type="text/javascript" src="<%=path %>/resources/plugins/easyui-1.3.4/jquery.easyui.min.js"></script>
<script type="text/javascript" src="<%=path %>/resources/plugins/easyui-1.3.4/locale/easyui-lang-zh_CN.js"></script>
<link rel="stylesheet" type="text/css" href="<%=path %>/resources/plugins/easyui-1.3.4/themes/default/easyui.css"></link>
<link rel="stylesheet" type="text/css" href="<%=path %>/resources/plugins/easyui-1.3.4/themes/icon.css"></link>

</head>
<body>
    <h2>在控制面板上布局</h2>  
    <div class="demo-info" style="margin-bottom:10px">  
        <div class="demo-tip icon-tip"></div>  
        <div>它也能在控制面板上布局。</div>  
    </div>  
    <div class="easyui-layout"  fit="true">  
        <div region="west" title="West" split="true" style="width:200px;"></div>  
        <div region="center" iconCls="icon-save" href="center.jsp" title="Center Title" style="overflow:hidden;"></div>
        </div>  
    </div>  
</body>
</html>