<%@page language="java" contentType="text/html; charset=UTF-8" pageEncoding="UTF-8"%>
<%
  String path = request.getContextPath();
  //id
  String nodeId = request.getParameter("id");
  //displayName
  byte[] nodeDisplayName= request.getParameter("displayName").getBytes("iso8859-1") ; //解码  
  String displayName = new String(nodeDisplayName,"UTF-8");
  //moduleName
  String moduleName = request.getParameter("moduleName");
  //iconCls
  String iconCls = request.getParameter("iconCls");
  //url
  String url = request.getParameter("url");
  //descn
  byte[] nodeDescn = (request.getParameter("descn").getBytes("iso8859-1"));
  String descn = new String(nodeDescn,"UTF-8");
  //style
  String style = request.getParameter("style");
  //moduleType
  String moduleType = request.getParameter("moduleType");
  String pId = request.getParameter("pId");
%>
<html>
<head>
<jsp:include page="<%=path%>/common/sysInclude.jsp" flush="true"/>
</head>
<body>
<div  style="width:500px;height:250px;">
  <div style="height:10px;"></div>
  <form action="" id="update_module_form" >
    <input name="id" type="hidden" value="<%=nodeId %>">
    <table id="add_table">
      <tr>
        <td width="100px;" align="right"><label for="displayName">显示名称:</label></td>
        <td><input id="displayName" name="displayName" type="text" style="width:120px;" value="<%=displayName %>"/></td>
        <td width="100px;" align="right">模块类型:</td>
        <td><input id="moduleType" name="moduleType" value="" style="width: 120px;"></td>
      </tr>
      <tr>
        <td width="100px;" align="right"><label for="iconCls">模块图标:</label></td>
        <td><input id="iconCls" name="iconCls" style="width:120px;" value="<%=iconCls %>"/></td>
        <td width="100px;" align="right">模块样式:</td>
        <td><input id="style" name="style" value="" style="width: 120px;"></td>
      </tr>
      <tr>
        <td width="100px;" align="right"><label for="pName">上级模块:</label></td>
        <td><input id="pName" name="parentId" value="<%=pId %>" class="easyui-combotree" data-options="method:'get',required:true" style="width:120px;"></td>
        <td width="100px;" align="right"><label for="moduleName" >模块名称:</label></td>
        <td><input id="moduleName" name="moduleName"  style="width:120px;" value="<%=moduleName%>"/></td>
      </tr>
      <tr>
        <td width="100px;" align="right"><label for="url">url:</label></td><td colspan="3"><input id="url" name="url" style="width:300px;" value="<%=url%>"/></td>
      </tr>
      <tr>
        <td width="100px;" align="right"><label for="descn">功能描述:</label></td><td colspan="3"><textarea id="descn" name="descn"  style="width:300px;height:50px;"><%=descn %></textarea></td>
      </tr>
      <tr height="50px;" align="right">
        <td width="100px;" colspan="4">
          <a id="btnEdit" href="#" class="easyui-linkbutton" data-options="iconCls:'icon-save'" onclick="update();">提交</a>
          <a id="btnEdit" href="#" class="easyui-linkbutton" data-options="iconCls:'icon-cancel'" onclick="reset();">清空</a>
        </td>
      </tr>
    </table>
  </form>
</div>
</body>
<script type="text/javascript">
$(function(){
	init();
});
function init(){
	$('#moduleType').combobox({    
    url:'<%=path%>/component/moduleManage/comboboxData/moduleType.json',    
    valueField:'id',
    editable:false,
    textField:'text'   
  });  
  $('#style').combobox({    
    url:'<%=path%>/component/moduleManage/comboboxData/style.json',    
    valueField:'id',
    editable:false,
    textField:'text'   
  });
  $.ajax({type:"post", async:true, url:'<%=path%>/showAllTree.do', dataType:"json",
    success: function(data) {
      treeData=data;
       $('#pName').combotree({    
          data:treeData.children,
          editable:false
       });
    }
  });
}
function update(){
	console.info(formField2Object('update_module_form'));
	var formData = formField2Object('update_module_form');
	$.ajax({type:"post", async:true,data:formData, url:'<%=path%>/updateModule.do', dataType:"json",
    success: function(data) {
   	  $.messager.alert('更新信息','操作成功!','info');
    },error:function(data){
    	$.messager.alert('更新信息','操作失败!','info');
    }
  });
	wId = getUrlParam(window.location.href, "_winID");
  closeWin(wId);
}
function reset(){
	$('#add_module_form').form().find('input').val('');
	$('#descn').val('');
}
</script>
</html>
