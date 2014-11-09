package com.gmteam.framework.component.login.service;

import java.util.Map;

import javax.servlet.http.HttpServletRequest;

import com.gmteam.framework.UGA.UgaUser;

public interface LoginService {

    /**
     * 在用户登录前的处理过程
     * @param req reqeust对象
     * @return 返回为map对象，若成功，必须要放入一个key="success"的键值对；否则把出现的问题组织为Map返回
     */
    Map<String, Object> beforeUserLogin(HttpServletRequest req);

    /**
     * 在用户登录成功后的处理过程
     * @param user user对象，登录成功后的user对象，此对象是UgaUser的子类，各平台可以定义自己的user对象
     * @param req reqeust对象
     * @return 返回为map对象，若成功，必须要放入一个key="success"的键值对；否则把出现的问题组织为Map返回
     */
    Map<String, Object> afterUserLoginOk(UgaUser user, HttpServletRequest req);
}