package com.gmteam.framework.component.UGA.service;

import java.util.Map;

import com.gmteam.framework.UGA.UgaAuthorityService;
import com.gmteam.framework.UGA.UgaConstants;
import com.gmteam.framework.component.module.pojo.Module;
import com.gmteam.framework.core.cache.CacheEle;
import com.gmteam.framework.core.cache.SystemCache;
import com.gmteam.framework.core.model.tree.TreeNode;

public class AuthorityService implements UgaAuthorityService {
    @Override
    public TreeNode<Module> getUserModuleAuthByUserId(String userId) {
        CacheEle<?> mc = SystemCache.getCache(UgaConstants.CATCH_UGA_USERMODULE);
        TreeNode<Module> userModuleTree = null;
        if (mc!=null&&mc.getContent()!=null) {
            userModuleTree = ((Map<String, TreeNode<Module>>)mc.getContent()).get(userId);
        }
        return userModuleTree;
    }
}