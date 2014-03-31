
//从右边滑出
function SlipWinRight(div,opt){
    //alert(_PATH);
    //alert(parent.document.body.clientWidth);
    this.widthPos = parent.document.body.clientWidth;
	this.container = $("<div></div>");
	var container = this.container;
	this.left = $("<div></div>");
	this.leftBody = $("<div></div>");
	this.button = $("<div></div>");//
	this.button_img =  $("<img src='"+_PATH+"/resources/gis/slipWin/tanru_1.gif'>");
	this.lineImg = $("<img src='"+_PATH+"/resources/gis/slipWin/pic_2.png'>");
	this.contentDiv = div;
	
	//alert(document.body.clientWidth);
	//this.bodyW = 200;
	//this.bodyH = document.body.clientHeight-22;

    this.bodyW = 200;
    this.bodyH = 500;
    this.bodyL = 20;
    this.bodyT = 30;
	
	this.opt = opt;
    var me = this;
	
	if(opt){
        //alert(opt.w);
		if(opt.w){
			this.bodyW = opt.w;
		}
		if(opt.h){
			this.bodyH = opt.h;
		}
		//alert(opt.l);
        if(opt.l){
            this.bodyL = opt.l-20;
        }
        if(opt.t){
            this.bodyT = opt.t;
        }
	}else{
		if($(div).css("width"))
		this.bodyW = $(div).css("width");
		if($(div).css("height"))
		this.bodyH = $(div).css("height");
	}
	
//	alert(this.bodyW+","+this.bodyH);
		
	this.container.css({
		position:"absolute",
		overflow:"hidden",
		top:this.bodyT,
		left:this.bodyL,
		width:this.bodyW+10,
		height:this.bodyH+15,
//	lkn	background:"url('"+_PATH+"/resources/gis/slipWin/menutop.gif') repeat-x top",
		zIndex:1
	});
	
	

    this.left.css({
        position:"relative",
        display:"none",
        overflow:"auto",
        float:"right",
        width:this.bodyW-20,
        border:"2px solid #c5dbf2",
        background: "#eff5f4",
 //       background:"#eee",
 //       marginTop:"7px",
 //       marginLeft:"0px",
 //       marginRight:"0px",
        marginBottom:"3px",
        height:this.bodyH+15
    });
    
    this.leftBody.css({
        position:"relative",
        overflow:"auto",
        float:"left",
        border:"2px solid #c5dbf2",
//        background:"#eee",
        background: "#eff5f4",
        width:this.bodyW,
 //       marginTop:"10px",
 //       marginLeft:"3px",
 //       marginRight:"3px",
        height:this.bodyH+15
    });
     this.lineImg.css({
     	position:"relative",
        width:"7px",
		float:"left",
        height:this.bodyH+15
    });   
	this.button.css({
		position:"relative",
        widht:"18px",
		float:"left"
	});

	this.button_img.bind('click',function(){
		if(me.left.css("display")=="block"){
			me.floatIn();
		}else{
			me.floatOut();
		}
	});
	
	//this.leftBody.append(div);
	this.left.append(this.contentDiv);
	this.button.append(this.button_img);
    this.container.append(this.button);
    this.container.append(this.lineImg);
	this.container.append(this.left);
	//$(parent.document.body).append(this.container);

    $(document.body).append(this.container);
	
	//alert(container.css("zIndex"));
}
//设置浮动窗口显示内容
SlipWinRight.prototype.setContentDiv = function(newDiv){
  var me = this;
  //me.contentDiv = newDiv;   
  //me.left.removeChild(me.contentDiv);
  
  me.left.append(newDiv);
  //alert(222);
}
//显示浮动窗口
SlipWinRight.prototype.show = function(){
    var me = this;
    me.container.css({display:"block"});    
}
//隐藏浮动窗口
SlipWinRight.prototype.hidden = function(){
    var me = this;
    me.container.css({display:"none"});    
}
//滑动弹出，并显示查询结果内容
SlipWinRight.prototype.floatOut = function(){
    //alert("show");
	var me = this;
	//me.left.animate({width:me.bodyW});
	me.container.animate({left:me.bodyL-me.bodyW+20});
	//me.container.animate({width:'700px'},2000);
	me.button_img.attr('src', _PATH+'/resources/gis/slipWin/tanru.png');
	me.left.css({display:"block"});	
}
//滑动缩进，并隐藏查询结果内容
SlipWinRight.prototype.floatIn = function(){
    //alert("hidden");
	var me = this;
	//me.left.animate({width:0});
    me.container.animate({left:me.bodyL});
	//me.container.animate({width:'800px'},2000);
	me.button_img.attr('src',_PATH+'/resources/gis/slipWin/tanchu.png');
	me.left.css({display:"none"});
}