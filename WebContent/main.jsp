<%@page language="java" contentType="text/html; charset=UTF-8" pageEncoding="UTF-8"%>
<%@page import="java.util.*"%>
<%@page import="com.gmteam.framework.IConstants"%>
<%
  String path = request.getContextPath();
  String username = (String)session.getAttribute("username");
%>
<!DOCTYPE html PUBLIC "-//W3C//DTD HTML 4.01 Transitional//EN" "http://www.w3.org/TR/html4/loose.dtd">
<html>
<head>
<jsp:include page="/common/sysInclude.jsp" flush="true"/>
<title><%=IConstants.PLATFORM_NAME%></title>
</head>
<style type="text/css">
#top {
  height:105px;
  overflow:hidden;
  background:url('<%=path%>/resources/images/sys/skin0/banner/top_b.jpg') repeat-x fixed #E6EEF8;
}
#top_top {
  height:77px;
  width:996px;
  background:url('<%=path%>/resources/images/sys/skin0/banner/top.jpg') no-repeat fixed;
}
.top_under {
  position:absolute;
  top:76px;
  height:26px;
  width:100%;
  background:url('<%=path%>/resources/images/sys/skin0/banner/under1_b.jpg') repeat;
}
.top_under1 {
  //position:absolute;
  top:76px;
  height:26px;
  width:100%;
  background:url('<%=path%>/resources/images/sys/skin0/banner/under1_b.jpg') repeat;
}
#mainBar {
  top:76px;
  height:26px;
  width:996px;
  background:url('<%=path%>/resources/images/sys/skin0/banner/under1.jpg') repeat;
}
#foot {
  height:30px;
  background:#E6EEF8;
  background-image:url('<%=path%>/resources/images/sys/skin0/banner/menu1.jpg');
  background-repeat:repeat-x;
  background-position:bottom;
  background-origin:content-box;
}
#user span { display:inline-block; width:150px; margin-left:20px; margin-right:40px; line-height:26px; color:#fff; }
#user a.mainMenu { margin-left:10px; }
#switcherBtn { width:15px; height:15px; display:block; cursor:pointer; }
#logout {position:relative; float:right; margin-top:-22px; margin-right:10px;}
#logout img {float:left; margin:5px;}
#logout li a {margin-left:3px; vertical-align:bottom; display:inline-block;}
body {margin:0 auto; width:1000px;}
</style>
<body><center>
<div id="mainLayout" class="easyui-layout" style="width:1000px;height:500px" data-options="border:false">
  <div id="top" data-options="region:'north',border:false" style="width:1000px">
    <div id="top_under" class="top_under">
      <div id="mainBar"><div id="user"> <span>欢迎您，<%=username%>!</span> </div></div>
      <!--   <div class="mainTabButton imgTabButton fullscrenn" id="fullscreen">全屏</div>-->
      <div id="logout"><div class="mainTabButton imgTabButton fullscrenn" id="fullscreen">全屏</div></div>
    </div>
    <div id="top_top"></div>
  </div>
  <div id="foot" data-options="region:'south',border:false" style="width:1000px"></div>
  <div id="mainCenter" data-options="region:'center'" style="width:1000px">
  <!-- 功能菜单 -->
    <div class="easyui-layout" data-options="fit:true,border:false">  
      <div id="left" data-options="region:'west',split:true,title:'功能导航',collapsible:true" style="width:205px;">
        <div id="navigate" class="easyui-accordion" data-options="fit:true,border:false"></div>
      </div>
      <div data-options="region:'center'" data-options="border:true, fit:false">
        <div id="tabBar" class="easyui-tabs" data-options="fit:true,border:false"></div>
      </div>
     </div>  
  </div>
