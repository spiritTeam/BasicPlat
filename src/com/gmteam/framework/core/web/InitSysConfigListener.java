package com.gmteam.framework.core.web;

import java.util.HashMap;
import java.util.Map;
import java.util.TreeMap;

import javax.servlet.ServletContext;
import javax.servlet.ServletContextEvent;
import javax.servlet.ServletContextListener;

import org.apache.log4j.Logger;
import org.springframework.beans.factory.config.AutowireCapableBeanFactory;
import org.springframework.web.context.WebApplicationContext;
import org.springframework.web.context.support.WebApplicationContextUtils;

import com.gmteam.framework.core.cache.CacheEle;
import com.gmteam.framework.core.cache.CachePool;
import com.gmteam.framework.core.cache.CatchLifecycle;
import com.gmteam.framework.core.cache.SystemCache;
import com.gmteam.framework.component.login.pojo.UserLogin;
import com.gmteam.framework.FConstants;

/**
 * @author Roy zhu,WH
 * Mar 15, 2010
 * 系统初始化监听器
 */
public class InitSysConfigListener implements ServletContextListener {
    private Logger logger = Logger.getLogger(InitSysConfigListener.class);
    /**
     * 缓存管理
     */
    private CachePool cachePool;
    public CachePool getCachePool() {
        return cachePool;
    }
    public void setCachePool(CachePool cachePool) {
        this.cachePool=cachePool;
    }

    /**
     * context初始化
     * @param event
     */
    public void contextInitialized(ServletContextEvent event) {
        try {
            ServletContext sc = event.getServletContext();

            //装载系统服务器路径到缓存中
//            SystemCache.setCache(
//                new CacheEle<ServletContext>(FConstants.SERVLET_CONTEXT, "服务上下文环境", sc)
//            );

            //装载系统服务器路径到缓存中
            SystemCache.setCache(
                new CacheEle<String>(FConstants.APPOSPATH, "系统服务器路径", sc.getRealPath("/"))
            );

            //用户Session缓存，用于处理不同用户的登录
            SystemCache.setCache(
                new CacheEle<Map<String, UserLogin>>(FConstants.USERSESSIONMAP, "用户Session缓存", new HashMap<String, UserLogin>())
            );

            //依赖注入，注入本类，为cacheManager做准备
            dependencyInject(sc);
            //缓存框架存储
            if (cachePool!=null) {
                Map<String,CatchLifecycle> catchMap = new TreeMap<String,CatchLifecycle>();
                catchMap.putAll(cachePool.getCaches());
                for (String key : catchMap.keySet()) {
                    CatchLifecycle ic = catchMap.get(key);
                    ic.init();
                }
            }
        } catch (Exception e) {
            logger.error("运行环境初始化失败：",e);
        }
    }

    //销毁
    public void contextDestroyed(ServletContextEvent sce) {
    }

    /**
     * 根据TYPE用setter方法做注入，此方法用于把CachePool从bean中引入
     */
    private void dependencyInject(ServletContext sc) {
        WebApplicationContext context = WebApplicationContextUtils.getWebApplicationContext(sc);
        AutowireCapableBeanFactory factory = context.getAutowireCapableBeanFactory();
        factory.autowireBeanProperties(this, AutowireCapableBeanFactory.AUTOWIRE_BY_TYPE, false);
    }
}