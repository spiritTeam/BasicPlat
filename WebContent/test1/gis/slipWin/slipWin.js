//������ʾ���ݣ���Ҫ���ܣ�������ҳ���ϣ���Զ�λ������ʾ���������ݡ�
//����div����ʾ���ݶ���opt.width����div�� opt.heigth����div��
//从左边滑出
function SlipWin(div,opt){
	this.container = $("<div></div>");
	var container = this.container;
	this.left = $("<div></div>");//�������
	this.leftBody = $("<div></div>");
	this.button = $("<div></div>");//
	this.button_img =  $("<img src='../lib/slipWin/lmenui.gif'>");
	this.bodyW = 200;
	this.bodyH = document.body.clientHeight-22;
	this.opt = opt;
    var me = this;
	
	if(opt){
		if(opt.w){
			this.bodyW = opt.w;
		}
		if(opt.h){
			this.bodyH = opt.h;
		}
	}else{
		if($(div).css("width"))
		this.bodyW = $(div).css("width");
		if($(div).css("height"))
		this.bodyH = $(div).css("height");
	}
		
	this.container.css({
		position:"absolute",
		overflow:"hidden",
		top:"2px",
		left:"2px",
		width:28,
		height:this.bodyH+15,
		background:"url('../lib/slipWin/menutop.gif') repeat-x top",
		zIndex:1
	});
	
	this.left.css({
		position:"relative",
		display:"none",
		overflow:"auto",
		float:"left",
		width:this.bodyW,
		border:"1px solid #c5dbf2",
		background:"#eee",
		marginTop:"7px",
		marginLeft:"0px",
		marginRight:"0px",
		height:this.bodyH
	});
	
	this.leftBody.css({
		position:"relative",
		overflow:"auto",
		float:"left",
		border:"1px solid #c5dbf2",
		background:"#eee",
		width:this.bodyW,
		marginTop:"10px",
		marginLeft:"3px",
		marginRight:"3px",
		height:this.bodyH
	});
	
	this.button.css({
		position:"relative",
		widht:"18px",
		float:"right"
	});

	this.button_img.bind('click',function(){
		if(me.left.css("display")=="block"){
			me.hidd();
		}else{
			me.show();
		}
	});
	
	//this.leftBody.append(div);
	this.left.append(div);

	this.button.append(this.button_img);
	this.container.append(this.left);
	this.container.append(this.button);
	$(document.body).append(this.container);
	
	//alert(container.css("zIndex"));
}
SlipWin.prototype.show = function(){
	var me = this;
	//me.left.animate({width:me.bodyW});
	me.container.animate({width:me.bodyW+28});
	me.button_img.attr('src','../lib/slipWin/lmenui.gif');
	me.left.css({display:"block"});
	
}
SlipWin.prototype.hidd = function(){
	var me = this;
	//me.left.animate({width:0});
	me.container.animate({width:28});
	me.button_img.attr('src','../lib/slipWin/lmenuo.gif');
	me.left.css({display:"none"});
}