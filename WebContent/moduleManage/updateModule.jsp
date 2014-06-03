<%@page language="java" contentType="text/html; charset=UTF-8" pageEncoding="UTF-8"%>
<%
  String path = request.getContextPath();
  String username = (String)session.getAttribute("username");
  byte[] nodeTextByte= request.getParameter("text").getBytes("iso8859-1") ; //解码  
  String nodeText = new String(nodeTextByte,"UTF-8");
  String nodeId = request.getParameter("id");
  String nodeUrl = request.getParameter("url");
  String nodeIconCss = request.getParameter("iconCss");
  byte[] descnByte = (request.getParameter("descn").getBytes("iso8859-1"));
  String nodeDescn = new String(descnByte,"UTF-8");
%>
<html>
<head>
<jsp:include page="/common/sysInclude.jsp" flush="true"/>
<script  type="text/javascript"  src="form.js"></script>
</head>
<body>
<!-- 新增模块dialog -->
<div  style="width:500px;height:250px;">
  <div  style="height:10px;"></div>
  <form action="" id="update_module_form" >
    <input id="id" name="id" type="hidden" value="<%=nodeId %>"/>
    <table id="add_table">
      <tr>
        <td width="100px;" align="right"><label for="text">模块名称:</label></td><td><input id="text" name="text" style="width:120px;" value="<%=nodeText%>"/></td>
        <td width="100px;" align="right"><label for="iconCss">模块图标:</label></td><td><input id="iconCss" name="iconCss" style="width:120px;" value="<%=nodeIconCss%>"/></td>
      </tr>
      <tr>
        <td width="100px;" align="right"><label for="url">url:</label></td><td colspan="3"><input id="url" name="url" style="width:300px;" value="<%=nodeUrl%>"/></td>
      </tr>
      <tr>
        <td width="100px;" align="right"><label for="descn">功能描述:</label></td><td colspan="3"><textarea id="descn" name="descn"  style="width:300px;height:50px;" ><%=nodeDescn%></textarea></td>
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
function update(){
	console.info(serializeObject($('#update_module_form').form()));
	var formData = serializeObject($('#update_module_form').form());
	$.ajax({type:"post", async:true,data:formData, url:'<%=path%>/updateModule.do', dataType:"json",
        success: function(data) {
       	  $.messager.alert('更新信息','操作成功!','info');
        },error:function(data){
        	$.messager.alert('更新信息','操作失败!','info');
        }
      });
}
function reset(){
	$('#add_module_form').form().find('input').val('');
	$('#descn').val('');
}
</script>
</html>
