package com.spiritdata.plugin.UGA.gisSm.service;

import java.util.Map;

import com.spiritdata.framework.UGA.UgaAuthorityService;
import com.spiritdata.framework.UGA.UgaConstants;
import com.spiritdata.framework.core.cache.CacheEle;
import com.spiritdata.framework.core.cache.SystemCache;
import com.spiritdata.framework.core.model.tree.TreeNode;
import com.spiritdata.plugin.UGA.gisSm.pojo.Function;

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