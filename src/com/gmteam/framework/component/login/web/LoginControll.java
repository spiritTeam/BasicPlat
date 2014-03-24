package com.gmteam.framework.component.login.web;

import org.springframework.stereotype.Controller;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.ResponseBody;

import com.gmteam.framework.component.login.pojo.UserLogin;

@Controller
public class LoginControll {
    @RequestMapping(value="/login.do")
    public @ResponseBody String Login(UserLogin user) {
    	System.out.println("进入l losgin Contsdsoller");
    	System.out.println(user.getLoginName());
    	System.out.println(user.getPassword());
		return null;
    }
}