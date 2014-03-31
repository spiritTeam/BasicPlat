<%@page language="java" contentType="text/html; charset=UTF-8" pageEncoding="UTF-8"%>
<%
  String path = request.getContextPath();
%>
<!DOCTYPE html PUBLIC "-//W3C//DTD HTML 4.01 Transitional//EN" "http://www.w3.org/TR/html4/loose.dtd">
<html>
<head>
<jsp:include page="/common/sysInclude.jsp" flush="true"/>
<script src="<%=path%>/DIS_jsonData/dataResource.json" type="text/javascript"></script>
<title>数据源管理</title>
<style>
body {margin:0 auto;}
.icon-1   {background:url('<%=path%>/resources/images/disIcon/1.png') no-repeat;}
.icon-101 {background:url('<%=path%>/resources/images/disIcon/101.png') no-repeat;}
.icon-2   {background:url('<%=path%>/resources/images/disIcon/2.png') no-repeat;}
.icon-201 {background:url('<%=path%>/resources/images/disIcon/201.gif') no-repeat;}
.icon-202 {background:url('<%=path%>/resources/images/disIcon/201.gif') no-repeat;}
.icon-3   {background:url('<%=path%>/resources/images/disIcon/3.png') no-repeat;}
.icon-301 {background:url('<%=path%>/resources/images/disIcon/301.png') no-repeat;}
.icon-a1 {background:url('<%=path%>/resources/images/disIcon/a1.png') no-repeat;}
.icon-a2 {background:url('<%=path%>/resources/images/disIcon/a2.gif') no-repeat;}
.icon-a3 {background:url('<%=path%>/resources/images/disIcon/a3.png') no-repeat;}
.icon-view {background:url('<%=path%>/resources/images/disIcon/view.png') no-repeat;}
.toolbar {height: 27px; background-color: #E0ECFF; text-align: right; padding:1px 1px 0 0; border-bottom: 1px solid #99bbe8;}
#drTree {margin:0 auto;overflow:auto;}
.formTable th{width:120px; text-align:right; font-size:14px; font-weight:bold; padding-right:5px; padding-top:10px; border:0;}
.formTable td{padding-top:10px; border:0;}
.requiredTh{width:120px; text-align:right; font-size:14px; font-weight:bold; padding-right:5px; padding-top:10px; border:0; color:red;}
.toolSpace {float:right; margin-right:5px; margin-top:4px; margin-left:2px; width:1px; border-right:1px solid #8ebbda; border-left:1px solid #8ebbda;}
</style>
</head>
<body class="easyui-layout" style="margin-bottom:6px;">
  <div id="left" data-options="region:'west',split:true,title:'数据源',collapsible:false,border:true,minWidth:200,maxWidth:350" style="width:280px;padding:0px;overflow:false">
    <div id="drToolbar" class="toolbar">
      <a href="#" id="dr_MBadd"  class="easyui-menubutton" data-options="menu:'#mmDrAdd',iconCls:'icon-add'">添加</a>
      <a href="#" id="dr_MBedit" class="easyui-linkbutton" data-options="plain:true,iconCls:'icon-edit'" onclick="edit()">修改</a>
      <a href="#" id="dr_MBdel"  class="easyui-linkbutton" data-options="plain:true,iconCls:'icon-remove'">删除</a>
    </div>
    <div id="mmDrAdd" style="width:100px;">
      <div data-options="iconCls:'icon-a1'" onclick="addDB()">添加数据库</div>
      <div data-options="iconCls:'icon-a2'" onclick="addFile()">添加文件系统</div>
      <div data-options="iconCls:'icon-a3'" onclick="addShape()">添加空间数据</div>
    </div>
    <div id="drTree" class="easyui-tree tree"></div>
    <div id="pAttr" class="easyui-panel" title="&nbsp;属性" style="width:273px;height:120px;padding:10px;" data-options="collapsible:true,border:false">
    </div>
  </div>
  <div data-options="region:'center',title:'详情',border:false">
    <div id="dd_DBToolbar" class="toolbar">
      <div style="float:right;">
        <span>类型:</span>
        <select class="easyui-combobox" panelHeight="auto" style="width:100px;margin-top:-2px;">
          <option value="all">全部</option>
          <option value="table">表</option>
          <option value="view">视图</option>
        </select>
        <a href="#" class="easyui-linkbutton" iconCls="icon-search" plain="true"></a>
      </div>
      <span class="toolSpace" style="height:20px;"></span>
      <a id="db_MBview" href="#" class="easyui-linkbutton" iconCls="icon-view" plain="true" style="float:right;" onclick="showDb()">查看</a>
    </div>
    <div id="dd_DBtvDiv"><table class="easyui-datagrid" id="dd_DBtableview"
      data-options="url:'<%=path%>/DIS_jsonData/dr_DB_tableview.json',
        method:'get',border:false,singleSelect:true,fitColumns:true,rownumbers:true,
        pagination:true,pageSize:10">
      <thead>
        <tr>
          <th data-options="field:'dbsOType',halign:'center',align:'center'" width="80"><strong>类型</strong></th>
          <th data-options="field:'dbsOName',halign:'center',align:'left'" width="100"><strong>名称</strong></th>
          <th data-options="field:'dbsOCount',halign:'center',align:'right'" width="80"><strong>记录条数</strong></th>
          <th data-options="field:'dbsOmemo',halign:'center',align:'left'" width="80"><strong>说明</strong></th>
        </tr>
      </thead>
    </table></div>

    <div id="dd_FILEToolbar" class="toolbar">
      <div style="float:right;">
        <span>类型:</span>
        <select class="easyui-combobox" panelHeight="auto" style="width:100px;margin-top:-2px;">
          <option value="all">全部</option>
          <option value="dir">目录</option>
          <option value="file">文件</option>
        </select>
        <span>路径：</span><input class="easyui-validatebox" style="width:150px;" type="text" name="server"></input>
        <a href="#" class="easyui-linkbutton" iconCls="icon-search" plain="true"></a>
      </div>
      <span class="toolSpace" style="height:20px;"></span>
      <a id="file_MBview" href="#" class="easyui-linkbutton" iconCls="icon-view" plain="true" style="float:right;" onclick="showFile()">查看</a>
    </div>
    <div id="dd_FILEvDiv"><table class="easyui-treegrid" id="dd_FILEview"
      data-options="url:'<%=path%>/DIS_jsonData/dr_FILE.json',
        method:'get',border:false,singleSelect:true,fitColumns:true,rownumbers:true,
        pagination:false,idField:'id',treeField:'name'">
      <thead>
        <tr>
          <th data-options="field:'name',halign:'center',align:'left'" width="180"><strong>名称</strong></th>
          <th data-options="field:'size',halign:'center',align:'right'" width="80"><strong>文件大小</strong></th>
          <th data-options="field:'date',halign:'center',align:'left'" width="80"><strong>修改时间</strong></th>
        </tr>
      </thead>
    </table></div>

    <div id="dd_SHAPEToolbar" class="toolbar">
      <div style="float:right;">
        <span>类型:</span>
        <select class="easyui-combobox" panelHeight="auto" style="width:100px;margin-top:-2px;">
          <option value="all">全部</option>
          <option value="table">点</option>
          <option value="view">线</option>
          <option value="view">面</option>
        </select>
        <a href="#" class="easyui-linkbutton" iconCls="icon-search" plain="true"></a>
      </div>
      <span class="toolSpace" style="height:20px;"></span>
      <a id="shape_MBview" href="#" class="easyui-linkbutton" iconCls="icon-view" plain="true" style="float:right;" onclick="showShape()">查看</a>
    </div>
    <div id="dd_SHAPEvDiv"><table class="easyui-datagrid" id="dd_SHAPEview"
      data-options="url:'<%=path%>/DIS_jsonData/dr_SHAPE.json',
        method:'get',border:false,singleSelect:true,fitColumns:true,rownumbers:true,
        pagination:true,pageSize:10">
      <thead>
        <tr>
          <th data-options="field:'dbsSType',halign:'center',align:'center'" width="80"><strong>类型</strong></th>
          <th data-options="field:'dbsSName',halign:'center',align:'left'" width="100"><strong>名称</strong></th>
          <th data-options="field:'dbsSCount',halign:'center',align:'right'" width="80"><strong>记录条数</strong></th>
          <th data-options="field:'dbsSmemo',halign:'center',align:'left'" width="80"><strong>说明??</strong></th>
        </tr>
      </thead>
    </table></div>
  </div>

<!-- 以下为添加/编辑窗口 -->
<!-- 数据库  -->
<div id="w_DB" class="easyui-window" style="width:350px;height:390px;padding:0px;"
  closed="true" collapsible="false" minimizable="false" maximizable="false" closable="true" scrolling="no" resizable="false">
  <div class="easyui-layout" fit="true">
    <div region="center" border="false" style="padding:0px;background:#fff;border-bottom:1px solid #ccc; padding-top:10px;">
      <table class="formTable">
        <tr>
          <th>数据源名称:</th>
          <td><input class="easyui-validatebox" style="width:150px;" type="text" name="server"></input></td>
        </tr>
        <tr>
          <th>数据库类型:</th>
          <td><select class="easyui-combobox" name="dbType" style="width:156px;">
            <option value="Oracle">Oracle</option>
            <option value="MySQL">MySQL</option>
            <option value="DB2">DB2</option>
            <option value="SQLServer">SQL Server</option>
          </select></td>
        </tr>
        <tr>
          <th>服务器(ip/名称):</th>
          <td><input class="easyui-validatebox" style="width:150px;" type="text" name="server"></input></td>
        </tr>
        <tr>
          <th>实例名称:</th>
          <td><input class="easyui-validatebox" style="width:150px;" type="text" name="SID"></input></td>
        </tr>
        <tr>
          <th>端口号:</th>
          <td><input class="easyui-validatebox" style="width:150px;" type="text" name="portNum"></input></td>
        </tr>
        <tr>
          <th>用户名:</th>
          <td><input class="easyui-validatebox" style="width:150px;" type="text" name="userName"></input></td>
        </tr>
        <tr>
          <th>密　码:</th>
          <td><input class="easyui-validatebox" style="width:150px;" type="text" name="password"></input></td>
        </tr>
        <tr>
          <th></th><td></td>
        </tr>
        <tr>
          <td colspan=2 style="padding-left:25px;">连接URL<input class="easyui-validatebox" style="width:206px;" type="text" name="connUrl"></input></td>
        </tr>
      </table>
    </div>
    <div region="south" border="false" style="text-align:center;height:40px;line-height:35px;">
      <a class="easyui-linkbutton" data-options="plain:false" href="#" onclick="saveFolder()">确定</a>&nbsp;&nbsp;
      <a class="easyui-linkbutton" data-options="plain:false" href="#" onclick="saveFolder()">测试链接</a>&nbsp;&nbsp;
      <a class="easyui-linkbutton" data-options="plain:false" href="#" onclick="closeW('w_DB')">取消</a>
    </div>
  </div>
</div>

<!-- 文件  -->
<div id="w_FILE" class="easyui-window" style="width:350px;height:310px;padding:0px;"
  closed="true" collapsible="false" minimizable="false" maximizable="false" closable="true" scrolling="no" resizable="false">
  <div class="easyui-layout" fit="true">
    <div region="center" border="false" style="padding:0px;background:#fff;border-bottom:1px solid #ccc; padding-top:10px;">
      <table class="formTable">
        <tr>
          <th>数据源名称:</th>
          <td><input class="easyui-validatebox" style="width:150px;" type="text" name="server"></input></td>
        </tr>
        <tr>
          <th>文件类型:</th>
          <td><select class="easyui-combobox" name="fType" id="fType" style="width:156px;">
            <option value="ftp">FTP</option>
            <option value="remoteDir">远程目录</option>
          </select></td>
        </tr>
        <tr>
          <th>服务器(ip/名称):</th>
          <td><input class="easyui-validatebox" style="width:150px;" type="text" name="server"></input></td>
        </tr>
        <tr>
          <th id="fport_name">端口号:</th>
          <td><input id="file_port" class="easyui-validatebox" style="width:150px;" type="text" name="portNum"></input></td>
        </tr>
        <tr>
          <th>用户名:</th>
          <td><input class="easyui-validatebox" style="width:150px;" type="text" name="userName"></input></td>
        </tr>
        <tr>
          <th>密　码:</th>
          <td><input class="easyui-validatebox" style="width:150px;" type="text" name="password"></input></td>
        </tr>
      </table>
    </div>
    <div region="south" border="false" style="text-align:center;height:40px;line-height:35px;">
      <a class="easyui-linkbutton" data-options="plain:false" href="#" onclick="saveFolder()">确定</a>&nbsp;&nbsp;
      <a class="easyui-linkbutton" data-options="plain:false" href="#" onclick="saveFolder()">测试链接</a>&nbsp;&nbsp;
      <a class="easyui-linkbutton" data-options="plain:false" href="#" onclick="closeW('w_FILE')">取消</a>
    </div>
  </div>
</div>

<!-- 空间  -->
<div id="w_SHAPE" class="easyui-window" style="width:350px;height:310px;padding:0px;"
  closed="true" collapsible="false" minimizable="false" maximizable="false" closable="true" scrolling="no" resizable="false">
  <div class="easyui-layout" fit="true">
    <div region="center" border="false" style="padding:0px;background:#fff;border-bottom:1px solid #ccc; padding-top:10px;">
      <table class="formTable">
        <tr>
          <th>数据源名称:</th>
          <td><input class="easyui-validatebox" style="width:150px;" type="text" name="server"></input></td>
        </tr>
        <tr>
          <th>空间数据类型:</th>
          <td><select class="easyui-combobox" name="sType" id="sType" style="width:156px;">
            <option value="Esri">Esri SDE</option>
            <option value="superMap">SuperMap SDX+</option>
          </select></td>
        </tr>
        <tr>
          <th>服务器(ip/名称):</th>
          <td><input class="easyui-validatebox" style="width:150px;" type="text" name="server"></input></td>
        </tr>
        <tr>
          <th id="sport_name">实例/端口号:</th>
          <td><input id="shape_port" class="easyui-validatebox" style="width:150px;" type="text" name="portNum" value="5151"></input></td>
        </tr>
        <tr>
          <th>用户名:</th>
          <td><input class="easyui-validatebox" style="width:150px;" type="text" name="userName"></input></td>
        </tr>
        <tr>
          <th>密　码:</th>
          <td><input class="easyui-validatebox" style="width:150px;" type="text" name="password"></input></td>
        </tr>
      </table>
    </div>
    <div region="south" border="false" style="text-align:center;height:40px;line-height:35px;">
      <a class="easyui-linkbutton" data-options="plain:false" href="#" onclick="saveFolder()">确定</a>&nbsp;&nbsp;
      <a class="easyui-linkbutton" data-options="plain:false" href="#" onclick="saveFolder()">测试链接</a>&nbsp;&nbsp;
      <a class="easyui-linkbutton" data-options="plain:false" href="#" onclick="closeW('w_SHAPE')">取消</a>
    </div>
  </div>
</div>

<!-- 以下为浏览页面 -->
<div id="w_dDB" class="easyui-window" style="width:600px;height:400px;padding:0px;"
  closed="true" collapsible="false" minimizable="false" maximizable="false" closable="true" scrolling="no" resizable="false">
  <div class="easyui-layout" fit="true">
    <div region="center" border="false" style="padding:0px;background:#fff;border-bottom:1px solid #ccc;overflow:hidden;">
      <div id="wd_SHAPETabBar" class="easyui-tabs" data-options="fit:true,border:false">
        <div title="列">
          <table class="easyui-datagrid"
            data-options="url:'<%=path%>/DIS_jsonData/dr_DB_tablecolumn.json',
            method:'get',border:false,singleSelect:true,fitColumns:true,rownumbers:true,
            pagination:true,pageSize:10,fit:true">
          <thead>
            <tr>
              <th data-options="field:'db_tcname',halign:'center',align:'left'" width="80"><strong>名称</strong></th>
              <th data-options="field:'db_tctype',halign:'center',align:'left'" width="100"><strong>类型</strong></th>
              <th data-options="field:'db_tcnull',halign:'center',align:'center'" width="80"><strong>是否为空</strong></th>
              <th data-options="field:'db_tcmemo',halign:'center',align:'left'" width="100"><strong>说明</strong></th>
            </tr>
          </thead>
          </table>
        </div>
        <div title="键及索引">
          <table class="easyui-datagrid"
            data-options="url:'<%=path%>/DIS_jsonData/dr_DB_keyindex.json',
            method:'get',border:false,singleSelect:true,fitColumns:true,rownumbers:true,
            pagination:true,pageSize:10,fit:true">
          <thead>
            <tr>
              <th data-options="field:'db_tkiname',halign:'center',align:'left'" width="80"><strong>名称</strong></th>
              <th data-options="field:'db_tkitype',halign:'center',align:'left'" width="100"><strong>类型</strong></th>
              <th data-options="field:'db_tkirel',halign:'center',align:'left'" width="80"><strong>相关对象</strong></th>
            </tr>
          </thead>
          </table>
        </div>
      </div>
    </div>
    <div region="south" border="false" style="text-align:center;height:40px;line-height:35px;">
      <a class="easyui-linkbutton" data-options="plain:false" href="#" onclick="closeW('w_dDB')">关闭</a>&nbsp;
    </div>
  </div>
</div>

<div id="w_dFILE" class="easyui-window" style="width:400px;height:360px;padding:0px;"
  closed="true" collapsible="false" minimizable="false" maximizable="false" closable="true" scrolling="no" resizable="false">
  <div class="easyui-layout" fit="true">
    <div region="center" border="false" style="padding:0px;background:#fff;border-bottom:1px solid #ccc; padding-top:10px;overflow:hidden;">
      <table>
        <tr><td>
          <span style="padding-left:20px">文件地址：</span>
          <input class="easyui-validatebox" style="width:260px;" id="wd_filename" type="text" name="wd_filename" readonly value=""></input>
        </td></tr>
        <tr><td style="padding-left:10px;padding-top:10px">
          <div style="height:220px;width:360px;border:1px solid #ccc;">文章内容</div>
        </td></tr>
      </table>
    </div>
    <div region="south" border="false" style="text-align:center;height:40px;line-height:35px;">
      <a class="easyui-linkbutton" data-options="plain:false" href="#" onclick="closeW('w_dFILE')">关闭</a>&nbsp;
    </div>
  </div>
</div>

<div id="w_dSHAPE" class="easyui-window" style="width:600px;height:400px;padding:0px;"
  closed="true" collapsible="false" minimizable="false" maximizable="false" closable="true" scrolling="no" resizable="false">
  <div class="easyui-layout" fit="true">
    <div region="center" border="false" style="padding:0px;background:#fff;border-bottom:1px solid #ccc;overflow:hidden;">
      <div id="wd_SHAPETabBar" class="easyui-tabs" data-options="fit:true,border:false">
        <div title="列">
          <table class="easyui-datagrid"
            data-options="url:'<%=path%>/DIS_jsonData/dr_DB_tablecolumn.json',
            method:'get',border:false,singleSelect:true,fitColumns:true,rownumbers:true,
            pagination:true,pageSize:10,fit:true">
          <thead>
            <tr>
              <th data-options="field:'db_tcname',halign:'center',align:'left'" width="80"><strong>名称</strong></th>
              <th data-options="field:'db_tctype',halign:'center',align:'left'" width="100"><strong>类型</strong></th>
              <th data-options="field:'db_tcnull',halign:'center',align:'center'" width="80"><strong>是否为空</strong></th>
              <th data-options="field:'db_tcmemo',halign:'center',align:'left'" width="100"><strong>说明</strong></th>
            </tr>
          </thead>
          </table>
        </div>
        <div title="键及索引">
          <table class="easyui-datagrid"
            data-options="url:'<%=path%>/DIS_jsonData/dr_DB_keyindex.json',
            method:'get',border:false,singleSelect:true,fitColumns:true,rownumbers:true,
            pagination:true,pageSize:10,fit:true">
          <thead>
            <tr>
              <th data-options="field:'db_tkiname',halign:'center',align:'left'" width="80"><strong>名称</strong></th>
              <th data-options="field:'db_tkitype',halign:'center',align:'left'" width="100"><strong>类型</strong></th>
              <th data-options="field:'db_tkirel',halign:'center',align:'left'" width="80"><strong>相关对象</strong></th>
            </tr>
          </thead>
          </table>
        </div>
        <div title="图" style="padding:10px;overflow:hidden">
          <div style="height:272px;width:565px;border:1px solid #ccc;"><img id="shapeImg" src="" style="height:272px;width:565px;border:0;"></img></div>
        </div>
      </div>
    </div>
    <div region="south" border="false" style="text-align:center;height:40px;line-height:35px;">
      <a class="easyui-linkbutton" data-options="plain:false" href="#" onclick="closeW('w_dSHAPE')">关闭</a>&nbsp;
    </div>
  </div>
</div>
</body>
<script>
var winHDeta=0;
$(function() {
  //页面调整设置
  $("#left").panel({
    onResize:function(width,height) {
      $("#pAttr").parent().find(".panel-header").css("height", "19px");
      $("#pAttr").panel("resize", {"width":width-6});
      $("#dd_DBtableview").datagrid("resize", {
        height:$(window).height()-$("#dd_DBToolbar").height()-28,
        width:$(window).width()-width
      });
      $("#dd_FILEview").treegrid("resize", {
        height:$(window).height()-$("#dd_FILEToolbar").height()-28,
        width:$(window).width()-width-1
      });
      $("#dd_SHAPEview").datagrid("resize", {
        height:$(window).height()-$("#dd_SHAPEToolbar").height()-28,
        width:$(window).width()-width-1
      });
    }
  });
  $(window).resize(adjustSize);
  $("div[data-options^='region:\'center\'']").parent().css("border-left", "1px solid #95B8E7");
  $("div[data-options^='region:\'west\'']").parent().find(".panel-header").css("border-top", "0px").css("border-left", "0px");
  $("div[data-options^='region:\'west\'']").parent().find(".panel-body").css("border-bottom", "0px").css("border-left", "0px").css("overflow", "hidden");
  $("div[data-options^='region:\'center\'']").parent().find(".panel-body").css("overflow", "hidden");
  $("#drTree").css("border-bottom", "1px solid #95b8e7");
  $("#pAttr").parent().find(".panel-header").css("height", "19px");
  $("#dd_DBToolbar").hide();
  $("#dd_DBtvDiv").hide();
  $("#dd_FILEToolbar").hide();
  $("#dd_FILEvDiv").hide();
  $("#dd_SHAPEToolbar").hide();
  $("#dd_SHAPEvDiv").hide();
  adjustSize();

  $("#pAttr").panel("collapse");
  $("#pAttr").panel({
    onBeforeCollapse:function() {
      $("#drTree").css("height", ($(window).height()-winHDeta-55)+"px");
    },
    onBeforeExpand:function() {
      $("#drTree").css("height", ($(window).height()-winHDeta-175)+"px");
    }
  });

  $("#drTree").tree({data:dataResource});

  $("#fType").combo({
    onChange:function(_new, _old) {
      if (_new=="ftp") {
        $("#fport_name").html("端口号:");
      } else {
        $("#fport_name").html("根目录:");
      }
    }
  });
  $("#sType").combo({
    onChange:function(_new, _old) {
      if (_new=="Eris") {
        $("#shape_port").val("5151");
        $("#sport_name").html("实例/端口号:");
      } else {
        $("#shape_port").val("");
        $("#sport_name").html("端口号:");
      }
    }
  });

  $("#drTree").tree({
    onSelect: function(node) {
      if (node.attributes) $("#dr_MBedit,#dr_MBdel").linkbutton("enable");
      else $("#dr_MBedit,#dr_MBdel").linkbutton("disable");
      //修改属性窗口
      if (node.attributes) {
        $("#pAttr").panel({
          title: "&nbsp;属性-"+node.text,
          iconCls: function () {
            if (node.attributes.type=="DB") return "icon-101";
            if (node.attributes.type=="FILE") return "icon-201";
            if (node.attributes.type=="SHAPE") return "icon-301";
          }
        });
        var _content = "";
        if (node.attributes.type=="DB") {
          _content += "数据库类型："+node.attributes.DBtype;
          _content += "<br>连接URL："+node.attributes.connUrl;
          _content += "<br>用户名："+node.attributes.userName;
          _content += "<br>密　码："+node.attributes.password;
          $("#dd_DBToolbar").show();
          $("#dd_DBtableview").datagrid("resize", {
            height:$(window).height()-$("#dd_DBToolbar").height()-28,
            width:$(window).width()-$("#left").width()
          });
          $("#dd_DBtvDiv").show();
          $("#dd_DBtableview").datagrid("reload");
          $("#dd_FILEToolbar").hide();
          $("#dd_FILEvDiv").hide();
          $("#dd_SHAPEToolbar").hide();
          $("#dd_SHAPEvDiv").hide();
        }
        if (node.attributes.type=="FILE") {
          _content += "文件类型："+node.attributes.FILEtype;
          if (node.attributes.FILEtype=="ftp") {
            _content += "<br>URI："+node.attributes.connUrl;
            _content += "<br>端口号："+node.attributes.port;
          }
          if (node.attributes.FILEtype=="remoteDir") {
            _content += "<br>远程目录："+node.attributes.connUrl;
          }
          _content += "<br>用户名："+node.attributes.userName;
          _content += "<br>密　码："+node.attributes.password;
          $("#dd_DBToolbar").hide();
          $("#dd_DBtvDiv").hide();
          $("#dd_FILEToolbar").show();
          $("#dd_FILEview").treegrid("resize", {
            height:$(window).height()-$("#dd_FILEToolbar").height()-28,
            width:$(window).width()-$("#left").width()-8
          });
          $("#dd_FILEvDiv").show();
          $("#dd_FILEview").treegrid("reload");
          $("#dd_SHAPEToolbar").hide();
          $("#dd_SHAPEvDiv").hide();
        }
        if (node.attributes.type=="SHAPE") {
          _content += "空间数据类型："+node.attributes.SHAPEtype;
          _content += "<br>服务器："+node.attributes.server;
          _content += "<br>端口："+node.attributes.port;
          _content += "<br>用户名："+node.attributes.userName;
          _content += "<br>密　码："+node.attributes.password;
          $("#dd_DBToolbar").hide();
          $("#dd_DBtvDiv").hide();
          $("#dd_FILEToolbar").hide();
          $("#dd_FILEvDiv").hide();
          $("#dd_SHAPEToolbar").show();
          $("#dd_SHAPEview").datagrid("resize", {
            height:$(window).height()-$("#dd_SHAPEToolbar").height()-28,
            width:$(window).width()-$("#left").width()
          });
          $("#dd_SHAPEvDiv").show();
          $("#dd_SHAPEview").datagrid("reload");
        }
      $("#pAttr").panel({
        content: _content
      });
        $("#pAttr").parent().find(".panel-header").css("height", "19px");
      }
    }
  });

  $("#dd_DBtableview").datagrid({
    onClickRow: function(rowIndex, rowData) {
      $("#db_MBview").linkbutton("enable");
    },
    onLoadSuccess: function(data) {
      $("#db_MBview").linkbutton("disable");
    }
  });
  $("#file_MBview").linkbutton("disable");
  $("#dd_FILEview").treegrid({
    onSelect: function(node) {
      if ($("#dd_FILEview").treegrid("getChildren", node.id).length==0) {
        $("#file_MBview").linkbutton("enable");
      } else {
        $("#file_MBview").linkbutton("disable");
      }
    },
    onLoadSuccess: function(data) {
      $("#file_MBview").linkbutton("disable");
    }
  });
  $("#dd_SHAPEview").datagrid({
    onClickRow: function(rowIndex, rowData) {
      $("#shape_MBview").linkbutton("enable");
    },
    onLoadSuccess: function(data) {
      $("#shape_MBview").linkbutton("disable");
    }
  });
});
//调整布局大小
function adjustSize() {
  if (winHDeta==0) {
    winHDeta=$(window).height()-$("#left").height()+$("#dbToolbar").height()+2;
    winWDeta=$("#left").width()+6;
  }
  if ($("#pAttr").panel("options").collapsed)
    $("#drTree").css("height", ($(window).height()-winHDeta-55)+"px");
  else
    $("#drTree").css("height", ($(window).height()-winHDeta-175)+"px");
  $("#dd_DBtableview").datagrid("resize", {
    height:$(window).height()-$("#dd_DBToolbar").height()-28,
    width:$("#dd_DBToolbar").width()
  });
  $("#dd_FILEview").datagrid("resize", {
    height:$(window).height()-$("#dd_FILEToolbar").height()-28,
    width:$("#dd_FILEToolbar").width()
  });
  $("#dd_SHAPEview").datagrid("resize", {
    height:$(window).height()-$("#dd_SHAPEToolbar").height()-28,
    width:$("#dd_SHAPEToolbar").width()
  });
  $("#pAttr").parent().find(".panel-header").css("height", "19px");
}

var dbWtype=0, fileWtype=0, shapeWtype=0;
function addDB() {
  dbWtype=0;
  $("#w_DB").window({
    title: "&nbsp;添加数据库",
    iconCls: "icon-a1",
    modal: true
  });
  $("#w_DB").window("open");
}
function addFile() {
  fileWtype=0;
  $("#w_FILE").window({
    title: "&nbsp;添加文件系统",
    iconCls: "icon-a2",
    modal: true
  });
  $("#w_FILE").window("open");
}
function addShape() {
  shapeWtype=0;
  $("#w_SHAPE").window({
    title: "&nbsp;添加空间数据",
    iconCls: "icon-a3",
    modal: true
  });
  $("#w_SHAPE").window("open");
}

function edit() {
  var node = $("#drTree").tree("getSelected");
  if (node.attributes) {
    if (node.attributes.type=="DB") {
      dbWtype=0;
      $("#w_DB").window({
        title: "&nbsp;修改数据库",
        iconCls: "icon-a1",
        modal: true
      });
      $("#w_DB").window("open");
    } else if (node.attributes.type=="FILE") {
      fileWtype=0;
      $("#w_FILE").window({
        title: "&nbsp;修改文件系统",
        iconCls: "icon-a2",
        modal: true
      });
      $("#w_FILE").window("open");
    } else if (node.attributes.type=="SHAPE") {
      shapeWtype=0;
      $("#w_SHAPE").window({
        title: "&nbsp;修改空间数据",
        iconCls: "icon-a3",
        modal: true
      });
      $("#w_SHAPE").window("open");
    }
  }
}

function showDb() {
  var sRow = $("#dd_DBtableview").datagrid("getSelected");
  $("#w_dDB").window({
    title: "&nbsp;数据库-"+sRow.dbsOType+"-"+sRow.dbsOName,
    iconCls: "icon-101",
    modal: true
  });
  $("#w_dDB").window("open");
}

function showFile() {
  $("#w_dFILE").window({
    title: "&nbsp;文件",
    iconCls: "icon-201",
    modal: true
  });
  var sNode = $("#dd_FILEview").treegrid("getSelected");
  var fileN = sNode.name;
  while ($("#dd_FILEview").treegrid("getParent", sNode.id)) {
    sNode = $("#dd_FILEview").treegrid("getParent", sNode.id);
    fileN = sNode.name+"/"+fileN;
  }
  $("#wd_filename").val(fileN);
  $("#w_dFILE").window("open");
}

function showShape() {
  var sRow = $("#dd_SHAPEview").datagrid("getSelected");
  $("#w_dSHAPE").window({
    title: "&nbsp;空间数据-"+sRow.dbsSType+"-"+sRow.dbsSName,
    iconCls: "icon-301",
    modal: true
  });
  var imgName="dot.jpg";
  if (sRow.dbsSType=="线") imgName="line.jpg";
  if (sRow.dbsSType=="面") imgName="flat.jpg";
  $("#shapeImg").attr("src", "<%=path%>/resources/images/"+imgName);
  $("#w_dSHAPE").window("open");
}

//关闭Folder窗口
function closeW(w_id) {
  $("#"+w_id).window("close");
}

function allPrpos(obj) {
  //var i=0;n=0;k=0;
  var props = "";
  for(var p in obj) {
    //n=n+1;
    if (typeof(p)!="function") {
      //i=i+1;
      if ((obj[p]+"").indexOf("[")!==0) {
        //k=k+1;
        props += p+"="+obj[p]+";\n";
      }
    }
  }
  return props;
}
</script>
</html>