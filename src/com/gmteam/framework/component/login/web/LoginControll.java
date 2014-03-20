package com.gmteam.framework.component.login.web;

import javax.servlet.http.HttpServletRequest;
import org.springframework.stereotype.Controller;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RequestMethod;
import org.springframework.web.bind.annotation.ResponseBody;

@Controller
public class LoginControll {
    @RequestMapping(value="/login.do",method=RequestMethod.POST)
    public @ResponseBody String Login(HttpServletRequest request) {
        System.out.println("进入l  login Contrsdsdsdsoller");
        String loginMessage = null;
        String name = request.getParameter("name");
        String password = request.getParameter("password");
        if("zhangsan".equals(name)&&"12345".equals(password)){
            loginMessage = "登录成功！欢迎您"+name;
        }else{
            loginMessage = "登录失败，用户名或密码错误";
        }
        return loginMessage;
    }
}