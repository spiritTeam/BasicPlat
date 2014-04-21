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
    <div style="position:relative;  
        top:2%;width:100%;height:13%;">
      <table>
        <tr>
          <td colspan="1" rowspan="2" align="center"><span style="font-size: 15px;font-weight: bolder;">&nbsp;&nbsp;导入Excel数据:</span></td><td></td><td></td><td></td><td></td>
        </tr>
         <tr>
          <td></td><td></td><td></td><td></td><td></td>
        </tr>
        <tr>
          <td align="right">请选择文件</td><td><input type="file"/></td><td><input type="checkbox"/>首行包含标题</td><td><input type="button" value="开始导入"/></td><td><div id="p" class="easyui-progressbar" data-options="value:60" style="width:300px;"></div></td>
        </tr>
      </table>
    </div>
    <!-- 概览+消息div -->
    <div style="width:25%;height:85%;display:inline;float:left;" style="border:1px solid #95B8E7; border-width:1px 1px 1px 1px;">
      <!-- 简报模版 -->
      <iframe src="briefStencil.jsp"  style="border:1px solid #95B8E7;border-width:0px 0px 1px 0px ;width:100%;height:50%;"></iframe>
      <!-- 消息中心 -->
      <iframe src="news.jsp"  style="border:1px solid #95B8E7;border-width:0px 0px 1px 0px ;width:100%;height:50%;"></iframe>
      <!--<div class="easyui-layout" data-options="fit:true,border:false"  >
        <div data-options="region:'north',title:'我的简报模版',split:false,border:true,collapsible:false" style="height:150px;border-width:0px 0px 0px 0px;" >
          <table id="dg"></table>
        </div>   
        <div data-options="region:'center',title:'我的消息',fit:true,border:true" style="height:80px;border-width:0px 0px 1px 0px;"></div>   
      </div> -->
    </div>
    <!-- 简报列表div --> 
    <div style="width:75%;height:85%;display:inline;float:left;border:0px solid #95B8E7;">
      <iframe src="briefList.jsp"  style="border:1px solid #95B8E7;width:100%;height:90%;"></iframe>
      <!--<div class="easyui-layout" data-options="fit:true,border:true" style="border-width:1px 1px 1px 0px;">   
         <div data-options="region:'west',title:'概览',split:false,border:true,collapsible:false" style="width:200px;border-width:0px 0px 0px 1px;"></div>   
        <div data-options="region:'center',title:'简报列表',fit:true,border:true,href:'briefList.jsp'" style="border-width:0px 0px 0px 1px;"></div>
      </div>--> 
    </div>
  </body>
  <script type="text/javascript">
  $(function(){
	  $('#dg').datagrid({    
		    url:'datagrid_data.json',    
		    columns:[[    
		        {field:'code',title:'Code',width:100}    
		    ]]    
		});  
  });
  </script>
</html>