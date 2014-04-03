package com.gmteam.framework.component.login.web;

import java.util.ArrayList;
import java.util.HashMap;
import java.util.List;
import java.util.Map;

import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpSession;

import org.springframework.stereotype.Controller;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.ResponseBody;

@Controller
public class TestLogin {
    @RequestMapping("/toLogin.do")
    public @ResponseBody Map<String,Object> toLogin(HttpServletRequest request,HttpSession session){
        Map<String,Object> map = new HashMap<String,Object>();
        String name = "abc";
        TreeA t = new TreeA();
        TreeA t2 = null;
        TreeA t3 = null;
        TreeA t4 = null;
        t.setId(""+1);
        t.setUrl("/test/appSys/dataIS/manage/manageMain.jsp");
        t.setAllowChildren(true);
        t.setTitle("主功能");
        //第二层
        List<TreeA> list = new ArrayList<TreeA>();
        for(int i=2;i<=4;i++){
            //第三层
            List<TreeA> list2 = new ArrayList<TreeA>();
            t2 = new TreeA();
            t2.setId(""+i);
            t2.setSelected(true);
            t2.setUrl("/test/appSys/dataIS/manage/manageMain.jsp");
            t2.setTitle("功能"+i);
            for(int k=5;k<=7;k++){
                //第四层
                List<TreeA> list3 = new ArrayList<TreeA>();
                for(int j=8;j<=10;j++){
                     t4 = new TreeA();
                     t4.setData("模块"+i+k+j+"的数据");
                     t4.setId(""+i+k+j);
                     t4.setTitle("模块"+i+k+j);
                     list3.add(t4);
                }
                t3 = new TreeA();
                t3.setChildren(list3);
                t3.setUrl("/test/appSys/dataIS/manage/manageMain.jsp");
                t3.setId(""+i+k);
                t3.setTitle("子功能"+i+k);
                list2.add(t3);
            }
            t2.setChildren(list2);
            list.add(t2);
        }
        //Dict4CacheService类 
       t.setChildren(list);
        map.put("isPass", true);
        map.put("msg","登陆成功");
        map.put("type", 1);
        System.out.println(t.getId().equals("1"));
        map.put("data", t);
        session.setAttribute("username", name);
        return map;
    }
    
}
