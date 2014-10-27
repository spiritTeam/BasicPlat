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

<link rel="stylesheet" type="text/css" href="<%=path%>/demo/css/mainPage.css"/>
<!--
<link rel="stylesheet" type="text/css" href="<%=path%>/resources/plugins/spiritui/themes/default/all.css"/>
-->
<link rel="stylesheet" type="text/css" href="<%=path%>/demo/css/tabs.css"/>
</head>

<body class="_body">
<!-- 遮罩层 -->
<div id="mask" style="display:none; position:absolute;vertical-align:middle;text-align:center; align:center;">
  <img align="middle" src="<%=path%>/resources/images/waiting_circle.gif"/><br/><br/>
  <span style="font-weight:bold;" id="maskTitle"></span>
</div>

<!-- 头部:悬浮 -->
<div id="topSegment" style="">
  <div id="logoImg_top"></div><!-- 头部1:logo区域 -->
  <div id="titleImg_top"></div><!-- 头部2:系统名称 -->
  <div id="commonFunc_top"><!-- 头部3:通用功能 -->
    <div class="cfLeft"></div>
    <div id="commonFuncButton">
      <div class="mainTabButton imgTabButton logout" id="logoutU" onclick="logout()">注销</div>
      <div class="mainTabButton imgTabButton refreshCache" id="refreshCacheU" onclick="refreshCache()">刷新缓存</div>
    </div>
    <div class="cfRight"></div>
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

var _topBarLeft=490;
var _topQuickFuncWidth=60;
var _topMiniHeight=26;//全屏后的头部高度，此高度也是非全屏时功能条的高度

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
  top_height: 78, //顶部高度
//  top_shadow_color: "#3e45a0",
  page_width: 0,
  page_height: 0,
  top_peg: false,
  myInit: initPosition,
  myResize: myResize,
  myScroll: myScroll
};
//  myInit: null,//initPosition,
//  myResize: null,//myResize,
//  myScroll: null//myScroll

$(function() {
  var initStr = $().spiritPageFrame(INIT_PARAM);
  if (initStr) {
    $.messager.alert("页面初始化失败", initStr, "error");
    return ;
  };
  //设置界面主功能页签
  $("#mainTab_top").spiritTabs(testBar2);
});

