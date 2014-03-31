<%@ page language="java" import="java.util.*" pageEncoding="UTF-8"%>
<%
  String path = request.getContextPath();
  String basePath = request.getScheme()+"://"+request.getServerName()+":"+request.getServerPort()+path+"/";
  String bdnm = request.getParameter("bdnm");
  String bdlbnm = request.getParameter("bdlbnm");
  System.out.println("bdlb_detail bdnm="+bdnm+"  bdlbnm="+bdlbnm);
%>
<!DOCTYPE HTML PUBLIC "-//W3C//DTD HTML 4.01 Transitional//EN">
<html>
<head> 

    <script type="text/javascript" src="../dwr/engine.js"></script>
    <script type="text/javascript" src="../dwr/util.js"></script>  
    <script type="text/javascript" src="../dwr/interface/BDDetailServlet.js"></script>

    <jsp:include page="<%=path%>/common/sysInclude.jsp" flush="true"/>  
    <script type="text/javascript" src="../resources/plugins/flot/excanvas.min.js"></script>
    <script type="text/javascript" src="../resources/plugins/flot/jquery.flot.js"></script>
    <script type="text/javascript" src="../resources/plugins/flot/jquery.flot.pie.js"></script>
	<script language="javascript" type="text/javascript" src="../resources/plugins/flot/jquery.flot.categories.js"></script>

	
