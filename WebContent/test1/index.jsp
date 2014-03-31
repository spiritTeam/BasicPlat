<%@page language="java" contentType="text/html; charset=UTF-8" pageEncoding="UTF-8"%>
<%@page import="com.gmteam.framework.IConstants"%>
<%
  String path = request.getContextPath();
  //String username = ((Object)session.getAttribute(IConstants.SESSION_USER)).getV_user_xm();
%>
<!DOCTYPE html>
<html>
<head>
<title><%=IConstants.PLATFORM_NAME%></title>
<meta charset="UTF-8">
<meta http-equiv="pragma" content="no-cache"/>
<meta http-equiv="cache-control" content="no-cache"/>
<meta http-equiv="expires" content="0"/>
<jsp:include page="/common/sysInclude.jsp" flush="true"/>
<script type="text/javascript" src="<%=path%>/resources/js/mainPage.utils.js"></script>
<link rel="stylesheet" type="text/css" href="<%=path%>/test1/mainPage.gis.css"/>
<style type="text/css">
/* 页脚暂时去掉 */
#foot {
  height:25px;
  overflow:hidden;
  background:#F7F7F7;
  border-top:1px solid #285D91;
}
</style>
</head>
<body>
<!-- 遮罩层 -->
<div id="mask" style="display:none">
  <table id="maskContent" style="position:relative;"><tr><td style="vertical-align:middle;align:center"><center>
    <img align="middle" src="<%=path%>/test1/images/waiting_circle.gif"/><br/><br/>
    <span style="font-weight:bold" id="maskTitle">请稍候，正在读取用户权限...</span>
  </center></td></tr></table>
</div>

<div id="main" style="width:400px;height:300px;display:none;">
  <div id="mainLayout" class="easyui-layout" data-options="fit:true">
    <div id="top" data-options="region:'north',border:false">
      <div id="logoImg"></div>
      <div id="titleImg"></div>
      <div id="commonFunc">
        <div class="cfRight"></div>
        <div id="commonFuncButton">
          <div class="mainTabButton imgTabButton commButton logout" id="logoutU" onclick="logout()">注销</div>
          <div class="mainTabButton imgTabButton commButton refreshCache" id="refreshCacheU" onclick="refreshCache()">刷新缓存</div>
        </div>
        <div class="cfLeft"></div>
      </div>
     <!--   <div id="welcom"><span class="username"></span>&nbsp;您好！欢迎您使用本系统</div>-->
      <div id="mainTab">
        <div class="mainTabButton imgTabButton fullscrenn" id="fullscreen">全屏</div>
        <!-- <div class="mainTabButton imgTabButton fullscrenn" id="test" onclick="test()">测试，准备删除</div>  -->
        <div class="mainTabButton imgTabButton logout" id="logoutD" onclick="logout()" style="display:none;">注销</div>
        <div class="mainTabButton imgTabButton refreshCache" id="refreshCacheD" onclick="refreshCache()" style="display:none;">刷新缓存</div>
        <div id="vline"></div>
      </div>
    </div>
    <div id="center" data-options="region:'center',border:false">
      <div id="west">
        <div id="leftModule" class="easyui-accordion" data-options="border:false,fit:true"></div>
      </div>
      <div id="gisMain">
        <iframe id="gisFrame" name="gisFrame" frameborder=0 scrolling=0 src="" style="border:0px; width:100%; height:100%"></iframe>
      </div>
    </div>
  </div>
</div>
<div id="splitbutton" style="display:none;" title="收起左栏"></div>
</body>
<script>
//singleSetTimeOut(function(){alert('ssss')}, 100);
var setTimeoutHandle = null;
var westWidth=300, minWidth=640, minHeight=480;
var westExpand=true, isFullScreen=false, _westExpand=true;//_westExpand是点击全屏时westExpand的状态
var userAuthData=null;
var curTab=null, curModule=null, curMframe=null;
var hasRefreshCacheAuth = false;
var moduleArray=[];//模块列表

