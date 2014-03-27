package com.gmteam.framework.core.cache;

/**
 * 缓存生命周期接口。
 * 实现一个缓存单元内缓存对象周期管理
 * @author wh
 */
public interface CatchLifecycle {
    /**
     * cache初始化，在此方法中，把需要缓存的内容放入SystemCache中
     */
    public void init();

    /**
     * 刷新指定的元素(key)的cache内容
     * 注意：此方法实现时要在SystemCache.CacheContent对象上加同步锁，否则会出现不可预料的问题，如：
     * <pre>
     * synchronized(SystemCache.CacheContent) {
     *     try {
     *         if (key.equals(IConstants.RESOURCEMAP)) { // 资源缓存
     *         ...
     *         }
     *     } catch (Exception e) {
     *         logger.info("重载缓存项["+key+"]：", e);
     *     }
     * }
     * </pre>
     * @param key 元素的标示
     */
    public void refresh(String key);
}