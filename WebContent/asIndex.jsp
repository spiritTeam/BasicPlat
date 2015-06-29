<%@page language="java" contentType="text/html; charset=UTF-8" pageEncoding="UTF-8"%>
<%@page import="java.util.*"%>
<%
  String path = request.getContextPath();
%>
<!DOCTYPE html>
<html xmlns="http://www.w3.org/1999/xhtml">
<head>
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
<style>
#waittingArea {
  position:absolute;
  border:1px solid #BCCBDC;
  width:505px;
  height:305px;
  top:230px;
//  background-image:url(resources/images/waitting.gif);
  display:none;
}
#fileIn {
  position:absolute;
  width:650px;
  height:175px;
  top:50px;
}
#dayLogo {
  position:absolute;
  border:1px solid blue;
  width:400px;
  height:130px;
}
#upf{
  width:523px;
  height:22px;
  font:16px\/22px arial;
  margin:5px 0 0 7px;
  padding:0;
  background:#fff;
  border:0;
  outline:0;
  -webkit-appearance:none;
}
#inForm {
  position:absolute;
  padding:0px;
  margin:0px;
  width:640px;
  height:36px;
  top:135px;
}
#upIcon {
  position:absolute;
  width:25px;
  height:25px;
  border:1px solid blue;
  top:4px;
  left:7px;
  background-image:url(resources/images/uploadIcon.gif)
}
#su {
  width:100px;
  height:35px;
  display:inline-block;
  border:1px solid #36B148;
  border-radius:0 3px 3px 0;
  background-image:-webkit-linear-gradient(bottom, #64CD4F 31%, #43D454);
  background-color:#399D27;
  font:15px 宋体 Tahoma, Helvetica, Arial, 'Microsoft YaHei', sans-serif;
  color:#fff;
  margin-left:-7px;
}
#upfs {
  width:495px;
  height:33px;
  display:inline-block;
  border:1px solid #36B148;
  border-radius:3px 0 0 3px;
  padding-left:35px;
  font:12px 宋体 Tahoma, Helvetica, Arial, 'Microsoft YaHei', sans-serif;
  color:#bfbfbf;
}
#mask {
  z-index: 1000;
  top:52px;
  left:170px;
  width:400px;
  height:130px;
  border:1px solid red;
}
</style>
</head>

<body class="_body">
<!-- 遮罩层 -->
<div id="mask" style="display:none; position:absolute;vertical-align:middle;text-align:center; align:center;">
  <div style="background-image:resources/images/waiting_circle.gif; width:85px; height:81px;"></div>
  <span style="font-weight:bold;" id="maskTitle">正在分析，请稍候...</span>
</div>

<!-- 头部:悬浮 -->
<div id="topSegment">数据是关键！！ 登录|注册 || 收藏</div>

<!-- 脚部:悬浮 -->
<div id="footSegment"></div>

<!-- 实际功能区中部 -->
<div id="mainSegment">
<div id="fileIn">
  <div id="dayLogo"></div>
  <div id="inForm"><form method="post" action="/abc/uploadtest.do" enctype="multipart/form-data" id="afUpload" target="tframe">
    <input id="upf" name="upf" type=file style="display:none;" onchange="showFileInfo()"/>
    <div id="upIcon" onclick="upIcon_clk();"></div>
    <input id="upfs" name="upfs" type="text" readonly="readonly" onclick="upfs_clk();"/>
    <input id="su" type="button" value="分析一下" onclick="uploadF();"/>
  </form></div>
</div>
<!-- 等待提示区 -->
<div id="waittingArea">
</div>
</div>
<iframe id="tframe" name="tframe" bordercolor=red frameborder="yes" border=1 width="600" height="200" style="width:600px;heigth:200px; boder:1px solid red;display:none;"></iframe>

<script>
//提示信息
var _promptMessage="点击选择分析的文件";
var analysizeing=false;

