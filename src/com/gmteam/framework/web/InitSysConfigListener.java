package com.gmteam.framework.web;

import java.util.HashMap;
import java.util.Map;
import java.util.TreeMap;

import javax.servlet.ServletContext;
import javax.servlet.ServletContextEvent;

import org.apache.log4j.Logger;
import org.springframework.beans.factory.config.AutowireCapableBeanFactory;
import org.springframework.web.context.WebApplicationContext;
import org.springframework.web.context.support.WebApplicationContextUtils;

import com.gmteam.framework.cache.CacheEle;
import com.gmteam.framework.cache.CachePool;
import com.gmteam.framework.cache.CatchLifecycle;
import com.gmteam.framework.cache.SystemCache;
import com.gmteam.framework.component.login.pojo.UserLoginInfo;
import com.gmteam.framework.IConstants;

/**
 * @author Roy zhu,WH
 * Mar 15, 2010
 * 系统初始化监听器
 */
public class InitSysConfigListener {
    /** 日志 */
    private Logger logger = Logger.getLogger(InitSysConfigListener.class);
    /** 缓存管理 */
    //CacheLifecycleManager cacheManager;
    CachePool cachePool;

    /**
     * context初始化
     * @param event
     */
    public void contextInitialized(ServletContextEvent event) {
        try {
            ServletContext sc = event.getServletContext();
            //依赖注入，注入本类，为cacheManager做准备
            dependencyInject(sc);

            //装载系统服务器路径到缓存中
            SystemCache.setCache(new CacheEle<String>(IConstants.ROOTPATH, "系统服务器路径", sc.getRealPath("/")));

            //Session结构缓存
            SystemCache.setCache(
                new CacheEle<Map<String, UserLoginInfo>>(IConstants.USERSESSIONMAP, "用户Session缓存", new HashMap<String, UserLoginInfo>())
            );

            //缓存框架存储
            Map<String,CatchLifecycle> catchMap = new TreeMap<String,CatchLifecycle>();
            catchMap.putAll(cachePool.getCaches());
            for (String key : catchMap.keySet()) {
                CatchLifecycle ic = catchMap.get(key);
                ic.init();
            }
        } catch (Exception e) {
            logger.error("运行环境初始化失败：",e);
        }
    }

    //销毁
    public void contextDestroyed(ServletContextEvent sce) {
    }

    /**
     * 根据TYPE用setter方法做注入
     */
    private void dependencyInject(ServletContext sc) {
        WebApplicationContext context = WebApplicationContextUtils.getWebApplicationContext(sc);
        AutowireCapableBeanFactory factory = context.getAutowireCapableBeanFactory();
        factory.autowireBeanProperties(this, AutowireCapableBeanFactory.AUTOWIRE_BY_TYPE, false);
    }

    public CachePool getCachePool() {
        return cachePool;
    }

    public void setCachePool(CachePool cachePool) {
        this.cachePool=cachePool;
    }
}