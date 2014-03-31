/**
 * @author yezhiwang
 */
//初始化浏览器变量 star_Browser
/*
var Browser = new Object();
Browser.isMozilla = (typeof document.implementation != 'undefined') && (typeof document.implementation.createDocument != 'undefined') && (typeof HTMLDocument != 'undefined');
Browser.isIE = window.ActiveXObject ? true : false;
Browser.isGecko = navigator.userAgent.indexOf("Gecko") != -1;
Browser.isOpera = (navigator.userAgent.toLowerCase().indexOf("opera") != -1);
*/
function Menu(url, div, showEvent, classname){
    this.url = url;
    this.showId = div;
    this.showEvent = showEvent;
    this.className = classname;
	this.init();
}
Menu.prototype.init = function(){
	var xmlhttprequest = $j.get(this.url);
	//var id = this.showId;
	var me = this;
	xmlhttprequest.onreadystatechange = function(){
            if (xmlhttprequest.readyState == 4 && xmlhttprequest.status == 200) {
				me.show(xmlhttprequest.responseXML);
            }
        }
}
Menu.prototype.show = function(dom){
    var pDiv = this.showId;//document.getElementById(this.showId);
    //获取跟节点，AppMenu
    var root = dom.documentElement;
    //所有子节点
    var childNodeList = root.childNodes;
    //展示第一层，如果是Menu调用子层
    var isLoading=true;
    for (var i = 0; i < childNodeList.length; i++) {
        var node = childNodeList[i];
        if (node.nodeName == "A") {
        
            addA(pDiv, node);
            //console.log("A");
        }
        else 
            if (node.nodeName == "Menu") {
                //console.log("Menu");
                //添加菜单
                var menuDiv = addMenu(pDiv, node);
                //添加菜单子连接
                //alert(addMenu());
                nesting(menuDiv, node);
            }
    }
    //子层嵌套 函数
    function nesting(pDiv, menu){
        //console.log("Menu.SHOW.nesting"+this)
        //所有子节点
        var childNodeList = menu.childNodes;
        //展示第一层，调用子层
        for (var i = 0; i < childNodeList.length; i++) {
        
            var node = childNodeList[i];
            
            if (node.nodeName == "A") {
                addA(pDiv, node);
                //console.log("A");
            }
            else 
                if (node.nodeName == "Menu") {
                    //console.log("Menu");
                    menuDiv = addMenu(pDiv, node);
                    nesting(menuDiv, node);
                }
        }
    }
    //添加url
    function addA(pDiv, node){
        //console.log(node.nodeType);
        
        //alert("nodeId:" + node.getAttribute("ID")+ ";privileg:" + _privileges_[node.getAttribute("ID")]);
        if(!_privileges_[node.getAttribute("ID")])//权限判断
        {
        	return;
        }
        //alert(_privileges_[node.getAttribute("ID")]);
        
        var aDiv = document.createElement("div");
        aDiv.className = "aDivCss";
        aDiv.name = node.getAttribute("text")+"_div";
        var _href=node.getAttribute("href");
        var a = document.createElement("a");

        if(_href.indexOf("?")==-1){
        	_href+="?uid="+typeof(userid)!="undefined"?"":userid+"&pwd="+typeof(pwd)!="undefined"?"":pwd;
        }else{
        	_href+="&uid="+typeof(userid)=="undefined"?"":userid+"&pwd="+typeof(pwd)=="undefined"?"":pwd;
        }
        a.href =_href; //node.getAttribute("href");
        a.target = node.getAttribute("target");
        a.innerHTML = node.getAttribute("text");
        a.name = node.getAttribute("text")+"_a";
       
        pDiv.appendChild(aDiv);
        aDiv.appendChild(a);
        if(isLoading){
        	try{
        	 a.style.color='#fc3';
        	 var _obj=document.getElementsByName("module");
        	 if(_obj){
        		 _obj[0].src=a.href;
        	 }
        	 }catch(e){alert(e.message)}
        	 isLoading=false;
        }
       
    }
    //添加菜单
    function addMenu(pDiv, node){
        //菜单div
    	
        var menuDiv = document.createElement("div");
        menuDiv.className = "menuCss";
        menuDiv.name= node.getAttribute("text")+"menu";
        //事件
        menuDiv.onmouseover = Menu.displaySubMenu;
        menuDiv.onmouseout = Menu.hideSubMenu;      
        //菜单标题span
        var menuTitle = document.createElement("span");
        menuTitle.innerHTML = node.getAttribute("text");
        menuTitle.className = "menuTitleCss";
        menuTitle.name= node.getAttribute("text")+"title";
        
        //菜单body div
        var menuDivBody = document.createElement("div");
        menuDivBody.className = "menuDivBodyCss";
        menuDivBody.name= node.getAttribute("text")+"body";
        menuDivBody.style.display = "none";
        
        pDiv.appendChild(menuDiv);
        menuDiv.appendChild(menuTitle);
        menuDiv.appendChild(menuDivBody);
        return menuDivBody;
    }
}

Menu.displaySubMenu = function(){
    var subMenu = this.childNodes[1];
    subMenu.style.display = "block";
}

Menu.hideSubMenu = function(){
    var subMenu = this.childNodes[1];
    subMenu.style.display = "none";
}

