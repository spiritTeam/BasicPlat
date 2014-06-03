<%@page language="java" contentType="text/html; charset=UTF-8" pageEncoding="UTF-8"%>
<%
  String path = request.getContextPath();
  //String username = (String)session.getAttribute("username");
  //byte[] nodepText = request.getParameter("pText").getBytes("iso8859-1");
  //int pLevel = Integer.parseInt(request.getParameter("pLevel"))+1; 
  //String pText = new String(nodepText,"UTF-8");
%>
<html>
<head>
<jsp:include page="/common/sysInclude.jsp" flush="true"/>
<script  type="text/javascript"  src="form.js"></script>
</head>
<body>
<!-- 新增模块dialog -->
<div  style="width:500px;height:250px;">
  <div style="height:10px;"></div>
  <form action="" id="add_module_form" >
    <table id="add_table">
      <tr>
        <td width="100px;" align="right"><label for="nodeName">模块名称:</label></td>
        <td><input name="nodeName" type="text" style="width:120px;"/></td>
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
        <td width="100px;" align="right"><label for="pName">上级模块:</label></td>
        <td><input id="pName" name="pName" style="width:120px;" value=""/></td>
        <td width="100px;" align="right"><label for="levels" >模块层级:</label></td>
        <td><input id="levels" name="levels"  style="width:120px;" value=""/></td>
      </tr>
      <tr>
        <td width="100px;" align="right"><label for="url">url:</label></td><td colspan="3"><input id="url" name="url" style="width:300px;"/></td>
      </tr>
      <tr>
        <td width="100px;" align="right"><label for="descn">功能描述:</label></td><td colspan="3"><textarea id="descn" name="descn"  style="width:300px;height:50px;"></textarea></td>
      </tr>
      <tr height="50px;" align="right">
        <td width="100px;" colspan="4">
          <a id="btnEdit" href="#" class="easyui-linkbutton" data-options="iconCls:'icon-save'" onclick="save();">提交</a>
          <a id="btnEdit" href="#" class="easyui-linkbutton" data-options="iconCls:'icon-cancel'" onclick="reset();">清空</a>
        </td>
      </tr>
    </table>
  </form>
</div>
</body>
<script type="text/javascript">
$(function(){
	initcombox();
});
function initcombox(){
	$('#moduleType').combobox({    
    url:'<%=path%>/moduleManage/comboboxData/moduleType.json',    
    valueField:'id',
    editable:false,
    textField:'text'   
  });  
  $('#style').combobox({    
    url:'<%=path%>/moduleManage/comboboxData/style.json',    
    valueField:'id',
    editable:false,
    textField:'text'   
  });  
}
function save(){
	console.info(serializeObject($('#add_module_form').form()));
	var formData = serializeObject($('#add_module_form').form());
	$.ajax({type:"post", async:true,data:formData, url:'<%=path%>/insertModule.do', dataType:"json",
        success: function(data) {
       	  $.messager.alert('新增信息','操作成功!','info');
        },error:function(data){
          $.messager.alert('新增信息','操作失败!','info');
        }
      });
}
function reset(){
	$('#add_module_form').form().find('input').val('');
	$('#descn').val('');
}
</script>
</html>
