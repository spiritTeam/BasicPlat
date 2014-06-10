package com.gmteam.framework.UGA;

/**
 * UGA框架。
 * 用户服务对象的基础类，用户服务需继承此类。
 * 否则本框架登录会出现问题。
 * @author wanghui
 */
public abstract class UgaUserService {

    /**
     * 根据登录名，得到用户信息
     * @param loginName 用户登录名
     * @return 用户信息
     */
    public abstract UgaUser getUserByLoginName(String loginName);

    /**
     * 根据用户Id，得到用户信息
     * @param userId 用户Id
     * @return 用户信息
     */
    public abstract UgaUser getUserById(String userId);
}