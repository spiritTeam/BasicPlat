package com.spiritdata.plugin.UGA.gisSm.service;

import java.util.Map;

import com.spiritdata.framework.UGA.UgaConstants;
import com.spiritdata.framework.UGA.UgaUserService;
import com.spiritdata.framework.core.cache.CacheEle;
import com.spiritdata.framework.core.cache.SystemCache;
import com.spiritdata.plugin.UGA.gisSm.pojo.User;

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