<%@page language="java" contentType="text/html; charset=UTF-8" pageEncoding="UTF-8"%>
<%
  String path = request.getContextPath();
%>
<!DOCTYPE html PUBLIC "-//W3C//DTD HTML 4.01 Transitional//EN" "http://www.w3.org/TR/html4/loose.dtd">
<html>
<head>
<meta http-equiv="Content-Type" content="text/html; charset=ISO-8859-1">
<jsp:include page="/common/sysInclude.jsp" flush="true"/>
<title>数据源管理</title>
<style>
body {margin:0 auto;}
</style>
</head>
<body class="easyui-layout" style="margin-bottom:6px;">
  <div data-options="region:'west',split:true,title:'数据源',collapsible:false, border:true" style="width:240px;padding:10px;">tree</div>
  <div data-options="region:'center',title:'详情',border:false"></div>
</body>
<script>
$(function() {
  $("div[data-options^='region:\'center\'']").parent().css("border-left", "1px solid #95B8E7");
  $("div[data-options^='region:\'west\'']").parent().find(".panel-header").css("border-top", "0px").css("border-left", "0px");
  $("div[data-options^='region:\'west\'']").parent().find(".panel-body").css("border-bottom", "0px").css("border-left", "0px");
});
</script>
</html>