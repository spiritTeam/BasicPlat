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
<link rel="stylesheet" type="text/css" href="<%=path%>/resources/css/mainPage.css"/>
<style type="text/css">
</style>
</head>
<body class="_body">
<!-- 遮罩层 -->
<div id="mask" style="display:none; position:absolute;vertical-align:middle;text-align:center; align:center;">
  <img align="middle" src="<%=path%>/resources/images/waiting_circle.gif"/><br/><br/>
  <span style="font-weight:bold;" id="maskTitle"></span>
</div>

<!-- 头部:悬浮 -->
<div id="topSegment" class="hoverArea">
<div id="aa">AABBCCDD</div>
<span onclick="test()">TEST</span>
</div>
<!-- 脚部:悬浮 -->
<div id="footSegment" class="hoverArea" style="text-align:right;">just test</div>

<div id="main"><!-- 主体 -->
  <div id="_top" style="background-color:white;"><span onclick="test()">TEST</span></div><!-- 头部:占位，被悬浮盖住不显示 -->
  <div id="mainSegment" style=""><!-- 实际功能区 -->
  </div>
  <div id="_foot" style="background-color:blue;"></div><!-- 脚部:占位，被悬浮盖住不显示 -->
</div>

<script>
function test() {
  alert("\t\t   宽     高\n"
    +"INIT.设定\t\t="+(INIT_PARAM.page_width==0?"自动":(INIT_PARAM.page_width<0?"不处理":INIT_PARAM.page_width))
    +":"+(INIT_PARAM.page_height==0?"自动":(INIT_PARAM.page_height<0?"不处理":INIT_PARAM.page_height))
    +"\nINIT.min\t\t="+INIT_PARAM.win_min_width+":"+INIT_PARAM.win_min_height
    +"\nwin\t\t="+$(window).width()+":"+$(window).height()
    +"\nWwin\t\t="+wWidth()+":"+wHeight()
    +"\nmain\t\t="+$("#main").width()+":"+$("#main").height()
    +"\ndocument\t="+$(document).width()+":"+$(document).height()
    +"\nscroll偏移\t\t="+$(document).scrollLeft()+":"+$(document).scrollTop()
    +"\n上下偏移\t\t="+caculateWidthOffSet()+":"+caculateHeightOffSet()
    +"\n滚动条宽度\t="+getWScrollbarWidth()+":"+getHScrollbarWidth()
    +"\n悬浮\t\t=顶部-"+INIT_PARAM.top_peg+(INIT_PARAM.top_peg?"固定":"浮动")+":脚部-"+INIT_PARAM.foot_peg+(INIT_PARAM.foot_peg?"固定":"浮动")
    +"\n===================================="
    +"\nfootTop="+parseFloat($("#footSegment").css("top"))
    +"\n"+_topFlag4foot+":"+_bottomFlag4foot+":"+(parseFloat($(document).scrollTop())+parseFloat($(window).height()))
  );
}
/**
 * 初始化参数
 */
var INIT_PARAM = {
  page_width: 0, //主页面的宽度。<0：宽度不控制；0：宽度自适应；>0：宽度值，页面定宽
  page_height: 4000, //主页面的高度。<0：高度不控制；0：高度自适应；>0：高度值，页面定高

  win_min_width: 800, //页面最小的高度。当窗口高度小于这个值，不对界面位置及尺寸进行调整。主体部分宽度也照此设置
  win_min_height: 600, //页面最小的高度。当窗口高度小于这个值，不对界面位置及尺寸进行调整。主体部分高度也照此设置

  top_height: 120, //顶部高度
  top_peg: true, //是否钉住头部在顶端。false：顶部随垂直滚动条移动(浮动)；true：顶部钉在顶端
  foot_height: 40, //脚部高度
  foot_peg: true, //是否钉住脚部在底端。false：脚部随垂直滚动条移动(浮动)；true：脚部钉在底端

  iframe_height_flag: 1 //具体功能区域（可能是整个中部，也可能是带左侧导航的中部）的iframe高度标志。1：iframe高度与框架匹配；非1：框架高度适应iframe内部高度(反向适应)
};

