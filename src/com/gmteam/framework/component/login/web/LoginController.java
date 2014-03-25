package com.gmteam.framework.component.login.web;

import java.util.HashMap;
import java.util.Map;

import javax.annotation.Resource;
import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpSession;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Controller;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.ResponseBody;

import com.gmteam.framework.IConstants;
import com.gmteam.framework.cache.CacheEle;
import com.gmteam.framework.cache.SystemCache;
import com.gmteam.framework.component.login.pojo.PlatUser;
import com.gmteam.framework.component.login.pojo.UserLogin;
import com.gmteam.framework.component.login.service.PlatUserService;

@Controller
public class LoginController {
	@Resource
    private PlatUserService userService;
    
    /**
     * @param outSideUser
     * @param req
     * @return
     */
    @RequestMapping("/login.do")
    public @ResponseBody Map<String,Object> Login(UserLogin userLogin,HttpServletRequest req) {
        Map<String,Object> retObj = new HashMap<String,Object>();
        try {
            PlatUser user = userService.getPlatUserByLoginName(userLogin.getLoginName());
            if(user==null){
                retObj.put("type", "2");
                retObj.put("data", "没有登录名为["+userLogin.getLoginName()+"]的用户！");
            }else if(!userLogin.getPassword().equals(user.getPassword())){
                retObj.put("type", "2");
                retObj.put("data", "密码不匹配！");
            }else{
                //设置用户Session缓存
                HttpSession session = req.getSession();
                if(SystemCache.getCache(IConstants.USERSESSIONMAP)==null){
                    SystemCache.setCache(
                            new CacheEle<Map<String, UserLogin>>(IConstants.USERSESSIONMAP, "用户Session缓存", new HashMap<String, UserLogin>())
                        );
                }
                UserLogin oldUserLogin = ((CacheEle<Map<String, UserLogin>>) SystemCache.getCache(IConstants.USERSESSIONMAP)).getContent().remove(user.getId());
                userLogin.setSessionId(session.getId());
                ((CacheEle<Map<String, UserLogin>>) SystemCache.getCache(IConstants.USERSESSIONMAP)).getContent().put(user.getId(), userLogin);
                //写用户信息
                session.setAttribute(IConstants.SESSION_USER, user);
                retObj.put("type", "1");
                retObj.put("data", oldUserLogin);
            }
        } catch (Exception e) {
            e.printStackTrace();
            retObj.put("type", "-1");
            retObj.put("data", e.getMessage());
        }
        return retObj;
    }
}