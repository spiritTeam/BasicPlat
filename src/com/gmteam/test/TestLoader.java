package com.gmteam.test;

import javax.servlet.http.HttpSession;

import org.springframework.stereotype.Service;

import com.gmteam.framework.core.web.SessionLoader;

@Service
public class TestLoader implements SessionLoader {

    @Override
    public void loader(HttpSession session) throws Exception {
        System.out.println("aabbcc==============ddeeff");
        
    }
}