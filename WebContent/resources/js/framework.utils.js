/**
 * 框架通用方法集，处理与框架结构相关逻辑
 */

/**
 * 得到首页
 */
function getMainPage() {
  var mainPage = null, win = window, pWin = null;
  while (!mainPage) {
    if (win.IS_MAINPAGE) {
      mainPage = win;
    } else {
      pWin = win.top;
      if (pWin==win||pWin==null) pWin = win.opener;
      if (pWin==win||pWin==null) break;
      win=pWin;
    }
  }
  return mainPage;
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
function openWin(title, url, height, width, icon, modal, expandAttr) {
  var mainPage = getMainPage();
  if (mainPage) {
    return mainPage.newWin(title, url, height, width, icon, modal, expandAttr);
  }
}

/**
 * 关闭并销毁窗口
 * @param winId 窗口的ID
 */
function closeWin(winId) {
  var mainPage = getMainPage();
  if (mainPage) {
    mainPage.closeWin(winId);
  }
}

/**
 * 得到窗口jquery对象
 * @param winId 窗口的ID
 */
function getWin(winId) {
  var mainPage = getMainPage();
  if (mainPage) {
    mainPage.getWin(winId);
  }
}
