package com.gmteam.framework.component.login.web;

import org.apache.log4j.Logger;
import java.io.IOException;
import java.util.Map;
import javax.servlet.*;
import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;
import javax.servlet.http.HttpSession;
import com.gmteam.framework.IConstants;
import com.gmteam.framework.core.cache.CacheEle;
import com.gmteam.framework.core.cache.SystemCache;
import com.gmteam.framework.component.login.pojo.PlatUser;
import com.gmteam.framework.component.login.pojo.UserLogin;

public class LoginFilter implements Filter {
    private static Logger logger = Logger.getLogger(LoginFilter.class);
    String ingores;
    String noLogin;
    String hasNewLogin;

    public void init(FilterConfig config) throws ServletException {
        this.ingores = config.getInitParameter("ingores");
        this.noLogin = config.getInitParameter("noLogin");
        this.hasNewLogin = config.getInitParameter("hasNewLogin");
    }

    public void doFilter(ServletRequest req, ServletResponse res,FilterChain  chain) throws IOException, ServletException {
        HttpServletRequest request = (HttpServletRequest)req;
        HttpServletResponse response = (HttpServletResponse)res;
        HttpSession session = request.getSession();
        String path=request.getServletPath();
        try {
            String ingoresArray[] = ingores.split(",");
            if (isIngore(path, ingoresArray)) chain.doFilter(req, res);
            else if (session.getAttribute(IConstants.SESSION_USER)!=null) {
                //判断是否用其他Sesson登录了
                CacheEle<Map<String, UserLogin>> userSessionMap =
                    (CacheEle<Map<String, UserLogin>>)SystemCache.getCache(IConstants.USERSESSIONMAP);
                PlatUser user = (PlatUser)session.getAttribute(IConstants.SESSION_USER);
                UserLogin uli = userSessionMap.getContent().get(user.getId());
                if (uli!=null&&!uli.getSessionId().equals(session.getId())) {
                    String loginInfo = "";
                    loginInfo += "&clientIp="+uli.getClientIp();
                    loginInfo += "&clientMacAddr="+uli.getClientMacAddr();
                    loginInfo += "&browser="+uli.getBrowser();
                    response.sendRedirect(request.getContextPath()+hasNewLogin+"?"+loginInfo);
                } else chain.doFilter(req, res);
            } else response.sendRedirect(request.getContextPath()+noLogin);
        } catch (Exception e) {
            logger.error("登陆验证过滤器产生异常：",e);
            response.sendRedirect(request.getContextPath()+noLogin);
        }
    }

    private boolean isIngore(String path,String ingores[]) {
        String _path=(path.indexOf("?")>0)?path.substring(0, path.indexOf("?")):path;
        _path=(_path.indexOf(".do")>0)?(_path.indexOf("!")>0?(_path.substring(0,_path.indexOf("!"))+".do"):_path):_path;
        for (int i=0; i<ingores.length; i++) {
            String ingore=ingores[i];
            if(_path.indexOf(ingore)>=0 ) return true;
        }
        return false;
    }

    public void destroy() {}
}