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

<script type="text/javascript" src="<%=path%>/resources/plugins/spiritui/jq.spirit.utils.js"></script>
<script type="text/javascript" src="<%=path%>/resources/plugins/spiritui/jq.spirit.pageFrame.js"></script>
<script type="text/javascript" src="<%=path%>/resources/plugins/spiritui/jq.spirit.tabs.js"></script>

<script type="text/javascript" src="<%=path%>/resources/js/mainPage.utils.js"></script>

<link rel="stylesheet" type="text/css" href="<%=path%>/resources/css/mainPage.css"/>
<link rel="stylesheet" type="text/css" href="<%=path%>/resources/plugins/spiritui/themes/default/all.css"/>
<link rel="stylesheet" type="text/css" href="<%=path%>/resources/plugins/spiritui/themes/default/tabs.css"/>
</head>

<body class="_body">
<!-- 遮罩层 -->
<div id="mask" style="display:none; position:absolute;vertical-align:middle;text-align:center; align:center;">
  <img align="middle" src="<%=path%>/resources/images/waiting_circle.gif"/><br/><br/>
  <span style="font-weight:bold;" id="maskTitle"></span>
</div>

<!-- 头部:悬浮 -->
<div id="topSegment">
  <div id="logoImg_top">头部1:logo区域</div><!-- 头部1:logo区域 -->
  <div id="titleImg_top">头部2:系统名称，广告</div><!-- 头部2:系统名称 -->
  <div id="commonFunc_top">头部3:通用功能<!-- 头部3:通用功能 -->
    <div class="cfRight"></div>
    <div id="commonFuncButton">
      <div class="mainTabButton imgTabButton commButton logout" id="logoutU" onclick="logout()">注销</div>
      <div class="mainTabButton imgTabButton commButton refreshCache" id="refreshCacheU" onclick="refreshCache()">刷新缓存</div>
    </div>
    <div class="cfLeft"></div>
  </div>
  <div id="welcom_top"><!-- 头部4:用户欢迎 -->
    <span class="username" style="text-decoration:underline;text-shadow:0px 0px 1px #2F4A1F;color:blue;">？？？</span>&nbsp;您好！欢迎您使用本系统
  </div>
  <div id="bar_top"><!-- 头部5:功能条 -->
    <div id="mainTab_top"></div><!-- 头部:界面功能页签 -->
    <div id="quickFunc_top"><!-- 头部:快速功能 -->
      <div class="mainTabButton imgTabButton fullscrenn" id="fullscreen" onclick="abc()">全屏</div>
      <div class="mainTabButton imgTabButton logout" id="logoutD" onclick="logout()" style="display:none;">注销</div>
      <div class="mainTabButton imgTabButton refreshCache" id="refreshCacheD" onclick="refreshCache()" style="display:none;">刷新缓存</div>
      <div id="vline"></div>
    </div>
  </div>
</div>
<!-- 脚部:悬浮 -->
<div id="footSegment"></div>

<!-- 实际功能区中部 -->
<div id="mainSegment">12345678</div>

<script>
function abc() {
  $("#mainTab_top").spiritTabs({id:"1235", tabs:[{title:"测试8", onclick:""},{title:"测试9", onclick:""}]});
}

var _topBarLeft=220;
var _topQuickFuncWidth=100;
var _topMiniHeight=26;//全屏后的头部高度，此高度也是非全屏时功能条的高度

function testClk(jqMy) {
  //alert(jqMy.attr("id"));
}

var testBar = {
  id: "test", //标识
  mutualType: true, //两页标签的交互区域的处理模式，若为false，则无交互区域，用css处理交互，若为true则有交互区域，交互用图片来处理
  mutualStyle: { //交互区样式，当mutualType=true生效
    width: "10px" //交互区宽度
  },
  tabs:[//页标签数组
    {title:"测试1", onClick:"", selected:"true"},
    {title:"测试2测试123"},
    {title:"测试3", onClick:""/*, normalCss:{"background-color":"yellow"}, selCss:{"background-color":"#589C8D"}*/},
    {title:"12345", onClick:""},
    {title:"测试5", onClick:""}
  ]
};
/**
    {title:"测试2测试2测试2", onClick:""},
    {title:"测试3测试3测试3", onClick:""},
    {title:"123456789", onClick:""},
    {title:"测试24测试24测试24测试24", onClick:""},
    {title:"测试8", onClick:""},
    {title:"测试9测试9测试9", onClick:""},
    {title:"12", onClick:""},
    {title:"测试10", onClick:""},
*/

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
  top_height: 130, //顶部高度
  top_shadow_color: "#95b8e7",
  page_width: 0,
  page_height: 4000,
  top_peg: false,
  myInit: initPosition,
  myResize: myResize,
  myScroll: myScroll
};

