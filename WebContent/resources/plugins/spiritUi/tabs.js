/**
 * jQuery spiritUi-spiretTabs 精灵组件
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
  //插件内部的全局变量
  var tabs = [],    //所有页签信息
  _sTabs = [],  //所有可显示的页签信息
  _lTabs = [],  //左侧隐藏的页签信息
  _rTabs = [];  //右侧隐藏的页签信息
  var selectedTabs; //当前被选中的tab
  /**
   * 处理tabs，包括创建和应用新的属性
   */
  function doTabs(target, options) {
    var oldData = $.data(target, 'spiretTabs');//取得原来绑定到对象上的spiretTabs数据
    if (oldData) {//若已经是本控件了，则合并参数
      var newStyleCss = $.extend(true, {}, oldData.styleCss, isEmpty(options)?"":options.styleCss);
      //处理每一个tab
      var i=0, j=0, otlen=oldData.tabs?oldData.tabs.length:0, dtlen=isEmpty(options)?0:(options.tabs?options.tabs.length:0);
      for (; j<dtlen; j++) {
        oneNewTabs = $.extend(true, {}, oldData.styleCss, isEmpty(options)?"":options.styleCss);
      }
      j=0;
      for (; i<otlen; i++) {
      }

      oldData= $.extend({}, oldData, data);
      oldData.styleCss = newStyleCss;
    } else {//若不是本控件，则删除对象的所有数据，和内部的内容，为新建做准备
      oldData = $.extend(true, {}, $.fn.spiretTabs.defaults, options);
      //处理每一个tab
      
    }

    $(target).css(oldData.styleCss);
    oldData.width?$(target).css({"width":400}):0;
    oldData.height?$(target).css({"height":30}):0;
    alert($(target).css("width"));

    $(target).css({"width":100, "height": 100});
    //处理数据
    
    //绑定数据到具体的对象(这里是jQuery选择器选择出来的Dom对象)
    //根据数据绘制控件
  }
  //页标签主函数
  $.fn.spiretTabs = function(options, param) {
    //若参数一为字符串，则直接当作本插件的方法进行处理，这里的this是本插件对应的jquery选择器的选择结果
    if (typeof options=='string') {
      return $.fn.spiretTabs.methods[options](this, param);
    }
    var i=0, _length=this.length;
    if (_length>0) {
      for (; i<_length; i++) doTabs(this[i], options);
    };
    return this;
  };
  //插件方法，参考eaqyUi的写法
  $.fn.spiretTabs.methods = {
  };

  //默认属性
  $.fn.spiretTabs.defaults = {
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
    defaultTabs: { //默认的页签规则，若每个页标签不设定自己的规则，则所有页签的规则以此为准
      fontSize: 12, //字体大小，根据此计算宽度，这个参数将覆盖normalCss，mouseOverCss中有关字体大小的设置，但不覆盖selCss中的设置
      maxTextLength: 30,//最大文字宽度:大于此值,遮罩主
      normalCss: "", //常态css样式(未选中，鼠标未悬停)，可包括边框/字体/背景，注意，要是json格式的
      mouseOverCss: "", //鼠标悬停样式，可包括边框/字体/背景，注意，要是json格式的
      selCss: "", //选中后样式，可包括边框/字体/背景，注意，要是json格式的
    },
    tabs:[] //页标签数组
  };
})(jQuery);