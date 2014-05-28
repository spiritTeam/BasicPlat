/**
 * 主界面相关的Javascript方法，注意这个js只能被引用在主界面，这里的方法不处理主界面的业务逻辑。
 * 只处理框架中针对主界面，或其他界面调用主界面的逻辑。
 * 注意，他要引用在jquery引用的后面
 */
var IS_MAINPAGE=true;

/**
 * 打开windows的数组
 */
var winArray = [];

/**
 * 得到窗口的ID
 * @returns 窗口ID
 */
function getWinUUID() {
  //得到UUID
  var _uuid="";
  var isID=false;
  while (!isID) {
    _uuid=getUUID();
    isID=true;
    $(winArray).each(function(){
      if (this.winID==_uuid) {
        isID=false;
        return;
      }
    });
  }
  return _uuid;
}

/**
 * 创建并打开easyUi的窗口
 * @param title 窗口标题
 * @param url 窗口内嵌的iframe的url
 * @param height 窗口高度
 * @param width 窗口宽度
 * @param icon 窗口图标
 * @param modal 是否是模态窗口
 * @param expandAttr 窗口的扩展属性，可定义iframe的id，是javaScript对象，如expandAttr={"frameID":"iframeID"}
 * @returns 返回生成窗口的UUID
 */
function newWin(title, url, height, width, icon, _modal, expandAttr) {
  //得到UUID
  var _uuid = getWinUUID();
  if (!_uuid) return ;
  //在顶层窗口创建对象
  var newWinDiv = window.document.createElement("div");
  var newWin = window.document.createElement("iframe");
  $(newWin).attr("width", "100%").attr("height", "100%")
    .attr("scrolling", "no")
    .attr("frameborder", "no")
    .attr("src", url.indexOf("?")==-1?url+"?_winID="+_uuid:url+"&_winID="+_uuid);
  if (expandAttr) {
    if (expandAttr.frameID) $(newWin).attr("id", expandAttr.frameID);
  }
  $(newWin).appendTo($(newWinDiv));
  //esayUi win处理
  var top = ($(window).height() - parseInt(height))*0.5;
  var left = ($(window).width() - parseInt(width))*0.5;
  $(newWinDiv).window({
    title: title,
    iconCls: (icon?icon:"icon-aWin"),
    width: parseInt(width),
    height: parseInt(height),
    top: top,
    left: left,     
    modal: _modal,
    collapsible: false,
    shadow: true,
    closed: true,
    resizable:false,
    draggable: true,
    inline: false,
    minimizable: false,
    maximizable: false,
    onBeforeClose: function(){
      winID=$(newWinDiv).attr("winID");
      var _i=-1;
      $(winArray).each(function(i){
        if (winID==this.winID) {
          _i=i;
          return ;
        }
      });
      if (_i!=-1) winArray.splice(_i,1);
    }
  });

  $(newWinDiv).attr("winID", _uuid);
  //设置效果
  var winObj = $(newWinDiv).parent();
  winObj.css("padding", "0");
  winObj.css("border", "1px solid #999999");
  var tempObj = winObj.parent().find(".window-shadow");
  if (tempObj) {
    tempObj.css("width", (parseInt(tempObj.css("width"))-14)+"px");
    tempObj.css("height", (parseInt(tempObj.css("height"))-14)+"px");
  }
  tempObj = winObj.find(".panel-header");
  if (tempObj) {
    tempObj.css({
      "height":"26px",
      "background-image":"url('"+_PATH+"/resources/images/mainPage/bg_gery.png')",
      "filter":"",
      "border-bottom":"0px"
    });
  }
  tempObj = tempObj.find(".panel-title");
  if (tempObj) {
    tempObj.css({
      "height":"22px",
      "padding-left":(parseInt(tempObj.css("padding-left"))+10)+"px",
      "padding-top":"10px",
      "color":"black",
      "font-weight":"bold",
      "font-size":"14px"
    });
  }
  tempObj = tempObj.parent().find(".panel-icon");
  if (tempObj) {
    tempObj.css({
      "top":"60%",
      "left":"8px"
    });
  }
  tempObj = tempObj.parent().find(".panel-tool");
  if (tempObj) {
    tempObj.css({
      "top":"60%",
      "right":"10px"
    });
    tempObj = tempObj.find(".panel-tool-close");
    if (tempObj) {
      tempObj.css({
        "background":"url('"+_PATH+"/resources/images/mainPage/close_1.png')",
        "height":"15px",
        "width":"15px"
      });
    }
  }
  tempObj = winObj.find(".panel-body");
  if (tempObj) {
    tempObj.css({
      "border":"0px",
      "width":(parseInt(tempObj.css("width"))+2)+"px",
      "border-top":"1px solid #274019"
    });
    var _bodyHeight=tempObj.css("height");
    tempObj=tempObj.find("iframe");
    tempObj.attr("height", (parseInt(_bodyHeight)-4)+"px");
  }
  $(newWinDiv).window("open");
  //全局变量处理
  winArray.push({"winID": _uuid, "winOBJ": $(newWinDiv)});
  return _uuid;
}

/**
 * 关闭并销毁窗口
 * @param winId 窗口的ID
 */
function closeWin(winId) {
  $(winArray).each(function(){
    if (this.winID==winId) {
      this.winOBJ.window('close');
      return ;
    }
  });
}

/**
 * 得到窗口jquery对象
 * @param winId 窗口的ID
 */
function getWin(winId) {
  var ret=null;
  $(winArray).each(function(){
    if (this.winID==winId) {
      ret = this.winOBJ;
      return ;
    }
  });
  return ret;
}