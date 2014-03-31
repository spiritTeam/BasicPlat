/*****
	页面工具栏操作
	div  显示菜单div
	opt.imgUrl 引用页面相对跟目录位置
	opt.mapUrl 控制iframe中的地图时，为iframe 的id
	opt.menu true 添加菜单，默认false
*****/

function Maptoolbar(div,opt){
	var me = this;
	this.container = $("<div></div>");
	this._opt = opt;
	var imgUrl = this._opt.imgUrl;
	var mapUrl = this._opt.mapUrl;
	if(mapUrl){
		window.getMapApp = mapUrl.getMapApp;
		//debugger;
	}else{
		mapUrl = window;
	}
	if(div){
		$(div).append(this.container);
	}else{
		$("#bodydiv").prepend(this.container);
	}
	this.container.css({
		position:"relative",
		overflow:"hidden",
		top:"0px",
		left:"0px",
		width:'100%',
		height:28,
		zIndex:1
	}); 
	
	var table = $("<table cellpadding=0 cellspacing=0 ></table>").css({
		height:"29",
		width:"100%",
		background:"url('"+imgUrl+"/resources/gis/maptoolbar/lqImages/bg.jpg')"
		
	}).appendTo(this.container);
	var tr = $("<tr></tr>").appendTo(table);
	$("<td width='5'></td>").appendTo(tr);
	var td = $("<td ></td>").appendTo(tr);
			
	//放大$('<img src="'+imgUrl+'/resources/gis/maptoolbar/lqImages/button01.jpg" width="64" height="25" style="margin-top:-5px;margin-left:5px" onmouseover="movOver(this)" onmouseout="movOver(this)"  />').bind("click",function(){mapUrl.getMapApp().activateRecZoomIn()}).appendTo(td);
	//放大
	$('<img src="'+imgUrl+'/resources/gis/maptoolbar/lqImages/button01.jpg" width="64" height="25" style="margin-top:-3px;margin-left:5px" onmouseover="movOver(this)" onmouseout="movOver(this)" />').bind("click",function(){mapUrl.getMapApp().activateRecZoomIn()}).appendTo(td);
	//缩小
	$('<img src="'+imgUrl+'/resources/gis/maptoolbar/lqImages/button02.jpg" width="64" height="25" style="margin-top:-3px;margin-left:5px" onmouseover="movOver(this)" onmouseout="movOver(this)" />').bind("click",function(){mapUrl.getMapApp().activateRecZoomOut()}).appendTo(td);
	//漫游
	$('<img src="'+imgUrl+'/resources/gis/maptoolbar/lqImages/button03.jpg" width="64" height="25" style="margin-top:-3px;margin-left:5px" onmouseover="movOver(this)" onmouseout="movOver(this)" />').bind("click",function(){mapUrl.getMapApp().deactivate()}).appendTo(td);
	//全图
	$('<img src="'+imgUrl+'/resources/gis/maptoolbar/lqImages/button04.jpg" width="64" height="25" style="margin-top:-3px;margin-left:5px" onmouseover="movOver(this)" onmouseout="movOver(this)" />').bind("click",function(){mapUrl.getMapApp().fullExtent()}).appendTo(td);
	//测距
	$('<img src="'+imgUrl+'/resources/gis/maptoolbar/lqImages/button06.jpg" width="64" height="25" style="margin-top:-3px;margin-left:5px" onmouseover="movOver(this)" onmouseout="movOver(this)" />').bind("click",function(){mapUrl.getMapApp().activateMeaturePolyline()}).appendTo(td);
	//测面
	$('<img src="'+imgUrl+'/resources/gis/maptoolbar/lqImages/button07.jpg" width="75" height="25" style="margin-top:-3px;margin-left:5px" onmouseover="movOver(this)" onmouseout="movOver(this)" />').bind("click",function(){mapUrl.getMapApp().activateMeaturePolygon()}).appendTo(td);
	//鹰眼
	$('<img src="'+imgUrl+'/resources/gis/maptoolbar/lqImages/button05.jpg" width="64" height="25" style="margin-top:-3px;margin-left:5px" onmouseover="movOver(this)" onmouseout="movOver(this)" />').bind("click",function(){			  
		//alert(mapUrl.getControl("OverviewMapControl").cp);
			mapUrl.getMapApp().openOverviewMap();
		}).appendTo(td);
	//打印
	$('<img src="'+imgUrl+'/resources/gis/maptoolbar/lqImages/button08.jpg" width="64" height="25" style="margin-top:-3px;margin-left:5px" onmouseover="movOver(this)" onmouseout="movOver(this)" />').bind("click",function(){mapUrl.getMapApp().print()}).appendTo(td);
	//清除
	$('<img src="'+imgUrl+'/resources/gis/maptoolbar/lqImages/button09.jpg" width="64" height="25" style="margin-top:-3px;margin-left:5px" onmouseover="movOver(this)" onmouseout="movOver(this)" />').bind("click",function(){mapUrl.getMapApp().clearOverlays()}).appendTo(td);
	
	//叠加矢量数据
	$('<input type="checkbox" id="addLayer" onclick="addLayers(this);"><span>叠加矢量图层</span></input>').appendTo(td);
	td.append("    ");
	//保留查询结果
	$('<input type="checkbox" id="keepResult" onclick="checkResult(this);"><span>保留态势展示结果</span></input>').appendTo(td);
	//菜单		
	if(opt.menu){
		this.menu =  $("<div></div>");
		this.menu.addClass("menuDiv")
//		new Menu(config.MenuUrl, this.menu.get(0));
		$("<td></td>").append(this.menu).appendTo(tr);
	}
}
function movOver(obj){
	var index=obj.src.indexOf("_b");
	var val=obj.src.lastIndexOf('\.');
	if(index!=-1){
		obj.src=obj.src.replace('_b','');
	}else{
		obj.src=obj.src.substr(0,val)+"_b"+obj.src.substr(val);
	}
	
}

var uuid = null;
function addLayers(obj){   
    if(obj.checked){
       var screenH=screen.Height, screenW=screen.Width;
	   var winH=400, winW=680;
	   var wTop=0, wLeft=0;
	   if (screenH>winH) wTop =parseInt((screenH-winH)/3);
	   if (screenW>winW) wLeft=parseInt((screenW-winW)/3);
       uuid = openWin("叠加图层列表", "gisMap/addLayers.jsp", winH, winW, "icon-aWin", true, "detail");

    }else{
       var len = markers == null ? 0: markers.length;
       for(var i=0;i<len;i++){
           map.removeOverlay(markers[i]);
       }
    }  
}

function closeOpenWin(){
    if(uuid){
       try{
          closeWin(uuid);
          uuid=null;
       }catch(e){} 
    }
}

function checkResult(obj){
    if(obj.checked){
       ctm.ifClear=false;
    }else{
        ctm.ifClear=true;
    }
}

function showDialog(){
	if(dialog){
		dialog.shutDiv();
	}	
	var _opt = {width:400,height:200,ofLeft:2,ofTop:20,margin:0,padding:0,opacity:0.3};
    dialog = new Dialog('bzjlDialog',_opt);
    dialog.setTitle('<span style="font-size:14px;line-height:20px;">部队类别</span>');
    dialog.setContent("dialog内容部分！");
    dialog.showDiv();
}
	