$(function(){
  resizeMask();
  _resizeTimeout();
  $(window).resize(_resizeTimeout);
  $("#mask").show();

  var url="<%=path%>/toLogin.do";
  $.ajax({type:"post", async:true, url:url, data:null, dataType:"json",
    success: function(json) {
      if (json.type==1) {
        var authData=json.data;
        if (!authData) {
          $.messager.alert("提示", "您没有操作本系统的任何权限，请联系管理员！<br/>现返回登录页面。", "info", function(){
            window.location.href="<%=path%>/common/login.jsp?noAuth";
          });
          return;
        }
        userAuthData = authData;
        init(authData);
      } else {
        $.messager.alert("错误", "获取用户权限失败："+json.data+"！<br/>返回登录页面。", "error", function(){
          window.location.href="<%=path%>/common/login.jsp?noAuth";
        });
      }
    },
    error: function(errorData) {
      $.messager.alert("错误", "获取用户权限异常："+(errorData?errorData.responseText:"")+"！<br/>返回登录页面。", "error", function(){
        window.location.href="<%=path%>/common/login.jsp?noAuth";
      });
    }
  });
});

function _resizeTimeout() {
  setTimeout("onResizeWin()",200);
  setTimeout("onResizeWin()",400);
}
function onResizeWin() {
  var newHeight =($(window).height()>minHeight?$(window).height():minHeight);
  var newWidth = ($(window).width()>minWidth?$(window).width():minWidth);
  $("#main").css({
    "width": (newWidth),
    "height": (newHeight)
  });
  newHeight = (newHeight-$("#top").height());
  $("#west").css("height", newHeight);
  $("#gisMain").css("height", newHeight);
  $("#gisFrame").css({
    "width": newWidth-(isFullScreen?0:(westExpand?westWidth:0))-5,
    "height": newHeight
  });
  $("#mainLayout").layout({"fit":"true"});
  //图标
  $("#splitbutton").css({
    "top": ((newHeight-$("#splitbutton").height())/2)+$("#top").height(),
    "left": westExpand?(westWidth+4):4
  });
  //遮罩
  resizeMask();
  //功能按钮
  $("#commonFunc").css("left", newWidth-((hasRefreshCacheAuth?170:86)+20));
  $("#commonFuncButton").css("left", 13);
  $(".cfRight").css("left", 13+(hasRefreshCacheAuth?144:60));
}
//调整遮罩层大小
function resizeMask() {
  $("#mask").css({
    "top": "0px",
    "left": "0px",
    "width": $(window).width(),
    "height": $(window).height()
  });
  $("#maskContent").css({
    "top": ($(window).height()-$("#maskContent").height())/2,
    "left": ($(window).width()-$("#maskContent").width())/2
  });
}
function init(authTreeData) {
  //设置显示效果
  $("#splitbutton").mouseover(function(){
    $(this).css({
      "background-image":"url('"+(westExpand?"<%=path%>/test1/images/mainPage/splitbutton1_.gif":"<%=path%>/test1/images/mainPage/splitbutton2_.gif")+"')"
    });
  }).mouseout(function(){
    $(this).css({
      "background-image":"url('"+(westExpand?"<%=path%>/test1/images/mainPage/splitbutton1.gif":"<%=path%>/test1/images/mainPage/splitbutton2.gif")+"')"
    });
  });
  $(".mainTabButton").mouseover(function(){
    $(this).css("border", "1px solid #DADADA").css("margin-top", "3px").css("margin-right", "6px").css("color", "blue");
  }).mouseout(function(){
    $(this).css("border", "0px solid #DADADA").css("margin-top", "4px").css("margin-right", "8px").css("color", "black");;
  });
  //设置操作
  $("#splitbutton").click(function(){
    if (westExpand) {
      $("#splitbutton").attr("title","显示左栏");
      $("#west").hide();
      $("#gisMain").css("margin-left","0px");
      $("#gisFrame").css("width",$("#gisFrame").width()+westWidth);
      $("#splitbutton").css("left","4px");
    } else {
      $("#splitbutton").attr("title","收起左栏");
      $("#west").show();
      $("#gisMain").css("margin-left",westWidth);
      $("#gisFrame").css("width",$("#gisFrame").width()-westWidth);
      $("#splitbutton").css("left",westWidth+4);
      $("#leftModule").accordion();
    }
    westExpand=!westExpand;
    if (isFullScreen) _westExpand=westExpand;//清除全屏时所记录的左侧栏打开的状态，在全屏模式下对左边栏进行了操作后，进入全屏时所记录的左边栏状态就没有用了
    $(this).css({
      "background-image":"url('"+(westExpand?"<%=path%>/test1/images/mainPage/splitbutton1.gif":"<%=path%>/test1/images/mainPage/splitbutton2.gif")+"')"
    });
  });
  $("#fullscreen").click(function(){
    var centerP = $("#mainLayout").layout("panel","center");
    if (isFullScreen) {//退出全屏
      $(this).css({
        "background-image":"url('<%=path%>/test1/images/mainPage/expand.png')"
      }).html("全屏");
      $("#mainLayout").layout("panel","north").panel("resize", {"height":80});
      $("#mainTab").css("margin-top","53px");
      $("#top").css({
        "background-image":"url('<%=path%>/test1/images/mainPage/topBanner.jpg')"
      });
      $("#mainLayout").layout("resize");
      $("#west").css("height", $("#west").height()-52);
      $("#gisMain").css("height", $("#gisMain").height()-52);
      $("#gisFrame").css("height", $("#gisFrame").height()-52);
      $("#splitbutton").css("left", westExpand?(westWidth+4):4);
      $("#logoImg").show();
      $("#titleImg").show();
      if (_westExpand!=westExpand) $("#splitbutton").click();
      /*
      $("#mainTab").find(".mtabText").show();
      $("#mainTab").find(".mtabSM").show();
      $("#mainTab").find(".mtabSF").show();
      $("#mainTab").css("margin-left","301px");
      */
      $("#welcom").css("float","right").css("margin-top","32px").css("margin-right","20px");
      $("#commonFunc").show();
      $("#logoutD").hide();
      $("#refreshCacheD").hide();
      _resizeTimeout();
    } else {//全屏
      _westExpand=westExpand;//记录左边栏的状态
      $(this).css({
        "background-image":"url('<%=path%>/test1/images/mainPage/collapse.png')"
      }).html("退出全屏");
      $("#mainLayout").layout("panel","north").panel("resize", {"height":28});
      $("#mainTab").css("margin-top","1px");
      $("#top").css({
        "background-image":"url('<%=path%>/test1/images/mainPage/topBanner_.jpg')"
      });
      $("#mainLayout").layout("resize");
      $("#west").css("height", $("#west").height()+52);
      $("#gisMain").css("height", $("#gisMain").height()+52);
      $("#gisFrame").css("height", $("#gisFrame").height()+52);
      if (westExpand) $("#splitbutton").click();
      $("#logoImg").hide();
      $("#titleImg").hide();
      /*
      $("#mainTab").find(".mtabText").hide();
      $("#mainTab").find(".mtabSM").hide();
      $("#mainTab").find(".mtabSF").hide();
      $("#mainTab").css("margin-left","0px");
      */
      $("#welcom").css("float","left").css("margin-top","6px").css("margin-left","20px");
      $("#commonFunc").hide();
      $("#logoutD").show();
      if (hasRefreshCacheAuth) $("#refreshCacheD").show();
    }
    isFullScreen=!isFullScreen;
  });

  //根据权限数据，构建界面
  buildMainTab(authTreeData.children);
  $("#gisFrame").attr("src", "<%=path%>/test1/gisMap/gisMain.jsp");
  //设置右上角功能菜单显示
  //查找是否有刷新缓存的权限
  if (hasRefreshCacheAuth) {
    $("#commonFunc").css("width", "170px");
    $("#commonFuncButton").css("width", "144px");
    $("#refreshCacheU").show();
  } else {
    $("#commonFunc").css("width", "86px");
    $("#commonFuncButton").css("width", "60px");
    $("#refreshCacheU").hide();
  }
  $("#commonFunc").css("left", $(window).width()-((hasRefreshCacheAuth?170:86)+20));
  $("#commonFuncButton").css("left", 13);
  $(".cfRight").css("left", 13+(hasRefreshCacheAuth?144:60));

  $("#main").show();
  $("#splitbutton").show();
  setTimeout(function(){$("#leftModule").accordion();$("#mask").hide();},100);
}
//构建主标签
function buildMainTab(tabList) {
	//主
  var newTab, ntWidth, _curTab;
  $(tabList).each(function(i){
    if (i==0) _curTab=this.id, $("#mainTab").append("<div id='mtsf' class='mtabSF'></div>");
    newTab = $("<div id='"+this.id+"' class='mtabText' index='"+i+"'>"+this.title+"</div>");
    ntWidth = 30+(((this.title).cnLength())*6);
    $(newTab).css("width", ntWidth).mouseover(function(){
      if (this.id!=curTab) {
        $(this).css("color", "blue").css("cursor","pointer");
      } else {
        $(this).css("cursor","default");
      }
    }).mouseout(function(){
      if (this.id!=curTab) {
        $(this).css("color", "black");
      }
    }).click(function(){
      buildModule(this.id, i);
    });
    $("#mainTab").append(newTab);
    $("#mainTab").append("<div id='mts"+i+"' class='mtabSM'></div>");
  });
  alert("_curTab::"+_curTab);
  buildModule(_curTab, 0);
}
//构建导航部分
function buildModule(tabId, tabIndex){
  if (curTab==tabId) return;
  alert("===========");
  //变换显示效果
  //-重置所有效果，恢复为初始状态
  $("#mainTab").find(".mtabText").css({
    "background-image":"url('')",
    "color":"#000",
    "font-weight":"normal"
  });
  $("#mainTab").find(".mtabSM").css({
    "background-image":"url('')"
  });
  $("#mtsf").css({
    "background-image":"url('<%=path%>/test1/images/mainPage/mtabSf.jpg')"
  });
  //-设置选中效果
  $("#"+tabId).css({
    "background-image":"url('<%=path%>/test1/images/mainPage/mtabSelected.jpg')",
    "color":"#fff",
    "cursor":"default",
    "font-weight":"bold"
  });
  if (tabIndex==0) {
    $("#mtsf").css({
      "background-image":"url('<%=path%>/test1/images/mainPage/mtabSfSelected.jpg')"
    });
  } else {
    $("#mts"+(tabIndex-1)).css({
      "background-image":"url('<%=path%>/test1/images/mainPage/mtabSSelectedL.png')"
    });
  }
  $("#mts"+tabIndex).css({
    "background-image":"url('<%=path%>/test1/images/mainPage/mtabSSelectedR.png')"
  });
  curTab=tabId;

  //构建导航部分
  var moduleList = null;
  alert("===========002");
  $(userAuthData.children).each(function(){
    if (this.id==tabId) {
    	alert(this.id);
      moduleList = this.children;
    }
  });
  //删除原有
  var fp = $("#leftModule").accordion("getPanel", 0);
  while(fp) {
    $("#leftModule").accordion("remove", 0);
    fp = $("#leftModule").accordion("getPanel", 0);
  }
  if (moduleArray) {
    for (i=moduleArray.length-1; i>=0; i--) moduleArray.removeByIndex(i);
  }
  //加入新的
  alert("===========003:"+moduleList.length);
  $(moduleList).each(function(i){
    if (i==0) curModule=i;
    var url = "";//this.attributes.v_jumpurl;
    alert(url);
    //if (this.attributes.v_urltype==0) url = "<%=path%>/"+url;
    moduleArray.push({"id":this.id, "url":url, title:this.title});
    var _content = '<iframe name="'+this.id+'" id="'+this.id+'"scrolling="no" frameborder="0"  src="'+url+'" style="width:100%;height:100%;"></iframe>';
    $("#leftModule").accordion("add",{
      id: this.id,
      selected: false,
      iconCls: "icon-star",
      title: this.title,
      onBeforeCollapse: function(){
        var p = $("#leftModule").accordion("getSelected");
        if (p) {
          var index= $("#leftModule").accordion("getPanelIndex",p);
        }
      },
      onBeforeExpand: function(){
        var p = $("#leftModule").accordion("getSelected");
        if (p) {
          var index= $("#leftModule").accordion("getPanelIndex",p);
        }
      },
      onExpand: function(){
        var p = $("#leftModule").accordion("getSelected");
        if (p) {
          curModule=$("#leftModule").accordion("getPanelIndex",p);
          $("#leftModule").find(".accordion-header-selected").find(".panel-title").css("color", "#ffffff");
          
        }
      },
      content: _content
    });
  });
  $("#leftModule").find("div.accordion-header").each(function(i){
    $(this).find("div.panel-title").attr("mIndex",i);
    $(this).css({
      "background-image":"url('<%=path%>/test1/images/mainPage/accordionTitleBg.jpg')",
      "color":"#ffffff"
    });
  });
  setTimeout(function(){$("#leftModule").accordion("select", 0);},200);
}

