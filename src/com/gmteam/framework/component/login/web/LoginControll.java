package com.gmteam.framework.component.login.web;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Controller;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.ResponseBody;

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

    @RequestMapping("/login.do")
    public @ResponseBody String Login(UserLogin user) {
    	System.out.println("进入l losgin Contsdsoller");
    	System.out.println(user.getLoginName());
    	System.out.println(user.getPassword());
    	PlatUser u =userService.getPlatUserByLoginName(user.getLoginName());
    	System.out.println(u.getLoginName());
        System.out.println(u.getPassword());
		return null;
    }
}