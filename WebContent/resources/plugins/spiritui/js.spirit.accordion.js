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
   * 处理tabs，包括创建和应用新的属性
   */
  function spiritAccordion(target, options) {
  	
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
      maxTextLength: 100,//最大宽度:大于此值,遮罩主
      normalCss: "", //常态css样式(未选中，鼠标未悬停)，可包括边框/字体/背景，注意，要是json格式的
      mouseOverCss: "", //鼠标悬停样式，可包括边框/字体/背景，注意，要是json格式的
      selCss: "" //选中后样式，可包括边框/字体/背景，注意，要是json格式的
    },
    accordions:[] //页标签数组
  };
})(jQuery);