package com.gmteam.framework.component.login.web;

import java.io.Serializable;

import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;

import org.springframework.stereotype.Controller;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RequestMethod;
import org.springframework.web.bind.annotation.ResponseBody;

@Controller
public class LoginControll implements Serializable {
    @RequestMapping(value="/login.do",method=RequestMethod.POST)
    public @ResponseBody Json Login(HttpServletRequest request,HttpServletResponse response) {
        Json j = new Json();
        String name = request.getParameter("name");
        System.out.println(name);
        String password = request.getParameter("password");
        System.out.println(password);
        if("zhangsan".equals(name)&&"aaa".equals(password)){
            System.out.println("进入success");
            j.setMessage("登录成功！欢迎您"+name);
            j.setSuccess(true);
        }else{
            j.setMessage("失败，用户名密码错误");
            j.setSuccess(false);
        }
        return j;
    }
}