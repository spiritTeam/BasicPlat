package com.gmteam.framework.core.cache;

import java.util.Map;

/**
 * 缓存池，用来在启动时按照不同的单元进行加载
 * @author wh
 */
public class CachePool {
    /*
     * 缓冲池对象
     */
    protected Map<String, CatchLifecycle> caches;

    //根据缓存的
    public Map<String, CatchLifecycle> getCaches() {
        return caches;
    }

    public void setCaches(Map<String, CatchLifecycle> caches) {
        this.caches = caches;
    }
}