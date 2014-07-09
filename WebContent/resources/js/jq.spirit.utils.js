/**
 * jQuery spirit 通用方法
 */

(function($) {
  $.fn.spiritUtils = function(options, param) {
    if (typeof options == 'string'){
      return $.fn.spiritUtils.methods[options](this, param);
    }
  };

  $.fn.spiritUtils.methods = {
    getAbsWidth: function (jObj) {//计算对象的绝对宽度
      if (jObj.length!=1) return "不能对多于一个对象计算绝对宽度！";
      return jObj.width()+parseFloat($(jObj).css("margin-left"))+parseFloat($(jObj).css("border-left-width"))+parseFloat($(jObj).css("padding-left"))
        +parseFloat($(jObj).css("padding-right"))+parseFloat($(jObj).css("border-right-width"))+parseFloat($(jObj).css("margin-right"));
    },
    getAbsHeight: function (jObj) {//计算对象的绝对高度
      if (jObj.length!=1) return "不能对多于一个对象计算绝对高度！";
      return jObj.height()+parseFloat($(jObj).css("margin-top"))+parseFloat($(jObj).css("border-top-width"))+parseFloat($(jObj).css("padding-top"))
        +parseFloat($(jObj).css("padding-bottom"))+parseFloat($(jObj).css("border-bottom-width"))+parseFloat($(jObj).css("margin-bottom"));
    },
    getViewWidth: function (jObj) {//计算对象的显示宽度
      if (jObj.length!=1) return "不能对多于一个对象计算显示宽度！";
      return jObj.width()+parseFloat($(jObj).css("border-left-width"))+parseFloat($(jObj).css("padding-left"))
        +parseFloat($(jObj).css("padding-right"))+parseFloat($(jObj).css("border-right-width"));
    },
    getViewHeight: function (jObj) {//计算对象的显示高度
      if (jObj.length!=1) return "不能对多于一个对象计算显示高度！";
      return jObj.height()+parseFloat($(jObj).css("border-top-width"))+parseFloat($(jObj).css("padding-top"))
        +parseFloat($(jObj).css("padding-bottom"))+parseFloat($(jObj).css("border-bottom-width"));
    },
    getSetWidth: function (jObj, viewWidth) {//根据显示宽度，得到对象的设置宽度
      if (jObj.length!=1) return "不能对多于一个对象计算设置宽度！";
      var retW = parseFloat(viewWidth)-(parseFloat($(jObj).css("border-left-width"))+parseFloat($(jObj).css("padding-left"))+parseFloat($(jObj).css("padding-right"))+parseFloat($(jObj).css("border-right-width")));
      return retW>0?retW:0;
    },
    setWidthByViewWidth: function (jObj, viewWidth) {//根据显示宽度，设置对象的宽度
      if (jObj.length!=1) return "不能对多于一个对象计算设置宽度并设置！";
      var retW = parseFloat(viewWidth)-(parseFloat($(jObj).css("border-left-width"))+parseFloat($(jObj).css("padding-left"))+parseFloat($(jObj).css("padding-right"))+parseFloat($(jObj).css("border-right-width")));
      $(jObj).css("width", retW>0?retW:0);
      return retW>0?retW:0;
    },
    getSetHeight: function (jObj, viewHeight) {//根据显示高度，得到对象的设置高度
      if (jObj.length!=1) return "不能对多于一个对象计算设置高度！";
      var retH = parseFloat(viewHeight)-(parseFloat($(jObj).css("border-top-width"))+parseFloat($(jObj).css("padding-top"))+parseFloat($(jObj).css("padding-bottom"))+parseFloat($(jObj).css("border-bottom-width")));
      return retH>0?retH:0;
    },
    setHeightByViewHeight: function (jObj, viewHeight) {//根据显示高度，设置对象的高度
      if (jObj.length!=1) return "不能对多于一个对象计算设置高度并设置！";
      var retH = parseFloat(viewHeight)-(parseFloat($(jObj).css("border-top-width"))+parseFloat($(jObj).css("padding-top"))+parseFloat($(jObj).css("padding-bottom"))+parseFloat($(jObj).css("border-bottom-width")));
      $(jObj).css("height", retH>0?retH:0);
      return retH>0?retH:0;
    }
  };
})(jQuery);