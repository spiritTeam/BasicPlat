package com.spiritdata.framework.component.UGA.service;

import java.util.Map;

import com.spiritdata.framework.UGA.UgaAuthorityService;
import com.spiritdata.framework.UGA.UgaConstants;
import com.spiritdata.framework.component.module.pojo.Module;
import com.spiritdata.framework.core.cache.CacheEle;
import com.spiritdata.framework.core.cache.SystemCache;
import com.spiritdata.framework.core.model.tree.TreeNode;

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