var _hScrollbarWidth = 0; //纵向滚动条宽度
var _wScrollbarWidth = 0; //横向滚动条宽度
var _topFlag4foot = -1; //脚部，上标志
var _bottomFlag4foot = -1; //脚部，下标志

$(function() {
  //页面调整
  initPosition();
  $(window).resize(_resizeTimeout);
  $(window).scroll(scrollPositioin);
//  $("#aa").spiretTabs({});
});

//-adjustPagePosition begin:以下函数为界面调整-----------------------------------------------------------------
/**
 * 界面位置初始化
 */
function initPosition() {
  $("#main").css({"border":"3px solid red", "background-color":"yellow"});
  //0-预处理
  if (_hScrollbarWidth==0) _hScrollbarWidth=getHScrollbarWidth();//纵向滚动条
  if (_wScrollbarWidth==0) _wScrollbarWidth=getWScrollbarWidth();//横向滚动条
  //1-调整中间主体
  $("#main").css({
    "left": getLeft(), "width": getWidth(), //X轴，宽
    "top": $("body").css("margin-top"), "height": getHeight() //Y轴，高
  });
  //1.1-若出现滚动条，进行处理
  var rh=parseFloat($("#main").css("height")), rw=parseFloat($("#main").css("width"));
  if (INIT_PARAM.page_width==0)  {//若为自适应宽
    if ((rh+caculateHeightOffSet())>wHeight()) rw -= _hScrollbarWidth;//若出现纵向滚动条，则宽度为页面宽度减去滚动条宽度
    if (rw>INIT_PARAM.win_min_width) $("#main").css({"width": rw});
  }
  if (INIT_PARAM.page_height==0) {//若为自适应高
    if ((rw+caculateWidthOffSet())>wWidth()) rh -= _wScrollbarWidth;//若出现横向滚动条，则宽度为页面宽度减去滚动条宽度
    if (rh>INIT_PARAM.win_min_height) $("#main").css({"height": rh});
  }
  //2-调整顶部
  $("#topSegment").css({
    "position": INIT_PARAM.top_peg?"absolute":"fixed", //形式
    "left": parseFloat($("#main").css("left"))+parseFloat($("#main").css("margin-left")), //X轴，宽
    "width": getViewWidth("topSegment", "main"),
    "top": parseFloat($("body").css("margin-top"))+parseFloat($("#main").css("margin-top")), //Y轴，高
    "height":parseFloat(INIT_PARAM.top_height)
  });
  //3-调整脚部
  $("#footSegment").css({
    "position": INIT_PARAM.foot_peg?"absolute":"fixed", //形式
    "left": parseFloat($("#main").css("left"))+parseFloat($("#main").css("margin-left")), //X轴，宽
    "width": getViewWidth("footSegment", "main"),
    "height":parseFloat(INIT_PARAM.foot_height) //Y轴，高
  });
  //4-中间分体部分
  var _topHeight = getViewHeight("_top", "topSegment")-parseFloat($("#main").css("padding-top"))-parseFloat($("#main").css("border-top-width"));
  var _footHeight = getViewHeight("_foot", "footSegment")-parseFloat($("#main").css("padding-bottom"))-parseFloat($("#main").css("border-bottom-width"));
  $("#_top").css({"width":$("#main").css("width"), "height": _topHeight});
  $("#_foot").css({"width":$("#main").css("width"), "height": _footHeight});
  $("#mainSegment").css({"width":$("#main").css("width"), "height": parseFloat($("#main").css("height"))-parseFloat(_topHeight)-parseFloat(_footHeight)});
  //5-调整脚部top
  $("#footSegment").css({"top":$("#_foot")[0].offsetTop+parseFloat($("body").css("margin-top"))+parseFloat($("body").css("padding-top"))+parseFloat($("#main").css("margin-top"))+parseFloat($("#main").css("border-top-width"))-parseFloat($("#footSegment").css("margin-top"))});
  if (!INIT_PARAM.foot_peg) {//浮动脚部
    var _offsetHeight = $(document).scrollTop()+$(window).height();//窗口绝对高度
    if (INIT_PARAM.page_height>0) {
      if ((parseFloat($("body").css("margin-top"))+parseFloat($("#main").css("margin-top"))+parseFloat($("#main").css("border-top-width"))+parseFloat($("#main").css("padding-top"))
          +parseFloat($("#main").css("height"))+parseFloat($("#main").css("padding-bottom"))+parseFloat($("#main").css("border-bottom-width")))<_offsetHeight) return;
    }
    if (_topFlag4foot==-1) _topFlag4foot = parseFloat($("body").css("margin-top"))+parseFloat(INIT_PARAM.win_min_height)
      +parseFloat($("#main").css("margin-top"))+parseFloat($("#main").css("border-top-width"))+parseFloat($("#main").css("padding-top"))
      +parseFloat($("#main").css("border-bottom-width"))+parseFloat($("#main").css("padding-bottom"))
      -parseFloat($("#footSegment").css("height"))-parseFloat($("#footSegment").css("border-bottom-width"))-parseFloat($("#footSegment").css("padding-bottom"))
      -parseFloat($("#footSegment").css("margin-top"))-parseFloat($("#footSegment").css("padding-top"))-parseFloat($("#footSegment").css("border-top-width"));
    if (_bottomFlag4foot==-1) _bottomFlag4foot = _topFlag4foot
      +parseFloat($("#footSegment").css("margin-top"))+parseFloat($("#footSegment").css("border-top-width"))+parseFloat($("#footSegment").css("padding-top"))
      +parseFloat($("#footSegment").css("height"))+parseFloat($("#footSegment").css("padding-bottom"))+parseFloat($("#footSegment").css("border-bottom-width"));
    var _staticTop4foot=$(window).height()-parseFloat($("#footSegment").css("height"))
      -parseFloat($("#footSegment").css("margin-top"))-parseFloat($("#footSegment").css("padding-top"))-parseFloat($("#footSegment").css("border-top-width"))
      -parseFloat($("#footSegment").css("padding-bottom"))-parseFloat($("#footSegment").css("border-bottom-width"));
    var _newTop=0;
    if (_offsetHeight<=_bottomFlag4foot) _newTop = _topFlag4foot; //小于尺度+//高度内
    else _newTop = _staticTop4foot;//大于尺度
    $("#footSegment").css({"top": _newTop});
  }
}
/**
 * 界面位置调整
 */
