<%@page language="java" contentType="text/html; charset=UTF-8" pageEncoding="UTF-8"%>
<%@page import="java.util.List"%>
<%@page import="com.gmteam.framework.IConstants"%>
<%@page import="com.gmteam.framework.model.tree.BaseTreeNode"%>
<%
  String path = request.getContextPath();
  String username = (String)session.getAttribute(IConstants.USER_NAME);
  List<BaseTreeNode> modules = (List<BaseTreeNode>)session.getAttribute(IConstants.USER_MODULES);
%>
<!DOCTYPE html PUBLIC "-//W3C//DTD HTML 4.01 Transitional//EN" "http://www.w3.org/TR/html4/loose.dtd">
<html>
<head>
<jsp:include page="/common/sysInclude.jsp" flush="true"/>
<script src="<%=path%>/test/DIS_jsonData/modules.json" type="text/javascript"></script>
<title><%=IConstants.PLATFORM_NAME%></title>
<style>
body {margin:0 auto; width:1000px;}
.accordion .accordion-header-selected {background: #E0ECFF;}
.tree-img {
  display: inline-block;
  width: 16px;
  height: 18px;
  vertical-align: top;
  overflow: hidden;
}

.icon-1{
  background:url('<%=path%>/resources/images/sys/skin0/icon/moduleIcon/4.gif') no-repeat;
}
.icon-2{
  background:url('<%=path%>/resources/images/sys/skin0/icon/moduleIcon/3.gif') no-repeat;
}
.icon-3{
  background:url('<%=path%>/resources/images/sys/skin0/icon/moduleIcon/8.gif') no-repeat;
}
.icon-4{
  background:url('<%=path%>/resources/images/sys/skin0/icon/moduleIcon/9.gif') no-repeat;
}
.icon-5{
  background:url('<%=path%>/resources/images/sys/skin0/icon/moduleIcon/11.gif') no-repeat;
}
.icon-6{
  background:url('<%=path%>/resources/images/sys/skin0/icon/moduleIcon/14.png') no-repeat;
}
.icon-7{
  background:url('<%=path%>/resources/images/sys/skin0/icon/moduleIcon/2.gif') no-repeat;
}
.icon-8{
  background:url('<%=path%>/resources/images/sys/skin0/icon/moduleIcon/1.gif') no-repeat;
}
.icon-9{
  background:url('<%=path%>/resources/images/sys/skin0/icon/moduleIcon/12.png') no-repeat;
}
.icon-10{
  background:url('<%=path%>/resources/images/sys/skin0/icon/moduleIcon/13.png') no-repeat;
}

</style>
</head>
<style type="text/css">
</style>
<body class="easyui-layout" style="background:#E6EEF8">
  <div id="left" data-options="region:'west',split:true,title:'功能导航',collapsible:false" style="width:205px;">
    <div id="navigate" class="easyui-accordion" data-options="fit:true,border:false"></div>
  </div>
  <div data-options="region:'center'" data-options="border:true, fit:false">
    <div id="tabBar" class="easyui-tabs" data-options="fit:true,border:false"></div>
  </div>
  <div data-options="region:'east',split:false" style="width:0px;background:#E6EEF8"></div>
</body>
<script>
var thisAccordion = null;
var idx = 0;
var tabArray = [];
var tabIndex;
$(function() {
  $("div[data-options^='region:\'east\'']").css("border", "0px");
  $("#left").parent().find(".panel-header").find(".panel-title").css("font-size", "14px");
  $("#left").parent().find(".panel-header").css("text-align", "center").css("height", "18px");
  $("#left").css("height", (parseInt($("#left").css("height"))-2)+"px");
  //生成菜单数据
  var treeData = null;
  var newTree = null;
  var _temp = 0;
  //从json读取
  $(modules).each(function() {
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
  //--从后台读取
  <%if(modules != null) {
  for(BaseTreeNode module : modules) {%>
    if (<%=module.getLevel()%>==1) {
      $("#navigate").accordion("add",{
        selected:false,
        iconCls:"icon-add",
        title:"<%=module.getTitle()%>"
      });
      thisAccordion = $("#navigate").accordion("getPanel", $("#navigate").accordion("panels").length-1);
      $(thisAccordion).parent().find(".panel-icon").css("background-image", "url(<%=path%>/<%=module.getIconsFowShow()%>)");

      newTree = window.document.createElement("div");
      $(newTree).attr("class", "easyui-tree tree").attr("id", "mTree"+idx);
      $(newTree).appendTo($(thisAccordion));
      if (treeData&&treeData.length>0&&(idx-1)>0) $("#mTree"+(idx-1)).tree({data:treeData});
      treeData = new Array();
      idx++;
    } else {
      var newTreeNode = {
        "id": "<%=module.getId()%>",
        "text": "<%=module.getTitle()%>",
        "iconCls": "icon-"+(++_temp),
        "attributes":{"icon":"<%=module.getIconsFowShow()%>", "url":"<%=module.getUrl()%>"}
      };
      treeData.push(newTreeNode);
    }<%
  }
  }%>
  $("#mTree"+(idx-1)).tree({data:treeData});
  //加载数据后，处理图标、处理onclick事件
  $("#left").find(".easyui-tree").each(function(i) {
    $(this).tree({
      onSelect: function(node) {
        showTab(node.text, node.attributes.url, node.iconCls);
      }
    });
  });
  $("#navigate").accordion("select", 0);
  //tab处理
  //加首页
  $("#tabBar").tabs("add",{
    title: "首页",
    content: '<iframe scrolling="no" frameborder="0"  src="<%=path%>/test/appSys/dataIS/manage/portal.jsp" style="width:100%;height:100%;"></iframe>',
    tools: [{iconCls:"icon-mini-refresh", title:"刷新", handler:refreshTab}],
    closable: false
  });
  $("#tabBar").tabs({
    onBeforeClose: function(title, index) {
      tabArray.removeByIndex(index-1);
    }
  });
});

function showTab(_title, _url, _icon) {
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
      _content = '未实现';  
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
</script>
</html>