function findNode(treeNode, id) {
  var findN = null;
  if (treeNode.id==id) return treeNode;
  for (var i=0; i<treeNode.children.length; i++) {
    var tn = treeNode.children[i];
    if (tn.id==id) {
      findN=tn;
      break;
    }
  }
  if (findN==null) {
    for (var i=0; i<treeNode.children.length; i++) {
      var tn = treeNode.children[i];
      if (tn.children) {
        findN=findNode(tn, id);
        if (findN!=null) break;
      }
    }
  }
  return findN;
}
//刷新Gis系统管理的缓存
function refreshCache() {
  $.messager.progress({
    title: "提示",
    msg: "正在刷新后台数据，请稍候...",
    interval: 60
  });
  var url="<%=path%>/component/loginAction!refreshCache.do";
  $.ajax({type:"post", async:true, url:url, data:null, dataType:"json",
    success: function(json) {
      $.messager.progress("close");
      if (json.type==1) {
        $.messager.alert("提示", "缓存刷新成功！<br/>将重载页面。", "info", function(){
          window.location.href=window.location.href;
        });
      } else $.messager.alert("错误", "刷新缓存失败："+json.data+"！", "error");
    },
    error: function(errorData) {
      $.messager.progress("close");
      $.messager.alert("错误", "刷新缓存失败：</br>"+(errorData?errorData.responseText:"")+"！", "error");
    }
  });
}

