package com.gmteam.framework.component.login.web;

import java.util.HashMap;
import java.util.Map;

import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpSession;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Controller;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.ResponseBody;

import com.gmteam.framework.IConstants;
import com.gmteam.framework.cache.SystemCache;
import com.gmteam.framework.component.login.pojo.PlatUser;
import com.gmteam.framework.component.login.pojo.UserLogin;
import com.gmteam.framework.component.login.service.PlatUserService;

@Controller
public class LoginControll {
	
    private PlatUserService userService;
    
    public PlatUserService getUserService() {
        return userService;
    }
    @Autowired
    public void setUserService(PlatUserService userService) {
        this.userService = userService;
    }

    /**
     * @param outSideUser
     * @param req
     * @return
     */
    @RequestMapping("/login.do")
    public @ResponseBody Map<String,Object> Login(PlatUser outSideUser,HttpServletRequest req) {
        Map<String,Object> retObj = new HashMap<String,Object>();
    	PlatUser user;
        try {
            user = userService.getPlatUserByLoginName(outSideUser.getLoginName());
            if(user==null){
                retObj.put("type", "2");
                retObj.put("data", "没有登录名为["+outSideUser.getLoginName()+"]的用户！");
            }else if(!outSideUser.getPassword().equals(user.getPassword())){
                retObj.put("type", "2");
                retObj.put("data", "密码不匹配！");
            }else{
                HttpSession session = req.getSession();
                //设置用户Session缓存
                Map<String,UserLogin> userSessionMap = (Map<String, UserLogin>) SystemCache.getCache(IConstants.USERSESSIONMAP);
                System.out.println(user.getId());
                UserLogin userLogin = ((Map<String, UserLogin>)SystemCache.getCache(IConstants.USERSESSIONMAP)).remove(user.getId());
                System.out.println("bbbbb");
                userLogin.setSessionId(session.getId());
                ((Map<String,UserLogin>)SystemCache.getCache(IConstants.USERSESSIONMAP)).put(user.getId(), userLogin);
                //写用户信息
                session.setAttribute(IConstants.SESSION_USER, user);
                retObj.put("type", "1");
                retObj.put("data", userLogin);
            }
        } catch (Exception e) {
            e.printStackTrace();
            retObj.put("type", "-1");
            retObj.put("data", e.getMessage());
        }
        return retObj;
    }
}