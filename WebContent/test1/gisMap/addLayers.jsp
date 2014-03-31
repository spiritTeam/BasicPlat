<%@ page language="java" contentType="text/html; charset=UTF-8" pageEncoding="UTF-8"%>
<!DOCTYPE html PUBLIC "-//W3C//DTD HTML 4.01 Transitional//EN" "http://www.w3.org/TR/html4/loose.dtd">
<html>
<head>
    <script type="text/javascript" src="../dwr/engine.js"></script>
    <script type="text/javascript" src="../dwr/util.js"></script>
	<script type="text/javascript" src="../dwr/interface/CommonQuery.js"></script>
	
    <jsp:include page="/common/sysInclude.jsp" flush="true"/>
    <script src="gisConf.js"></script>
    <meta http-equiv="Content-Type" content="text/html; charset=UTF-8">
<title>详细信息表</title>
<style>
	.searchSpan select { font:'arial', '宋体', 'helvetica', 'clean', 'sans-serif'; font-size:12px; margin-top:-4px;}
	.tableBanner { line-height:30px; height:30px; }
	.tableBanner th, .tableBanner td { text-align:center;  background:url(../test1/images/banner/banner_1.jpg) repeat-x; }
	.tableBanner th span, .tableBanner td span { font-weight:bold; display:inline-block; color:#000; }
	.inTableBanner th,  .inTableBanner td{ height:18px; line-height:18px; font-weight:bold; background:url(../test1/images/banner/banner_1.jpg) repeat-x; }
	.viewTable { width:98%; table-layout:fixed; }
	.viewTable th ,.viewTable td{ padding:3px; height:20px; line-height:20px; border:1px solid #d5dfeb; }
	.viewTable th { background-color:#e8ecf0; text-align:right; }
	.viewTable td { padding-left:10px; }
	.viewTable .viewTextarea textarea{ border:none; }

</style>
</head>
<body  style='background:#fff' onload="init();">
  <div id="contentHtml" style="height:334px;overflow:scroll;overFlow-x: hidden;"></div>
  <div style="width:100%;"><div style="float:right;padding:3px 10px 0 0"><button onclick='queryBefore()'>确定</button></div></div>
  <!-- <table><tr><td width=93%></td><td><button onclick='queryBefore()'>确定</button></td></tr></talbe> -->
<script>
var tempArray = []; //把当前页面加载的所有图层条件都存起来
var conditionArray; //传到后台SQL的查询条件
var map;
var DMap;
var markers;
var checkSubject;
var queryArray = []; 
function init(){
    map = parent.gisFrame.map;
    DMap = parent.gisFrame.DMap;
    markers = parent.gisFrame.markers;
    conditionArray = parent.gisFrame.conditionArray;
    checkSubject = parent.gisFrame.checkSubject;
    var count = -1;
    var strHtml = "";
    $.each(layerArrays,function(i,m){
        strHtml+="<table class='viewTable'><tr class='tableBanner'><td style='color: #555555;font: 14px Arial, Helvetica, sans-serif, Verdana, Arial, Helvetica, sans-serif;'>"+m.mc+"</td></tr></table>";
	    var layersValue = m.value;
	    strHtml+="<table class='viewTable'>";
		$.each(layersValue,function(j,n){
		   count++;
		   if(j%2==0){
		      strHtml +="<tr><td>";
		   }else{	   
		      strHtml +="<td>";
		   } 
		   var ifChecked=false;
		   for(var h=0; h<checkSubject.length; h++){  
			   if(checkSubject[h]==count){
			       ifChecked = true;
			       strHtml += "<input type='checkbox' checked id='check_"+count+"' onclick='checkLayer(this)' /><img src='"+n.imgUrl+"' height='24' width='24' /><span>"+n.cname+"</span>";			       
			   }
		   }
		   if(!ifChecked){
		       strHtml += "<input type='checkbox' id='check_"+count+"' onclick='checkLayer(this)' /><img src='"+n.imgUrl+"' height='24' width='24' /><span>"+n.cname+"</span>";
		   }
		   strHtml +="</td>";
		   if(j%2==1){
		      strHtml +="</tr>";
		   }
		   tempArray[count]=n.ename+","+n.zdyw+","+n.zdzw+","+n.imgUrl+","+n.showLevel;   
		});
	});	
	strHtml += "</table>";	
    document.getElementById("contentHtml").innerHTML = strHtml;
 }
 
function checkLayer(obj){
   var cou = parseInt(obj.id.replace("check_",""));
   if(obj.checked){
       conditionArray[conditionArray.length] = tempArray[cou];
       checkSubject[checkSubject.length] = cou;
   }else{
        for(var i=0; i<conditionArray.length; i++){
            if(conditionArray[i]==tempArray[cou]){
               conditionArray.splice(i,1);
               checkSubject.splice(i,1);
            }
        }
   }
}

function queryBefore(){
    parent.gisFrame.closeOpenWin();
    queryLayerInfo();
    bindMapEvent();    
}

function queryLayerInfo(){
   var bounds = map.getBounds();
   var currentLevel = map.getZoom();
   if(conditionArray && conditionArray.length>0){
       for(var h=0; h<conditionArray.length; h++){
           var tempStr = conditionArray[h];
           var tempArray = tempStr.split(",");
           if(parseInt(tempArray[4])<=parseInt(currentLevel)){
               queryArray[queryArray.length] = tempStr;
           }
       }
	   CommonQuery.queryForLayers(queryArray,bounds.left,bounds.bottom,bounds.right,bounds.top,function(resData){
	       if(resData && resData!=""){
	          for(var j=0; j<markers.length; j++){  //清除图上所有marker
	              map.removeOverlay(markers[j]);	              
	          }
	          markers=[];
	          for(var i=0; i<resData.length; i++){
	              var mc = resData[i].NAME;
	              var x = resData[i].X;
	              var y = resData[i].Y;
	              var record = resData[i].RECORD;
	              drawMarker(mc,x,y,record);            
	          }
	       }      
	   });
   }
}

function drawMarker(mc,x,y,record){
	     var jbxxStr = queryArray[record];   //去JAVA查询符合当前显示级别的数组
	     var jbxxArray = jbxxStr.split(",");
         var symble={
 	         type : 0,
             url : jbxxArray[3],
 			 size : new DMap.Size(24,24),
 			 offsetType : "mm",
 			 borderWidth : 4,
 			 borderColor : "red",
 			 color :"white",
 			 opacity : 1,
 			 labelText : mc,
 			 labelFontSize: 12
          }
          var point =new DMap.LonLat(x,y);
  	      var mark=new DMap.Marker(point,symble);
 	      map.addOverlay(mark);
 	      markers[markers.length]=mark;
}

function bindMapEvent(){
      DMap.$(map).bind("move",moveEvent);
      DMap.$(map).bind("zoomed",moveEvent);
  //    map.addMapChangeListener(moveEvent);
}

function unbindMapEvent(){
     DMap.$(map).unbind("moveend",moveEvent);
     DMap.$(map).unbind("zoomend",moveEvent);
}

function moveEvent(){
    queryLayerInfo();
}

</script>
</body>
</html>