function resizePosition() {
  //1-调整中间主体
  $("#main").css({
    "left": getLeft(), "width": getWidth(), //X轴，宽
    "height": getHeight() //Y轴，高
  });
  //1.1-若出现滚动条，进行处理
  var rh=parseFloat($("#main").css("height")), rw=parseFloat($("#main").css("width"));
  if (INIT_PARAM.page_width==0)  {//若为自适应宽
    if ((rh+caculateHeightOffSet())>wHeight()) rw -= _hScrollbarWidth;//若出现纵向滚动条，则宽度为页面宽度减去滚动条宽度
    if (rw>INIT_PARAM.win_min_width) $("#main").css({"width": rw});
  }
  if (INIT_PARAM.page_height==0) {//若为自适应高
    if ((rw+caculateWidthOffSet())>wWidth()) rh -= _wScrollbarWidth;//若出现横向滚动条，则宽度为页面宽度减去滚动条宽度
    if (rh>INIT_PARAM.win_min_height) $("#main").css({"height": rh});
  }
  //2-调整顶部
  $("#topSegment").css({
    "width": getViewWidth("topSegment", "main"),
    "left": parseFloat($("#main").css("left"))+parseFloat($("#main").css("margin-left"))-$(document).scrollLeft()
  });
  //3-调整脚部
  $("#footSegment").css({
    "width": getViewWidth("footSegment", "main"),
    "left": parseFloat($("#main").css("left"))+parseFloat($("#main").css("margin-left"))-$(document).scrollLeft()
  });
  if (INIT_PARAM.foot_peg) $("#footSegment").css({"left": parseFloat($("#main").css("left"))+parseFloat($("#main").css("margin-left"))  }); //钉住脚部
  //4-中间分体部分
  $("#_top").css({"width":$("#main").css("width")});
  $("#_foot").css({"width":$("#main").css("width")});
  $("#mainSegment").css({"width":$("#main").css("width"), "height": parseFloat($("#main").css("height"))-parseFloat($("#_top").css("height"))-parseFloat($("#_foot").css("height"))});
  //5-调整脚部top
  $("#footSegment").css({"top":$("#_foot")[0].offsetTop+parseFloat($("body").css("margin-top"))+parseFloat($("body").css("padding-top"))+parseFloat($("#main").css("margin-top"))+parseFloat($("#main").css("border-top-width"))-parseFloat($("#footSegment").css("margin-top"))});
  if (!INIT_PARAM.foot_peg) {//浮动脚部
    var _offsetHeight = $(document).scrollTop()+$(window).height();//窗口绝对高度
    if (INIT_PARAM.page_height>0) {
      if ((parseFloat($("body").css("margin-top"))+parseFloat($("#main").css("margin-top"))+parseFloat($("#main").css("border-top-width"))+parseFloat($("#main").css("padding-top"))
          +parseFloat($("#main").css("height"))+parseFloat($("#main").css("padding-bottom"))+parseFloat($("#main").css("border-bottom-width")))<_offsetHeight) return;
    }
    if (_topFlag4foot==-1) _topFlag4foot = parseFloat($("body").css("margin-top"))+parseFloat(INIT_PARAM.win_min_height)
      +parseFloat($("#main").css("margin-top"))+parseFloat($("#main").css("border-top-width"))+parseFloat($("#main").css("padding-top"))
      +parseFloat($("#main").css("border-bottom-width"))+parseFloat($("#main").css("padding-bottom"))
      -parseFloat($("#footSegment").css("height"))-parseFloat($("#footSegment").css("border-bottom-width"))-parseFloat($("#footSegment").css("padding-bottom"))
      -parseFloat($("#footSegment").css("margin-top"))-parseFloat($("#footSegment").css("padding-top"))-parseFloat($("#footSegment").css("border-top-width"));
    if (_bottomFlag4foot==-1) _bottomFlag4foot = _topFlag4foot
      +parseFloat($("#footSegment").css("margin-top"))+parseFloat($("#footSegment").css("border-top-width"))+parseFloat($("#footSegment").css("padding-top"))
      +parseFloat($("#footSegment").css("height"))+parseFloat($("#footSegment").css("padding-bottom"))+parseFloat($("#footSegment").css("border-bottom-width"));
    var _staticTop4foot=$(window).height()-parseFloat($("#footSegment").css("height"))
      -parseFloat($("#footSegment").css("margin-top"))-parseFloat($("#footSegment").css("padding-top"))-parseFloat($("#footSegment").css("border-top-width"))
      -parseFloat($("#footSegment").css("padding-bottom"))-parseFloat($("#footSegment").css("border-bottom-width"));
    var _newTop=0;
    if (_offsetHeight<=_topFlag4foot) _newTop = _topFlag4foot; //小于尺度
    else if (_offsetHeight>_topFlag4foot&&_offsetHeight<=_bottomFlag4foot) _newTop = _topFlag4foot-$(document).scrollTop(); //高度内
    else _newTop = _staticTop4foot;//大于尺度
    $("#footSegment").css({"top": _newTop});
  }
}
/**
 * 滚动条调整
 */