//-界面位置调整begin-----------------------------------------------------------------
function initPosition() {//注意，不要在此设置topSegment/mainSegment/footSegment等框架元素的宽高等，否则，页面不会自动进行调整
  //3-通用功能部分
  var absW = $("#commonFunc_top").spiritUtils("getAbsWidth");
  $("#commonFunc_top").css("left", $("#topSegment").width()-absW-5);
  //4-欢迎条
  //$("#welcom_top").css({"float":"right", "margin-top":"32px", "margin-right":"20px", "right":"2px"});
  /*
  //4-欢迎条:宽
  $("#welcom_top").css("left", $("#topSegment").css("margin-left"));
  $("#welcom_top").spiritUtils("setWidthByViewWidth", _topBarLeft);
  //4-欢迎条:高
  $("#welcom_top").spiritUtils("setHeightByViewHeight", _topMiniHeight);
  var absH = $("#welcom_top").spiritUtils("getAbsHeight");
  $("#welcom_top").css("top", $("#topSegment").height()-absH);
  */
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
  alert($("#mainTab_top").css("width"));
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


function testClk(jqMy) {
  //alert(jqMy.attr("id"));
  //alert($("#tab_m_2").css("background-color"));
  alert(getBrowserVersion());
}

var testBar = {
  id: "test", //标识
  mutualType: true, //两页标签的交互区域的处理模式，若为false，则无交互区域，用css处理交互，若为true则有交互区域，交互用图片来处理
  mutualStyle: { //交互区样式，当mutualType=true生效
    width: "10px", //交互区宽度
    firstCss:     {"border-top":"2px solid #2F4A1F","border-right":"0px solid #2F4A1F","border-left":"2px solid #2F4A1F","border-top-left-radius":"10px","border-top-right-radius":"0px","background-color":"#fff"},    //最左边未选中交互区样式，要是json格式的
    firstSelCss:  {"border-top":"2px solid #2F4A1F","border-right":"0px solid #2F4A1F","border-left":"2px solid #2F4A1F","border-top-left-radius":"10px","border-top-right-radius":"0px","background-color":"#589C8D"}, //最左边选中交互区样式，要是json格式的
    lastCss:      {"border-top":"2px solid #2F4A1F","border-right":"2px solid #2F4A1F","border-left":"0px solid #2F4A1F","border-top-left-radius":"0px","border-top-right-radius":"10px","background-color":"#fff"},    //最右边未选中交互区样式，要是json格式的
    lastSelCss:   {"border-top":"2px solid #2F4A1F","border-right":"2px solid #2F4A1F","border-left":"0px solid #2F4A1F","border-top-left-radius":"0px","border-top-right-radius":"10px","background-color":"#589C8D"}, //最右边选中交互区样式，要是json格式的
    middleLCss:   {"border-top":"2px solid #2F4A1F","border-right":"0px solid #2F4A1F","border-left":"2px solid #2F4A1F","border-top-left-radius":"10px","border-top-right-radius":"0px","background-color":"#fff"},    //中间未选中左交互区样式，要是json格式的
    middleRCss:   {"border-top":"2px solid #2F4A1F","border-right":"2px solid #2F4A1F","border-left":"0px solid #2F4A1F","border-top-left-radius":"0px","border-top-right-radius":"10px","background-color":"#fff"},    //中间未选中右交互区样式，要是json格式的
    middleSelLCss:{"border-top":"2px solid #2F4A1F","border-right":"0px solid #2F4A1F","border-left":"2px solid #2F4A1F","border-top-left-radius":"10px","border-top-right-radius":"0px","background-color":"#589C8D"}, //中间选中左交互区样式，要是json格式的
    middleSelRCss:{"border-top":"2px solid #2F4A1F","border-right":"2px solid #2F4A1F","border-left":"0px solid #2F4A1F","border-top-left-radius":"0px","border-top-right-radius":"10px","background-color":"#589C8D"}  //中间未选中右交互区样式，要是json格式的
  },
  defaultTab: { //默认的页签规则，若每个页标签不设定自己的规则，则所有页签的规则以此为准
    maxTextLength: 100,//最大文字宽度:大于此值,遮罩主
    normalCss: "", //常态css样式(未选中，鼠标未悬停)，可包括边框/字体/背景，注意，要是json格式的
    mouseOverCss: "", //鼠标悬停样式，可包括边框/字体/背景，注意，要是json格式的
    selCss: "", //选中后样式，可包括边框/字体/背景，注意，要是json格式的
  },
  tabs:[//页标签数组
    {title:"测试1", onClick:"", selected:"true"},
    {title:"测试2测试", onClick:testClk, maxTextLength:70},
    {title:"测试3", onClick:""/*, normalCss:{"background-color":"yellow"}, selCss:{"background-color":"#589C8D"}*/},
    {title:"12345", onClick:""},
    {title:"测试5", onClick:""}
  ]
};

var testBar2 = {
  id: "test", //标识
  mutualType: true, //两页标签的交互区域的处理模式，若为false，则无交互区域，用css处理交互，若为true则有交互区域，交互用图片来处理
  mutualStyle: { //交互区样式，当mutualType=true生效
    width: "10px" //交互区宽度
  },
  defaultTab: { //默认的页签规则，若每个页标签不设定自己的规则，则所有页签的规则以此为准
    maxTextLength:70,//最大文字宽度:大于此值,遮罩主
    normalCss:"", //常态css样式(未选中，鼠标未悬停)，可包括边框/字体/背景，注意，要是json格式的
    mouseOverCss:"", //鼠标悬停样式，可包括边框/字体/背景，注意，要是json格式的
    selCss:"", //选中后样式，可包括边框/字体/背景，注意，要是json格式的
  },
  tabs:[//页标签数组
    {title:"测试1", onClick:"", selected:"true"},
    {title:"测试2测试123"},
    {title:"测试3", onClick:""/*, normalCss:{"background-color":"yellow"}, selCss:{"background-color":"#589C8D"}*/},
    {title:"12345", onClick:""},
    {title:"测试5", onClick:""}
  ]
};

var BJLQ = {
  id: "bjlq", //标识
  mutualType:true, //两页标签的交互区域的处理模式，若为false，则无交互区域，用css处理交互，若为true则有交互区域，交互用图片来处理
  mutualStyle: { //交互区样式，当mutualType=true生效
    firstCss:{"background-image":"url('images/mainPage/mtabSf.jpg')"},
    firstSelCss:{"background-image":"url('images/mainPage/mtabSfSelected.jpg')"},
    lastCss:{"background-image":"url('images/mainPage/mainTabBanner.jpg')"},
    lastSelCss:{"background-image":"url('images/mainPage/mtabSSelectedR.png')", "background-color":"transparent"},
    middleLCss:{"background-image":"url('images/mainPage/mainTabBanner.jpg')"},
    middleRCss:{"background-image":"url('images/mainPage/mainTabBanner.jpg')"},
    middleSelLCss:{"background-image":"url('images/mainPage/mtabSSelectedL.png')", "background-color":"transparent"},
    middleSelRCss:{"background-image":"url('images/mainPage/mtabSSelectedR.png')", "background-color":"transparent"},
    width:"11px" //交互区宽度
  },
  defaultTab: {
    maxTextLength:70,
    normalCss:{"font-size":"12px", "font-family":"宋体", "border-top":"0", "color":"#2F4A1F", "background-image":"url('images/mainPage/mainTabBanner.jpg')", "background-color":"transparent"},
    mouseOverCss:{"font-size":"12px", "font-family":"宋体", "border-top":"0", "color":"#2F4A1F", "background-image":"url('images/mainPage/mainTabBanner.jpg')", "background-color":"transparent"},
    selCss:{"font-size":"12px", "font-family":"宋体", "border-top":"0", "color":"#fff", "background-image":"url('images/mainPage/mtabSelected.jpg')"}
  },
  tabs:[//页标签数组
    {title:"编程部署", onClick:"", selected:"true"},
    {title:"统计分析", onClick:testClk},
    {title:"测试3", onClick:""/*, normalCss:{"background-color":"yellow"}, selCss:{"background-color":"#589C8D"}*/},
    {title:"12345", onClick:""},
    {title:"测试5", onClick:""}
  ]
};
var BJLQ_2 = {
  id: "bjlq2", //标识
  mutualType:true, //两页标签的交互区域的处理模式，若为false，则无交互区域，用css处理交互，若为true则有交互区域，交互用图片来处理
  mutualStyle: { //交互区样式，当mutualType=true生效
    width:"11px" //交互区宽度
  },
  tabs:[//页标签数组
    {title:"编程部署", onClick:"", selected:"true"},
    {title:"统计分析", onClick:testClk},
    {title:"测试3", onClick:""/*, normalCss:{"background-color":"yellow"}, selCss:{"background-color":"#589C8D"}*/},
    {title:"12345", onClick:""},
    {title:"测试5", onClick:""}
  ],
  onClick: function(title, d, c) {
  }
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
</script>
</body>
</html>