</div>
</center></body>
<script>
var currentUrl = null, currentId = null,sanList = null;
var thisAccordion = null,newTree=null;idx = 0;
var treeData = null, newTree = null, _temp = 0;
var westExpand=true, isFullScreen=false, _westExpand=true;//_westExpand是点击全屏时westExpand的状态
var moduleArray=[];//模块列表
var tabArray = [];//tab
var hasRefreshCacheAuth = false;
var tabIndex;
$(function() {
  $("#center_east").css("border", "0px");
    $("#left").parent().find(".panel-header").find(".panel-title").css("font-size", "14px");
    $("#left").parent().find(".panel-header").css("text-align", "center").css("height", "18px");
    $("#left").css("height", (parseInt($("#left").css("height"))-2)+"px");
  //加载树
  fullscreen();
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
          userAuthData = authData.children;
          addMainTab();
          $(userAuthData).each(function() {
            sanList = this.children;
              $("#user").html($("#user").html()
                +"<a href='javascript:void(0);' onclick='turnSubApp(\""+this.id+"\")' class='mainMenu' id='"+this.id+"'>"+this.title+"</a></div>");
              if (this.selected) {currentUrl=this.url;currentId=this.id;}
            });
            $("#mainCenters").css("background", "#E6EEF8")
              .css("border-top", "1px solid #E6EEF8")
              .css("border-right", "0px solid #E6EEF8")
              .css("border-bottom", "0px solid #E6EEF8")
              .css("border-left", "5px solid #E6EEF8");
            $("#mainCenter").css("overflow", "hidden");
            turnSubApp(currentId, currentUrl,sanList);
            $(window).resize(onResize);
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
function turnSubApp(id, url,children) {
  //remove原有的
  var fp = $("#navigate").accordion("getPanel", 0);
  if(fp){
      while(fp) {
        $("#navigate").accordion("remove", 0);
        fp = $("#navigate").accordion("getPanel", 0);
      }
      if (moduleArray) {
        for (i=moduleArray.length-1; i>=0; i--) moduleArray.removeByIndex(i);
      }
  }
  //创建新的
  $(userAuthData).each(function() {
    if(this.id==id){
      children = this.children;
      $(children).each(function() {
        children = this.children;
        $("#navigate").accordion("add",{
          selected:false,
          iconCls:"icon-add",
          title:this.title
        });
        thisAccordion = $("#navigate").accordion("getPanel", $("#navigate").accordion("panels").length-1);
       //用于绑定导航栏的图标
        //$(thisAccordion).parent().find(".panel-icon").css("background-image", "url('<%=path%>/"+this.icon+"')");
        newTree = window.document.createElement("div");
        $(newTree).attr("class", "easyui-tree tree").attr("id", "mTree"+idx);
        $(newTree).appendTo($(thisAccordion));
        $("#mTree"+idx).tree({data:this.treeData});
        treeData = new Array();
        idx++;
        //建立分支
        $("#mTree"+(idx-1)).tree({data:children});
        /*if(children){
          $(children).each(function() {
            var newTreeNode = {
              "id": this.id,
              "text": this.title,
              "iconCls":"" ,
              "attributes":{"data":this.data,"url":""}
            };
            treeData.push(newTreeNode);
            $("#mTree"+(idx-1)).tree({data:treeData});
            //加载数据后，处理图标、处理onclick事件
            $("#left").find(".easyui-tree").each(function(i) {
              $(this).tree({
                onSelect: function(node) {
                  showTab(node.id,node.text,node.attributes.url,node.iconCls);
                }
              });
            });
           $("#navigate").accordion("select", 0);
          });
        }*/
      });
    }
  });
  onResize();
}
function showTab(_id,_title, _url, _icon) {
  var hasTab = false;
  for (var i=0; i<tabArray.length; i++) {
    if (tabArray[i].title==_title&&tabArray[i].url==_url) {
      //重新显示tab
      $("#tabBar").tabs("select", i+1);
      hasTab = true;
    }
  }
  if (!hasTab) {
    var _content = '<iframe scrolling="no" frameborder="0"  src="'+'<%=path%>/'+_url+'" style="width:100%;height:100%;"></iframe>';
    if (!_url||_url=="") {
      _content = "功能未实现";  
    }
    //创建新的tab
    $("#tabBar").tabs("add",{
      title: _title,
      content: _content,
      iconCls: _icon,
      tools: [{iconCls:"icon-mini-refresh", title:"刷新", handler:refreshTab}],
      closable: true
    });
    var _tab = {"title":_title, "url": _url};
    tabArray.push(_tab);
  }
}
function refreshTab() {
  var refresh_tab = $("#tabBar").tabs('getSelected');
  if (refresh_tab&&refresh_tab.find('iframe').length>0) {
    var _refresh_ifram = refresh_tab.find('iframe')[0];
    _refresh_ifram.contentWindow.location.href=_refresh_ifram.src;
  } 
}
function addMainTab(){
  $(".mainTabButton").mouseover(function(){
    $(this).css("border", "1px solid #DADADA").css("margin-top", "3px").css("margin-right", "6px").css("color", "blue");
  }).mouseout(function(){
    $(this).css("border", "0px solid #DADADA").css("margin-top", "4px").css("margin-right", "8px").css("color", "black");;
  });
  //添加首页
  // add a new tab panel    
  $('#tabBar').tabs('add',{    
    title:'首页',    
    content:'萨瓦迪卡', 
    //不让关闭
    closable:false,    
    tools:[{    
        iconCls:'icon-mini-refresh',    
        handler:refreshTab
    }]
  });
  $("#tabBar").tabs({
    onBeforeClose: function(title, index) {
      tabArray.removeByIndex(index-1);
    }
  });
}
function onResize() {
  var _height = $(window).height()-$("#top").height()-$("#foot").height();
  $(".easyui-layout").css("height",$(window).height()+"px");
  $("#mainCenter").css("height", _height);
  $("#foot").parent().css("top", _height+$("#top").height());
  $("iframe").each(function(){
    if ($(this).attr("id")==currentId) {
      $(this).css({
        height: _height-2,
        width: "998px"
      });
    }
  });
}
function fullscreen(){
$("#fullscreen").click(function(){
    var centerP = $("#mainLayout").layout("panel","center");
    alert("11111"+isFullScreen);
    $("#top_under").removeClass("top_under");
    $("#top_under").removeClass("top_under1");
    $("#top_under").addClass("top_under");
    if (isFullScreen) {//退出全屏
      $(this).css({
        //"background-image":"url('<%=path%>/test1/images/mainPage/expand.png')"
      }).html("全屏");
      $("#mainLayout").layout("panel","north").panel("resize", {"height":100});
      $("#mainTab").css("margin-top","53px");
      //$("#top").css({
        //"background-image":"url('<%=path%>/test1/images/mainPage/topBanner.jpg')"
     // });
      $("#mainLayout").layout("resize");
      $("#west").css("height", $("#west").height()-52);
      $("#gisMain").css("height", $("#gisMain").height()-52);
      $("#gisFrame").css("height", $("#gisFrame").height()-52);
      //$("#splitbutton").css("left", westExpand?(westWidth+4):4);
      $("#logoImg").show();
      $("#titleImg").show();
      //if (_westExpand!=westExpand) $("#splitbutton").click();
      /*
      $("#mainTab").find(".mtabText").show();
      $("#mainTab").find(".mtabSM").show();
      $("#mainTab").find(".mtabSF").show();
      $("#mainTab").css("margin-left","301px");
      */
      //$("#welcom").css("float","right").css("margin-top","32px").css("margin-right","20px");
      isFullScreen=false;
    } else {//全屏
      alert("in 全屏");
      _westExpand=westExpand;//记录左边栏的状态
      $("#top_under").removeClass("top_under");
      $("#top_under").removeClass("top_under1");
      $("#top_under").addClass("top_under1");
      $(this).css({
       // "background-image":"url('<%=path%>/test1/images/mainPage/collapse.png')"
      }).html("退出全屏");
      //返回指定的面板
      $("#mainLayout").layout("panel","north").panel("resize", {"height":28});
      $("#mainTab").css("margin-top","1px");
      //$("#top").css({
        //"background-image":"url('<%=path%>/test1/images/mainPage/topBanner_.jpg')"
      //});
      $("#mainLayout").layout("resize");
      $("#west").css("height", $("#west").height()+52);
      $("#gisMain").css("height", $("#gisMain").height()+52);
      $("#gisFrame").css("height", $("#gisFrame").height()+52);
      //if (westExpand) $("#splitbutton").click();
      $("#logoImg").hide();
      $("#titleImg").hide();
      /*
      $("#mainTab").find(".mtabText").hide();
      $("#mainTab").find(".mtabSM").hide();
      $("#mainTab").find(".mtabSF").hide();
      $("#mainTab").css("margin-left","0px");
      */
      //$("#welcom").css("float","left").css("margin-top","6px").css("margin-left","20px");
      if (hasRefreshCacheAuth) $("#refreshCacheD").show();
      isFullScreen=true;
    }
    alert("2222"+isFullScreen);
  });
  alert("out Method fullscreen");
}
</script>
</html>
