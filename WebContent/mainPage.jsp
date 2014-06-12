<%@page language="java" contentType="text/html; charset=UTF-8" pageEncoding="UTF-8"%>
<%@page import="java.util.*"%>
<%@page import="com.gmteam.framework.IConstants"%>
<%
  String path = request.getContextPath();
%>
<!DOCTYPE html PUBLIC "-//W3C//DTD HTML 4.01 Transitional//EN" "http://www.w3.org/TR/html4/loose.dtd">
<html>
<head>
<jsp:include page="<%=path%>/common/sysInclude.jsp" flush="true"/>
<script type="text/javascript" src="<%=path%>/resources/js/mainPage.utils.js"></script>
<link rel="stylesheet" type="text/css" href="<%=path%>/resources/css/mainPage.css"/>

<title><%=IConstants.PLATFORM_NAME%></title>
</head>
<style type="text/css">
</style>
<body class="_body">
<center>
<div id="topFrame">
</div>
<div id="mainFrame">
</div>
<div id="footFrame">
</div>
</center>
</body>
<script>
var PAGE_WIDTH=-1;//主页面的宽度，若为-1表明页面宽度自适应
var PAGE_HEIGHT=-1;//主页面的高度，若为-1表明页面宽度自适应
$(function (){
  
});
</script>
</html>
