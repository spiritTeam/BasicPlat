/**
 * jQuery spiritui-accordion 精灵组件
 * 手风琴控件。
 *
 * Copyright (c) 2014.8 wh
 *
 * Licensed same as jquery - MIT License
 * http://www.opensource.org/licenses/mit-license.php
 */

(function($) {
	//本控件内的全局变量
	var _bv = getBrowserVersion();

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
   * 处理accordion，包括创建和应用新的属性
   */
  function doAccordion(target, options) {
    var _options = $.data(target, 'spiritAccordions');//取得原来绑定到对象上的spiritAccordion数据

    //合并accordion
    var newAccordions = _options?(_options.accordions?_options.accordions:[]):[];
    if (_options&&_options.accordions) _options.accordions=[];
    var j=0, olen=newTabs.length;
    var i=0, len=0, aTab=null;
    if (options&&options.accordions&&options.accordions.length>0) len=options.accordions.length;
    if (len>0) {
      for (; i<len; i++) {
        aAccordion=options.tabs[i];
        aAccordion._index=i;
        var hasFound=false;
        if (olen>0) {
          for (j=0; j<olen; j++){
            var aAccordion_o=newAccordions[j];
            if (aAccordion.id) hasFound=(aAccordion.id==aAccordion_o.id);
            else hasFound=(aAccordion.title==aAccordion_o.title);
            if (hasFound) {//合并tabs的属性
              var _id=aAccordion_o.id;
              aAccordion_o=$.extend(true, {}, aAccordion_o, aAccordion);
              aAccordion_o.id=_id;
              break;
            }
          }
        }
        if (!hasFound) {//把新的accordion插入accordions
          if (!aAccordion.id) {
            var uId="sui-accordionID-"+getUuid(6), flag=true, count=0;
            while(flag&&count<5) {
              for (j=0; j<olen; j++) {
                if (newAccordions[j].id==uId) {flag=false; break;}
              }
              if (!flag) uId="sui-accordionID-"+getUuid(6);
              flag=!flag;
              count++;
            }
            if (count>=5) uId=t+getUuid(3);
            aAccordion.id=uId;
          }
          aAccordion=$.extend(true, {}, $.fn.spiritTabs.defaults.defaultAccordion, _options?(_options.defaultTab?_options.defaultTab:null):null, options.defaultTab, aTab);
          newAccordions[olen]=aAccordion;
          olen++;
        }
      }
    }
    _options = _options?$.extend(true, {}, _options, options):$.extend(true, {}, $.fn.spiritAccordion.defaults, options);
    _options.tabs=newAccordions;
    //绑定变量
    $.data(target, 'spiritAccordions', _options);
  }

  //手风琴主函数
  $.fn.spiritAccordion = function(options, param) {
    //若参数一为字符串，则直接当作本插件的方法进行处理，这里的this是本插件对应的jquery选择器的选择结果
    if (typeof options=='string') return $.fn.spiritAccordion.methods[options](this, param);
    var i=0, _length=this.length;
    if (_length>0) for (; i<_length; i++) doAccordion(this[i], options);
    return this;
  };
  //插件方法，参考eaqyUi的写法
  $.fn.spiritAccordion.methods = {
  };

  //默认属性
  $.fn.spiritAccordion.defaults = {
    id: null, //标识
    defaultAccordion: { //默认的页签规则，若每个页标签不设定自己的规则，则所有页签的规则以此为准
      maxTextLength: 100,//最大宽度:大于此值,遮罩住超出的字符
      normalCss: "", //常态css样式(未选中，鼠标未悬停)，可包括边框/字体/背景，注意，要是json格式的
      mouseOverCss: "", //鼠标悬停样式，可包括边框/字体/背景，注意，要是json格式的
      selCss: "" //选中后样式，可包括边框/字体/背景，注意，要是json格式的
    },
    accordions:[] //页标签数组
  };
})(jQuery);
/**
$.fn.spiritAccordion.defaults = {
  id: null, //标识
  //div容器的css，通过其定义容器的位置，边框等信息，注意，要是json格式的
  styleCss: {"border": "0", "border-top":"2px solid #2F4A1F", "border-left":"1px solid #589C8D", "border-right":"1px solid #589C8D"},
  width: "400px", //div容器的宽度，若无此信息，div容器宽度以styleCss为准，否则以此信息为div容器宽度
  height: "30px", //div容器的高度，若无此信息，div容器宽度以styleCss为准，否则以此信息为div容器高度
  mutualType: false, //两页标签的交互区域的处理模式，若为false，则无交互区域，用css处理交互，若为true则有交互区域，交互用图片来处理
  defaultAccordion: { //默认的页签规则，若每个页标签不设定自己的规则，则所有页签的规则以此为准
    maxTextLength: 100,//最大宽度:大于此值,遮罩住超出的字符
    normalCss: "", //常态css样式(未选中，鼠标未悬停)，可包括边框/字体/背景，注意，要是json格式的
    mouseOverCss: "", //鼠标悬停样式，可包括边框/字体/背景，注意，要是json格式的
    selCss: "", //选中后样式，可包括边框/字体/背景，注意，要是json格式的
    iconCss: "" //标题栏的图标样式
  },
  accordions:[] //页标签数组
};
*/