<%@page language="java" contentType="text/html; charset=UTF-8" pageEncoding="UTF-8"%>
<%@page import="java.util.*"%>
<%@page import="com.gmteam.framework.FConstants"%>
<%
  String path = request.getContextPath();
%>
<!DOCTYPE html>
<html xmlns="http://www.w3.org/1999/xhtml">
<head>
<title><%=FConstants.PLATFORM_NAME%></title>
<meta http-equiv="Content-Type" content="text/html; charset=UTF-8">
<meta http-equiv="cache-control" content="no-cache"/>
<meta http-equiv="pragma" content="no-cache"/>
<meta http-equiv="expires" content="0"/>
<jsp:include page="/common/sysInclude.jsp" flush="true"/>
<script type="text/javascript" src="<%=path%>/resources/js/mainPage.utils.js"></script>
<script type="text/javascript" src="<%=path%>/resources/plugins/spiritUi/pageFrame.js"></script>
<link rel="stylesheet" type="text/css" href="<%=path%>/resources/css/mainPage.css"/>
<link rel="stylesheet" type="text/css" href="<%=path%>/resources/plugins/spiritUi/themes/default/all.css"/>
</head>

<body class="_body">
<!-- 遮罩层 -->
<div id="mask" style="display:none; position:absolute;vertical-align:middle;text-align:center; align:center;">
  <img align="middle" src="<%=path%>/resources/images/waiting_circle.gif"/><br/><br/>
  <span style="font-weight:bold;" id="maskTitle"></span>
</div>

<!-- 头部:悬浮 -->
<div id="topSegment">
  <div id="button">AABBCCDD</div>
</div>
<!-- 脚部:悬浮 -->
<div id="footSegment"></div>
<!-- 实际功能区中部 -->
<div id="mainSegment">12345678</div>

<script>
/**
 * 初始化参数
 */
var INIT_PARAM = {
  //页面中所用到的元素的id，只用到三个Div，另，这三个div应在body层
  pageObjs: {
    topId: "topSegment", //头部Id
    mainId: "mainSegment", //主体Id
    footId: "footSegment" //尾部Id
  },
  top_shadow_color: "red",
  page_width: 900,
  page_height: 4000
};

$(function() {
  var initStr = $().spiretPageFrame(INIT_PARAM);
  if (initStr) {
    $.messager.alert("页面初始化失败", initStr, "error");
    return ;
  }
});

//-logout begin:以下两函数负责注销-----------------------------------------------------------------
/**
 * 子界面调用此方法进行注销，主要用于用户同时登录不同浏览器的判断逻辑 
 */
function onlyLogout(ip, mac, browser) {
  var msg = "您已经在["+ip+"("+mac+")]客户端用["+browser+"]浏览器重新登录了，当前登录失效！";
  if ((!ip&&!mac)||(ip+mac=="")) msg = "您已经在另一客户端用["+browser+"]浏览器重新登录了，当前登录失效！";
  $.messager.alert("提示", msg+"<br/>现返回登录页面。", "info", function(){ logout(); });
}
/**
 * 注销
 */
function logout() {
  var url=_PATH+"/logout.do";
  $.ajax({type:"post", async:true, url:url, data:null, dataType:"json",
    success: function(json) {
      if (json.type==1) {
        window.location.href="<%=path%>/login/login.jsp?noAuth";
      } else {
        $.messager.alert("错误", "注销失败："+json.data+"！</br>返回登录页面。", "error", function(){
          window.location.href="<%=path%>/login/login.jsp?noAuth";
        });
      }
    },
    error: function(errorData) {
      $.messager.alert("错误", "注销失败：</br>"+(errorData?errorData.responseText:"")+"！<br/>返回登录页面。", "error", function(){
        window.location.href="<%=path%>/login/login.jsp?noAuth";
      });
    }
  });
};
//-logout end:以上两函数负责注销-----------------------------------------------------------------
</script>
</body>
</html>