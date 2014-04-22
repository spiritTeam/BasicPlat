<%@page language="java" contentType="text/html; charset=UTF-8" pageEncoding="UTF-8"%>
<%
  String path = request.getContextPath();
  String username = (String)session.getAttribute("username");
%>
<html>
<head>
  <jsp:include page="/common/sysInclude.jsp" flush="true"/>
  <style type="text/css">
    .divcss5{
      position:relative;  
      top:50px; 
      align:"center";
      width:400px;
      margin:0 auto;
      border:0px solid #95B8E7}
  </style>
</head>
<body>
  <!-- 上传div -->
  <div ></div>
  <div style="position:relative;top:2%;width:100%;height:13%;">
    <table>
      <tr>
        <td colspan="1" rowspan="2" align="center"><span style="font-size: 15px;font-weight: bolder;">&nbsp;&nbsp;导入Excel数据:</span></td><td></td><td></td><td></td><td></td>
      </tr>
       <tr>
        <td></td><td></td><td></td><td></td><td></td>
      </tr>
      <tr>
        <td align="right">请选择文件</td><td><input type="file"/></td><td><input type="checkbox"/>首行包含标题</td><td><input type="button" value="开始导入" onclick="startImport()"/></td><td><div id="p" class="easyui-progressbar" style="width:200px;height:18px;"></div></td>
      </tr>
    </table>
  </div>
  <div style="width:100%;height:85%;border:0px solid #95B8E7;">
    <div title="简报列表">
	    <div style="height:8%;">
	      &nbsp;&nbsp;<input id="briefType" name="briefType" style="height:20px;width:100px;">&nbsp;
	      <input id="bft" name="bft" style="height:20px;width:100px;">&nbsp;
	            全文检索&nbsp;<input id="searchKey" name="searchKey" type="text" class="easyui-validatebox combo combo-text" style="height:20px;width:100px;">
	    </div>
	    <div style="height:85%;border:1px solid #95B8E7;border-width:1px 0px 0px 0px ;">
	      <table id="datagrid"></table>
	    </div>
    </div>
  </div>
</body>
<script type="text/javascript">
var a=true, b=true, searchKey,comboboxId='';
$(function(){
  getBriefType();
  getBft();
  getDg(comboboxId,searchKey);
  //alert($('#briefType').combobox('getValue'));
  $("#searchKey").blur(function(){
	  searchKey = $('#searchKey').val();
	  alert("blur");
	  getDg(comboboxId,searchKey);
  });
});
//下拉框简报类别：学生，老师。。
function getBft(){
  $('#bft').combobox({    
    url:'<%=path%>/excel/comboboxData/bfT.json',    
    valueField:'id',    
    textField:'text'   
 });
}
//下拉框简报类别：年报，周报+全文检索
function getBriefType(){
  $('#briefType').combobox({    
    url:'<%=path%>/excel/comboboxData/briefType.json',    
    valueField:'id',    
    textField:'text',
    onSelect: function(a) {
      comboboxId = $('#briefType').combobox('getValue');
      searchKey = $('#searchKey').val();
      getDg(comboboxId,searchKey);
    }
  });
}
//简报列表
function getDg(comboboxId,searchKey){
	var _url;
	if(searchKey==""||searchKey==null){
		if(comboboxId==""||comboboxId==null){
			if(a){
				_url='<%=path %>/getBriefList.do';
				a=!a;
			}else{
				return;
			}
		}else{
			if(b){
				alert(1111);
				_url='<%=path %>/getBriefTypeList.do'+'?comboboxId='+comboboxId;
				b=!b;
			}else{
				return;
			}
		}
	}else{
		alert(444);
		if(comboboxId==""||comboboxId==null){
		  alert(2222);
	    _url='<%=path %>/getBriefTypeList.do'+'?searchKey='+searchKey;
		}else{
			alert(3333);
			_url='<%=path %>/getBriefTypeList.do'+'?comboboxId='+comboboxId+'&'+'searchKey='+searchKey;
		}
	
	}
	alert(_url);
	initDG(_url);
	if(comboboxId=="5"){
		$('#searchKey').val("");
	}
}
function initDG(_url){
	$("#datagrid").datagrid({
    url:_url,
    title:'简报列表',
    iconCls:'icon-save',
    pagination:true,
    pageSize:10,
    pageList:[10,20,30],
    fit:true,
    fitColumns:true,
    nowrap:false,
    border:true,
    idField:'id',
    columns:[[{
      title:'序号',
      field: 'id',
      width: 99
    },{
      title:'简报名称',
      field:'briefName',
      width:99
    },{
      title:'制作人',
      field:'producer',
      width:99
    },{
      title:'生成日期',
      field:'createTime',
      width:99
    },{
      title:'数据源类型',
      field:'sourceType',
      width:99
    },{
      title:'简报类型',
      field:'briefType',
      width:99
    }]]
  });
}
//进度条
function startImport(){
  var value = $('#p').progressbar('getValue');
  if (value < 100){
    value += Math.floor(Math.random() * 10);
    $('#p').progressbar('setValue', value);
    //次参数控制多长时间完成
    setTimeout(arguments.callee, 50);
  }
  else{
    $.messager.show({
      title:'上传信息',
      msg:'上传成功',
      timeout:2000,
      showType:'slide'
    });
    $('#p').progressbar('setValue',0);
  }
}
</script>
</html>