function scrollPositioin() {
  //1-调整顶部
  if (!INIT_PARAM.top_peg) {
    //Y轴方向
    var _top = parseFloat($("body").css("margin-top"))+parseFloat($("#main").css("margin-top"))-$(document).scrollTop();
    _top = _top>0?_top:0;
    $("#topSegment").css({"top": _top});
  	//X轴方向
    $("#topSegment").css({"left": parseFloat($("#main").css("left"))+parseFloat($("#main").css("margin-left"))-$(document).scrollLeft()});
  }
  //2-调整脚部
  if (!INIT_PARAM.foot_peg) {
    //X轴方向
    $("#footSegment").css({"left": parseFloat($("#main").css("left"))+parseFloat($("#main").css("margin-left"))-$(document).scrollLeft()});
    //Y轴方向
    var _offsetHeight = $(document).scrollTop()+$(window).height();//窗口绝对高度
    if (INIT_PARAM.page_height>0) {
      if ((parseFloat($("body").css("margin-top"))+parseFloat($("#main").css("margin-top"))+parseFloat($("#main").css("border-top-width"))+parseFloat($("#main").css("padding-top"))
          +parseFloat($("#main").css("height"))+parseFloat($("#main").css("padding-bottom"))+parseFloat($("#main").css("border-bottom-width")))<_offsetHeight) return;
    }
    if (_topFlag4foot==-1) _topFlag4foot = parseFloat($("body").css("margin-top"))+parseFloat(INIT_PARAM.win_min_height)
      +parseFloat($("#main").css("margin-top"))+parseFloat($("#main").css("border-top-width"))+parseFloat($("#main").css("padding-top"))
      +parseFloat($("#main").css("border-bottom-width"))+parseFloat($("#main").css("padding-bottom"))
      -parseFloat($("#footSegment").css("height"))-parseFloat($("#footSegment").css("border-bottom-width"))-parseFloat($("#footSegment").css("padding-bottom"))
      -parseFloat($("#footSegment").css("margin-top"))-parseFloat($("#footSegment").css("padding-top"))-parseFloat($("#footSegment").css("border-top-width"));
    if (_bottomFlag4foot==-1) _bottomFlag4foot = _topFlag4foot
      +parseFloat($("#footSegment").css("margin-top"))+parseFloat($("#footSegment").css("border-top-width"))+parseFloat($("#footSegment").css("padding-top"))
      +parseFloat($("#footSegment").css("height"))+parseFloat($("#footSegment").css("padding-bottom"))+parseFloat($("#footSegment").css("border-bottom-width"))
    var _staticTop4foot=$(window).height()-parseFloat($("#footSegment").css("height"))
      -parseFloat($("#footSegment").css("margin-top"))-parseFloat($("#footSegment").css("padding-top"))-parseFloat($("#footSegment").css("border-top-width"))
      -parseFloat($("#footSegment").css("padding-bottom"))-parseFloat($("#footSegment").css("border-bottom-width"));
    var _newTop=0;
    if (_offsetHeight<=_topFlag4foot) _newTop = _topFlag4foot; //小于尺度
    else if (_offsetHeight>_topFlag4foot&&_offsetHeight<=_bottomFlag4foot) _newTop = _topFlag4foot-$(document).scrollTop(); //高度内
    else _newTop = _staticTop4foot;//大于尺度
    $("#footSegment").css({"top": _newTop});
  }
}
/**
 * 200毫秒后调整页面位置，为其中的控件调整位置准备时间
 */
