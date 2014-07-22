package com.gmteam.framework.component.UGA.service;

import java.util.Map;

import org.springframework.stereotype.Service;

import com.gmteam.framework.UGA.UgaConstants;
import com.gmteam.framework.UGA.UgaUser;
import com.gmteam.framework.UGA.UgaUserService;
import com.gmteam.framework.component.UGA.pojo.User;
import com.gmteam.framework.core.cache.CacheEle;
import com.gmteam.framework.core.cache.SystemCache;

@Service
public class UserService implements UgaUserService {

    @Override
    @SuppressWarnings("unchecked")
    public User getUserByLoginName(String loginName) {
        CacheEle<?> mc = SystemCache.getCache(UgaConstants.CATCH_UGA_USER);
        User user = null;
        if (mc!=null&&mc.getContent()!=null) {
            user=((Map<String, User>)((Map<String, Object>)mc.getContent()).get("loginMap")).get(loginName);
        }
        return user;
    }

    @Override
    @SuppressWarnings("unchecked")
    public User getUserById(String userId) {
        CacheEle<?> mc = SystemCache.getCache(UgaConstants.CATCH_UGA_USER);
        User user = null;
        if (mc!=null&&mc.getContent()!=null) {
            user=((Map<String, User>)((Map<String, Object>)mc.getContent()).get("idMap")).get(userId);
        }
        return user;
    }
}