$(function() {
  var initStr = $().spiritPageFrame(INIT_PARAM);
  if (initStr) {
    $.messager.alert("页面初始化失败", initStr, "error");
    return ;
  };
  //设置界面主功能页签
  $("#mainTab_top").spiritTabs(testBar);
});

//-界面位置调整begin-----------------------------------------------------------------
function initPosition() {//注意，不要在此设置topSegment/mainSegment/footSegment等框架元素的宽高等，否则，页面不会自动进行调整
  //3-通用功能部分
  var absW = $("#commonFunc_top").spiritUtils("getAbsWidth");
  $("#commonFunc_top").css("left", $("#topSegment").width()-absW-5);
  //4-欢迎条
  //4-欢迎条:宽
  $("#welcom_top").css("left", $("#topSegment").css("margin-left"));
  $("#welcom_top").spiritUtils("setWidthByViewWidth", _topBarLeft);
  //4-欢迎条:高
  $("#welcom_top").spiritUtils("setHeightByViewHeight", _topMiniHeight);
  var absH = $("#welcom_top").spiritUtils("getAbsHeight");
  $("#welcom_top").css("top", $("#topSegment").height()-absH);
  //5-功能条
  //5-功能条:宽
  $("#bar_top").spiritUtils("setWidthByViewWidth", $("#topSegment").width()-_topBarLeft);
  $("#bar_top").css("left", parseFloat($("#topSegment").css("margin-left"))+_topBarLeft-parseFloat($("#bar_top").css("margin-left")));
  //5-功能条:高
  $("#bar_top").spiritUtils("setHeightByViewHeight", _topMiniHeight);
  var absH = $("#bar_top").spiritUtils("getAbsHeight");
  $("#bar_top").css("top", $("#topSegment").height()-absH);
  //5-功能条:快速功能
  absH = $("#quickFunc_top").spiritUtils("getSetHeight", $("#bar_top").height()-parseFloat($("#bar_top").css("padding-top"))-parseFloat($("#bar_top").css("padding-bottom"))-parseFloat($("#quickFunc_top").css("margin-top"))-parseFloat($("#quickFunc_top").css("margin-bottom")));
  $("#quickFunc_top").css({"height": absH, "width": _topQuickFuncWidth});
  //5-功能条:主页签
  $("#mainTab_top").spiritUtils("setWidthByViewWidth", $("#bar_top").width()-$("#quickFunc_top").spiritUtils("getAbsWidth"));
  absH = $("#mainTab_top").spiritUtils("getSetHeight", $("#bar_top").height()-parseFloat($("#bar_top").css("padding-top"))-parseFloat($("#bar_top").css("padding-bottom"))-parseFloat($("#mainTab_top").css("margin-top"))-parseFloat($("#mainTab_top").css("margin-bottom")));
  $("#mainTab_top").css("height", absH);
};
function myResize() {
  if (INIT_PARAM.page_width==0) {
    //3-通用功能部分
    var absW = $("#commonFunc_top").spiritUtils("getAbsWidth");
    $("#commonFunc_top").css("left", $("#topSegment").width()-absW-5);
    //5-功能条
    $("#bar_top").spiritUtils("setWidthByViewWidth", $("#topSegment").width()-_topBarLeft);
    //5-功能条:主页签
    $("#mainTab_top").spiritUtils("setWidthByViewWidth", $("#bar_top").width()-$("#quickFunc_top").spiritUtils("getAbsWidth"));
  }
};
function myScroll() {

};
//-界面位置调整 end -----------------------------------------------------------------

//-logout begin:以下两函数负责注销---------------------------------------------------
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