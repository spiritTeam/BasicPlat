<%@page language="java" contentType="text/html; charset=UTF-8" pageEncoding="UTF-8"%>
<%@page import="java.util.*"%>
<%@page import="com.gmteam.framework.IConstants"%>
<%@page import="com.gmteam.framework.component.login.web.TreeA"%>
<%
  String path = request.getContextPath();
  String username = (String)session.getAttribute("username");
%>
<!DOCTYPE html PUBLIC "-//W3C//DTD HTML 4.01 Transitional//EN" "http://www.w3.org/TR/html4/loose.dtd">
<html>
<head>
<jsp:include page="/common/sysInclude.jsp" flush="true"/>
<script src="<%=path%>/test/DIS_jsonData/subApp_data.json" type="text/javascript"></script>
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
#top_under {
  position:absolute;
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
<div class="easyui-layout" style="width:1000px;height:500px" data-options="border:false">
  <div id="top" data-options="region:'north',border:false" style="width:1000px">
    <div id="top_top"></div>
    <div id="top_under">
      <div id="mainBar"><div id="user"> <span>欢迎您，<%=username%>!</span> </div></div>
      <div id="logout"><img src="<%=path%>/resources/images/sys/skin0/icon/logout.gif"/><a href="#" onclick="dologout()">注销</a></div>
    </div>
  </div>
  <div id="foot" data-options="region:'south',border:false" style="width:1000px"></div>
  <div id="mainCenter" data-options="region:'center'" style="width:1000px">
  <!-- 功能菜单 -->
    <div class="easyui-layout" data-options="fit:true">  
	    <div id="left" data-options="region:'west',split:true,title:'功能导航',collapsible:false" style="width:205px;">
	      <div id="navigate" class="easyui-accordion" data-options="fit:true,border:false"></div>
	    </div>
		  <div data-options="region:'center'" data-options="border:true, fit:false">
		    <div id="tabBar" class="easyui-tabs" data-options="fit:true,border:false"></div>
      </div>
      <div id="center_east" data-options="region:'east',split:false" style="width:0px;background:#E6EEF8"></div>
     </div>  
  </div>
</div>
</center></body>
<script>
var currentUrl = null, currentId = null,sanList = null;
var thisAccordion = null,newTree=null;idx = 0;
var treeData = null, newTree = null, _temp = 0;
var moduleArray=[];//模块列表
$(function() {
	$("#center_east").css("border", "0px");
    $("#left").parent().find(".panel-header").find(".panel-title").css("font-size", "14px");
    $("#left").parent().find(".panel-header").css("text-align", "center").css("height", "18px");
    $("#left").css("height", (parseInt($("#left").css("height"))-2)+"px");
	//加载树
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
          alert("moduleArray 长度:"+moduleArray.length);
          for (i=moduleArray.length-1; i>=0; i--) moduleArray.removeByIndex(i);
        }
    }
	$(userAuthData).each(function() {
		alert("构建导航");
		if(this.id==id){
			children = this.children;
			$(children).each(function() {
		    $("#navigate").accordion("add",{
	          selected:false,
	          iconCls:"icon-add",
	          title:this.title
        });
        thisAccordion = $("#navigate").accordion("getPanel", $("#navigate").accordion("panels").length-1);
        $(thisAccordion).parent().find(".panel-icon").css("background-image", "url('<%=path%>/"+this.icon+"')");
        newTree = window.document.createElement("div");
        $(newTree).attr("class", "easyui-tree tree").attr("id", "mTree"+idx);
        $(newTree).appendTo($(thisAccordion));
        $("#mTree"+idx).tree({data:this.treeData});
        idx++;
			  });
		}
	});
  currentUrl = url; currentId = id; sanList =children;
  var ifs = $("#mainCenters").find("iframe");
  var hasFind = false;
  $(ifs).each(function() {
    if ($(this).attr("id")==currentId) {
      $(this).css("display", "inline");
      hasFind = true;
    } else {
      $(this).css("display", "none");
    }
  });
  if (!hasFind) {
    var newIframe = window.document.createElement("iframe");
    $(newIframe).css("display", "inline")
      .attr("frameborder", "0").attr("scrolling", "no").attr("id", ""+id).attr("src", "<%=path%>"+url);
    $("#mainCenters").append($(newIframe));
  }
  onResize();
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
</script>
</html>