Menu.hsSubMenu = function(evt){
	var e=(evt)?evt:window.event; 
	if (window.event) { 
		e.cancelBubble=true; 
	} else { 
		e.stopPropagation(); 
	} 
    var subMenu = this.childNodes[1];
    
    if(subMenu.style.display == "none")
    	subMenu.style.display = "block";
    else 
   		 subMenu.style.display = "none";
}
///////////////////////////////////////////////////////////////////////////////////////////////
Menu.WINDOWS_DIV;
Menu.SHOW_WINDOWS = function(div){
	Menu.SHOW_MASK();
	//var str = "<div  onclick ='Menu.CLOSE_WINDOWS();'>关闭</div><br>";
	var titleDiv = document.createElement("div");
	titleDiv.className = "configWindowsTitle";
	titleDiv.onclick = Menu.CLOSE_WINDOWS;
	titleDiv.innerHTML = "关闭";
	//var htmlinfo = str+html;
	if(Menu.WINDOWS_DIV==null||!Menu.WINDOWS_DIV){
		Menu.WINDOWS_DIV = document.createElement("DIV");
		Menu.WINDOWS_DIV.className = "configWindows";
		
		//var p = Point.MOUNSE_POSITION();
		//Menu.WINDOWS_DIV.style.top = p.y;
		//console.log(p.x);
		//windDiv.style.cssText=css;
		//Menu.WINDOWS_DIV.className=cssName;
		Menu.WINDOWS_DIV.appendChild(titleDiv);
		Menu.WINDOWS_DIV.appendChild(div);

		Menu.WINDOWS_DIV.style.display="";
		Menu.WINDOWS_DIV.style.zIndex=100;
		Menu.WINDOWS_DIV.style.background= "#CCCFFF";
	}else{
		//windDiv.style.cssTxt=css;
		//Menu.WINDOWS_DIV.className=cssName;
		Menu.WINDOWS_DIV.innerHTML=htmlinfo;
		Menu.WINDOWS_DIV.style.display="";
		Menu.WINDOWS_DIV.stytle.zIndex=100;
	}
	document.body.appendChild(Menu.WINDOWS_DIV);
}
Menu.CLOSE_WINDOWS = function (){
	console.log("Menu.CLOSE_WINDOWS");
	if(Menu.WINDOWS_DIV!=null){
		document.body.removeChild(Menu.WINDOWS_DIV);
		Menu.WINDOWS_DIV=null;
	}
	Menu.CLOSE_MASK();
}

//蒙层
Menu.MASK_DIV;
Menu.SHOW_MASK = function ()
	{
		if(Menu.MASK_DIV==null||!Menu.MASK_DIV){
			Menu.MASK_DIV=document.createElement("DIV");
			Menu.MASK_DIV.style.cssText="position:absolute;top:0px;left:0px;bockground-color:#000";
			if(Browser.isMozilla){
				Menu.MASK_DIV.style.cssText="position:absolute;top:0px;left:0px;background-color:#000;opacity:.3;";
				}
			else{
				Menu.MASK_DIV.style.cssText="position:absolute;top:0px;ledt:0px;background-color:#000;filter:alpha(opacity=30)";
			}
			Menu.MASK_DIV.style.height=document.body.clientHeight;
			Menu.MASK_DIV.style.width=document.body.clientWidth;
			
			document.body.appendChild(Menu.MASK_DIV);
			}
	}
Menu.CLOSE_MASK =function(){
		if(Menu.MASK_DIV!=null)
		{
			document.body.removeChild(Menu.MASK_DIV);
			Menu.MASK_DIV=null;
			return true;
		}
	}


Menu.event = function(node, even, fun){
    if (node.attachEvent) 
        node.attachEvent('on' + even, fun);//		
    else 
        if (node.addEventListener) 
            node.addEventListener(even, fun, true);
    
}

Menu.checked= function(name){
	var raios = document.getElementsByName(name);
	for(var i=0;i<raios.length;i++){
		if(raios[i].checked){
			return raios[i];
		}
	}
	return null;
}
Menu.frames_checked= function(frame,name){
	var raios = window.frames[frame].document.getElementsByName(name);
	for(var i=0;i<raios.length;i++){
		if(raios[i].checked){
			return raios[i];
		}
	}
	return null;
}

function testRequest(method,urlstr,postDatastr,callback)
	{
		var xmlhttprequest=null;
		msxml_progid=['MSXML2.XMLHTTP.3.0','MSXML2.XMLHTTP','Microsoft.XMLHTTP'];
		try
			{
				xmlhttprequest=new XMLHttpRequest();
			}catch(e)
			{
				for(var i=0;i<msxml_progid.length;++i)
					{
						try
							{
								xmlhttprequest=new ActiveXObject(msxml_progid[i]);
								break;
							}catch(e){
							}
					}
			}
		if(xmlhttprequest)
			{
				var url="";
				var poststr="";
				if(method=='GET')
					{
						url=urlstr+"?"+postDatastr;
					}
				else if(method=='POST')
					{
						url=urlstr;
						poststr=postDatastr;
					}
				else if(method=="PUT"){
					url=urlstr;//+"?"+postDatastr;
					poststr=postDatastr;
				}
				else if(method=="DELETE"){
					url=urlstr+"?"+postDatastr;
					poststr=postDatastr;
				}
						
				xmlhttprequest.onreadystatechange=function()
					{
						if(xmlhttprequest.readyState==4&&xmlhttprequest.status==200)
							{
								if(Browser.isIE)
									{
										if(callback)
										callback(xmlhttprequest.responseText);
										//callback(gb2utf8(xmlhttprequest.responseBody));
									}
								else
									{
										//alert(callback);
										if(callback)
										callback(xmlhttprequest.responseText);
									}
							 }
					}
				xmlhttprequest.open(method,url,true);
				xmlhttprequest.setRequestHeader("Content-Type","application/x-www-form-urlencoded");
				xmlhttprequest.send(poststr);
		 }
	}

function console(){}
console.log = function(){}
