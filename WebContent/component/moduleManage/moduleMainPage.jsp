<%@page language="java" contentType="text/html; charset=UTF-8" pageEncoding="UTF-8"%>
<%
  String path = request.getContextPath();
%>
<html>
<head>
  <jsp:include page="/common/sysInclude.jsp" flush="true"/>
</head>
<body>
  <div id="toolbar" align="left" style="height:35px;">
    <div style="height:5px;"></div>
    &nbsp;<a id="btnAdd" href="#" class="easyui-linkbutton" data-options="iconCls:'icon-add'" onclick="toAdd();">新增</a> 
    <a id="btnEdit" href="#" class="easyui-linkbutton" data-options="iconCls:'icon-edit'" onclick="toUpdate();">修改</a>
    <a id="btnRemove" href="#" class="easyui-linkbutton" data-options="iconCls:'icon-remove'" onclick="toDelete();">删除</a> 
    <a id="btnRefresh" href="#" class="easyui-linkbutton" data-options="iconCls:'icon-reload'" onclick="toRefresh();">刷新</a>
  </div>
  <table id="treegrid" style="border:0px #FF0000 solid;"></table>
</body>
<script type="text/javascript">
var mPage =getMainPage();
var addWid;
var editWid;
$(function(){
	init();
	//绑定结点右击事件
	$('#treegrid').treegrid({'onContextMenu':function(e){
		//屏蔽自带的菜单等
	  e.preventDefault();
	  $('#mm').menu('show', {
      left: e.pageX,
      top: e.pageY
    });
	}});
});
function init(){
	$.ajax({type:"post", async:true, url:'<%=path%>/showAllTreeGrid.do', dataType:"json",
    success: function(data) {
      getTreeGrid(data.children);
    }
  });
}
function toRefresh(){
	$.ajax({type:"post", async:true, url:'<%=path%>/refreshManager.do', dataType:"json",
	    success: function(data) {
	    	getTreeGrid(data.children);
	    }
	  });
}
function toAdd(){
	var node= $('#treegrid').treegrid('getSelected');
	var pId = "1000";
  var pLevels = "0";
	if(node&&node.id!=null){
		pId = node.id;
		pLevels = node.levels;
		addWid = openWin("新增模块", "<%=path%>/component/moduleManage/addModule.jsp?pId="+pId+"&pLevels="+pLevels,"300", "500", "", true, null);
		alert(addWin);
	}else{
		addWid = openWin("新增模块", "<%=path%>/component/moduleManage/addModule.jsp?pId="+pId+"&pLevels="+pLevels,"300", "500", "", true, null);
	}
}
function toUpdate(){
	var node= $('#treegrid').treegrid('getSelected');
	if(node==null){
		$.messager.alert('提示信息','请先选择一个节点','info');
  }else{
	  var id = node.id;
	  var displayName = node.displayName;
	  var moduleName = node.nodeName;
	  var iconCls = node.iconCls;
	  var style = node.style;
	  var url = node.url;
	  var descn = node.descn;
	  var moduleType = node.moduleType;
	  var pId = node.parentId;
	  alert(pId);
	  openWin("修改模块", 
			  "<%=path%>/component/moduleManage/updateModule.jsp?id="+id+"&displayName="+displayName+"&moduleName="+moduleName+"&iconCls="+iconCls+"&style="+style+"&url="+url+"&descn="+descn+"&moduleType"+moduleType+"&pId="+pId,"300", "500", "", true, null);
  }
}
function toDelete(){
	var node= $('#treegrid').treegrid('getSelected');
	if(node==null){
		$.messager.alert('提示信息','请先选择一个节点','info');
  }else{
	  $.messager.confirm('确认信息', '您想要删除该模块么？', function(r){
		  if (r){
			  var nodeId=node.id;
			  alert(nodeId);
			  $.ajax({type:"post", async:true,data:nodeId,url:'<%=path%>/deleteModule.do?nodeId='+nodeId, dataType:"json",
				    success: function(data) {
				    	if(data=='1'){
				    		$.messager.alert('提示信息','操作成功','info');
				    	}else{
				    		$.messager.alert('提示信息','操作失败','info');
				    	}
				    }
				  });
		  }else{
			  return;
		  }
		});
  }
}
function getTreeGrid(data){
	var treeData = data;
	$('#treegrid').treegrid({    
  data:treeData,
  fit:true,
  nowrap:false,
  border:true,
  idField:'id',
  treeField:'displayName',
  frozenColumns:[[{
    title:'显示名称',
    field: 'displayName',
    width: 200
  }]],
  columns:[[
	  {
	    title:'模块名称',
	    field: 'nodeName',
	    width: 100,
	    align:'right'
	  },
    {
      title:'上级模块',
      field: 'parentName',
      width: 100,
      align:'right'
    },
    {
      title:'模块类型',
      field: 'moduleType',
      width: 100,
      align:'right'
    },
    {
      title:'样式类型',
      field: 'style',
      width: 100,
      align:'right'
    },
    {
      title:'模块图标',
      field: 'iconCls',
      width: 100,
      align:'right'
    },
    {
      title:'层级',
      field: 'levels',
      width: 100,
      align:'right'
    },
    {
        title:'功能描述',
        field: 'descn',
        width: 150,
        align:'right'
      }]]
  });
}
</script>
</html>