//刷新Gis系统管理的缓存
function logout() {
  var url=_PATH+"/component/loginAction!logout.do";
  $.ajax({type:"post", async:true, url:url, data:null, dataType:"json",
    success: function(json) {
      if (json.type==1) {
        window.location.href="<%=path%>/common/login.jsp?noAuth";
      } else {
        $.messager.alert("错误", "注销失败："+json.data+"！</br>返回登录页面。", "error", function(){
          window.location.href="<%=path%>/common/login.jsp?noAuth";
        });
      }
    },
    error: function(errorData) {
      $.messager.alert("错误", "注销失败：</br>"+(errorData?errorData.responseText:"")+"！<br/>返回登录页面。", "error", function(){
        window.location.href="<%=path%>/common/login.jsp?noAuth";
      });
    }
  });
}

//子界面调用此方法进行注销，主要用于用户同时登录不同浏览器的判断逻辑
function onlyLogout(ip, mac, browser) {
  $.messager.alert("提示", "您已经在["+ip+"("+mac+")]机器上用["+browser+"]浏览器重新登陆了，当前登录失效！<br/>现返回登录页面。", "info", function(){ logout(); });
}

function getCurIframe() {
  var p = $("#leftModule").accordion("getSelected");
  if (p) {
    return window.frames[moduleArray[curModule].id];
  } else {
    $.messager.alert("提示","没有打开任何左侧栏功能！", "info");
  }
}

function getIframeByID(iframeId){
  var p = $("#leftModule").accordion("getSelected");
  if (p) {
    return window.frames[iframeId];
  }
}

function test() {
  $.messager.alert("提示","TEST！", "info");
}
</script>
</html>