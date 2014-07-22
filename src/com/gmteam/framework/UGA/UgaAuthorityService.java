package com.gmteam.framework.UGA;

import com.gmteam.framework.core.model.tree.TreeNode;

/**
 * UGA框架。
 * 权限服务基础类，权限服务需继承此类。
 * @author wanghui
 */
public interface UgaAuthorityService {
    /**
     * 根据用户Id，得到用户模块权限
     * @param userId 用户id
     * @return 用户模块权限
     */
    public <V extends UgaModule> TreeNode<V> getUserModuleAuthByUserId(String userId);
}