//主窗口参数
var INIT_PARAM = {
  //页面中所用到的元素的id，只用到三个Div，另，这三个div应在body层
  pageObjs: {
    topId: "topSegment", //头部Id
    mainId: "mainSegment", //主体Id
    footId: "footSegment" //尾部Id
  },
  page_width: 0,
  page_height: 0,

  top_height: 30, //顶部高度
  top_peg: true,

  foot_height: 30, //脚部高度
  foot_peg: false, //是否钉住脚部在底端。false：脚部随垂直滚动条移动(浮动)；true：脚部钉在底端

  win_min_width: 800, //页面最小的高度。当窗口高度小于这个值，不对界面位置及尺寸进行调整。主体部分宽度也照此设置
  win_min_height: 580, //页面最小的高度。当窗口高度小于这个值，不对界面位置及尺寸进行调整。主体部分高度也照此设置
  myInit: initPosition,
  myResize: myResize
};

//点击大的输入框
function upfs_clk() {
  if ($("#upfs").val()==_promptMessage) $("#upf").click();
};
//点击上传按钮
function upIcon_clk() {
  $("#upf").click();
};
//显示选择的文件名称
function showFileInfo() {
  $("#upfs").val($("#upf").val());
};
//文件上传
function uploadF() {
  if ($("#upfs").val()==_promptMessage) {
  	 $("#upf").click();
  	return;
  }
  try {
    var form = $('#afUpload');
    $(form).attr('action', _PATH+'/uploadtest.do');
    $(form).attr('method', 'POST');
    $(form).attr('target', 'tframe');
    if (form.encoding) form.encoding = 'multipart/form-data';    
    else form.enctype = 'multipart/form-data';
    $(form).submit();
    analysizeing=true;//开始分析
    $("#waittingArea").fadeIn(200);//等待提示区
  } catch(e) {
  	$.messager.alert("文件上传失败", e, "error");
  }
}

//主函数
$(function() {
  var initStr = $().spiritPageFrame(INIT_PARAM);
  if (initStr) {
    $.messager.alert("页面初始化失败", initStr, "error");
    return ;
  };
  $("#upfs").val(_promptMessage);
  $("#su").mouseover(function(){
  	if ($("#upfs").val()!=_promptMessage) {
      $(this).attr("title", "");
      $(this).css({"color":"yellow", "background-color":"#81FC6A"});
  	} else {
      $(this).attr("title", "请先上传文件");
  	}
  }).mouseout(function(){
    $(this).css({"color":"white", "background-color":"#36B148"});
  });
});
//初始化界面
function initPosition() {//注意，不要在此设置topSegment/mainSegment/footSegment等框架元素的宽高等，否则，页面不会自动进行调整
  //控制中心区域图片
  var left=(parseFloat($("#mainSegment").width())-parseFloat($("#fileIn").width()))/2;
  $("#fileIn").css({"left": left});
  left = (parseFloat($("#fileIn").width())-parseFloat($("#dayLogo").width()))/2;
  $("#dayLogo").css({"left": left});
  left = (parseFloat($("#fileIn").width())-parseFloat($("#inForm").width()))/2;
  $("#inForm").css({"left": left});
  //遮罩层
//  left = (parseFloat($("#mainSegment").width())-parseFloat($("#mask").width()))/2;
//  $("#mask").css({"left": left+2});
  //等待提示区
  left = (parseFloat($("#mainSegment").width())-parseFloat($("#waittingArea").width()))/2;
  $("#waittingArea").css({"left": left});
};
//当界面尺寸改变
function myResize() {
  if (INIT_PARAM.page_width==0) {
    //控制中心区域图片
    var left=(parseFloat($("#mainSegment").width())-parseFloat($("#fileIn").width()))/2;
    $("#fileIn").css({"left": left});
    //遮罩层
//    left = (parseFloat($("#mainSegment").width())-parseFloat($("#mask").width()))/2;
//    $("#mask").css({"left": left+2});
    //等待提示区
    left = (parseFloat($("#mainSegment").width())-parseFloat($("#waittingArea").width()))/2;
    $("#waittingArea").css({"left": left});
  }
};
</script>
</body>
</html>