package com.gmteam.framework.component.cache;

import org.apache.log4j.Logger;
import org.springframework.stereotype.Component;

import com.gmteam.framework.core.cache.AbstractCacheLifecycleUnit;

/**
 * 缓存util类，
 * 其中包含缓存的刷新，初始化，销毁等方法
 */
@Component
public class FrameworkCacheLifecycleUnit extends AbstractCacheLifecycleUnit {
    /**
     * 日志
     */
    private Logger logger = Logger.getLogger(FrameworkCacheLifecycleUnit.class);

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
