/*****
	页面工具栏操作
	div  显示菜单div
	opt.imgUrl 引用页面相对跟目录位置
	opt.mapUrl 控制iframe中的地图时，为iframe 的id
	opt.menu true 添加菜单，默认false
*****/

function Maptoolbar(div,opt){
	var me = this;
	this.container = $j("<div></div>");
	this._opt = opt;
	var imgUrl = this._opt.imgUrl;
	var mapUrl = window.parent.module.map;
	if(mapUrl){
		window.getMapApp = mapUrl.getMapApp;
		//debugger;
	}else{
		mapUrl = window;
	}
	
	if(div){
		$j(div).append(this.container);
	}else{
		$j("#bodydiv").prepend(this.container);
	}
	this.container.css({
		position:"relative",
		overflow:"hidden",
		top:"0px",
		left:"0px",
		width:'100%',
		height:31,
		zIndex:1
	}); 
	//alert("window.parent.Map :"+);//module  zt
	var table = $j("<table cellpadding=0 cellspacing=0 ></table>").css({
		height:"31",
		width:"100%",
		background:"url('"+imgUrl+"lib/maptoolbar/images/PGIS-10.jpg')"
		
	}).appendTo(this.container);
	var tr = $j("<tr></tr>").appendTo(table);
	//
	$j("<td width='5'></td>").appendTo(tr);
	var td = $j("<td ></td>").appendTo(tr);
	//放大
	
	$j('<img src="'+imgUrl+'lib/maptoolbar/images/PGIS-11.jpg" width="59" height="31" style="margin-top:-5px;margin-left:5px" onmouseover="movOver(this)" onmouseout="movOver(this)"  />').bind("click",function(){mapUrl.activateRecZoomIn()}).appendTo(td);
	//缩小
	$j('<img src="'+imgUrl+'lib/maptoolbar/images/PGIS-12.jpg" width="59" height="31" style="margin-left:5px" onmouseover="movOver(this)" onmouseout="movOver(this)" />').bind("click",function(){mapUrl.activateRecZoomOut()}).appendTo(td);
	//漫游
	$j('<img src="'+imgUrl+'lib/maptoolbar/images/PGIS-13.jpg" width="59" height="31" style="margin-left:5px" onmouseover="movOver(this)" onmouseout="movOver(this)" />').bind("click",function(){mapUrl.deactivate()}).appendTo(td);
	//全图
	$j('<img src="'+imgUrl+'lib/maptoolbar/images/PGIS-14.jpg" width="59" height="31" style="margin-left:5px" onmouseover="movOver(this)" onmouseout="movOver(this)" />').bind("click",function(){mapUrl.fullExtent()}).appendTo(td);
	//测距
	$j('<img src="'+imgUrl+'lib/maptoolbar/images/PGIS-15.jpg" width="59" height="31" style="margin-left:5px" onmouseover="movOver(this)" onmouseout="movOver(this)" />').bind("click",function(){mapUrl.activateMeaturePolyline()}).appendTo(td);
	//侧面
	$j('<img src="'+imgUrl+'lib/maptoolbar/images/PGIS-16.jpg" width="72" height="31" style="margin-left:5px" onmouseover="movOver(this)" onmouseout="movOver(this)" />').bind("click",function(){mapUrl.activateMeaturePolygon()}).appendTo(td);
	//鹰眼
	$j('<img src="'+imgUrl+'lib/maptoolbar/images/PGIS-17.jpg" width="59" height="31" style="margin-left:5px" onmouseover="movOver(this)" onmouseout="movOver(this)" />').bind("click",function(){			  
		alert(mapUrl.getControl("OverviewMapControl").cp);
		}).appendTo(td);
	//打印
	$j('<img src="'+imgUrl+'lib/maptoolbar/images/PGIS-19.jpg" width="59" height="31" style="margin-left:5px" onmouseover="movOver(this)" onmouseout="movOver(this)" />').bind("click",function(){mapUrl.print()}).appendTo(td);
	//清除
	$j('<img src="'+imgUrl+'lib/maptoolbar/images/PGIS-21.jpg" width="59" height="31" style="margin-left:5px" onmouseover="movOver(this)" onmouseout="movOver(this)" />').bind("click",function(){mapUrl.clearOverlays()}).appendTo(td);
	//菜单
	
	if(opt.menu){
		this.menu =  $j("<div></div>");
		this.menu.addClass("menuDiv")
		new Menu(config.MenuUrl, this.menu.get(0));
		$j("<td></td>").append(this.menu).appendTo(tr);
	}
}
function movOver(obj){
	var index=obj.src.indexOf("-b");
	var val=obj.src.lastIndexOf('\.');
	if(index!=-1){
		obj.src=obj.src.replace('-b','');
	}else{
		obj.src=obj.src.substr(0,val)+"-b"+obj.src.substr(val);
	}
	
}