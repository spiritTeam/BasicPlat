package com.gmteam.framework.component.login.web;

import java.util.HashMap;
import java.util.Map;

import javax.annotation.Resource;
import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpSession;

import org.springframework.stereotype.Controller;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.ResponseBody;

import com.gmteam.framework.IConstants;
import com.gmteam.framework.component.login.pojo.User;
import com.gmteam.framework.component.login.pojo.UserLogin;
import com.gmteam.framework.component.login.service.UserService;
import com.gmteam.framework.core.cache.CacheEle;
import com.gmteam.framework.core.cache.SystemCache;
@Controller
public class LoginController {
    @Resource
    private UserService userService;
    /**
     * 用户登录
     * @param userLogin 用户登录信息
     * @param req 
     * @return
     */
    @RequestMapping("common/login.do")
    public @ResponseBody Map<String,Object> Login(UserLogin userLogin,HttpServletRequest req) {
        Map<String,Object> retObj = new HashMap<String,Object>();
        try {
            User user = (User) userService.getPlatUserByLoginName(userLogin.getLoginName());
            if(user==null){
                retObj.put("type", "2");
                retObj.put("data", "没有登录名为["+userLogin.getLoginName()+"]的用户！");
            }else if(!userLogin.getPassword().equals(user.getPassword())){
                retObj.put("type", "2");
                retObj.put("data", "密码不匹配！");
            }else{
                //设置用户Session缓存
                HttpSession session = req.getSession();
                UserLogin oldUserLogin = ((CacheEle<Map<String, UserLogin>>) SystemCache.getCache(IConstants.USERSESSIONMAP)).getContent().remove(user.getId());
                userLogin.setSessionId(session.getId());
                ((CacheEle<Map<String, UserLogin>>) SystemCache.getCache(IConstants.USERSESSIONMAP)).getContent().put(user.getId(), userLogin);
                //写用户信息
                session.setAttribute(IConstants.SESSION_USER, user);
                retObj.put("type", "1");
                retObj.put("data", "登录成功");
            }
        } catch (Exception e) {
            e.printStackTrace();
            retObj.put("type", "-1");
            retObj.put("data", e.getMessage());
        }
        return retObj;
    }
    /**
     * 用户注销
     * @param req
     * @return
     */
    @RequestMapping("common/outlogin.do")
    public @ResponseBody Map<String,Object> outlogin(HttpServletRequest req){
        Map<String, Object> retObj = new HashMap<String, Object>();
        try {
            //清除用户Session缓存
            Map<String, UserLogin> userSessionMap = ((CacheEle<Map<String, UserLogin>>)SystemCache.getCache(IConstants.USERSESSIONMAP)).getContent();
            HttpSession session = req.getSession();
            User user = (User)session.getAttribute(IConstants.SESSION_USER);
            UserLogin userLogin = userSessionMap.get(user.getId());
            if (userLogin!=null&&userLogin.getSessionId().equals(session.getId())) {
                userSessionMap.remove(user.getId());
            }
            //清除Session
            session.removeAttribute(IConstants.SESSION_USER);
            //清除全局变量中的Session
            retObj.put("type", "1");
            retObj.put("data", null);
        } catch(Exception e) {
            retObj.put("type", "-1");
            retObj.put("data", e.getMessage());
        }
        return retObj;
    }
}