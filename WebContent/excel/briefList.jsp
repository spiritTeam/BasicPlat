<%@page language="java" contentType="text/html; charset=UTF-8" pageEncoding="UTF-8"%>
<%
  String path = request.getContextPath();
%>
<jsp:include page="/common/sysInclude.jsp" flush="true"/>
<div class="easyui-tabs" fit="true" border="false">
  <div title="简报列表" border="false">
    <!-- 搜索div
    <input type="button" value="全部">&nbsp;
    <input type="button" value="日报">&nbsp;
    <input type="button" value="周报">&nbsp;
    <input type="button" value="月报">&nbsp;
    <input type="button" value="半年报">&nbsp;
    <input type="button" value="年报">&nbsp; -->
    <div>
      <div style="height:2px;"></div>
	    &nbsp;&nbsp;<input id="briefType" name="briefType" style="height:20px;width:100px;">&nbsp;
	    <input id="bft" name="bft" style="height:20px;width:100px;">&nbsp;
	        全文检索&nbsp;<input type="text" class="easyui-validatebox combo combo-text" style="height:20px;width:100px;">
      <div style="height:2px;"></div>
    </div>
    <div style="width:100%;height:1px;border:1px solid #95B8E7;border-width:0px 0px 1px 0px ;"></div>
    <table id="datagrid" ></table>
  </div>
</div>
<script type="text/javascript">
var datagrid;
$(function(){
	getBriefType();
	getBft();
	getDg();
});
function getBft(){
	$('#bft').combobox({    
    url:'<%=path%>/excel/comboboxData/bfT.json',    
    valueField:'id',    
    textField:'text'   
 });
}
function getBriefType(){
	$('#briefType').combobox({    
    url:'<%=path%>/excel/comboboxData/briefType.json',    
    valueField:'id',    
    textField:'text'   
  });
}
function getDg(){
 $("#datagrid").datagrid({
     url:'<%=path%>/getBriefList.do',
     title:'简报列表',
     iconCls:'icon-save',
     //显示分页框
     pagination:true,
     pageSize:10,
     pageList:[10,20,30],
     fit:true,
     //是否出现横向滚动条
     fitColumns:true,
     //超出文本框是否折行
     nowarp:false,
     border:false,
     //标识，记录选中项，不受翻页影响
     idField:'id',
     //支持多级表头
     columns:[[{
       title:'序号',
       field:'id',
       //必须要给宽
       width:100
     },{
       title:'简报名称',
       field:'briefName',
       //必须要给宽
       width:100
     },{
       title:'制作人',
       field:'producer',
       //必须要给宽
       width:100
     },{
       title:'生成日期',
       field:'createTime',
       //必须要给宽
       width:100
     },{
       title:'数据源类型',
       field:'sourceType',
       //必须要给宽
       width:100
     },{
       title:'简报类型',
       field:'briefType',
       //必须要给宽
       width:100
     }]]
   });
}
</script>
