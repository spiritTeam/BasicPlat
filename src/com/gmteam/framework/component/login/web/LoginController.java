package com.gmteam.framework.component.login.web;

import java.util.HashMap;
import java.util.Map;

import javax.annotation.Resource;
import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpSession;

import org.springframework.stereotype.Controller;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.ResponseBody;

import com.gmteam.framework.FConstants;
import com.gmteam.framework.UGA.UgaAuthorityService;
import com.gmteam.framework.UGA.UgaModule;
import com.gmteam.framework.UGA.UgaUser;
import com.gmteam.framework.UGA.UgaUserService;
import com.gmteam.framework.component.login.pojo.UserLogin;
import com.gmteam.framework.core.cache.CacheEle;
import com.gmteam.framework.core.cache.SystemCache;
import com.gmteam.framework.core.model.tree.TreeNode;

@Controller
public class LoginController {
    @Resource
    private UgaUserService ugaUserService;

    @Resource
    private UgaAuthorityService ugaAuthorityService;
    /**
     * 用户登录
     * @param userLogin 用户登录信息
     * @param req request对象
     * @return 返回登录信息对象
     */
    @RequestMapping("login.do")
    public @ResponseBody Map<String,Object> Login(UserLogin userLogin,HttpServletRequest req) {
        Map<String,Object> retObj = new HashMap<String,Object>();
        try {
            UgaUser user = (UgaUser)ugaUserService.getUserByLoginName(userLogin.getLoginName());
            if(user==null){
                retObj.put("type", "2");
                retObj.put("data", "没有登录名为["+userLogin.getLoginName()+"]的用户！");
            }else if(!userLogin.getPassword().equals(user.getPassword())){
                retObj.put("type", "2");
                retObj.put("data", "密码不匹配！");
            }else{
                //设置用户Session缓存
                HttpSession session = req.getSession();
                UserLogin oldUserLogin = ((CacheEle<Map<String, UserLogin>>) SystemCache.getCache(FConstants.USERSESSIONMAP)).getContent().remove(user.getUserId());
                userLogin.setSessionId(session.getId());
                ((CacheEle<Map<String, UserLogin>>) SystemCache.getCache(FConstants.USERSESSIONMAP)).getContent().put(user.getUserId(), userLogin);
                //写用户信息
                session.setAttribute(FConstants.SESSION_USER, user);
                //写用户权限信息
                TreeNode<UgaModule> um = ugaAuthorityService.getUserModuleAuthByUserId(user.getUserId());
                session.setAttribute(FConstants.SESSION_USERAUTHORITY, um);
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
    @RequestMapping("logout.do")
    public @ResponseBody Map<String,Object> logout(HttpServletRequest req){
        Map<String, Object> retObj = new HashMap<String, Object>();
        try {
            //清除用户Session缓存
            Map<String, UserLogin> userSessionMap = ((CacheEle<Map<String, UserLogin>>)SystemCache.getCache(FConstants.USERSESSIONMAP)).getContent();
            HttpSession session = req.getSession();
            UgaUser user = (UgaUser)session.getAttribute(FConstants.SESSION_USER);
            UserLogin userLogin = userSessionMap.get(user.getUserId());
            if (userLogin!=null&&userLogin.getSessionId().equals(session.getId())) userSessionMap.remove(user.getUserId());
            //清除Session
            session.removeAttribute(FConstants.SESSION_USER);
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