<%@page language="java" contentType="text/html; charset=UTF-8" pageEncoding="UTF-8"%>
<%
  String path = request.getContextPath();
  String pId = request.getParameter("pId");
  int pLevels = Integer.parseInt(request.getParameter("pLevels"))+1;
%>
<html>
<head>
<jsp:include page="<%=path%>/common/sysInclude.jsp" flush="true"/>
</head>
<body>
<!-- 新增模块dialog -->
<div  style="width:500px;height:250px;">
  <div style="height:10px;"></div>
  <form action="" id="add_module_form" >
    <input type="hidden" name="levels" value="<%=pLevels%>">
    <table id="add_table">
      <tr>
        <td width="100px;" align="right"><label for="displayName">显示名称:</label></td>
        <td><input id="displayName" name="displayName" type="text" style="width:120px;"/></td>
        <td width="100px;" align="right">模块类型:</td>
        <td><input id="moduleType" name="moduleType" value="" style="width: 120px;"></td>
      </tr>
      <tr>
        <td width="100px;" align="right"><label for="iconCls">模块图标:</label></td>
        <td><input id="iconCls" name="iconCls" style="width:120px;"/></td>
        <td width="100px;" align="right">模块样式:</td>
        <td><input id="style" name="style" value="" style="width: 120px;"></td>
      </tr>
      <tr>
        <td width="100px;" align="right"><label for="parentId">上级模块:</label></td>
        <td>
          <input id="parentId" name="parentId" value="<%=pId %>" class="easyui-combotree" data-options="method:'get',required:true" style="width:120px;">
        </td>
        <td width="100px;" align="right"><label for="moduleName" >模块名称:</label></td>
        <td><input id="moduleName" name="moduleName"  style="width:120px;" value=""/></td>
      </tr>
      <tr>
        <td width="100px;" align="right"><label for="url">url:</label></td><td colspan="3"><input id="url" name="url" style="width:300px;"/></td>
      </tr>
      <tr>
        <td width="100px;" align="right"><label for="descn">功能描述:</label></td><td colspan="3"><textarea id="descn" name="descn"  style="width:300px;height:50px;"></textarea></td>
      </tr>
      <tr height="50px;" align="right">
        <td width="100px;" colspan="4">
          <a id="save" href="#" class="easyui-linkbutton" data-options="iconCls:'icon-save'" onclick="save();">提交</a>
          <a id="reset" href="#" class="easyui-linkbutton" data-options="iconCls:'icon-cancel'" onclick="reset();">清空</a>
        </td>
      </tr>
    </table>
  </form>
</div>
</body>
<script type="text/javascript">
var wId;
var treeData;
$(function(){
	initcombox();
});
function initcombox(){
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
       $('#parentId').combotree({    
          data:treeData.children,
          editable:false
       });
    }
  });
}
function save(){
	console.info(formField2Object('add_module_form'));
	var formData = formField2Object('add_module_form');
	$.ajax({type:"post", async:true,data:formData, url:'<%=path%>/insertModule.do', dataType:"json",
    success: function(data) {
   	  $.messager.alert('新增信息','操作成功!','info');
    },error:function(data){
      $.messager.alert('新增信息','操作失败!','info');
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
