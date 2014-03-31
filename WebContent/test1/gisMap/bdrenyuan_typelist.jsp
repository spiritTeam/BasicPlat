<%@ page language="java" import="java.util.*" pageEncoding="UTF-8"%>
<%
  //点击部队人员结构饼图中的某个小扇区，弹出此窗口，显示该类型人员的列表信息
  String path = request.getContextPath();
  String basePath = request.getScheme()+"://"+request.getServerName()+":"+request.getServerPort()+path+"/";
  String bdnm = request.getParameter("bdnm");
  String bdlbnm = request.getParameter("bdlbnm");
  String tjType = request.getParameter("tjType");
  String nm = request.getParameter("nm");
  System.out.println("bdrenyuan_typelist bdnm="+bdnm+"  bdlbnm="+bdlbnm+"  tjType="+tjType+"  nm="+nm);
%>
<!DOCTYPE HTML PUBLIC "-//W3C//DTD HTML 4.01 Transitional//EN">
<html>
<head> 

    <script type="text/javascript" src="../dwr/engine.js"></script>
    <script type="text/javascript" src="../dwr/util.js"></script>  
    <script type="text/javascript" src="../dwr/interface/BDDetailServlet.js"></script>

    <jsp:include page="<%=path%>/common/sysInclude.jsp" flush="true"/>  

<title>人员类型列表信息</title>
<style>
   .trTitleClass {
	    width: 100px;
	    color: #555555;
	    font: 14px Arial, Helvetica, sans-serif, Verdana, Arial, Helvetica, sans-serif;
        text-align: center;    
	    font-size:14px;
		font-weight:600;
		
		widh:100%;
		padding:0px;
		margin:0px;
		font-family:Arial,Tahoma,Verdana,Sans-Serif,宋体；
		border-left:1px solid #ADD8E6;
		border-collaspe:collapse;
	}.trClass {
	    width: 120px;
	    font: 12px Arial, Helvetica, sans-serif, Verdana, Arial, Helvetica, sans-serif;
	    font-weight:bold;
		padding:5px 5px 5px 10px;
		color:#303030;
		word-break:break-all;
		word-wrap:break-word;
		white-space:normal;
		border-bottom:#a6bfd0 dashed 1px; 
        padding-left:8px;
	}.trColorClass {
	    width: 120px;
	    font: 12px Arial, Helvetica, sans-serif, Verdana, Arial, Helvetica, sans-serif;
		background:#ededed;
		padding:5px 5px 5px 10px;
		color:#303030;
		word-break:break-all;
		word-wrap:break-word;
		white-space:normal;
		border-bottom:#a6bfd0 dashed 1px; 
        padding-left:8px;
	}.trCenterClass {
	    width: 200px;
	    text-align: left;
	    font: 12px Arial, Helvetica, sans-serif, Verdana, Arial, Helvetica, sans-serif;
		white-space:normal;
		border-bottom:#a6bfd0 dashed 1px; 
        padding-left:8px;
	}.trRightClass {
	    width: 200px;
	    text-align: right;
	    font: 12px Arial, Helvetica, sans-serif, Verdana, Arial, Helvetica, sans-serif;
		white-space:normal;
		border-bottom:#a6bfd0 dashed 1px; 
        padding-left:8px;
	}.trColorCenterClass {
	    width: 200px;
	    text-align: left;
	    font: 12px Arial, Helvetica, sans-serif, Verdana, Arial, Helvetica, sans-serif;
		white-space:normal;
		background:#ededed;
		border-bottom:#a6bfd0 dashed 1px; 
        padding-left:8px;
	}.searchSpan select { font:'arial', '宋体', 'helvetica', 'clean', 'sans-serif'; font-size:12px; margin-top:-4px;}
</style>
</head>
<body  style='background:#fff'>
    <div id="contentHtml" style='height:100%;overflow:scroll;overFlow-x: hidden;'>
        <table id="renyuanTable"></table>
    </div>
    
