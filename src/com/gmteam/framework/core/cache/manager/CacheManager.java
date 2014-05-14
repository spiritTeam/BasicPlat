package com.gmteam.framework.core.cache.manager;

import java.util.Map;

import org.apache.log4j.Logger;
import org.springframework.stereotype.Component;

import com.gmteam.framework.IConstants;
import com.gmteam.framework.core.cache.AbstractCacheLifecycleUnit;
import com.gmteam.framework.core.cache.CacheEle;
import com.gmteam.framework.core.cache.SystemCache;
import com.gmteam.framework.core.cache.service.CacheService;

/**
 * 缓存控制层
 * @author mht
 */
@Component
public class CacheManager extends AbstractCacheLifecycleUnit {
    /** 日志 */
    private Logger logger = Logger.getLogger(CacheManager.class);
    /** 
     * CacheService 
     */
    CacheService cacheService;
    public CacheService getcacheService() {
        return cacheService;
    }
    public void setCacheService(CacheService cacheService) {
        this.cacheService = cacheService;
    }
    /**
     * cache初始化
     */
    public void init() {
        try {
            loadModule();
        } catch (Exception e) {
            e.printStackTrace();
        }
    }

    /**
     * 刷新指定的cache
     * @param key
     */
    public void refresh(String key) {
        try {
            if (key.equals(IConstants.CACHE_MODULKEY)) {
                SystemCache.remove(IConstants.CACHE_MODULKEY);
                loadModule();
            }
        } catch (Exception e) {
            e.printStackTrace();
        }
    }

    /**
     * 刷新所有缓存
     */
    public void refresh() {
        init();
    }

    public void destory() {
//        SystemCache.remove(GisSMConstants.CACHE_USERLIST);
    }

    /**
     * 装载模块信息
     */
    private void loadModule() throws Exception {
        try {
            System.out.println("加载module缓存---------------------------------------");
            CacheEle<Map<String,Object>> moduleCache = new CacheEle<Map<String,Object>>();
            Map<String, Object> cacheMap = cacheService.loadModuleCache();
            moduleCache.setContent(cacheMap);
            moduleCache.setKey(IConstants.CACHE_MODULKEY);
            SystemCache.setCache(moduleCache);
        } catch (Exception e) {
            throw new Exception("加载缓存[UGA扩展-模块]失败：", e);
        }
    }
    /**
     * 装载用户信息
     */
    private void loadUser() throws Exception {
        try {
        } catch (Exception e) {
            throw new Exception("加载缓存[UGA扩展-用户]失败：", e);
        }
    }

    /**
     * 装载组织信息
     */
    private void loadDept() throws Exception {
        try {
        } catch (Exception e) {
            throw new Exception("加载缓存[UGA扩展-组织机构]失败：", e);
        }
    }

    /**
     * 装载功能信息
     */
    private void loadFunc() throws Exception {
        try {
        } catch (Exception e) {
            throw new Exception("加载缓存[UGA扩展-功能]失败：", e);
        }
    }

    /**
     * 装载用户功能
     */
    private void loadUserFunc() throws Exception {
        try {
        } catch (Exception e) {
            throw new Exception("加载缓存[UGA扩展-功能]失败：", e);
        }
    }
}