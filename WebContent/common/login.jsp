<%@ page language="java" contentType="text/html; charset=UTF-8" pageEncoding="UTF-8"%>
<%@  page  import="java.io.*"%>
<%
    String path = request.getContextPath();
%>
<!DOCTYPE html PUBLIC "-//W3C//DTD XHTML 1.0 Transitional//EN" "http://www.w3.org/TR/xhtml1/DTD/xhtml1-transitional.dtd">
<html xmlns="http://www.w3.org/1999/xhtml">
<head>
<meta http-equiv="Content-Type" content="text/html; charset=utf-8" />
<script type="text/javascript" src="../resources/plugins/jquery/jquery-1.10.2.min.js"></script>
<script type="text/javascript" src="../resources/js/common.utils.js"></script>
<title>平台登录</title>
<SCRIPT language=JScript event="OnCompleted(hResult,pErrorObject, pAsyncContext)" for=foo>
document.forms[0].txtMACAddr.value=unescape(MACAddr);
document.forms[0].txtIPAddr.value=unescape(IPAddr);
document.forms[0].txtDNSName.value=unescape(sDNSName);
</SCRIPT>
<SCRIPT language=JScript event=OnObjectReady(objObject,objAsyncContext) for=foo>
if(objObject.IPEnabled != null && objObject.IPEnabled != "undefined" && objObject.IPEnabled == true){
  if(objObject.MACAddress != null && objObject.MACAddress != "undefined") MACAddr = objObject.MACAddress;
  if(objObject.IPEnabled && objObject.IPAddress(0) != null && objObject.IPAddress(0) != "undefined") IPAddr = objObject.IPAddress(0);
  if(objObject.DNSHostName != null && objObject.DNSHostName != "undefined") sDNSName = objObject.DNSHostName;
}
</SCRIPT>
<META content="MSHTML 6.00.2800.1106" name=GENERATOR>
<style>
* {
  padding: 0px;
  margin: 0px;
}

body {
  font-family: Arial, Helvetica, sans-serif;
  background: url(../resources/images/login/grass.jpg);
  font-size: 13px;
}

img {
  border: 0;
}

.lg {
  width: 468px;
  height: 468px;
  margin: 100px auto;
  background: url(../resources/images/login/login_bg.png) no-repeat;
}

.lg_top {
  height: 200px;
  width: 468px;
}

.lg_main {
  width: 400px;
  height: 180px;
  margin: 0 25px;
}

.lg_m_1 {
  width: 290px;
  height: 100px;
  padding: 60px 55px 20px 55px;
}

.ur {
  height: 37px;
  border: 0;
  color: #666;
  width: 236px;
  margin: 4px 28px;
  background: url(../resources/images/login/user.png) no-repeat;
  padding-left: 10px;
  font-size: 16pt;
  font-family: Arial, Helvetica, sans-serif;
}

.pw {
  height: 37px;
  border: 0;
  color: #666;
  width: 236px;
  margin: 4px 28px;
  background: url(../resources/images/login/password.png) no-repeat;
  padding-left: 10px;
  font-size: 16pt;
  font-family: Arial, Helvetica, sans-serif;
}

.bn {
  width: 330px;
  height: 72px;
  background: url(../resources/images/login/enter.png) no-repeat;
  border: 0;
  display: block;
  font-size: 18px;
  color: #FFF;
  font-family: Arial, Helvetica, sans-serif;
  font-weight: bolder;
}

.lg_foot {
  height: 80px;
  width: 330px;
  padding: 6px 68px 0 68px;
}
</style>
</head>
<body class="b">

<OBJECT id=locator classid=CLSID:76A64158-CB41-11D1-8B02-00600806D9B6 VIEWASTEXT></OBJECT>
<OBJECT id=foo classid=CLSID:75718C9A-F029-11d1-A1AC-00C04FB6C223></OBJECT>
<SCRIPT language=JScript>
  var service = locator.ConnectServer();
  var MACAddr ;
  var IPAddr ;
  var DomainAddr;
  var sDNSName;
  service.Security_.ImpersonationLevel=3;
  service.InstancesOfAsync(foo, 'Win32_NetworkAdapterConfiguration');
</SCRIPT>

<FORM id="formfoo" name="formbar" action="" method="post" style="display:none">
  <INPUT value=00:05:5D:0E:C7:FA name=txtMACAddr>
  <INPUT value=192.168.0.2 name=txtIPAddr>
  <INPUT value=typ name=txtDNSName>
</FORM>
<script type="text/javascript" charset="utf-8">
var url="<%=path%>/common/login.do";
$(function(){
  $("#login_btn").click(function(){
    var pData={
        "loginName":$("#loginName").val(),
        "password":$("#password").val(),
        "clientMacAddr":formfoo.txtMACAddr.value,
        "clientIp":formfoo.txtIPAddr.value,
        "browser":getBrowserVersion()
      };
    alert(getBrowserVersion());
    alert(formfoo.txtIPAddr.value);
    alert(formfoo.txtMACAddr.value);
    $.ajax({
      type:"post",
      async:false,
      url:url,
      data:pData,
      dataType:"json",
      success: function(json){
        if(json.type==1){
          url = "<%=path%>/main.jsp";
          window.location.href=url;
        }else if(json.type==2){
          alert("登录失败："+json.data);
          $("#loginName").focus();
        }else{
          alert("登录异常："+json.data);
          $("#loginName").focus();
        }
      },
      error:function(errorData){
        alert("登录异常："+errorData.data);
        $("#loginName").focus();
      }
    });
  });
});
</script>
  <div class="lg">
    <form action="../login.do" method="post" id="login_form">
      <div class="lg_top"></div>
      <div class="lg_main">
        <div class="lg_m_1">
          <input id="loginName" name="loginName" class="ur" /> <input id="password" name="password" type="password" class="pw" />
        </div>
      </div>
      <div class="lg_foot">
        <input type="button" id="login_btn" value="登录" class="bn" />
      </div>
    </form>
  </div>
</body>
</html>
