<%@page language="java" contentType="text/html; charset=UTF-8" pageEncoding="UTF-8"%>
<!DOCTYPE html>
<html>
<head>
<meta charset="UTF-8">
<meta http-equiv="pragma" content="no-cache"/>
<meta http-equiv="cache-control" content="no-cache"/>
<meta http-equiv="expires" content="0"/>
<jsp:include page="/common/sysInclude.jsp" flush="true"/>
<title>测试树</title>
</head>

<body>
<div class="demo-info">
  <div class="demo-tip icon-tip"></div>
  <div>一次加载树</div>
</div>
<div id="moduleTree"></div>
<div class="demo-info">
  <div class="demo-tip icon-tip"></div>
  <div>一次加载树表</div>
</div>
<div>
  <table id="moduleTreeGrid"></table>
</div>
</body>

<script>
$(function(){
  $("#moduleTree").css({"width":299, "height":100, "overflow":"auto", "border":"1px red solid"});
  $("#moduleTree").tree({});

  var url=_PATH+"/showAllTree.do";
  $.ajax({type:"post", async:true, url:url, data:null, dataType:"json",
    success: function(json) {
      $("#moduleTree").tree("loadData", json.children);
    }
  });

  url=_PATH+"/showAllTreeGrid.do";
  $("#moduleTreeGrid").parent().css({"width":470, "height":200, "border":"0px red solid"});
  $("#moduleTreeGrid").css({
    "width":470,
    "height":200
  });
  $("#moduleTreeGrid").treegrid({
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
      $("#moduleTreeGrid").treegrid("loadData", json.children);
    }
  });
});
</script>
</html>