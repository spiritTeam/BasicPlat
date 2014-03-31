
//处理IE6PNG图片方法 
document.write("<!--[if IE 6]><script src='"+path+"/resources/js/png/DD_belatedPNG.js' type='text/javascript'></script><![endif]-->"); 

//根据浏览器类型，得到modalDialog大小[IE8/IE6]
function getDialogSize(obj){
	var ua = navigator.userAgent.toLowerCase();
	if (window.ActiveXObject){ 
		var ie = ua.match(/msie ([\d.]+)/)[1];
		if(ie == "8.0"){
		}else if(ie == "6.0"){
			obj.width += 7;
			obj.height +=55;
		}
	}else if (document.getBoxObjectFor){
		//var firefox = ua.match(/firefox\/([\d.]+)/)[1]
	}else if (window.MessageEvent && !document.getBoxObjectFor){
		//var chrome = ua.match(/chrome\/([\d.]+)/)[1]
	}else if (window.opera){
		//var opera = ua.match(/opera.([\d.]+)/)[1]
	}else if (window.openDatabase){
		//var safari = ua.match(/version\/([\d.]+)/)[1]
	}
	return {"width":obj.width,"height":obj.height};
}

//改变锚点,并刷新页面
function refreshAndPoint(url, name){
	url = url.toString();
	if(url.indexOf("#")>-1){
		url = url.substring(0,url.indexOf("#"));
	}
	window.location.href = url+"#"+name;
	window.location.reload();
}
//定位锚点
function pointPosition(){
	var pointUrl = window.location.toString();
	var point = pointUrl.split("#")[1];
	if(point){
		window.location.href = window.location.href;
	}
}

//左右结构，右边自适应宽度
//marginWidth为页面DIV margin+border宽度
function autoRight(marginWidth){
	if(!marginWidth) marginWidth = 0;
	var rightWidth = document.documentElement.clientWidth-$(".autoLeft").width()-marginWidth;
	$(".autoRight").css("width",rightWidth+"px");
}

//最大化打开主页 隐藏菜单栏，地址栏
function openIndex(url,title){
	if(!url || url==""){
		url = path+"/sys-index.jsp";
	}
	if(!title || tilte==""){
		title = "gb";
	}
	var options = "top=0, left=0, toolbar=no, menubar=no, scrollbars=yes, resizable=no, location=no, status=no, fullScreen=yes";
	window.open (url, title, options); 
	//self.close();
}

function enterToTab(){
	var e = document.activeElement;
	var b = (e.tagName == "INPUT" && window.event.keyCode == 13
				&&(e.type == "text" || e.type == "password"
				|| e.type == "checkbox" || e.type == "radio")
				|| e.tagName == "SELECT");
	var a = document.forms[0].elements, n=-1;
	for(var i = 0; i < a.length; i++){
		if(a[i] == e) n=i;
		if(n > -1 && b && n+1 < a.length){
			if(!a[n+1].disabled){
				window.event.keyCode = 0;
				window.event.returnValue = false;
				return;
			}
			n=i;
		}
	}						
}

//jQuery ready
$().ready(function(){
	/* 初始化表单文本框样式 */
	$.each( [$("input[type='text']"), $("textarea"), $("input[type='password']")], function(){
		$(this)
		  .addClass("textDefault")
          .mouseover(function(){$(this).attr("class") != "textFouce" ? $(this).removeClass().addClass("textOver") : null; })
          .mouseout(function(){$(this).attr("class") != "textFouce" ? $(this).removeClass().addClass("textDefault") : null; })
          .focus(function(){$(this).removeClass().addClass("textFouce"); })
          .blur(function(){$(this).removeClass().addClass("textDefault"); });
	}); 
  
	/* 初始化表单按钮样式 */
	$.each( [$("input[type='button']"), $("input[type='reset']"), $("input[type='submit']")], function(){
		$(this)
		  .addClass("buttonDefault")
          .mouseover(function(){$(this).addClass("buttonOver"); })
          .mouseout(function(){$(this).removeClass("buttonOver"); });
	});
  
	/* 初始化INPUT的file类型按钮样式 */
	$("input[type='file']")
	  .addClass("fileDefault")
      .mouseover(function(){$(this).attr("class") != "fileFouce" ? $(this).removeClass().addClass("fileOver") : null; })
      .mouseout(function(){$(this).attr("class") != "fileFouce" ? $(this).removeClass().addClass("fileDefault") : null; })
      .focus(function(){$(this).removeClass().addClass("fileFouce"); })
      .blur(function(){$(this).removeClass().addClass("fileDefault"); });
	  
	/* 初始化functionBanner li按钮样式 */
	$(".functionBanner li").each(function(){
		if($(this).hasClass("grayed")){
			$(this).attr("disabled","true");
		}						  								  
	})
	.mouseover(function(){$(this).addClass("hover");})
	.mouseout(function(){$(this).removeClass("hover active");})
	.mousedown(function(){$(this).addClass("active");})
	.mouseup(function(){$(this).removeClass("active");});	
});