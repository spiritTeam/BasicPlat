package com.gmteam.framework.UGA;

/**
 * UGA框架。
 * 权限服务基础类，权限服务需继承此类。
 * @author wanghui
 */
public interface UgaAuthorityService {
    /**
     * 根据登录名，得到用户信息
     * @param loginName 用户登录名
     * @return 用户信息
     */
    public UgaUser getUserModuleAuthByUserId(String loginName);
}