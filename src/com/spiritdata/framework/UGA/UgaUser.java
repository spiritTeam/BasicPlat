package com.spiritdata.framework.UGA;

import com.spiritdata.framework.core.model.BaseObject;

/**
 * UGA框架。
 * 用户对象的基础类，用户信息需继承此类。
 * 否则本框架登录会出现问题。
 * @author wanghui
 */
public abstract class UgaUser extends BaseObject {
    private static final long serialVersionUID = -2467050195321143589L;

    private String userId;
    private String userName;
    private String loginName;
    private String password;

    /**
     * 得到用户Id
     * @return 用户Id
     */
    public String getUserId() {
        return userId;
    }
    /**
     * 设置用户Id
     * @param userId 用户Id
     */
    public void setUserId(String userId) {
        this.userId = userId;
    }

    /**
     * 得到用户名称
     * @return 用户名称
     */
    public String getUserName() {
        return userName;
    }

    /**
     * 设置用户名称
     * @param userName 用户Name
     */
    public void setUserName(String userName) {
        this.userName = userName;
    }

    /**
     * 得到用户登录名
     * @return 用户登录名
     */
    public String getLoginName() {
        return loginName;
    }

    /**
     * 设置用户登录名
     * @param loginName 用户登录名
     */
    public void setLoginName(String loginName) {
        this.loginName = loginName;
    }

    /**
     * 得到用户登录密码
     * @return 用户登录密码
     */
    public String getPassword() {
        return password;
    }

    /**
     * 设置用户登录密码
     * @param password 用户登录密码
     */
    public void setPassword(String password) {
        this.password = password;
    }
}