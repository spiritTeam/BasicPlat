package com.gmteam.framework;

public class IConstants {
    //另个----之间的属性是我测试时加的
    //---------------------------------------------------------------------
    //对应tittle
    public final static String PLATFORM_NAME ="BasicPlat";
    /** 当前用户名*/
    public final static String USER_NAME = "User_Name";
    /** 当前用户功能列表*/
    public final static String USER_MODULES = "User_Modules";
    
    //---------------------------------------------------------------------
    //缓存常量 Module_name
    //public final static String CACHE_MODULNAME="moduleName";
    //缓存常量 Module_key
    public final static String CACHE_MODULKEY="cahceModuleKey";
    //系统路径
    public final static String APPOSPATH="AppOSPath";
    //用户Session缓存，用于处理不同用户的登录
    public final static String USERSESSIONMAP="userSessionMap";
   
    //以下session
    public final static String SESSION_USER="userInfo";
    public final static String SESSION_USERINGROUP="userInGroup";
    public final static String SESSION_USERAUTHORITY="userAuthority";
}
