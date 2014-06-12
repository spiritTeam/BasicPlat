<%@page language="java" contentType="text/html; charset=UTF-8" pageEncoding="UTF-8"%>
<%
  String path = request.getContextPath();
%>
<!DOCTYPE html>
<html>
<head>
<meta charset="UTF-8">
<meta http-equiv="pragma" content="no-cache"/>
<meta http-equiv="cache-control" content="no-cache"/>
<meta http-equiv="expires" content="0"/>
<jsp:include page="<%=path%>/common/sysInclude.jsp" flush="true"/>
<title>测试树功能</title>
</head>

<body>
<div class="demo-info">
  <div class="demo-tip icon-tip"></div>
  <div>重新构造树</div>
</div>
<div>
  <table id="allTreeGrid"></table>
</div>
<div>树id列表：<input id=tids type=text style="width:375px"/><input type=button value="重组" onclick="restructTree()"/></div>
<div>
  <table id="reTreeGrid"></table>
</div>

</body>

<script>
$(function(){
  var url=_PATH+"/showAllTreeGrid.do";
  $("#allTreeGrid").parent().css({"width":470, "height":200, "border":"0px red solid"});
  $("#allTreeGrid").css({
    "width":470,
    "height":200
  });
  $("#allTreeGrid").treegrid({
  	title: '原树',
    idField: 'id',
    treeField: 'displayName',
    columns:[[
      {title:'模块id',field:'id',width:60}, 
      {title:'模块显示名',field:'displayName',width:110},
      {title:'模块名称',field:'nodeName',width:110}
    ]] 
  });
  $.ajax({type:"post", async:true, url:url, data:null, dataType:"json",
    success: function(json) {
      $("#allTreeGrid").treegrid("loadData", json.children);
    }
  });

  $("#reTreeGrid").parent().css({"width":470, "height":200, "border":"0px red solid"});
  $("#reTreeGrid").css({
    "width":470,
    "height":200
  });
  $("#reTreeGrid").treegrid({
    title: '重组树',
    idField: 'id',
    treeField: 'displayName',
    columns:[[
      {title:'模块id',field:'id',width:60}, 
      {title:'模块显示名',field:'displayName',width:110},
      {title:'模块名称',field:'nodeName',width:110}
    ]] 
  });
});

function restructTree(){
  var url=_PATH+"/testRestructureTree.do?ids="+$("#tids").val();
  $.ajax({type:"post", async:true, url:url, data:null, dataType:"json",
    success: function(json) {
      $("#reTreeGrid").treegrid("loadData", json.children);
    }
  });
}
</script>
</html>