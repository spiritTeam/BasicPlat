/**
 * 通用Javascript函数，注意这里不要出现任何非基本javascript的方法，如jquery的方法。
 */

/**
 * 用于调试，得到对象中的元素及其值
 * @param obj 检测对象
 * @returns {String} 对象中元素的信息
 */
function allPrpos(obj) {
  var i=0;
  var props = "";
  if (obj==null) props="[allPrposCount=0]\nnull";
  for(var p in obj) {
    i=i+1;
    if (typeof(p)!="function") {
      if ((obj[p]+"").indexOf("[")!==0) {
        props += i+":"+p+"="+obj[p]+";\n";
      }
    }
  }
  props = "[allPrposCount="+i+"]\n"+props;
  return props;
}

/**
 * 扩展String属性：得到中英混排文字符串长度
 */
String.prototype.cnLength = function () {
  return ((this.replace(/[^x00-xFF]/g, "**")).length);
};

/**
 * 删除数组中的元素
 */
Array.prototype.removeByIndex = function (i){
  if (i>=0 && i < this.length) {
    var ret = this.slice(0,i).concat(this.slice(i+1));
    this.length = 0;
    this.push.apply(this,ret);
  }
};

/**
 * 获得url中参数名为paramName的参数值
 * 如果url没有名称为paramName的参数返回null
 * 如果url中参数paramName的值为空，返回值也是空。(如:userName=&password=a或userName&password=a)取userName值为""
 * @returns {String} 在url中指定的paramName参数的值
 */
function getUrlParam(url, paramName) {
  if (!paramName&&!url) return null;
  var _url = url+"";
  if (_url.indexOf("?")==-1) return null;
  _url = "&"+_url.substring(_url.indexOf("?")+1);
  var pos=_url.lastIndexOf("&"+paramName+"=");
  if (pos==-1) return null;
  _url=_url.substring(pos+paramName.length+2);
  return _url.indexOf("&")==-1?_url:_url.substring(0, _url.indexOf("&"));
}

/**
 * 得到当前浏览器版本
 * @returns {String}
 */
function getBrowserVersion() {
    var browser = {};
    var userAgent = navigator.userAgent.toLowerCase();
    var s;
    (s = userAgent.match(/msie ([\d.]+)/))
            ? browser.ie = s[1]
            : (s = userAgent.match(/firefox\/([\d.]+)/))
                    ? browser.firefox = s[1]
                    : (s = userAgent.match(/chrome\/([\d.]+)/))
                            ? browser.chrome = s[1]
                            : (s = userAgent.match(/opera.([\d.]+)/))
                                    ? browser.opera = s[1]
                                    : (s = userAgent
                                            .match(/version\/([\d.]+).*safari/))
                                            ? browser.safari = s[1]
                                            : 0;
    var version = "";
    if (browser.ie) {
        version = 'msie ' + browser.ie;
    } else if (browser.firefox) {
        version = 'firefox ' + browser.firefox;
    } else if (browser.chrome) {
        version = 'chrome ' + browser.chrome;
    } else if (browser.opera) {
        version = 'opera ' + browser.opera;
    } else if (browser.safari) {
        version = 'safari ' + browser.safari;
    } else {
        version = '未知浏览器';
    }
    return version;
} 
/**
 * 单例的setTimeOut,节省资源
 */
var TIMEOUTHANDLE=null;
var _EXCUTED=true;
function singleSetTimeOut(fun, ms) {
  if (TIMEOUTHANDLE!=null) {
    if (_EXCUTED) clearTimeout(TIMEOUTHANDLE);
    else {
      while(!_EXCUTED) {;}
      clearTimeout(TIMEOUTHANDLE);
    }
  }
  _EXCUTED = false;
  TIMEOUTHANDLE=setTimeout(_fun, ms);
  function _fun() {
    fun;
    excuted=true;
  }
}

/**
 * 生成UUID，默认为36位
 */
var CHARS = "0123456789ABCDEFGHIJKLMNOPQRSTUVWXYZabcdefghijklmnopqrstuvwxyz".split("");  
function getUUID(len,radix) {
  var chars = CHARS, uuid = [], i;
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


function string2Object(string) {
	eval("var result = " + decodeURI(string));
	return result;
}

