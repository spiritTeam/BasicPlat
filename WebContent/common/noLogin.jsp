<%@ page language="java" contentType="text/html; charset=UTF-8" pageEncoding="UTF-8"%>
<%
  String path = request.getContextPath();
%>
<!DOCTYPE html>
<html xmlns="http://www.w3.org/1999/xhtml">
<head>
<title></title>
<meta http-equiv="pragma" content="no-cache"/>
<meta http-equiv="cache-control" content="no-cache"/>
<meta http-equiv="expires" content="0"/>
<script type="text/javascript" src="<%=path%>/resources/js/framework.utils.js"></script>
<script>
var url=window.location.href;
var mainPage=getMainPage();
var noLoginUrl="<%=path%>/common/login.jsp";

if (mainPage) mainPage.location.href=noLoginUrl;
else window.location.href=noLoginUrl;
</script>