function _resizeTimeout() {
  setTimeout("resizePosition()",100);
}
//-adjustPagePosition end:以上函数为界面调整-----------------------------------------------------------------

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

//-以下函数为通用函数-----------------------------------------------------------------
/**
 * 获取纵向滚动条宽度
 */
function getHScrollbarWidth() {
  var oldOverflowY=$("html").css("overflow-y");
  $("html").css("overflow-y", "hidden");
  var barWidth=$(window).width();
  $("html").css("overflow-y", "scroll");
  barWidth -= $(window).width();
  $("html").css("overflow-y", oldOverflowY);
  return barWidth;
}
/**
 * 获取横向滚动条宽度
 */
function getWScrollbarWidth() {
  var oldOverflowX=$("html").css("overflow-x");
  $("html").css("overflow-x", "hidden");
  var barHeight=$(window).height();
  $("html").css("overflow-x", "scroll");
  barHeight -= $(window).height();
  $("html").css("overflow-x", oldOverflowX);
  return barHeight;
}
/**
 * 计算宽度偏移量
 */
function caculateWidthOffSet() {
  return parseFloat($("body").css("margin-left"))
    +parseFloat($("#main").css("margin-left"))//+parseFloat($("#main").css("margin-right"))
    +parseFloat($("#main").css("padding-left"))+parseFloat($("#main").css("padding-right"))
    +parseFloat($("#main").css("border-left-width"))+parseFloat($("#main").css("border-right-width"));
}
/**
 * 计算高度偏移量
 */
