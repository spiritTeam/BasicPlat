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
    public UgaUser getUserByLoginName(String loginName) {
        CacheEle<?> mc = SystemCache.getCache(UgaConstants.CATCH_UGA_USER);
        UgaUser ugaUser = null;
        if (mc!=null&&mc.getContent()!=null) {
            ugaUser=((Map<String, User>)((Map<String, Object>)mc.getContent()).get("loginMap")).get(loginName);
        }
        return ugaUser;
    }

    @Override
    public User getUserById(String userId) {
        CacheEle<?> mc = SystemCache.getCache(UgaConstants.CATCH_UGA_USER);
        User user = null;
        if (mc!=null&&mc.getContent()!=null) {
            user=((Map<String, User>)((Map<String, Object>)mc.getContent()).get("idMap")).get(userId);
        }
        return user;
    }
}