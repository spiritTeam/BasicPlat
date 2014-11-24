package com.gmteam.framework.core.cache;

import java.util.Map;
import java.util.TreeMap;

/**
 * 缓存池，用来在启动时按照不同的单元进行加载
 * @author wh
 */
public class CachePool {
    /*
     * 缓冲池对象
     */
    protected Map<String, CatchLifecycle> caches;

    public Map<String, CatchLifecycle> getCaches() {
        return caches;
    }

    public void setCaches(Map<String, CatchLifecycle> caches) {
        this.caches = caches;
    }

    /**
     * 执行缓存池中各缓存生命周期单元的init方法，向缓存中加载数据
     * @param session
     */
    public void initAll() {
        Map<String,CatchLifecycle> catchMap = new TreeMap<String,CatchLifecycle>();
        catchMap.putAll(caches);
        for (String key : catchMap.keySet()) {
            CatchLifecycle ic = catchMap.get(key);
            ic.init();
        }
    }
}