package com.gmteam.test;

import java.util.Map;

import javax.servlet.http.HttpServletRequest;

import com.gmteam.framework.UGA.UgaUser;
import com.gmteam.framework.component.login.service.LoginService;

public class TestLoginService implements LoginService {

    @Override
    public Map<String, Object> beforeUserLogin(HttpServletRequest req) {
        // TODO Auto-generated method stub
        return null;
    }

    @Override
    public Map<String, Object> afterUserLoginOk(UgaUser user,
            HttpServletRequest req) {
        // TODO Auto-generated method stub
        return null;
    }

}