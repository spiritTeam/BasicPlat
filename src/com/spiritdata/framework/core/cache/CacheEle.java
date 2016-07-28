package com.spiritdata.framework.core.cache;

/**
 * 缓存项目，所有缓存项目都要继承此类。系统缓存SystemCache的内容就是这个对象。
 * 这里的缓存是程序生命周期中都起作用的缓存
 * @author wh
 */
public class CacheEle<T> {
    private String cacheEleKey;
    /**
     * 得到缓存项目的key
     * @return 缓存项目的key
     */
    public String getKey() {
        return this.cacheEleKey;
    }
    /**
     * 设置缓存项目的key
     * @param key 缓存项目key
     */
    public void setKey(String key) {
        this.cacheEleKey = key;
    }

    private String cacheEleName;
    /**
     * 得到缓存项目的名称
     * @return 缓存项目的名称
     */
    public String getName() {
        return this.cacheEleName;
    }
    /**
     * 设置缓存项目的名称
     * @param name 缓存项目名称
     */
    public void setName(String name) {
        this.cacheEleName = name;
    }

    private T cacheEleContent;
    /**
     * 设置缓存项目的内容
     */
    public T getContent() {
        return this.cacheEleContent;
    }
    /**
     * 设置缓存项目的内容
     * @param content 缓存项目内容
     */
    public void setContent(T content) {
        this.cacheEleContent = content;
    }

    private long createTime;
    /**
     * 获得缓存创建时间
     */
    public long getCreateTime() {
        return this.createTime;
    }

    private long lastAccessTime;
    /**
     * 获得缓存最后访问时间
     */
    public long getLastAccessTime() {
        return this.lastAccessTime;
    }

    private long timeOuts;
    /**
     * 获得过期时间，单位为毫秒，过期后的缓存清除掉。
     * 若timeOuts<0;则为无过期时间
     */
    public long getTimeOuts() {
        return this.timeOuts;
    }

    /**
     * 创建一个空缓存项目
     */
    public CacheEle() {
        this.cacheEleKey = null;
        this.cacheEleName = null;
        this.cacheEleContent = null;
        this.createTime=System.currentTimeMillis();
        this.lastAccessTime=System.currentTimeMillis();
        this.timeOuts = -1;
    }
    /**
     * 创建一个缓存项目
     * @param key 缓存key
     * @param name 缓存名称
     * @param content 缓存内容，是Object
     */
    public CacheEle(String key, String name, T content) {
        this.cacheEleKey = key;
        this.cacheEleName = name;
        this.cacheEleContent = content;
        this.createTime=System.currentTimeMillis();
        this.lastAccessTime=System.currentTimeMillis();
        this.timeOuts = -1;
    }

    /**
     * 创建一个带有过期时间的缓存项目
     * @param key 缓存key
     * @param name 缓存名称
     * @param content 缓存内容，是Object
     * @param timeOuts 过期时间
     */
    public CacheEle(String key, String name, T content, long timeOuts) {
        this.cacheEleKey = key;
        this.cacheEleName = name;
        this.cacheEleContent = content;
        this.createTime=System.currentTimeMillis();
        this.lastAccessTime=System.currentTimeMillis();
        this.timeOuts = timeOuts;
    }
}