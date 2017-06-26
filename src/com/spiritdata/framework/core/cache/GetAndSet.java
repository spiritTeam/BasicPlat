package com.spiritdata.framework.core.cache;

import java.util.Map;

/**
 * 从缓存或持久化存储中获得单条业务数据。
 * @author wh
 * @param <T> 业务数据的数据类型
 */
public abstract class GetAndSet<T> {
    /**
     * 当从缓存获得数据为空时，刷新缓存
     */
    public final static int GETNULL_FLUSH=1;
    /**
     * 当从缓存获得数据为空时，不刷新缓存
     */
    public final static int GETNULL_NOFLUSH=0; //系统名称
    
    protected Map<String, Object> param; //参数，可以为空
    /**
     * 缓存刷新类型，=0不刷新缓存，=1若缓存中没有则刷新缓存
     */
    protected int flushType=1;

    public GetAndSet(Map<String, Object> param) {
        this.param=param;
        flushType=GetAndSet.GETNULL_FLUSH;
    }

    public GetAndSet(Map<String, Object> param, int flushType) {
        this.param=param;
        this.flushType=flushType;
    }

    /**
     * 获得数据
     * @return 单条数据
     */
    public T getBizData() {
        try {
            T r=getFromCache();
            if (r!=null) return r;
            r=getFromPersis();
            if (r==null) return null;
            if (flushType==GetAndSet.GETNULL_FLUSH) {
                flushCache(r);
            }
            return r;
        } catch(Exception e) {
            e.printStackTrace();
            return null;
        }
    }

    /**
     * 从缓存中获得数据
     * @return
     */
    protected abstract T getFromCache();

    /**
     * 从持久化存储中获得数据
     * @return
     */
    protected abstract T getFromPersis();

    /**
     * 刷新缓存中的数据
     */
    protected abstract void flushCache(T newItem);
}