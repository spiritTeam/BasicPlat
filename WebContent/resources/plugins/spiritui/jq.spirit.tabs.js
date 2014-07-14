/**
 * jQuery spiritui-spiritTabs 精灵组件
 * 页标签。
 * 标签只负责标签部分的展示，具体点击标签的功能(比如，刷新一个页面区域)，此插件不处理。
 * 通过每个标签的click事件来完成
 *
 * Copyright (c) 2014.7 wh
 *
 * Licensed same as jquery - MIT License
 * http://www.opensource.org/licenses/mit-license.php
 */
 
(function($) {
  /**
   * 生成UUID，默认为36位
   */
  var CHARS = "0123456789ABCDEFGHIJKLMNOPQRSTUVWXYZabcdefghijklmnopqrstuvwxyz".split("");  
  function getUuid(len,radix) {
    var chars = CHARS, uuid = [];
    radix = radix||chars.length;
    if (len) {
      for (var i=0; i<len; i++) uuid[i] = chars[0 | Math.random()*radix];  
    } else {
      var r;
      uuid[8] = uuid[13] = uuid[18] = uuid[23] = "-";
      uuid[14] = "4";
      for (var i=0; i<36; i++) {
        if (!uuid[i]) {
          r = 0 | Math.random()*16;
          uuid[i] = chars[(i == 19)?(r&0x3)|0x8:r];
        }
      }
    }
    return uuid.join("");
  }

  /**
   * 处理tabs，包括创建和应用新的属性
   */
  function doTabs(target, options) {
    var _options = $.data(target, 'spiritTabs');//取得原来绑定到对象上的spiritTabs数据

    //合并tabs
    var newTabs = _options?(_options.tabs?_options.tabs:[]):[];
    if (_options&&_options.tabs) _options.tabs=[];
    var j=0, olen=newTabs.length;
    var i=0, len=0, aTab=null;
    if (options&&options.tabs&&options.tabs.length>0) len=options.tabs.length;
    if (len>0) {
      for (; i<len; i++) {
        aTab=options.tabs[i];
        var hasFound=false;
        if (olen>0) {
          for (j=0; j<olen; j++){
            var aTab_o=newTabs[j];
            if (aTab.id) hasFound=(aTab.id==aTab_o.id);
            else hasFound=(aTab.title==aTab_o.title);
            if (hasFound) {//合并tabs的属性
              var _id=aTab_o.id;
              aTab_o=$.extend(true, {}, aTab_o, aTab);
              aTab_o.id=_id;
              break;
            }
          }
        }
        if (!hasFound) {//把新的tab插入tabs
          if (!aTab.id) {
            var uId="sui-tabID-"+getUuid(6), flag=true, count=0;
            while(flag&&count<5) {
              for (j=0; j<olen; j++) {
                if (newTabs[j].id==uId) {flag=false; break;}
              }
              if (!flag) uId="sui-tabID-"+getUuid(6);
              flag=!flag;
              count++;
            }
            if (count>=5) uId=t+getUuid(3);
            aTab.id=uId;
            aTab=$.extend(true, {}, $.fn.spiritTabs.defaults.defaultTab, aTab);
          }
          newTabs[olen]=aTab;
          olen++;
        }
      }
    }
    _options = _options?$.extend(true, {}, _options, options):$.extend(true, {}, $.fn.spiritTabs.defaults, options);
    _options.tabs=newTabs;
    //绑定变量
    $.data(target, 'spiritTabs', _options);

    //对宽高进行处理
    var _width = _options.width?_options.width:(_options.styleCss?(_options.styleCss.width?_options.styleCss.width:$(target).spiritUtils("getViewWidth")):$(target).spiritUtils("getViewWidth"));
    var _height = _options.height?_options.height:(_options.styleCss?(_options.styleCss.height?_options.styleCss.height:$(target).spiritUtils("getViewHeight")):$(target).spiritUtils("getViewHeight"));
    $(target).addClass("tabsContainer").css("overflow", "hidden");
    if (_options.styleCss&&_options.styleCss!="") $(target).css(_options.styleCss);
    $(target).spiritUtils("setWidthByViewWidth", _width);
    $(target).spiritUtils("setHeightByViewHeight", _height);
    //处理数据
    //根据数据绘制控件
    i=0, len=(_options?(_options.tabs?_options.tabs.length:0):0);
    var _tabLeft=0;
    for (;i<len; i++) {
      var t_normal_cls = (i==0?"tab_normal_f":(i==(len-1)?"tab_normal_l":"tab_normal"));
      aTab = _options.tabs[i];
      var to=$(target).find("#"+aTab.id);
      //查找是否已经有了tab
      if (to.length==0) {//未找到
        to=$("<div id='"+aTab.id+"'></div>");
        to.appendTo(target);
      }
      to.html("");
      //画标签
      to.removeClass("tab_normal_f").removeClass("tab_normal_l");
      to.addClass("tab").addClass(t_normal_cls);
      if (aTab.normalCss&&aTab.normalCss!="") to.css(aTab.normalCss);
      var titleDiv=$("<div id='t_"+aTab.id+"'>"+aTab.title+"</div>").addClass("tab_title");
      titleDiv.css({"height":to.css("font-size"), "line-height":to.css("font-size"), "font-size":to.css("font-size"), "font-family":to.css("font-family")});
      titleDiv.appendTo(to);
      titleDiv.css({"left":10});
      if (titleDiv.width()>aTab.maxTextLength) {
        titleDiv.css({"width":aTab.maxTextLength});
        to.attr("title", aTab.title);
        titleDiv.css({"left":15});
      }
      to.css({"left": _tabLeft, "width": titleDiv.width()+20});
      to.spiritUtils("setHeightByViewHeight", $(target).height());
      to.css("line-height", to.css("height"));
      titleDiv.css({"top":(to.height()-titleDiv.height())/2});
      _tabLeft += to.spiritUtils("getViewWidth");
      //绑定标签和数据
      $.data(to, "tabData", aTab);
      //鼠标效果
      to.mouseover(function(){
        if ($(this).hasClass("tab_sel")) return;
        $(this).addClass("tab_mouseOver");
        if (aTab.mouseOverCss&&aTab.mouseOverCss!="") to.css(aTab.mouseOverCss);
      }).mouseout(function(){
        if ($(this).hasClass("tab_sel")) return;
        $(this).removeClass("tab_mouseOver");
        if (aTab.normalCss&&aTab.normalCss!="") to.css(aTab.normalCss);
      });
      //点击
      to.bind("click", function(){
        if ($(this).hasClass("tab_sel")) return;
        $(target).find(".tab").removeClass("tab_sel").removeClass("tab_mouseOver");
        j=0, olen=len;
        for(;j<olen;j++) {
          aTab_o= _options.tabs[j];
          if (aTab_o.normalCss&&aTab_o.normalCss!=""){
            $(target).find("#"+aTab_o.id).css(aTab_o.normalCss);
          }
          if ($(this).attr("id")==aTab_o.id) aTab=aTab_o;
        }
        $(this).addClass("tab_sel");
        if (aTab.selCss&&aTab.selCss!="") to.css(aTab.selCss);
        //调用用户定义的点击
        if (aTab.onClick) aTab.onClick($(this));
      });
    }
  }

  //页标签主函数
  $.fn.spiritTabs = function(options, param) {
    //若参数一为字符串，则直接当作本插件的方法进行处理，这里的this是本插件对应的jquery选择器的选择结果
    if (typeof options=='string') return $.fn.spiritTabs.methods[options](this, param);

    var i=0, _length=this.length;
    if (_length>0) for (; i<_length; i++) doTabs(this[i], options);
    return this;
  };
  //插件方法，参考eaqyUi的写法
  $.fn.spiritTabs.methods = {
  };

  //默认属性
  $.fn.spiritTabs.defaults = {
    id: null, //标识
    mutualType: false, //两页标签的交互区域的处理模式，若为false，则无交互区域，用css处理交互，若为true则有交互区域，交互用图片来处理
    mutualStyle: { //交互区样式，当mutualType=true生效
      width: "10px", //交互区宽度
      firstImgUrl:"", //最左边未选中交互区图片
      firstSelImgUrl: "", //最左边选中交互区图片
      lastImgUrl:"", //最右边未选中交互区图片
      lastSelImgUrl: "", //最右边选中交互区图片
      middleImgUrl: "", //中间未选中交互区图片
      middleSelRImgUrl: "", //中间左选中交互区图片
      middleSelLImgUrl: "" //中间右选中交互区图片
    },
    defaultTab: { //默认的页签规则，若每个页标签不设定自己的规则，则所有页签的规则以此为准
      maxTextLength: 100,//最大宽度:大于此值,遮罩主
      normalCss: "", //常态css样式(未选中，鼠标未悬停)，可包括边框/字体/背景，注意，要是json格式的
      mouseOverCss: "", //鼠标悬停样式，可包括边框/字体/背景，注意，要是json格式的
      selCss: "", //选中后样式，可包括边框/字体/背景，注意，要是json格式的
    },
    tabs:[] //页标签数组
  };
})(jQuery);
/**
  $.fn.spiritTabs.defaults = {
    id: null, //标识
    //div容器的css，通过其定义容器的位置，边框等信息，注意，要是json格式的
    styleCss: {"border": "0", "border-top":"2px solid #2F4A1F", "border-left":"1px solid #589C8D", "border-right":"1px solid #589C8D"},
    width: "400px", //div容器的宽度，若无此信息，div容器宽度以styleCss为准，否则以此信息为div容器宽度
    height: "30px", //div容器的高度，若无此信息，div容器宽度以styleCss为准，否则以此信息为div容器高度
    mutualType: false, //两页标签的交互区域的处理模式，若为false，则无交互区域，用css处理交互，若为true则有交互区域，交互用图片来处理
    mutualStyle: { //交互区样式，当mutualType=true生效
      width: "10px", //交互区宽度
      firstImgUrl:"", //最左边未选中交互区图片
      firstSelImgUrl: "", //最左边选中交互区图片
      lastImgUrl:"", //最右边未选中交互区图片
      lastSelImgUrl: "", //最右边选中交互区图片
      middleImgUrl: "", //中间未选中交互区图片
      middleSelRImgUrl: "", //中间左选中交互区图片
      middleSelLImgUrl: "" //中间右选中交互区图片
    },
    defaultTab: { //默认的页签规则，若每个页标签不设定自己的规则，则所有页签的规则以此为准
      maxTextLength: 30,//最大宽度:大于此值,遮罩主
      normalCss: "", //常态css样式(未选中，鼠标未悬停)，可包括边框/字体/背景，注意，要是json格式的
      mouseOverCss: "", //鼠标悬停样式，可包括边框/字体/背景，注意，要是json格式的
      selCss: "", //选中后样式，可包括边框/字体/背景，注意，要是json格式的
    },
    tabs:[] //页标签数组
  };
 */