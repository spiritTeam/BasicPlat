<%@page language="java" contentType="text/html; charset=UTF-8" pageEncoding="UTF-8"%>
<%@page import="com.gmteam.framework.IConstants"%>
<%
  String path = request.getContextPath();
%>
<!DOCTYPE html PUBLIC "-//W3C//DTD XHTML 1.0 Transitional//EN" "http://www.w3.org/TR/xhtml1/DTD/xhtml1-transitional.dtd">
<html xmlns="http://www.w3.org/1999/xhtml">
<head>
<meta http-equiv="Content-Type" content="text/html; charset=UTF-8" />
<title><%=IConstants.PLATFORM_NAME%></title>
<script>
var path = "<%=path%>";
</script>
<link href="<%=path%>/resources/css/common.css" rel="stylesheet" type="text/css"/>
<script src="<%=path%>/resources/plugins/jquery/jquery-1.10.2.min.js" type="text/javascript"></script>
<script src="<%=path%>/resources/plugins/jquery/jquery.form.js" type="text/javascript"></script>
<script src="<%=path%>/resources/plugins/jquery/mainCommon.js" type="text/javascript"></script>

<style type="text/css">
html { background:#016ba9 url(<%=path%>/resources/images/sys/login/4.gif) repeat-x; height:768px; }
#leftDiv { position:absolute; left:0; top:0; width:20%; height:716px;background:url(<%=path%>/resources/images/sys/login/3.gif) repeat-x; }
#loginCenter { position:absolute; height:502px; width: 1003px; top:108px; left:50%; margin-left:-501px; background:url(<%=path%>/resources/images/sys/login/1.jpg) no-repeat; }
#loginCenter form { margin-top:325px; }
#userTable label { float:right; }
#userTable td { line-height:24px; }
#loginname, #password { height:17px; width:130px; border:1px solid #0f6479; }
#Submit { border:none; height:46px; *height:48px; _height:44px; width:60px; background:url(<%=path%>/resources/images/sys/login/2.gif) no-repeat; }
#footDiv { margin-top:84px; text-align:center; }
#footDiv ul { padding-left:210px; }
#footDiv ul li { float:left; margin-top:20px; color:#c2bebf; }
</style>
</head>
<body onload="document.all.loginname.focus();">
<!--左右背景填充-->
<div id="leftDiv"></div>
<!--左右背景填充-->
<div id="loginCenter">
  <form action="<%=path%>/tologin.do" method="post" name="frm" id="userForm">
    <table id="userTable">
      <tr>
        <td style="width:430px;"><label for="loginname">用户名：</label></td>
        <td style="width:138px;"><input type="text" name="loginname" id='loginname' tabindex="1" /></td><!-- 注意tabindex属性，不要漏掉了 -->
        <td rowspan="2"><div id="errorMsgDiv" style="display: none"> 登陆失败 </div>
          <input type="submit" id="Submit" name="Submit" tabindex="3"  value=""/>
        </td>
      </tr>
      <tr>
        <td><label for="password">密&nbsp;&nbsp;&nbsp;&nbsp;码：</label></td>
        <td><input type="password" name="password" id='password' tabindex="2"/></td>
      </tr>
      <tr class="other">
        <td>&nbsp;</td>
        <td colspan="2">&nbsp;</td>
      </tr>
    </table>
  </form>
  <input type="text" name="hiddentext" id="hiddentext" style="width:0px;height:0px; display:none;"/>
  <div id="footDiv"> <span style=" color:#F00;">版权所有</span><span style=" margin-left:15px;color:#FFF;">海南省公安厅国保总队</span>
    <ul>
      <li>操作提示： 支持windows2000/xp/2003</li>
      <li style=" width:230px;">客户端需要IE6.0或以上浏览器</li>
      <li>请保管好您的用户名及密码</li>
    </ul>
  </div>
</div>
<script language=JScript event="OnCompleted(hResult,pErrorObject, pAsyncContext)" for=foo></script>
<script language=JScript event=OnObjectReady(objObject,objAsyncContext) for=foo>
if (objObject.IPEnabled!=null && objObject.IPEnabled!="undefined" && objObject.IPEnabled==true) {
  if (objObject.MACAddress!=null && objObject.MACAddress!="undefined") objObject.MACAddress;
  if (objObject.DNSHostName!=null && objObject.DNSHostName!="undefined") objObject.DNSHostName;
  if (objObject.IPEnabled && objObject.IPAddress(0)) {
    ipList +=","+objObject.IPAddress(0);
  }
}
</script>
<script>
//初始化ajaxForm
$(function() {
  //ip赋值
  if (ipList==""||!ipList) {//用另一种方法得到IP
    $('#localIp').val(getLocalIp());
  } else { //用复杂方法获得的IP
    $('#localIp').val(ipList.substring(1));
  }
  $('#hiddentext').focus();
  $('#loginname').focus();
  var options = {
    dataType: 'json',
    beforeSubmit:fnCheckForm,
    success: showResponse
  };
  $('#userForm').ajaxForm(options);
});

//获得本机IP
var service = locator.ConnectServer();
service.Security_.ImpersonationLevel=3;
service.InstancesOfAsync(foo, 'Win32_NetworkAdapterConfiguration');
var ipList = "";
function getLocalIp() {
  var ipAxO=null, rslt = "";
  try {
    ipAxO = new ActiveXObject("rcbdyctl.Setting");
    rslt = ipAxO.GetIPAddress;
    ipAxO = null;
  } catch(e) {
  }
  return rslt;
}

function fnCheckForm(formData,jqFrom,options) {
  if ($('#loginname').val().length <= 0 ) {
    alert("请输入登录名!");
    $('#loginname').focus();
    return false;
  }
  if ($('#password').val().length<=0) {
    alert("密码不能为空!");
    $('#password').focus();
    return false;
  }
  return true;
}

//根据返回的内容进行处理
function showResponse(responseObject, statusText) {
  if (responseObject) {
    if (responseObject.isPass==true) {
      var url = "<%=path%>/sys-index.jsp";
      url = "<%=path%>/test/index/testIndexDIS.jsp";
      window.location.href= url;
    } else {
      alert(responseObject.msg);
      $('#password').val("");
      $('#loginname').select();
    }
  } else alert("登陆错误,请与管理员联系!");
}
function openFullScreen(url,title) {
  var options = "top=0, left=0, toolbar=no, menubar=no, scrollbars=yes, resizable=no, location=no, status=no, fullScreen=no";
  //var pop=window.open(url,'FullScreen','fullscreen=1');
  window.open (url, title, options); 	
}
function fullScreen() {
  var WshShell = new ActiveXObject('WScript.Shell');
  WshShell.SendKeys('{F11}');
}
</script>
</body>
</html>