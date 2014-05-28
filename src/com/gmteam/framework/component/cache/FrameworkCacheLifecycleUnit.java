package com.gmteam.framework.component.cache;

import java.util.List;
import java.util.Map;

import javax.annotation.Resource;

import org.apache.log4j.Logger;

import com.gmteam.framework.IConstants;
import com.gmteam.framework.component.module.pojo.Module;
import com.gmteam.framework.component.module.service.ModuleCacheService;
import com.gmteam.framework.core.cache.AbstractCacheLifecycleUnit;
import com.gmteam.framework.core.cache.CacheEle;
import com.gmteam.framework.core.cache.SystemCache;

public class FrameworkCacheLifecycleUnit extends AbstractCacheLifecycleUnit {
    /**
     * 日志
     */
    private Logger logger = Logger.getLogger(FrameworkCacheLifecycleUnit.class);

    @Resource
    private ModuleCacheService moduleCacheService;

    @Override
    public void init() {
        try {
            //装载模块信息
            loadModule();
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
            if (key.equals(IConstants.CATCH_MODULE)) {
                SystemCache.remove(IConstants.CATCH_MODULE);
                loadModule();
            }
        } catch (Exception e) {
            logger.info("重载缓存项{框架[" + key + "]}失败：", e);
        }
    }

    /**
     * 装载模块缓存
     * @throws Exception
     */
    public void loadModule() throws Exception {
        try {
            Map<String, Object> mo = moduleCacheService.makeCacheObject();
            List<Module> el = (List<Module>)mo.get("errors");
            for (Module m: el) {
                logger.debug("结点没有对应的根结点：{id="+m.getId()+"; name="+m.getNodeName()+"; parentId="+m.getParentId()+"}");
            }
            mo.remove("errors");
            SystemCache.setCache(new CacheEle<Map<String, Object>>(IConstants.CATCH_MODULE, "模块", mo));
        } catch(Exception e) {
            throw new Exception("加载缓存项{框架[模块]}失败：", e);
        }
    }
}