package com.spiritdata.framework;

public abstract class FConstants {
    //-平台参数常量--------------------------------------------------------------------
    public final static String PLATFORM_NAME ="基础平台"; //系统名称
    public final static String APPOSPATH="AppOSPath"; //服务器中，本系统根路径
    public final static String SERVLET_CONTEXT="ServletContext"; //服务上下文环境

    //-用户及用户管理相关常量-----------------------------------------------------------
    //用户Session缓存，用于处理不同用户的登录
    public final static String USERSESSIONMAP="userSessionMap";
    //以下session
    public final static String SESSION_USER="userInfo";//用户信息
    public final static String SESSION_USERAUTHORITY="userAuthority";//用户权限信息
}