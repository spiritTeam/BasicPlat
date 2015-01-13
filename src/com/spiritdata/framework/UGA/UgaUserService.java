package com.spiritdata.framework.UGA;

/**
 * UGA框架。
 * 用户服务的基础类，用户服务需继承此类。
 * 否则本框架登录会出现问题。
 * @author wanghui
 */
public interface UgaUserService {
    /**
     * 根据登录名，得到用户信息
     * @param loginName 用户登录名
     * @return 用户信息
     */
    public <V extends UgaUser> V getUserByLoginName(String loginName);

    /**
     * 根据用户Id，得到用户信息
     * @param userId 用户Id
     * @return 用户信息
     */
    public <V extends UgaUser> V getUserById(String userId);
}