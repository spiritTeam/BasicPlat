package com.gmteam.framework.component.cache;

import java.util.List;
import java.util.Map;

import javax.annotation.Resource;

import org.apache.log4j.Logger;
import org.springframework.stereotype.Component;

import com.gmteam.framework.IConstants;
import com.gmteam.framework.component.UGA.service.UgaCacheService;
import com.gmteam.framework.component.module.pojo.Module;
import com.gmteam.framework.component.module.service.ModuleCacheService;
import com.gmteam.framework.core.cache.AbstractCacheLifecycleUnit;
import com.gmteam.framework.core.cache.CacheEle;
import com.gmteam.framework.core.cache.SystemCache;
@Component
public class FrameworkCacheLifecycleUnit extends AbstractCacheLifecycleUnit {
    /**
     * 日志
     */
    private Logger logger = Logger.getLogger(FrameworkCacheLifecycleUnit.class);

    @Resource
    private ModuleCacheService moduleCacheService;

    @Resource
    private UgaCacheService ugaCacheService;
    
    @Override
    public void init() {
        try {
        } catch (Exception e) {
            logger.info("启动时加载{框架}缓存出错", e);
        }
    }

    /**
     * 刷新缓存中指定的缓存单元(CacheEle)
     * @param key 缓存单元的标识
     */
    @Override
    public void refresh(String key) {
        try {
        } catch (Exception e) {
            logger.info("重载缓存项{框架[" + key + "]}失败：", e);
        }
    }
}