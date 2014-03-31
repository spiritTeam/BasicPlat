<!DOCTYPE html>
<%@page language="java" contentType="text/html; charset=UTF-8" pageEncoding="UTF-8"%>
<%
String path = request.getContextPath();
String ipPort = request.getScheme()+"://"+request.getServerName()+":"+request.getServerPort();
%>
<html>
<head>
<jsp:include page="/common/sysInclude.jsp" flush="true"/>
<script src="comboTreeMap.js"></script>
<script src="../resources/gis/maptoolbar/toolbar.js"></script>
<script type="text/javascript" src="../gis/Dialog/js/dialog.js"></script> 
<script type="text/javascript" src="/BJLQ/test1/gis/slipWin/slipWinRight.js"></script>
<script src="./gisConf.js"></script>

</head>
<style type="text/css">
v\: * {
	behavior: url(#default#VML);
	display: inline-block
}
html {
  overflow:hidden;
/*  background:url('<%=path%>/resources/testGis.gif');*/
  background-position:center center;
}
  .14fontStyle{
       color:#6B6854;font:14px,Arial,Helvetica,sans-serif
  }.12fontStyle{
       color:#6B6854;font:12px,Arial,Helvetica,sans-serif
  }.qResultToolbarBtn {
    width: 82px;
    height: 20px;
    border-right: 1px solid #C1DAD7;
    border-bottom: 1px solid #C1DAD7;
    margin-left: 12px;
    margin-right: 1px;
    opacity: 10;
    background-color: #eff5f4;
    font: bold 12px Arial, Helvetica, sans-serif, Verdana, Arial, Helvetica, sans-serif;
    color: #4f6b72;
   } .qResultToolbarBtn2 {
    width: 60px;
    height: 20px;
    border-right: 1px solid #C1DAD7;
    border-bottom: 1px solid #C1DAD7;
    margin-left: 5px;
    margin-right: 1px;
    opacity: 10;
    background-color: #eff5f4;
    font: bold 12px Arial, Helvetica, sans-serif, Verdana, Arial, Helvetica, sans-serif;
    color: #4f6b72;
}.table{
		widh:100%;
		padding:1px;
		margin:0px;
		font-family:Arial,Tahoma,Verdana,Sans-Serif,宋体；
		border-left:1px solid #ADD8E6;
		border-collaspe:collapse;
		border:1px solid #ADD8E6;
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
		background:#eff5f4;
		padding:3px 3px 3px 6px;
		color:#303030;
		word-break:break-all;
		word-wrap:break-word;
		white-space:normal;
	}.trCenterClass {
	    width: 200px;
	    text-align: center;
	    font: 12px Arial, Helvetica, sans-serif, Verdana, Arial, Helvetica, sans-serif;
		white-space:normal;
		background:#eff5f4;
	}
.tableNoStyle{
     border-collapse:collapse;
     line-height:24px;
     
}.tableTr{
		font-size:12px;
		font-weight:200;
		color:#303030;
		border-right:1px solid #ADD8E6;
		border-botton:1px solid #ADD8E6;
	    border-top:1px solid #ADD8E6;
	    letter-spacing:2px;
	    text-align:left;
	    padding:10px 0px 10px 0px;
	    white-space:nowrap;
	    text-align:center;
}.tableTd{
        border-bottom:#a6bfd0 dashed 1px; 
        padding-left:8px;
}
.tableStyle{ border-collapse:collapse;}
.tableStyle td{ border-collapse:collapse; border:#8cacd7 solid 1px; height:24px; line-height:24px; padding:3px 1px 3px 3px;}
.tableStyle .tBlue01{ background:#e4f2fd;}
.tableStyle .tBlue02{ background:#F7FCFF;}
.tableStyle .tYellow{ background:#ffffdf;}
.f14Blue{ color:#0073ca; font-size:14px; font-weight:bold;}
.f14Orange{ color:#ff8800; font-size:14px; font-weight:bold;}
.f12Blue{ color:#0073ca; font-size:12px; font-weight:bold;}
.tableStyle .tabTitle{ background:url(tabTitle.jpg) repeat-x bottom; }

</style>
<body onload="init();">  
	    <div id="menuDiv"></div>
	    <div id="mapDiv">	
	        <iframe id="mapFrame" width="100%" height="100%" name="mapFrame" src="map.html" frameborder="0" scrolling="no"></iframe>	
	    </div>
	
	    <div id="treeGridSlipWin" class="easyui-tabs" style="width:280px;height:520px;">	
				<div title="按单位显示"> 
			    	<table id="dwTreegrid" title="分析结果" class="easyui-treegrid" style="height:480px;width:265px;" idField="id" treeField="wzmc">
						<thead>
							<tr>
							    <th field="wzmc" width="200" editor="text">名称</th>
								<th field="count" width="60" editor="text">数量</th>
							</tr>
						</thead>
			        </table>
		        </div>
		        <div title="按物资显示" style="width:280px;">
			    	<table id="wzTreegrid" title="分析结果" class="easyui-treegrid" style="height:480px;width:265px;" idField="id" treeField="wzmc">
						<thead>
							<tr>
							    <th field="wzmc" width="200" editor="text">名称</th>
								<th field="count" width="60" editor="text">数量</th>
							</tr>
						</thead>
			        </table>		        
		        </div>    	         
	   </div>     	
		
      <div id="resultSlipWin" class="easyui-accordion" data-options="border:false,fit:true" style="width:180px;height:500px;"> </div>	    
</body>
<script>

var map = null;//地图对象
var DMap = null;
var slipwin = null; //右侧滑动窗口，显示查询结果
var treeGridSlipWin = null; //右侧滑动窗口，显示查询结果
var showData = null; //查询结果，用于在右侧滑动窗口显示
var drawMarkerArr = new Array(); //存储在地图上标绘的图标数组，当在下次查询的时候需要清掉
var markers =[]; //储存叠加图层mark
var conditionArray =[];//存储已选中的数据
var checkSubject = []; //储存已经选中的checkbox的角标
function init(){
     $("#mapFrame").height(parent.gisMain.clientHeight); 
     $("#menuDiv").css({
    	position: "relative",
    	width:"100%",
    	top:"0px",
    	left:"0px",
    	float:"left",
    	clear:"both"
   	})
   	new Maptoolbar(document.getElementById("menuDiv"),{imgUrl:"../",mapUrl:mapFrame,menu:true});

    map = mapFrame.getMapApp();
    DMap = mapFrame.DMap;
         
     var cwidth = document.body.clientWidth;
     var cheight = document.body.clientHeight;
     slipwin = new SlipWinRight(document.getElementById("resultSlipWin"),{l:cwidth,w:200,t:100,h:450});
     treeGridSlipWin = new SlipWinRight(document.getElementById("treeGridSlipWin"),{l:cwidth,w:300,t:100,h:520});
     treeGridSlipWin.hidden();
     slipwin_hidden();
     $("#resultSlipWin").accordion({
     	onSelect:function(title){
     	    setACUnSelColor();
     		var pp = $("#resultSlipWin").accordion('getSelected');     		 
	        if (pp) {
	            var curModule=$("#resultSlipWin").accordion("getPanelIndex",pp);
	            var unSelColor = $("#resultSlipWin").find(".accordion-header-selected").find(".panel-title").css("background");
	            $("#resultSlipWin").find(".accordion-header-selected").find(".panel-title").parent().css("background", "#F4F4F4");
	        }
     		//pp.css("color","#FF0000");
     	}
     });
      alert($("#dwTreegrid").treegrid());
}

//设置accordion-header未选中的颜色
function setACUnSelColor(){
  //alert("set un sel color");
  $("#resultSlipWin").find(".accordion-header").each(function(i){
    //$(this).find("div.panel-title").attr("mIndex",i);
    $(this).find(".panel-title").parent().css("background", "#F4F4F4");
  });
}


//隐藏浮动结果窗口
function slipwin_hidden(){
    showData = null;
    slipwin_removeAllData();
    slipwin.floatIn();
    slipwin.hidden();
}

function onTRfocus(atr){
	$(atr).css("cursor","hand");
	$(atr).css("color","#B8860B");
}
function onTRLose(atr){
	$(atr).css("color","#000000");
}

function slipwin_showTreeGridStr(resultstr,xy){    
//   var resultstr={'total':'1','rows':[{'id':'4006030101000','wzmc':'维生素C','bdmc':'','count':''},{'id':'00','wzmc':'维生素C','bdmc':'天津药材仓库','count':'17','_parentId':'4006030101000'}]};
    treeGridSlipWin.floatOut();   	
    treeGridSlipWin.show();
    slipwin.floatIn();   
    slipwin.hidden();

    $("#wzTreegrid").treegrid("loadData",eval('('+resultstr[0]+')'));
    $("#dwTreegrid").treegrid("loadData",eval('('+resultstr[1]+')'));
}

//显示浮动结果窗口，并赋值json字符串
function slipwin_showJsonStr(resultstr,bjxy){
    //清除上次查询结果
    clearAllData();
    treeGridSlipWin.floatIn();   
    treeGridSlipWin.hidden();
    var idx = 0;  
    if(resultstr == null || resultstr == [] || resultstr == "[]" || resultstr=='' || resultstr =="null"){  
        $("#resultSlipWin").accordion('add',{
            title:"没有查到数据！",
            selected:true,
            fit:true,
            content:"没有查到数据！"
        });
    }else{    
        showData = eval('('+resultstr+')');
	    $.each(showData,function(i,n){
	        //alert(allPrpos(n.attributes));
	        var asize = n.attributes.length;
	        //alert(i+"  "+asize);
	        //根据部队类别选择对应的显示图标
	        var bdlbpath = "<%=path%>/test1/appModule/images/armyImg/";
            var bdlbimg = bdlbpath + "其它.png";
            if(asize>0){
                var tmpbdlbimg = bdlbImgs.imgLbMap[n.attributes[0].bdlbnm];
                if(tmpbdlbimg != null && tmpbdlbimg != ""){ 
                    bdlbimg = bdlbpath + tmpbdlbimg;
                }
	        }     
	        var cstr = "";
	        cstr+="<table class='tableNoStyle' width='100%' border='0' cellspacing='0' cellpadding='0'>";
	        for(var j=0;j<asize;j++){
	            var pos = ""+i+","+j;
	            cstr+="<tr class='tableTr' onclick='toPosition(\""+pos+"\");' onmouseover='onTRfocus(this);' onmouseout='onTRLose(this);'><td class='tableTd'><span>"+n.attributes[j].bdjc+"</span></td></tr>";  	                                
	            addADrawMarker(n.attributes[j],pos,bdlbimg); 
	        }   
	        cstr+="</table>";      
	        $("#resultSlipWin").accordion('add',{
	            title:"&nbsp;&nbsp;&nbsp;&nbsp;&nbsp;&nbsp;&nbsp;&nbsp;&nbsp;&nbsp;<img src='../appModule/images/extendImg/point.gif' />&nbsp;&nbsp;&nbsp;&nbsp;"+n.title,
	            content:cstr
	        });      
	        idx++;
	    });
	    
	    if(idx==0){
	        $("#resultSlipWin").accordion('add',{
	            title:"没有查到数据！",
	            selected:true,
	            //fit:true,
	            content:"没有查到数据！"
	        });
	    }
	    	
		//缩放到图元最大的范围
		zoomToDrawMarkerArr(bjxy);
	} 
    slipwin.show();
    slipwin.floatOut();
    $("#resultSlipWin").accordion();
    slipwin.floatOut();
    if(idx==1){
        $("#resultSlipWin").accordion('select',0);
        $("#resultSlipWin").accordion('add',{
            title:" ",
            content:" "
        });
        $("#resultSlipWin").accordion("remove", 1);
    }else if(idx>1){
        $("#resultSlipWin").accordion('select',1); 
   		$("#resultSlipWin").accordion();
    	slipwin.floatOut();   	
        $("#resultSlipWin").accordion('select',0); 
    	$("#resultSlipWin").accordion();
    	slipwin.floatOut();   	
    }    
}

//当每次查询返回结果时清除上次查询结果。清除所有数据，包括地图上标绘的数据和浮动窗口显示结果
function clearAllData(){
	map.closeInfoWindow();
	clearDrawMarkArr();
	slipwin_removeAllData();
}

//移除浮动窗口内的数据
function slipwin_removeAllData(){
    var fp = $("#resultSlipWin").accordion("getPanel", 0);
    while(fp) {
      $("#resultSlipWin").accordion("remove", 0);
      fp = $("#resultSlipWin").accordion("getPanel", 0);
    }  
}

//清除MAP上所有的标绘元素
function clearDrawMarkArr(){
    //map.clearOverlays();
    var len = drawMarkerArr == null ? 0: drawMarkerArr.length;
    for(var i=0;i<len;i++){
        map.removeOverlay(drawMarkerArr[i]);
    }
    drawMarkerArr = new Array();
}

//添加一个标绘元素
function addADrawMarker(nodeattr,pos,bdlbimg){
    //alert("add a draw marker "+nodeattr.jd+"  "+nodeattr.wd);
    //var bdlbimg = "<%=path%>/test1/appModule/images/armyImg/"+fetchBDLBImg()；
    //alert(bdlbimg);
    var hshssSymble={
        type : 0,
        url : bdlbimg,
        size : new DMap.Size(24,24),
        offsetType : "mm",
        borderWidth : 4,
        borderColor : "red",
        color :"white",
        opacity : 1,
        labelText : nodeattr.bdjc,
        labelFontSize: 12
    }
    var opt = {
        size : new DMap.Size(200,140), //infowindow大小
        isAdjustPositon : true, //infoWIndow是否自适应，即infoWindow总会自动调整到视野范围内
        offsetSize : new DMap.Size(0,0) //infoWindow箭头偏移大小
    }
    
    var point =new DMap.LonLat(nodeattr.jd,nodeattr.wd);
    var mark=new DMap.Marker(point,hshssSymble);
    mark.setCommonEvent();
    mark.name=nodeattr.bdjc;
    mark.point=point;
    var strHtml = "<div><span style='font: bold 12px Arial, Helvetica, sans-serif, Verdana, Arial, Helvetica, sans-serif;;color:#976d00'>"+nodeattr.bdjc+"</span>&nbsp;&nbsp;&nbsp;&nbsp;";
    strHtml+="<span style='text-decoration:underline;color:#6B6854;font:12px,Arial,Helvetica,sans-serif;cursor:hand' onclick='parent.getMainPage().gisFrame.toDetail(\""+pos+"\");'>详细信息</span></div>";
    strHtml+="<br><table style='border-collapse:collapse; background:#F7FCFF'>";
    //strHtml+="<tr style='font:12px;'><td width=30%><span class='12fontStyle'>部队番号：</span></td><td><span class='12fontStyle'>"+nodeattr.bdfh+"</span></td></tr>";       
    strHtml+="<tr style='font:12px;'><td width=40%><span class='12fontStyle'>部队简称：</span></td><td><span class='12fontStyle'>"+nodeattr.bdjc+"</span></td></tr>";
    strHtml+="<tr style='font:12px;'><td width=40%><span class='12fontStyle'>扩展地名：</span></td><td><span class='12fontStyle'>"+nodeattr.kzdm+"</span></td></tr>";
    //strHtml+=+"<tr><td colspan='2'></td></tr>";
    strHtml+="</table>"; 
    mark.strHtml = strHtml;
    mark.setCommonEvent();
     
    DMap.$(mark).bind("click",function(e){ 
        map.openInfoWindowHtml(this.point, this.strHtml ,opt);     
     }).bind("mouseover",function(e){
        map.showFloatHelper(this.name);
     }).bind("mouseout",function(e){
        map.hideFloatHelper();
     });
     
    map.addOverlay(mark);
    drawMarkerArr.push(mark);
}

//显示详细信息
function toDetail(ij){
	//alert("toDetail "+ij);
    var ijarr = ij.split(",");
    var nodeattr = showData[ijarr[0]].attributes[ijarr[1]];
    //alert(allPrpos(nodeattr));
  
    var screenH=screen.Height, screenW=screen.Width;
    var winH=600, winW=800;
    var wTop=0, wLeft=0;
    if (screenH>winH) wTop =parseInt((screenH-winH)/2);
    if (screenW>winW) wLeft=parseInt((screenW-winW)/2);
    //window.open("<%=path%>/gisMap/bdlb_detail.jsp?bdnm="+nodeattr.bdnm+"&bdlbnm="+nodeattr.bdlbnm,"详细描述页面","menubar=no,location=no,resizable=yes,scrollbars=yes,status=no,height="+winH+", width="+winW+", top="+wTop+", left="+wLeft);
    var uuid = openWin(nodeattr.bdjc+"", "<%=path%>/gisMap/bdlb_detail.jsp?bdnm="+nodeattr.bdnm+"&bdlbnm="+nodeattr.bdlbnm, winH,winW,  "", true, "detail");
    
}

//根据部队类别选择对应的显示图标
function fetchBDLBImg(bdlbnm){
	var bdlbimg = "qt.png";
	if(bdlbImgs==null){
	    return bdlbimg;
	}   
	bdlbimg = bdlbImgs.defaultimg;
	if(bdlbnm == null || bdlbnm == ""){
	    return bdlbimg;
	}

    var size = bdlbImgs.imgMap.length;
    for(var i=0;i<size;i++){
        var aimgmap = bdlbImgs.imgMap[i];
        if(aimgmap.bdlbnm == bdlbnm){
        	bdlbimg = aimgmap.bdlbimg;
        	break;
        }
    }
    return bdlbimg;
}

//缩放到drawMarkerArr里存储的元素的最大外边框范围
function zoomToDrawMarkerArr(bjxy){
	var ptstr=(bjxy==null ||bjxy=="")?"":bjxy;
    var len = drawMarkerArr == null ? 0: drawMarkerArr.length;
    for(var i=0;i<len;i++){
        var aMarker = drawMarkerArr[i];
        var lonlat = aMarker.getLonlat();
        if(i==0 && ptstr == ""){
        	//ptstr+=",";
        }else{
        	ptstr+=",";
        }
        ptstr += lonlat.lon + "," + lonlat.lat;
    }
    //alert(ptstr);
    map.centerAtLatLng(ptstr);
	
}

//查找一个标绘元素
function fecthADrawMarkerByLonLat(jd,wd){
    var retMarker = null;
    var len = drawMarkerArr == null ? 0: drawMarkerArr.length;
    for(var i=0;i<len;i++){
        var aMarker = drawMarkerArr[i];
        var lonlat = aMarker.getLonlat();
        if(lonlat.lon == jd && lonlat.lat == wd){
        	retMarker = aMarker;
        	break;
        }
    }
    return retMarker;
}

//地图定位指定数据并显示简项内容
function toPosition(ij){
    //alert(ij);
    var ijarr = ij.split(",");
    var nodeattr = showData[ijarr[0]].attributes[ijarr[1]];
    var marker = fecthADrawMarkerByLonLat(nodeattr.jd,nodeattr.wd);
    var point = marker.getLonlat();
    map.panTo(point);
    DMap.$(marker).click(); 
}

function getCurModule() {
  var m = getMainPage();
  var curLeft = m.getCurIframe();
  m.$.messager.alert("测试", $(curLeft.document).find("body").html(), "info");
}
function getLeftTName() {
  var m = getMainPage();
  var curLeft = m.getCurIframe();
  m.$.messager.alert("测试", curLeft.leftValue, "info", function(){
    m.$.messager.alert("测试", curLeft.$("#testLeft").val(), "info");
  });
}
</script>
</html>