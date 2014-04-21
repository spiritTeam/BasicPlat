<%@page language="java" contentType="text/html; charset=UTF-8" pageEncoding="UTF-8"%>
<%
  String path = request.getContextPath();
%>
<jsp:include page="/common/sysInclude.jsp" flush="true"/>
<div class="easyui-tabs"  border="false" style="height:80%;width:100%;">
  <div title="消息中心" border="false">
    <div style="width:100%;height:1px;border:1px solid #95B8E7;border-width:0px 0px 1px 0px ;"></div>
    <table id="datagrid" ></table>
  </div>
 
</div> 
<div style="height:20%;width:100%;">
  <a href="#">更多</a>
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
     pagination:false,
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
