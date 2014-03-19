<%@ page language="java" contentType="text/html; charset=UTF-8" pageEncoding="UTF-8"%>
<%@page import="my.learn.JsonTest"%>
<%@page import="my.learn.JsonTest2"%>
<%
  String aJsonStr = JsonTest.toJson();
  String aJsonStr2 = JsonTest2.toJson();
%>
<!DOCTYPE html PUBLIC "-//W3C//DTD HTML 4.01 Transitional//EN" "http://www.w3.org/TR/html4/loose.dtd">
<html>
<head>
<meta http-equiv="Content-Type" content="text/html; charset=ISO-8859-1">
<title>Insert title here</title>
</head>
<body>
ABCD<br/>
<%=aJsonStr %><br/>
<%=aJsonStr2 %>
</body>
</html>