<script>
var pno=1;
var psize=20;
$(function(){
	queryRenyuanByType('<%=bdnm%>','<%=bdlbnm%>','<%=tjType%>','<%=nm%>',pno,psize);
});

function queryRenyuanByType(bdnm,bdlbnm,tjType,nm,pno,psize){
    BDDetailServlet.queryRYListByType(bdnm,bdlbnm,tjType,nm,pno,psize,function(renyuanMap){
        $("#renyuanTable").datagrid({ 
		    width: 740,
			height: 560,
            border: false,		
			singleSelect: true,//只能选择单行					
			fitColumns: true,
			nowrap:true,
			rownumbers:false,
			showFooter:false,
			pagination:true,
            pageNumber: pno,//重点:传入当前页数
            pageSize: psize,//重点:传入每一页的大小           
			pageList:[10,20,30],   //当设置pagination属性时，初始化每页可选的数据大小清单
			columns:[[
			    {field:'XM', title:'姓名',   width:42,	align:'center', 
			       styler:function(value,row,index){
			                return 'color:blue;cursor:hand';
			              } 
			    },
				{field:'XB', title:'性别',   width:42,	align:'center' },
				{field:'RYLB', title:'人员类别',   width:42,	align:'center' },
				{field:'SFZHM', title:'身份证号',   width:60,	align:'center' },
				{field:'JXWZJ', title:'军衔文职级',   width:32,	align:'center' },
				{field:'XZZW', title:'行政职务',	width:42,	align:'center' },	
				{field:'ID', title:'ID',	width:42,	align:'center',hidden:'true' }			
			]],
			onClickCell:function(rowIndex,field,value){		
			    showRenYuanDetail(rowIndex,field,value);
			}					    
		});
								
		var p = $("#renyuanTable").datagrid('getPager');
		if (p){
		    $(p).pagination({ //设置分页功能栏
		       onSelectPage:function(pageNumber, pageSize){
					  queryRenyuanByType('<%=bdnm%>','<%=bdlbnm%>','<%=tjType%>','<%=nm%>',pageNumber,pageSize);
		       }
		    });
		}		       
		   
	   $("#renyuanTable").datagrid('loadData', string2Object(renyuanMap));
	   
	   $("#renyuanTable").datagrid('getPager').data('pagination').options.pageNumber=pno;		       
	   $(p).pagination({
	       //beforePageText:'第2页',
	       //afterPageText:'共5页',
	       //displayMsg:'共334'
	   });   
    });    
}

//显示人员详细信息，当点击人员名称后弹出详细信息页面
function showRenYuanDetail(rowIndex,field,value){
	//alert(rowIndex+"  "+field+"  "+value);
	if(field!="XM"){
	    $('#renyuanTable').datagrid('unselectRow',rowIndex);
	    return;
	}
	$('#renyuanTable').datagrid('selectRow',rowIndex);
	var selected = $('#renyuanTable').datagrid('getSelected');
	if (selected){
	   var renyuanid = selected.ID;
	   var screenH=screen.Height, screenW=screen.Width;
       var winH=600, winW=800;
       var wTop=0, wLeft=0;
       if (screenH>winH) wTop =parseInt((screenH-winH)/2);
       if (screenW>winW) wLeft=parseInt((screenW-winW)/2);
       //window.open("<%=path%>/gisMap/bdlb_detail.jsp?bdnm="+nodeattr.bdnm+"&bdlbnm="+nodeattr.bdlbnm,"详细描述页面","menubar=no,location=no,resizable=yes,scrollbars=yes,status=no,height="+winH+", width="+winW+", top="+wTop+", left="+wLeft);
       
       var uuid = openWin("人员详细描述", "<%=path%>/gisMap/bdrenyuan_detail.jsp?bdnm="+<%=bdnm%>+"&bdlbnm="+'<%=bdlbnm%>'+"&renyuanid="+renyuanid, winH+30,winW,  "", true, "renyuandetail");
    
	}      			
}

</script>
</body>
</html>