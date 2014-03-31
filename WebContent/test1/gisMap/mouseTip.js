
DMap.Class("MousePositionControl",DMap.Control,{
	_dom : null,
	_map : null,
	initialize: function() {
    },
	draw : function(map){
		var me=this;
		me._map=map;
		this._dom= document.createElement('div');
		me._map.getContainer().appendChild(this._dom);
		$(this._dom).attr('unselectable', 'on')
			.css({
				position : "absolute",			
				right : "20px",
				bottom : "30px",
				color: "black"
			});
		DMap.$(me._map).bind('mousemove.MousePositionControl',function(e,lonlat){
		    var x=lonlat.lon+"";
		    var y=lonlat.lat+"";
			me._dom.innerHTML="<font style='bold 12px Arial, Helvetica, sans-serif, Verdana, Arial, Helvetica, sans-serif;'>经度:"+x.substring(0,6)+"  .  纬度:"+y.substring(0,5)+".   地图等级:"+me._map.getZoom()+"</font>";
		});
	},
	remove : function(){
		DMap.$(this._map).unbind('.MousePositionControl');
		DMap.Util.removeNode(this._dom);
		this._dom=null;
		this._map=null;
	}
});