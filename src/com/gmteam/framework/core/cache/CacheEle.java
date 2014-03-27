package com.gmteam.framework.core.cache;

/**
 * 缓存项目，所有缓存项目都要继承此类。系统缓存SystemCache的内容就是这个对象
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

    /**
     * 创建一个空缓存项目
     */
    public CacheEle() {
        this.cacheEleKey = null;
        this.cacheEleName = null;
        this.cacheEleContent = null;
    }
    /**
     * 创建一个缓存项目
     * @param key
     * @param name
     * @param content
     */
    public CacheEle(String key, String name, T content) {
        this.cacheEleKey = key;
        this.cacheEleName = name;
        this.cacheEleContent = content;
    }
}