<title>部队类别详细信息表</title>
<link href="../examples.css" rel="stylesheet" type="text/css">
<style>
.demo-placeholder {
	width: 100%;
	height: 100%;
	font-size: 14px;
	line-height: 1.2em;
}

	 /*表格样式 */
	.table{
		widh:100%;
		padding:0px;
		margin:0px;
		font-family:Arial,Tahoma,Verdana,Sans-Serif,宋体；
		border-left:1px solid #ADD8E6;
		border-collaspe:collapse;
	}
	.talbe th{
		font-size:12px;
		font-weight:600;
		color:#303030;
		border-right:1px solid #ADD8E6;
		border-botton:1px solid #ADD8E6;
	    border-top:1px solid #ADD8E6;
	    letter-spacing:2px;
	    text-align:left;
	    padding:10px 0px 10px 0px;
	    background:url(o2.jpg);
	    white-space:nowrap;
	    text-align:center;
	    overflow:hiden;	
	}
	.thCenter{
		font-size:12px;
		font-weight:600;
	    text-align: center;
		color:#FF0000;
		border-right:1px solid #ADD8E6;
		border-botton:1px solid #ADD8E6;
	    border-top:1px solid #ADD8E6;
	    letter-spacing:2px;
	    padding:10px 0px 10px 0px;
	    background:url(o2.jpg);
	    white-space:nowrap;
	    overflow:hiden;	
	}
	/*单元格样式*/
	.table td{
		border-right:1px solid #add8e6;
		border-bottom:1px solid #add8e6;
		background:#fff;
		padding:3px 3px 3px 6px;
		color:#303030;
		word-break:break-all;
		word-wrap:break-word;
		white-space:normal;
	}.trTitleClass {
	    text-align: center;  
	    color: #555555;
	    font: 14px Arial, Helvetica, sans-serif, Verdana, Arial, Helvetica, sans-serif;
	    font-size:14px;
	    font-weight:600;
	    padding:0px;
	    margin:0px;
        font-family:Arial,Tahoma,Verdana,Sans-Serif,宋体；
        border-left:1px solid #ADD8E6;
        border-collaspe:collapse;
	}.trClass {
	    width: 120px;
	    font: 12px Arial, Helvetica, sans-serif, Verdana, Arial, Helvetica, sans-serif;
		background:#eff5f4;
		padding:3px 3px 3px 6px;
		color:#303030;
		word-break:break-all;
		word-wrap:break-word;
		white-space:normal;
	}.trCenterBoderClass {
	    width: 200px;
		font-weight:bold;
		color:#0073ca;
	    text-align: center;
	    font: 12px Arial, Helvetica, sans-serif, Verdana, Arial, Helvetica, sans-serif;
		white-space:normal;
		background:#eff5f4;
	}.trCenterClass {
	    width: 200px;
	    text-align: center;
	    font: 12px Arial, Helvetica, sans-serif, Verdana, Arial, Helvetica, sans-serif;
		white-space:normal;
		background:#eff5f4;
	}
	.searchSpan select { font:'arial', '宋体', 'helvetica', 'clean', 'sans-serif'; font-size:12px; margin-top:-4px;}
	.tableBanner { line-height:30px; height:30px; }
	.tableBanner th, .tableBanner td { text-align:center;  background:url(../test1/images/banner/banner_3.jpg) repeat-x; }
	.tableBanner th span, .tableBanner td span { font-weight:bold; display:inline-block; color:#000; }
	.inTableBanner th,  .inTableBanner td{ height:18px; line-height:18px; font-weight:bold; background:url(../test1/images/banner/banner_3.jpg) repeat-x; }
	.viewTable { width:99%;height:95%; table-layout:fixed; }
	.viewTable td{ padding:3px;  border:1px solid #d5dfeb; }
	.viewTable th { padding:3px; height:20px; line-height:20px;text-align:center; font: 14px Arial, Helvetica, sans-serif, Verdana, Arial, Helvetica, sans-serif;color: #555555;font-size:14px;font-weight:600;}
	.viewTable td { padding-left:10px; }
	.viewTable .viewTextarea textarea{ border:none; }
	
	.viewTable_width { width:99%;height:20%; table-layout:fixed; }
	.viewTable_width td{ padding:3px;  border:1px solid #d5dfeb; }
	.viewTable_width th { padding:3px; height:20px; line-height:20px;text-align:center; font: 14px Arial, Helvetica, sans-serif, Verdana, Arial, Helvetica, sans-serif;color: #555555;font-size:14px;font-weight:600;}
	.viewTable_width td { padding-left:10px; }
	.viewTable_width .viewTextarea textarea{ border:none; }
	
	.viewTable_halfHeight { width:99%;height:30%; table-layout:fixed; }
	.viewTable_halfHeight td{ padding:3px;  border:1px solid #d5dfeb; text-align:center;}
	.viewTable_halfHeight th { padding:3px; height:20px; line-height:20px;text-align:center; font: 14px Arial, Helvetica, sans-serif, Verdana, Arial, Helvetica, sans-serif;color: #555555;font-size:14px;font-weight:600;}
	.viewTable_halfHeight td { padding-left:10px; }
	.viewTable_halfHeight .viewTextarea textarea{ border:none; }
	
.page{margin:10px auto; }/****** 分页 ******/
.page a{ padding:4px 7px; font-family:"宋体", Arial ;   border:#8cacd7 solid 1px; background:#ebf6ff; margin:0 3px; color:#3e77c2;}
.page a:hover{ background:#ADD8E6; color:#fff; text-decoration:none;}
.page .current{background:#ADD8E6; color:#3e77c2; text-decoration:none; border:#cbcbcb solid 1px;}

</style>
</head>	
<script>
$(function(){
	$("#tabInfo").tabs({
		onSelect:function(title){
			var p = $(this).tabs('getTab',title);
			var id = p.attr('id');
			
			if(id=='renyuanInfo'){
				queryRenYuanPageList(pno,psize);
			}else if(id=='wuzichubeiInfo'){
			    querywzcb(id);
			}else if(id=='renyuanjiegou'){
			    queryRenYuanChart('RenYuanLeiBie');
			}else{		
				BDDetailServlet.queryInfo('<%=bdnm%>','<%=bdlbnm%>',id,function(strHtml){
					$("#"+id+"").html(strHtml);
				});	
			}
		}
	});
	
	hideAllChartDiv();
	$("#RenYuanChartPieDiv").show();
	$("#sel_show_type").change(function(){
	    var showObjName = $(this).children('option:selected').val();
	    //alert($(this).children('option:selected').val());
	    hideAllChartDiv();
	    $("#"+showObjName+"").show();
	});
});

function hideAllChartDiv(){
	$("#RenYuanChartPieDiv").hide();
	$("#RenYuanChartBarDiv").hide();
	$("#RenYuanChatTableDiv").hide();
}

//查询物资储备情况
function querywzcb(id){
    BDDetailServlet.queryInfo('<%=bdnm%>','<%=bdlbnm%>',id,function(wzMap){
        var wzdatalist = string2Object(wzMap);
        var wztab = $("#"+id+""); 
	    wztab.html("");
	    
	    var size = wzdatalist==null?0:wzdatalist.length;
	    if(size==0){
	        wztab.append("<div>没有查到数据！</div>");
	        return;
	    }
	    
	    //显示物资储备列表
	    var wzslpiedata=[];//物资数量表，用于饼图显示
	    var wzjfpiedata=[];//物资经费表，用于饼图显示
	    var wzslbardata=[];//物资数量表，用于柱图显示
	    var wzjfbardata=[];//物资经费表，用于柱图显示
	    var tbhtml = "";
	    var currBzssnm = null; 
	    for(var i=0;i<size;i++){
	        var arow = wzdatalist[i];
	        var abzssnm = arow.BZSSNM;
	        //alert(i+"  "+abzssnm);
	        if(abzssnm==null){
	            continue;
	        }
	        if(currBzssnm==null || currBzssnm!=abzssnm){ //每种部队内码里面可能有多个部署设施描述
	            if(i>0){  //第一次不能加，只有后来的才能加	                
	                tbhtml += "</table><br><br>";
	            }
	            tbhtml += "<table  class='viewTable_width'>";
//	            tbhtml += "<tr><th class='trTitleClass' colspan=4>"+arow.BZSSMC+"</th></tr>";
	            tbhtml += "<tr class='tableBanner'><th>物资名称</th><th class='trCenterBoderClass'>数量</th><th class='trCenterBoderClass'>经费</th><th class='trCenterBoderClass'>规模名称</th></tr>";
	            currBzssnm = abzssnm;
	        }
	        tbhtml += "<tr>";
	        tbhtml += "<td class='trCenterClass'>"+arow.WZMC+"</td>";
	        tbhtml += "<td class='trCenterClass'>"+arow.SL+"</td>";
	        tbhtml += "<td class='trCenterClass'>"+arow.JF+"</td>";
	        tbhtml += "<td class='trCenterClass'>"+arow.GMQF+"</td>";
	        tbhtml += "</tr>";
	        
	        wzslpiedata[i] = {
				label: arow.WZMC,
				data: arow.SL
			}
	        wzjfpiedata[i] = {
				label: arow.WZMC,
				data: arow.JF
			}
			wzslbardata[i] = [arow.WZMC,arow.SL];
			wzjfbardata[i] = [arow.WZMC,arow.JF];
	    }
	    if(size>0){
	        tbhtml += "</table>";
	    }
	    wztab.append(tbhtml);
	        
	    //显示物资经费图表
	    var jftuhtml = "";
	    var jfbarid = "wzjfbar";
	    var jfpieid = "wzjfpie";
	    if(size>0){
	    /*
	        jftuhtml +="<table class='viewTable'>";
	        jftuhtml +="<tr>";
	        jftuhtml += "<td class='trCenterBoderClass' colspan=7>物资经费图</td>";
	        jftuhtml +="</tr>";
	        jftuhtml +="<tr>";
	        jftuhtml += "<td class='trCenterClass'><div id='"+jfbarid+"' class='demo-placeholder'></div></td>";
	        jftuhtml +="</tr>";
	        jftuhtml +="<tr>";
	        jftuhtml += "<td class='trCenterClass'><div id='"+jfpieid+"' class='demo-placeholder'></div></td>";
	        jftuhtml +="</tr>";
	        jftuhtml += "</table>";
	  */      
	        jftuhtml += "<div id='"+jfbarid+"' class='demo-placeholder'></div>";
	        jftuhtml += "<div id='"+jfpieid+"' class='demo-placeholder'></div>";
	        wztab.append(jftuhtml);
	        
	        drawWZBar(wzjfbardata,jfbarid);
	        drawWZPie(wzjfpiedata,jfpieid);	        	
        }
	});	
}

//显示物资柱图
function drawWZBar(wzbardata,barid){    
	var placeholder = $("#"+barid+"");
	placeholder.css({"width":"500px","height":"350px","margin":"0 auto"});
	//padding和margin不起作用，因此使用offset
	//placeholder.offset({"top":-100,"left":-200});
	var maxVal = 0;
	var bcount = wzbardata == null ? 0 : wzbardata.length;
	for(var i=0;i<bcount;i++){
		if(maxVal < wzbardata[i][1]){maxVal = wzbardata[i][1];}
	}
	maxVal+=1;
	placeholder.unbind();
	var barObj = $.plot(placeholder, [ wzbardata ], {
		series: {
			bars: {
				show: true,
				barPadding: 10, //柱与柱之间的缝隙
				barWidth: 0.6  //柱子的宽度
			}
		},
		xaxis: {
			mode: "categories",
			tickLength: 0
		},
		yaxis:{
		    show:true,
			//min: 0,
			//max: maxVal,
			position: 'left',
			tickLength:25,
			tickDecimals:0
		},
		grid: {
		    hoverable: true,
            clickable: true
		}
	});
	
	//alert(placeholder.find(".flot-x-axis flot-x1-axis xAxis x1Axis"));
	placeholder.find("div.flot-text").find(".xAxis").each(function(){
	    $(this).css("width","30px");
	});
	
	//鼠标移到饼图事件
	placeholder.bind("plothover", function(event, pos, obj) {
		hoverChartPart(event, pos, obj,'bar');
	});
	
	//点击事件
	placeholder.bind("plotclick", function(event, pos, obj) {
		if (!obj) {
			return;
		}
		var xidx = obj.datapoint[0];
		queryRenYuanTypePageList(xidx);
	});
}
function showWZBar(wztab,wzbardata,barid){
	wztab.append("<div id='"+barid+"' class='demo-placeholder'></div>");	    
	var placeholder = $("#"+barid+"");
//	placeholder.css({"width":"500px","height":"400px"});
	$.plot(placeholder, [ wzbardata ], {
		series: {
			bars: {
				show: true,
				barWidth: 0.6,
				align: "center"
			}
		},
		xaxis: {
			mode: "categories",
			tickLength: 0
		}
	});
}

//显示物资数量饼图
function drawWZPie(wzpiedata,pieid){   
	var placeholder = $("#"+pieid+"");
	placeholder.css({"width":"750px","height":"420px","margin":"0 auto"});
	placeholder.unbind();
	var pieObj = $.plot(placeholder, wzpiedata, {
		series: {
			pie: { 
				show: true,
				radius: 0.8,
				align: "center",
				label: {
					show: true,
					radius: 0.8,
					formatter: labelFormatter,
					background: { 
						opacity: 0.5,
						color: "#000"
					}
				}
			}
		},
		legend: {
			show: false
		},
		grid: {
		    hoverable: true,
            clickable: true
		}
	});
	
	//鼠标移到饼图事件
	placeholder.bind("plothover", function(event, pos, obj) {
		hoverChartPart(event, pos, obj,'pie');	
	});
	
	
	//点击事件
	placeholder.bind("plotclick", function(event, pos, obj) {
		if (!obj) {
			return;
		}
		var xidx = obj.seriesIndex;
		queryRenYuanTypePageList(xidx);		
	});
}

function showWZPie(wztab,wzpiedata,pieid){
	wztab.append("<div id='"+pieid+"' class='demo-placeholder'></div>");	    
	var placeholder = $("#"+pieid+"");
//	placeholder.css({"width":"200px","height":"200px"});
	placeholder.unbind();
	$.plot(placeholder, wzpiedata, {
		series: {
			pie: { 
				show: true,
				radius: 3/4,
				label: {
					show: true,
					radius: 3/4,
					formatter: labelFormatter,
					background: { 
						opacity: 0.5,
						color: "#000"
					}
				}
			}
		},
		legend: {
			show: false
		}
	});
}

//画饼图扇区LABEL
function labelFormatter(label, series) {
	return "<div style='font-size:8pt; text-align:center; padding:2px; color:white;'>" + label + "<br/>" + (series.percent).toFixed(2) + "%</div>";
}

//鼠标移动到图上，触发事件
var preChartPartIdx = null;
function hoverChartPart(event, pos, item,chartType){
	if(item){
		var xidx = null;
		if(chartType=='pie'){
			xidx = item.seriesIndex;
		}else if(chartType=='bar'){
			xidx = item.datapoint[0];
		}
		if(preChartPartIdx != xidx){
			preChartPartIdx = xidx;
			$("#tooltip").remove();
			var contents = ""+rydatalist[xidx].MC+"："+rydatalist[xidx].TOTAL+"人";
			showTooltip(pos.pageX,pos.pageY,contents);
		}
	}else{
		$("#tooltip").remove();
		preChartPartIdx = null;
	}
}

//鼠标移动到图上，显示信息
function showTooltip(x,y,contents){
	$('<div id="tooltip">' + contents + '</div>').css({
		position:'absolute',
		display:'none',
		top:y+5,
		left:x+5,
		border:'1px solid #fdd',
		padding:'2px',
		'background-color':'#fee',
		opacity:0.8		
	}).appendTo("body").fadeIn(200);
}

//分页显示人员信息列表
var pno=1;
var psize=16;
function queryRenYuanPageList(pn,ps){
    pno = pn;
    psize = ps;
   
    BDDetailServlet.queryRenyuanInfo('<%=bdnm%>','<%=bdlbnm%>',pno,psize,function(renyuanMap){
        $("#renyuanTable").datagrid({ 
		    width: 740,
			height: 480,
            border: false,		
			singleSelect: true,//只能选择单行					
			fitColumns: true,
			nowrap:true,
			rownumbers:false,
			showFooter:false,
			pagination:true,
            pageNumber: pno,//重点:传入当前页数
            pageSize: psize,//重点:传入每一页的大小           
			pageList:[10,16,20,30],   //当设置pagination属性时，初始化每页可选的数据大小清单
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
					  queryRenYuanPageList(pageNumber,pageSize);
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

//点击人员结构饼图的某一个小扇区，弹出窗口列表分页显示此类人员的列表信息
function queryRenYuanTypePageList(xidx){
    //如果此前查询的统计表数据为空，则告警并返回
    if(rydatalist==null || rydatalist.length==0){
    	alert("rydatalist is null or empty");
    	return;
    }
    
	//从统计表数据中，把xidx对应的内码找出
    var nm = rydatalist[xidx].NM;
    var showLable = rydatalist[xidx].MC;
    
    if(nm==null){
        //alert("没找到符合要求的数据");
        //return;
    }    
    //如果找到内码，则按内码查询相应的人员    
	var screenH=screen.Height, screenW=screen.Width;
	var winH=600, winW=800;
	var wTop=0, wLeft=0;
	if (screenH>winH) wTop =parseInt((screenH-winH)/2);
	if (screenW>winW) wLeft=parseInt((screenW-winW)/2);
	var uuid = openWin(showLable+"--人员列表信息", "<%=path%>/gisMap/bdrenyuan_typelist.jsp?bdnm="+<%=bdnm%>+"&bdlbnm="+'<%=bdlbnm%>'+"&tjType="+tjType+"&nm="+nm, winH,winW,  "", true, "renyuantypelist");
    
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

//人员条件查询统计显示图表
var rydatalist = null;
var tjType = null;
function queryRenYuanChart(tongjiType){
	//alert(queryType);	
	//清除选中样式
	$("#renyuanjiegou").find("a").each(function(){
		$(this).css({"background":"#ebf6ff","color":"#3e77c2"});
		
	});
	//设置选中样式背景色
	$("#"+tongjiType+"").css({"background":"#ADD8E6"});
	
	tjType = tongjiType;
    BDDetailServlet.queryRenYuanChart('<%=bdnm%>','<%=bdlbnm%>',tongjiType,function(renyuanMap){
        rydatalist = string2Object(renyuanMap);
	    var size = rydatalist==null?0:rydatalist.length;
	    if(size==0){
	        $('#RenYuanChartPieDiv').html("<div>没有查到数据！</div>");
	        return;
	    }
	     
	    //构造人员统计信息数据
	    var rypiedata=[];//人员饼图数据
	    var rybardata=[];//人员柱图数据
	    var tbhtml = ""; //图表html
	    for(var i=0;i<size;i++){
	        var arow = rydatalist[i];
	        rypiedata[i] = {label: arow.MC,	data: arow.TOTAL};
			rybardata[i] = [arow.MC,arow.TOTAL];
			
			if(i==0){
		        tbhtml += "<table  class='viewTable_width'>";
		        tbhtml += "<tr class='tableBanner'><th>名称</th><th class='trCenterBoderClass'>数量</th><th class='trCenterBoderClass'>百分比</th></tr>";
	        }
	        tbhtml += "<tr>";
	        tbhtml += "<td class='trCenterClass' style='text-align:left;padding-left:10px;'>"+arow.MC+"</td>";
	        tbhtml += "<td class='trCenterClass' style='text-align:right;padding-right:10px;'>"+arow.TOTAL+"</td>";
	        tbhtml += "<td class='trCenterClass' style='text-align:right;padding-right:10px;'>"+arow.RYPERCENT+"%</td>";
	        tbhtml += "</tr>";
	    }
	    if(size>0){
	    	tbhtml += "<tr>";	
	        tbhtml += "<td class='trCenterClass' style='font-weight:bold;border-top:1px solid #ADD8E6;'>总人数</td>";
	        tbhtml += "<td class='trCenterClass' colspan=2  style='font-weight:bold;text-align:right;border-top:1px solid #ADD8E6;'>"+rydatalist[0].ALLCOUNT+"</td>";  
	        tbhtml += "</tr>";      
	        tbhtml += "</table>";
	    }
	    //alert(rypiedata);
	    
	    //获取当前下拉选择项，设置第一个为选中项，待绘制完毕后，再设置选中项，因为饼图如果不是第一个绘制的话，样式出不来
	    var selidx = $("#sel_show_type").get(0).selectedIndex;
	    //alert(selidx);
	    $("#sel_show_type").get(0).selectedIndex = 0;
	    $("#sel_show_type").change();
	    
		//画饼图
		drawWZPie(rypiedata,'RenYuanChartPieDiv');
		//画柱图
	    drawWZBar(rybardata,'RenYuanChartBarDiv'); 
	    //生成表格
	    $("#RenYuanChatTableDiv").html(tbhtml);	  
	    	    
	    $("#sel_show_type").get(0).selectedIndex = selidx; 	
	    $("#sel_show_type").change();
	});
}
	
</script>
<body>  
	<div id="tabInfo" class="easyui-tabs" fit="false" plain="true" style="height:550px;width:780px;">
		<div id="danweiInfo"      title="单位情况"    style="padding:12px;">单位详细信息</div>
		<div id="renyuanInfo"     title="人员情况"    style="padding:12px;"><table id="renyuanTable"></table></div>
		<div id="renyuanjiegou"   title="人员结构" class="page"    style="padding:12px;">
		    <a href="#" onclick="queryRenYuanChart('RenYuanLeiBie')" id="RenYuanLeiBie">人员类别</a>
		    <a href="#" onclick="queryRenYuanChart('XingZhengZhiWuDengJi')" id="XingZhengZhiWuDengJi">行政职务等级</a>
		    <a href="#" onclick="queryRenYuanChart('XingZhengZhiWu')" id="XingZhengZhiWu">行政职务</a>
		    <a href="#" onclick="queryRenYuanChart('JunXianWenZhiJi')" id="JunXianWenZhiJi">军衔文职级</a>	
		    
		    <select id="sel_show_type" name="dept" style="width:80px;text-align:right;float:right;">
		        <option value="RenYuanChartPieDiv">   饼  图  </option>
		        <option value="RenYuanChartBarDiv">   柱  图  </option>
		        <option value="RenYuanChatTableDiv">  表  格  </option>
		    </select>
		    <br>
		    <table class='viewTable_width'>
		    	<tr>
		    		<td align="center"><div id='RenYuanChartPieDiv' class='demo-placeholder'></div></td>		    		
		    	</tr>
		    	<tr>
		    		<td align="center"><div id='RenYuanChartBarDiv' class='demo-placeholder'></div></td>		    		
		    	</tr>
		    	<tr>
		    		<td align="center"><div id='RenYuanChatTableDiv'></div></td>		    		
		    	</tr>
		    </table>
		    <br>
		    <br>
		</div>
		<div id="wuzichubeiInfo" title="物资储备情况" style="padding:12px;">物资储备详细信息</div>
		<div id="zhuanyeInfo"     title="专业信息"    style="padding:12px;">专业详细信息</div>
		<div id="meitiInfo"       title="媒体信息"    style="padding:12px;">媒体详细信息</div>
	</div>

</body>
</html>