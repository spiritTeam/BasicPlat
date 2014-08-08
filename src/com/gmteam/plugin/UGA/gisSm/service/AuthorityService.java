package com.gmteam.plugin.UGA.gisSm.service;

import java.util.Map;

import com.gmteam.framework.UGA.UgaAuthorityService;
import com.gmteam.framework.UGA.UgaConstants;
import com.gmteam.framework.core.cache.CacheEle;
import com.gmteam.framework.core.cache.SystemCache;
import com.gmteam.framework.core.model.tree.TreeNode;
import com.gmteam.plugin.UGA.gisSm.pojo.Function;

public class AuthorityService implements UgaAuthorityService {
    @Override
    public TreeNode<Function> getUserModuleAuthByUserId(String userId) {
        CacheEle<?> mc = SystemCache.getCache(UgaConstants.CATCH_UGA_USERMODULE);
        TreeNode<Function> userModuleTree = null;
        if (mc!=null&&mc.getContent()!=null) {
            userModuleTree = ((Map<String, TreeNode<Function>>)mc.getContent()).get(userId);
        }
        return userModuleTree;
    }
}