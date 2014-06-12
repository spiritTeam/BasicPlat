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
<script type="text/javascript" src="../resources/js/mainPage.utils.js"></script>
<script type="text/javascript" src="../resources/js/framework.utils.js"></script>
<script type="text/javascript" src="../resources/js/common.utils.js"></script>
<script>
var url=window.location.href;
var ip = getUrlParam(url, "clientIp");
var mac = getUrlParam(url, "clientMacAddr");
var browser = decodeURI(getUrlParam(url, "browser"));

var mainPage = getMainPage();
if (mainPage==null) {
  $.ajax({type:"post", async:true, url:"common/outlogin.do", data:null, dataType:"json",
    success: function(json) {
      if (json.type==1) {
        alert("提示", "您已经在["+ip+"("+mac+")]机器上用["+browser+"]浏览器重新登陆了，当前登录失效！", "info", function(){
          window.location.href="/common/login.jsp";
        });
      }
    },
    error: function(errorData) {}
  });
} else mainPage.onlyLogout(ip, mac, browser);
</script>