function caculateHeightOffSet() {
  return parseFloat($("body").css("margin-top"))
    +parseFloat($("#main").css("margin-top"))//+parseFloat($("#main").css("margin-bottom"))
    +parseFloat($("#main").css("padding-top"))+parseFloat($("#main").css("padding-bottom"))
    +parseFloat($("#main").css("border-top-width"))+parseFloat($("#main").css("border-bottom-width"));
}
/**
 * 得到窗口的绝对高度和宽度，包括滚动条
 */
function wWidth() {
  if (_hScrollbarWidth==0) _hScrollbarWidth=getHScrollbarWidth();//纵向滚动条
  return ($(window).height()<$(document).height()&&($("html").css("overflow-y")=="auto"))?($(window).width()+_hScrollbarWidth):$(window).width();
}
function wHeight() {
  if (_wScrollbarWidth==0) _wScrollbarWidth=getWScrollbarWidth();//横向滚动条
  return ($(window).width()<$(document).width()&&($("html").css("overflow-x")=="auto"))?($(window).height()+_wScrollbarWidth):$(window).height();
}
/**
 * 计算左边距
 */
function getLeft() {
  var retLeft = parseFloat($("body").css("margin-left"));
  if (INIT_PARAM.page_width>0) {//若指定宽度
    if ((wWidth()-caculateWidthOffSet())>INIT_PARAM.page_width) {
      retLeft = (wWidth()-caculateWidthOffSet()-INIT_PARAM.page_width)/2;
    };
  };
  return retLeft;
}
/**
 * 计算宽度，根据是否自动定义宽度，是否有垂直滚动条等计算
 */
function getWidth() {
  if (INIT_PARAM.page_width>0) return INIT_PARAM.page_width;//若指定宽度，返回定宽
  var retWidth = $("#main").width();//不控制
  if (INIT_PARAM.page_width==0) {//若为自适应
    retWidth = wWidth()-caculateWidthOffSet();//主体宽度=窗口宽度-宽度偏移量
    if (retWidth<INIT_PARAM.win_min_width) retWidth=INIT_PARAM.win_min_width;
  };
  return retWidth;
}
/**
 * 计算高度，根据是否自动定义高度，是否小雨最小值，是否有垂直滚动条等计算
 */
function getHeight() {
  if (INIT_PARAM.page_height>0) return INIT_PARAM.page_height;//若指定宽度，返回定宽
  var retHeight = $("#main").height();//不控制
  if (INIT_PARAM.page_height==0) {//若为自适应
    retHeight = wHeight()-caculateHeightOffSet();//主体宽度=窗口宽度-宽度偏移量
    if (retHeight<INIT_PARAM.win_min_height) retHeight=INIT_PARAM.win_min_height;
  };
  return retHeight;
}
/**
 * 使两个对象的可见宽度相同。
 * viewObjId：显示对象Id
 * targetObjId:被比较目标对象Id
 * return 显示对象的宽度
 */
function getViewWidth(viewObjId, targetObjId) {
  var _view = $("#"+viewObjId);
  var _target = $("#"+targetObjId);
  return (parseFloat(_target.css("width"))+parseFloat(_target.css("padding-left"))+parseFloat(_target.css("padding-right"))+parseFloat(_target.css("border-left-width"))+parseFloat(_target.css("border-right-width")))
    -(parseFloat(_view.css("padding-left"))+parseFloat(_view.css("padding-right"))+parseFloat(_view.css("border-left-width"))+parseFloat(_view.css("border-right-width")));
}
/**
 * 使两个对象的可见高度相同。
 * viewObjId：显示对象Id
 * targetObjId:被比较目标对象Id
 * return 显示对象的高度
 */
function getViewHeight(viewObjId, targetObjId) {
  var _view = $("#"+viewObjId);
  var _target = $("#"+targetObjId);
  return (parseFloat(_target.css("height"))+parseFloat(_target.css("padding-top"))+parseFloat(_target.css("padding-bottom"))+parseFloat(_target.css("border-top-width"))+parseFloat(_target.css("border-bottom-width")))
    -(parseFloat(_view.css("padding-top"))+parseFloat(_view.css("padding-bottom"))+parseFloat(_view.css("border-top-width"))+parseFloat(_view.css("border-bottom-width")));
}
</script>
</body>
</html>