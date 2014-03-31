/**
  用于树状显示结果信息
  多选时可以都定位到地图上
  单击叶子节点时可以临时定位到地图上
*/
var allMarkerArray=new Array(); //所有marker集合，按照模块ID进行分类存储
function comboTreeMap() {
  this.easyuiTreeObj;
  this.urlPath;
  this.dataArray;  
  this.drawMarkerArr = new Array(); //存储在地图上标绘的图标数组，用于定位图标所在区域
  this.ifClear = true;  //是否保留mark记录
  /*初始化
  if(typeof init!="function") {
    comboTreeMap.prototype.init = function(easyuiTreeObj, urlPath) { 
        this.easyuiTreeObj = easyuiTreeObj;
        this.urlPath = urlPath;
    };
  }
  */
  //添加一个标绘元素
  if (typeof addADrawMarker!="function") {
    comboTreeMap.prototype.addADrawMarker = function(node, drawimg, moduleId) {
      var mark= null;
      //选择元素显示的图标
      var iconImg = this.urlPath+"/"+(drawimg!=null&&drawimg!="")?drawimg:"/test1/appModule/images/armyImg/其它.png";
      //元素显示的名称，如果JSON格式不对，则不显示此元素
      if(!node.attributes) return mark;
      var labelstr = "";
      if(node.attributes.text!=null) labelstr = node.attributes.text;
      var hshssSymble= {
        type: 0,
        url: iconImg,
        size: new DMap.Size(24,24),
        offsetType: "mm",
        borderWidth: 4,
        borderColor: "red",
        color:"white",
        opacity: 1,
        labelText: labelstr,
        labelFontSize: 12
      };
      var opt = {
        size : new DMap.Size(200,140), //infowindow大小
        isAdjustPositon : true, //infoWIndow是否自适应，即infoWindow总会自动调整到视野范围内
        offsetSize : new DMap.Size(0,0) //infoWindow箭头偏移大小
      };
      var point =new DMap.LonLat(node.attributes.jd,node.attributes.wd);
      mark=new DMap.Marker(point, hshssSymble);
      mark.setCommonEvent();
      mark.name=node.attributes.text;
      mark.point=point;
      mark.moduleId=moduleId;
      
      mark.strHtml = this.getSimpleInfo(node,mark.moduleId);
      mark.setCommonEvent();

      DMap.$(mark).bind("click",function(e) { 
         map.openInfoWindowHtml(this.point, this.strHtml ,opt);     
      }).bind("mouseover",function(e){
         map.showFloatHelper(this.name);
      }).bind("mouseout",function(e){
         map.hideFloatHelper();
      });
      map.addOverlay(mark);
      return mark;
    };
  };

  //多选部队树后点击地图定位触发该方法，实现地图定位功能
  if(typeof showInMap!="function"){
    comboTreeMap.prototype.showInMap = function(easyuiTreeObj,urlPath,moduleId){
      this.easyuiTreeObj = easyuiTreeObj;
      this.urlPath = urlPath;
      if(this.ifClear){  //清除以往记录
          this.clearAllMarkArr();
      }else{  //保留以往记录
	      this.clearDrawMarkArr(moduleId);
      }
      this.getChecked(moduleId);
    };
  }

  //多选部队树后点击地图定位触发该方法，实现地图定位功能
  if (typeof getChecked!="function") {
    comboTreeMap.prototype.getChecked = function(moduleId) {
      var drawMarkerArr=[];
      var nodes = this.easyuiTreeObj.tree('getChecked');
      for (var i=0; i<nodes.length; i++) {
        var bdlbpath = this.urlPath + "/test1/appModule/images/armyImg/";
        var bdlbimg = bdlbpath + "qt.png";
        var marker = this.addADrawMarker(nodes[i],bdlbimg,moduleId);
        if(marker != null) drawMarkerArr.push(marker);
        this.drawMarkerArr = drawMarkerArr;
      }
      var ifHas = false;
      var markerJson={
          id:"",
          value:[]
      }
      for(var j=0; j<allMarkerArray.length; j++){
          var tempJson = allMarkerArray[j];
          if(tempJson.id==moduleId){
             ifHas = true;
             tempJson.value=drawMarkerArr;
          }
          allMarkerArray[j]=tempJson;
      }
      if(!ifHas){
	      markerJson.id=moduleId;
	      markerJson.value=drawMarkerArr;
	      allMarkerArray[allMarkerArray.length] = markerJson;
      }
      
      //缩放到图元最大的范围
      if (nodes.length>0) this.zoomToDrawMarkerArr();
    };
  }

  //点击树的时候调用此方法，只会显示一个图元
  if (typeof getSelected!="function") {
    comboTreeMap.prototype.getSelected = function(easyuiTreeObj,urlPath,moduleId) {
      this.easyuiTreeObj = easyuiTreeObj;
      this.urlPath = urlPath;
      if(this.ifClear){  //清除以往记录
          this.clearAllMarkArr();
      }else{  //保留以往记录
	      this.clearDrawMarkArr(moduleId);
      }
      var node = this.easyuiTreeObj.tree('getSelected');  
      var bdlbpath = this.urlPath + "/test1/appModule/images/armyImg/";
      var bdlbimg = bdlbpath + "其它.png";
      var drawMarkerArr=[];
      var tmpDrawMarker = this.addADrawMarker(node,bdlbimg,moduleId);
      if(tmpDrawMarker != null) drawMarkerArr.push(tmpDrawMarker);
      this.drawMarkerArr = drawMarkerArr;
      var ifHas = false;
      var markerJson={
          id:"",
          value:[]
      }
      for(var j=0; j<allMarkerArray.length; j++){
          var tempJson = allMarkerArray[j];
          if(tempJson.id==moduleId){
             ifHas = true;
             tempJson.value=drawMarkerArr;
          }
          allMarkerArray[j]=tempJson;
      }
      if(!ifHas){
	      markerJson.id=moduleId;
	      markerJson.value=drawMarkerArr;
	      allMarkerArray[allMarkerArray.length] = markerJson;
      }
      
      map.setCenter(tmpDrawMarker.point);	
    };
  }
  //清除本模块上次绘制marker
  if (typeof clearDrawMarkArr!="function") {
    comboTreeMap.prototype.clearDrawMarkArr = function(moduleId) {
      for(var i=0; i<allMarkerArray.length; i++){
          var tempJson = allMarkerArray[i];
 //         alert(i+" "+tempJson.id+" "+moduleId);
          if(tempJson.id==moduleId){           
             var needRemoveArray = tempJson.value;
             for (var j=0;j<needRemoveArray.length;j++) {	
		        map.removeOverlay(needRemoveArray[j]);
		     }
		     allMarkerArray.splice(i,1);
          }
      }
    };
  }
    //清除所有缓存mark
  if (typeof clearAllMarkArr!="function") {
    comboTreeMap.prototype.clearAllMarkArr = function() {
      for(var i=0; i<allMarkerArray.length; i++){
            var tempJson = allMarkerArray[i];        
            var needRemoveArray = tempJson.value;
            for (var j=0;j<needRemoveArray.length;j++) {	
	           map.removeOverlay(needRemoveArray[j]);
	        }
	        tempJson.value=[];
      }
      allMarkerArray=[];
    };
  }
 
  //根据部队类别选择对应的显示图标,bdlbImgs在gisMap/gisConf.js里
  if (typeof fetchBDLBImg!="function") {
    comboTreeMap.prototype.fetchBDLBImg = function(bdlbnm) {
      var bdlbimg = "qt.png";
      if (bdlbImgs==null) return bdlbimg;
      bdlbimg = bdlbImgs.defaultimg;
      if (bdlbnm == null || bdlbnm == "") return bdlbimg;
      var size = bdlbImgs.imgMap.length;
      for(var i=0;i<size;i++){
        var aimgmap = bdlbImgs.imgMap[i];
        if (aimgmap.bdlbnm == bdlbnm) {
          bdlbimg = aimgmap.bdlbimg;
          break;
        }
      }
      return bdlbimg;
    };
  }

  //查找一个标绘元素
  if(typeof fecthADrawMarkerByLonLat!="function"){
    comboTreeMap.prototype.fecthADrawMarkerByLonLat = function(jd,wd){
      var retMarker = null;
      var len = this.drawMarkerArr == null ? 0: this.drawMarkerArr.length;
      for(var i=0;i<len;i++){
        var aMarker = this.drawMarkerArr[i];
        var lonlat = aMarker.getLonlat();
        if (lonlat.lon == jd && lonlat.lat == wd){
          retMarker = aMarker;
          break;
        }
      }
      return retMarker;
    };
  }

  //缩放到drawMarkerArr里存储的元素的最大外边框范围
  if (typeof zoomToDrawMarkerArr!="function") {
    comboTreeMap.prototype.zoomToDrawMarkerArr = function(bjxy){
      var ptstr=(bjxy==null ||bjxy=="")?"":bjxy;
      var len = this.drawMarkerArr == null ? 0: this.drawMarkerArr.length;
      for (var i=0;i<len;i++) {
        var aMarker = this.drawMarkerArr[i];
        var lonlat = aMarker.getLonlat();
        if(i==0 && ptstr == ""){} //ptstr+=",";
        else ptstr+=",";
        ptstr += lonlat.lon + "," + lonlat.lat;
      }
      map.centerAtLatLng(ptstr);
    };
  }
  //组装简项信息页面
    comboTreeMap.prototype.getSimpleInfo = function(node,moduleId){
        var strHtml = "";
        if(moduleId=="bfgd"){
			strHtml+="<div><span style='font: bold 12px Arial, Helvetica, sans-serif, Verdana, Arial, Helvetica, sans-serif;;color:#976d00'>"+node.attributes.text+"</span>&nbsp;&nbsp;&nbsp;&nbsp;";
			strHtml+="<span style='text-decoration:underline;color:#6B6854;font:12px,Arial,Helvetica,sans-serif;cursor:hand'  onclick='parent.parent.gisFrame.ctm.toDetails(\""+node.attributes.id+"\",\""+node.attributes.gcflnm+"\",\""+moduleId+"\");'>详细信息</span></div>";
			strHtml+="<br><table style='border-collapse:collapse; background:#F7FCFF'>";
		    var gcflnm = node.attributes.gcflnm;
		    if(gcflnm=="9999"){
			    strHtml+="<tr style='font:12px;'><td><span class='12fontStyle'>部署类别：</span></td><td><span class='12fontStyle'>"+node.attributes.bslbval+"</span></td></tr>";	      
		        strHtml+="<tr style='font:12px;'><td><span class='12fontStyle'>部署形式：</span></td><td><span class='12fontStyle'>"+node.attributes.bsxsval+"</span></td></tr>";
		        strHtml+="<tr style='font:12px;'><td><span class='12fontStyle'>天文时间：</span></td><td><span class='12fontStyle'>"+node.attributes.twsj+"</span></td></tr>";
		        strHtml+="<tr><td colspan='2'></td></tr>";
		        strHtml+="</table>"; 
		    }else if(gcflnm == "12" || gcflnm=="13" || gcflnm=="14"){
		        strHtml+="<tr style='font:12px;'><td><span class='12fontStyle'>工程分类：</span></td><td><span class='12fontStyle'>"+node.attributes.gcflval+"</span></td></tr>";
		        strHtml+="<tr style='font:12px;'><td><span class='12fontStyle'>扩展地名：</span></td><td><span class='12fontStyle'>"+node.attributes.kzdm+"</span></td></tr>";       
		        strHtml+="<tr style='font:12px;'><td><span class='12fontStyle'>质量描述：</span></td><td><span class='12fontStyle'>"+node.attributes.zlms+"</span></td></tr>";
		        strHtml+="<tr><td colspan='2'></td></tr>";
		        strHtml+="</table>";    		  
		    }	 
		}else if(moduleId=="hshss"){
                strHtml+="<div><span style='font: bold 12px Arial, Helvetica, sans-serif, Verdana, Arial, Helvetica, sans-serif;;color:#976d00'>"+node.attributes.wxymc+"</span>&nbsp;&nbsp;&nbsp;&nbsp;";
				strHtml+="<span style='text-decoration:underline;color:#6B6854;font:12px,Arial,Helvetica,sans-serif;cursor:hand' onclick='parent.parent.gisFrame.ctm.toDetails(\""+node.attributes.id+"\",\""+node.attributes.lb+"\",\""+moduleId+"\");'>详细信息</span></div>";
				strHtml+="<br><table style='border-collapse:collapse; background:#F7FCFF'>";
				strHtml+="<tr style='font:12px;'><td><span class='12fontStyle'>危险源名：</span></td><td><span class='12fontStyle'>"+node.attributes.wxymc+"</span></td></tr>";	      
			    strHtml+="<tr style='font:12px;'><td><span class='12fontStyle'>仓储物资：</span></td><td><span class='12fontStyle'>"+node.attributes.ccwxwzmc+"</span></td></tr>";
			    strHtml+="<tr style='font:12px;'><td><span class='12fontStyle'>单位地址：</span></td><td><span class='12fontStyle'>"+node.attributes.kzdm+"</span></td></tr>";
			    strHtml+="<tr><td colspan='2'></td></tr>";
			    strHtml+="</table>"; 	     		  		
		}else if(moduleId=="zbgc"){
				strHtml+="<div><span style='font: bold 12px Arial, Helvetica, sans-serif, Verdana, Arial, Helvetica, sans-serif;;color:#976d00'>"+node.attributes.text+"</span>&nbsp;&nbsp;&nbsp;&nbsp;";
			   	strHtml+="<span style='text-decoration:underline;color:#6B6854;font:12px,Arial,Helvetica,sans-serif;cursor:hand'  onclick='parent.parent.gisFrame.ctm.toDetails(\""+node.attributes.zbgcnm+"\",\""+node.attributes.text+"\",\""+moduleId+"\");'>详细信息</span></div>";
				strHtml+="<br><table style='border-collapse:collapse; background:#F7FCFF'>";
				strHtml+="<tr style='font:12px;'><td><span class='12fontStyle'>战备工程代号：</span></td><td><span class='12fontStyle'>"+node.attributes.zbgcdh+"</span></td></tr>";	      
				strHtml+="<tr style='font:12px;'><td><span class='12fontStyle'>军标代字(汉)：</span></td><td><span class='12fontStyle'>"+node.attributes.jbdzh+"</span></td></tr>";
				strHtml+="<tr style='font:12px;'><td><span class='12fontStyle'>扩展地名：</span></td><td><span class='12fontStyle'>"+node.attributes.kzdm+"</span></td></tr>";
				strHtml+="<tr><td colspan='2'></td></tr>";
				strHtml+="</table>";      		  		
		}else if(moduleId=="zhfh"){
				strHtml+="<div><span style='font: bold 12px Arial, Helvetica, sans-serif, Verdana, Arial, Helvetica, sans-serif;;color:#976d00'>"+node.attributes.text+"</span>&nbsp;&nbsp;&nbsp;&nbsp;";
			   	strHtml+="<span style='text-decoration:underline;color:#6B6854;font:12px,Arial,Helvetica,sans-serif;cursor:hand'  onclick='parent.parent.gisFrame.ctm.toDetails(\""+node.attributes.zbgcnm+"\",\""+node.attributes.text+"\",\""+moduleId+"\");'>详细信息</span></div>";
				strHtml+="<br><table style='border-collapse:collapse; background:#F7FCFF'>";
				strHtml+="<tr style='font:12px;'><td><span class='12fontStyle'>指挥防护工程代号：</span></td><td><span class='12fontStyle'>"+node.attributes.zbgcdh+"</span></td></tr>";       
				strHtml+="<tr style='font:12px;'><td><span class='12fontStyle'>军标代字(汉)：</span></td><td><span class='12fontStyle'>"+node.attributes.jbdzh+"</span></td></tr>";
				strHtml+="<tr style='font:12px;'><td><span class='12fontStyle'>扩展地名：</span></td><td><span class='12fontStyle'>"+node.attributes.kzdm+"</span></td></tr>";
				strHtml+="<tr><td colspan='2'></td></tr>";
				strHtml+="</table>";      		  		
		}
		
		return strHtml;     	  
    };
//显示详细信息
    comboTreeMap.prototype.toDetails = function(id,lb,moduleId){
		var screenH=screen.Height, screenW=screen.Width;		
		var wTop=0, wLeft=0;
		var uuid;
		if(moduleId=="bfgd"){
		    var winH=340, winW=600;
			if (screenH>winH) wTop =parseInt((screenH-winH)/2);
			if (screenW>winW) wLeft=parseInt((screenW-winW)/2);
		    uuid= openWin("详细描述", "/BJLQ/appModule/app_tszs/tszs_bfgd/bfgd_detail.jsp?id="+id+"&lb="+lb, winH, winW, "", true, "detail");
        }else if(moduleId=="hshss"){
		    var winH=340, winW=680;
			if (screenH>winH) wTop =parseInt((screenH-winH)/2);
			if (screenW>winW) wLeft=parseInt((screenW-winW)/2);
		    uuid= openWin("详细描述", "/BJLQ/appModule/app_tszs/tszs_hshss/details.jsp?id="+id+"&lb="+lb, winH, winW, "", true, "detail");
        }else if(moduleId=="zbgc"){
			var winH=620, winW=700;
			if (screenH>winH) wTop =parseInt((screenH-winH)/2);
			if (screenW>winW) wLeft=parseInt((screenW-winW)/2);
		    uuid= openWin("详细描述", "/BJLQ/appModule/app_tszs/tszs_zbgc/zbgc_detail.jsp?id="+id+"&lb="+lb, winH,winW,  "", true, "detail");
        }else if(moduleId=="zhfh"){
            var winH=620, winW=700;
			if (screenH>winH) wTop =parseInt((screenH-winH)/2);
			if (screenW>winW) wLeft=parseInt((screenW-winW)/2);
		    uuid= openWin("详细描述", "/BJLQ/appModule/app_tszs/tszs_zhfh/zhfh_detail.jsp?id="+id+"&lb="+lb, winH,winW,  "", true, "detail");
        }
    };
    
    //为解决动态树加载setData使用
  if(typeof init!="function") {
    comboTreeMap.prototype.setData = function(moduleId,dataArray) {
        this.dataArray = dataArray;
        this.clearDrawMarkArr(moduleId);  //清除本模块id下已有的marker
        var drawMarkerArr=[];
        for(var i=0; i<this.dataArray.length; i++){
            var singleData = this.dataArray[i];
            var marker = this.drawMarkers(singleData);    
            drawMarkerArr.push(marker);
        }
        this.drawMarkerArr = drawMarkerArr;
        var ifHas = false;
	    var markerJson={
	        id:"",
	        value:[]
	    }
	    for(var j=0; j<allMarkerArray.length; j++){
            var tempJson = allMarkerArray[j];
            if(tempJson.id==moduleId){
               ifHas = true;
               tempJson.value=drawMarkerArr;
            }
	        allMarkerArray[j]=tempJson;
	    }
	    if(!ifHas){
		    markerJson.id=moduleId;
		    markerJson.value=drawMarkerArr;
		    allMarkerArray[allMarkerArray.length] = markerJson;
	    }
	    this.zoomToDrawMarkerArr();
    };
  }
  if(typeof init!="function") {
    comboTreeMap.prototype.drawMarkers = function(singleData) { 
        var text = singleData.text;
        var x = singleData.jd;
        var y = singleData.wd;
        var imgUrl = singleData.imgUrl;
        var detail = singleData.detail; 
        var fieldName = singleData.fieldName;
        var fieldNameArray =fieldName.split(",");
        var fieldValue = singleData.fieldValue;       
	    var mark= null;
	    var markSymble= {
	        type: 0,
	        url: imgUrl,
	        size: new DMap.Size(24,24),
	        offsetType: "mm",
	        borderWidth: 4,
	        borderColor: "red",
	        color:"white",
	        opacity: 1,
	        labelText: text,
	        labelFontSize: 12
	     };
	     var infoHeight=140;
	     var infoWidth=260;
	     if (fieldNameArray&&fieldNameArray.length>4) {
	         infoHeight = (140-64)+(fieldNameArray.length*16);
	     }
	     var opt = {
	        size : new DMap.Size(infoWidth, infoHeight), //infowindow大小
	        isAdjustPositon : true, //infoWIndow是否自适应，即infoWindow总会自动调整到视野范围内
	        offsetSize : new DMap.Size(0,0) //infoWindow箭头偏移大小
	     };
	     var markPoint =new DMap.LonLat(x,y);
	     mark=new DMap.Marker(markPoint, markSymble);
	     mark.setCommonEvent();
	     mark.name=text;
	     mark.point=markPoint;
	     if(singleData.simpleHtml&&singleData.simpleHtml.length>0){
	        mark.strHtml = singleData.simpleHtml;
	     } else {	        
	        var fieldValueArray =fieldValue.split(",");
	        //alert(allPrpos(detail));
	        var wintitle = "详细描述";
	        for(var p in detail){
	        	if (typeof(p)!="function") {
	        		if (p == "wintitle") {
	        			wintitle = detail.wintitle;
	        			break;
	        		}
	        	}
	        }
	        var strHtml="";
	        strHtml+="<div><span style='font: bold 12px Arial, Helvetica, sans-serif, Verdana, Arial, Helvetica, sans-serif;;color:#976d00 white-space:nowrap; text-overflow: ellipsis; word-break:keep-all; overflow:hidden'>"+text+"</span>&nbsp;&nbsp;&nbsp;&nbsp;";
			strHtml+="<span style='text-decoration:underline;color:#6B6854;font:12px,Arial,Helvetica,sans-serif;cursor:hand'  onclick='parent.parent.gisFrame.ctm.showDetailPage(\""+detail.url+"\",\""+detail.height+"\",\""+detail.width+"\",\""+wintitle+"\");'>详细信息</span></div>";
			strHtml+="<br><table style='border-col+lapse:collapse; background:#F7FCFF; width:"+(infoWidth-30)+"px'>";
	        for(var i=0; i<fieldNameArray.length; i++){
	            strHtml+="<tr style='font:12px;'><td width='*' title='"+fieldNameArray[i]+"'><span class='12fontStyle' style='width=80px; white-space:nowrap; text-overflow: ellipsis; word-break:keep-all; overflow:hidden'>"+fieldNameArray[i]+"：</span></td><td width='*' title='"+fieldValueArray[i]+"'><span class='12fontStyle' style='width: "+(infoWidth-30-84)+"px; white-space:nowrap; text-overflow: ellipsis; word-break:keep-all; overflow:hidden'>"+fieldValueArray[i]+"</span></td></tr>";	      
	        }
	        mark.strHtml = strHtml;
	     }
	     mark.setCommonEvent();
	     DMap.$(mark).bind("click",function(e) { 
	         map.openInfoWindowHtml(this.point, this.strHtml ,opt);     
	     }).bind("mouseover",function(e){
	         map.showFloatHelper(this.name);
	     }).bind("mouseout",function(e){
	         map.hideFloatHelper();
	     });
	     map.addOverlay(mark);	
	     this.drawMarkerArr.push(mark);
	     return mark;
    };
  }

  comboTreeMap.prototype.showDetailPage = function(url, height, width, wintitle){
    uuid= openWin(wintitle, url, height, width,  "", true, "detail");
  };
}
var ctm = new comboTreeMap();