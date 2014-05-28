<%@page language="java" contentType="text/html; charset=UTF-8" pageEncoding="UTF-8"%>
<!DOCTYPE html>
<html>
<head>
<title>测试</title>
<meta charset="UTF-8">
<meta http-equiv="pragma" content="no-cache"/>
<meta http-equiv="cache-control" content="no-cache"/>
<meta http-equiv="expires" content="0"/>
<jsp:include page="/common/sysInclude.jsp" flush="true"/>
<title>测试树功能</title>
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
<table id="moduleTreeGrid"></table>
</body>

<script>
$(function(){
  $("#moduleTree").css({"width":299, "height":100, "overflow":"auto", "border":"1px red solid"});
  $("#moduleTree").tree({});
  $("#moduleTreeGrid").css({"width":470, "height":100, "overflow":"auto", "border":"1px red solid"});
  $("#moduleTreeGrid").treegrid({
    width:470,
    height:100,
    nowrap:false,
    animate:true,
    collapsible:true,
    idField:"id",
    treeField:"displayNamne",
    columns:[[
      {field:"displayName", title:"模块名称", width:100, align:'center'},
      {field:"url", title:"对应Url", width:170, align:'center'}
    ]]
  });

  var url=_PATH+"/showAllTree.do";
  $.ajax({type:"post", async:true, url:url, data:null, dataType:"json",
    success: function(json) {
      $("#moduleTree").tree("loadData", json.children);
    }
  });

  url=_PATH+"/showAllTreeGrid.do";
//  $.ajax({type:"post", async:true, url:url, data:null, dataType:"json",
  //  success: function(json) {
    //  $("#moduleTreeGrid").treegrid("loadData", json.children);
    //}
  //});
});
</script>
</html>