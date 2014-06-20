<%@page language="java" contentType="text/html; charset=UTF-8" pageEncoding="UTF-8"%>
<%@page import="java.util.*"%>
<%@page import="com.gmteam.framework.FConstants"%>
<%
  String path = request.getContextPath();
%>
<!DOCTYPE html>
<html xmlns="http://www.w3.org/1999/xhtml">
<head>
<title><%=FConstants.PLATFORM_NAME%></title>
<meta http-equiv="Content-Type" content="text/html; charset=UTF-8">
<meta http-equiv="cache-control" content="no-cache"/>
<meta http-equiv="pragma" content="no-cache"/>
<meta http-equiv="expires" content="0"/>
<jsp:include page="/common/sysInclude.jsp" flush="true"/>
<script type="text/javascript" src="<%=path%>/resources/js/mainPage.utils.js"></script>
<link rel="stylesheet" type="text/css" href="<%=path%>/resources/css/mainPage.css"/>

<style type="text/css">
</style>
</head>

<body class="_body"><span style="font-size:10px;">3</span>
<center>
<div id="topFrame">
<iframe id ="test"  width=180 height=120 src="<%=path%>/demo/treeDemo.jsp" ></iframe>
</div>
<div id="mainFrame">
<div id="aa"></div>
<input type=button onclick="test()" value="test"/>
</div>
<div id="footFrame">
</div>
</center>
<script>
var i=true;
function test() {
 i=!i;
 if (i)
 $("#test").attr("src", "<%=path%>/demo/fileUploadDemo.jsp" );
 else
 $("#test").attr("src", "<%=path%>/demo/treeDemo.jsp" );
}

var PAGE_WIDTH=-1;//主页面的宽度，若为-1表明页面宽度自适应
var PAGE_HEIGHT=-1;//主页面的高度，若为-1表明页面宽度自适应
$(function() {
  $("#mainFrame").css({"position":"absolute", "width":"200", "height":"160", "border":"red solid 1px"});
});

//子界面调用此方法进行注销，主要用于用户同时登录不同浏览器的判断逻辑
function onlyLogout(ip, mac, browser) {
  var msg = "您已经在["+ip+"("+mac+")]客户端用["+browser+"]浏览器重新登录了，当前登录失效！";
  if ((!ip&&!mac)||(ip+mac=="")) msg = "您已经在另一客户端用["+browser+"]浏览器重新登录了，当前登录失效！";
  $.messager.alert("提示", msg+"<br/>现返回登录页面。", "info", function(){ logout(); });
}
//注销
function logout() {
  var url=_PATH+"/logout.do";
  $.ajax({type:"post", async:true, url:url, data:null, dataType:"json",
    success: function(json) {
      if (json.type==1) {
        window.location.href="<%=path%>/login/login.jsp?noAuth";
      } else {
        $.messager.alert("错误", "注销失败："+json.data+"！</br>返回登录页面。", "error", function(){
          window.location.href="<%=path%>/login/login.jsp?noAuth";
        });
      }
    },
    error: function(errorData) {
      $.messager.alert("错误", "注销失败：</br>"+(errorData?errorData.responseText:"")+"！<br/>返回登录页面。", "error", function(){
        window.location.href="<%=path%>/login/login.jsp?noAuth";
      });
    }
  });
};


</script>
<script>
//下面是jQuery插件部分
/*
 * jQuery spiritUi-spiretTabs
 * 精灵页标签，注意这个标签只负责标签部分的展示，具体点击标签的功能(比如，刷新一个页面区域)，此插件不处理。
 * 通过每个标签的click事件来完成
 *
 * Copyright (c) 2014.6 wh
 *
 * Licensed same as jquery - MIT License
 * http://www.opensource.org/licenses/mit-license.php
 */
 
(function($) {
  //插件内部的全局变量
  var tabs = [],    //所有页签信息
  _sTabs = [],  //所有可显示的页签信息
  _lTabs = [],  //左侧隐藏的页签信息
  _rTabs = [];  //右侧隐藏的页签信息
  var selectedTabs; //当前被选中的tab

  /**
   * 处理tabs，包括创建和应用新的属性
   */
  function doTabs(target) {
  	//
  }
  //页标签主函数
  $.fn.spiretTabs = function(options, param) {
    //若参数一为字符串，则直接当作本插件的方法进行处理，这里的this是本插件对应的jquery选择器的选择结果
    if (typeof options=='string') {
      return $.fn.spiretTabs.methods[options](this, param);
    }
    return {
      var i=0, _length=this.length;
      if (_length>0) {
        for (; i<_length; i++) {
          var _this = this[i];
          var state = $.data(_this, 'spiretTabs');
          if (state) {//若已经是本控件了，则合并参数
          } else {//若不是本控件，则删除对象的所有数据，和内部的内容，为新建做准备
          	
          }

          //判断本对象是否已经是spiretTabs了？
          //处理数据
          //绑定数据到具体的对象(这里是jQuery选择器选择出来的Dom对象)
          //根据数据绘制控件
          doTabs(_this);
        };
      };
    };
  };
  //插件方法，参考eaqyUi的写法
  $.fn.spiretTabs.methods = {
  };

  //默认属性
  $.fn.spiretTabs.defaults = {
    id: null, //标识
    styleCss: null, //div容器的css，通过其定义容器的位置，边框等信息
    width: "400px", //div容器的宽度，若无此信息，div容器宽度以styleCss为准，否则以此信息为div容器宽度
    mutualType: false, //两页标签的交互区域的处理模式，若为false，则无交互区域，用css处理交互，若为true则有交互区域，交互用图片来处理
    
    defaultTabs: { //默认的页签规则，若每个页标签不设定自己的规则，则所有页签的官则以此为准
      maxTextLength: 30,//最大文字宽度:大于此值,用...代替
      
    },
    tabs:[]
  };
})(jQuery);


var a = $("#aa").spiretTabs("abc");
$("#aa").spiretTabs("def");
//alert(a.abc);
</script>
</body>
</html>