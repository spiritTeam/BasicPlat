package com.gmteam.framework.model;

public interface User {
    /**
     * 得到用户Id
     * @return 用户Id
     */
    String getUserId();

    /**
     * 得到用户名称
     * @return 用户名称
     */
    String getUserName();

    /**
     * 设置用户Id
     * @param userId 用户Id
     */
    void setUserId(String userId);

    /**
     * 设置用户名称
     * @param userName 用户Name
     */
    void setUserName(String userName);
}