package com.spiritdata.framework.core.cache;

/**
 * 一个缓存生命周期单元。
 * 在此单元中管理不同的缓存元素，采用这种方式使得缓存可以拆分为多个单元进行管理。
 * @author wh
 *
 */
public abstract class AbstractCacheLifecycleUnit implements CatchLifecycle {
    /**
     * 刷新所有缓存
     */
    public void refreshAll() {
        synchronized(SystemCache.CacheContent) {
            init();
        }
    }
}