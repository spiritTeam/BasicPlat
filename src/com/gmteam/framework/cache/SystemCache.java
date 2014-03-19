package com.gmteam.framework.cache;

import java.util.ArrayList;
import java.util.Collections;
import java.util.HashMap;
import java.util.List;
import java.util.Map;

import com.gmteam.framework.utils.JsonUtil;

/**
 * @author WH
 * Mar 14, 2014
 */
public class SystemCache {
    /** cache的主体，一个线程安全的key-value */
    public static Map<String, CacheEle<?>> CacheContent = Collections.synchronizedMap(new HashMap<String, CacheEle<?>>());

    private SystemCache(){};

    /**
     * 设置cache
     * @param cacheEle 缓存项目对象 
     */
    public static void  setCache(CacheEle<?> cacheEle) {
        CacheContent.put(cacheEle.getKey(), cacheEle);
    }

    /**
     * 获取cache中的内容
     * @param key
     * @return 根据key获得cache中的缓存项目CacheEle
     */
    public static CacheEle<?> getCache(String key) {
        return CacheContent.get(key);
    }

    /**
     * 删除cache中的内容
     * @param key
     */
    public static void remove(String key) {
        CacheContent.remove(key);
    }

    /**
     * 清空整个cache
     */
    public static void clearCache() {
        CacheContent.clear();
    }

    /**
     * 得到系统缓存中的所有内容
     * @return 列表形式的缓存内容，列表中为缓存元素CacheEle
     */
    public List<CacheEle<?>> getCacheList() {
        List<CacheEle<?>> cl = new ArrayList<CacheEle<?>>();
        for (String key : CacheContent.keySet()) {
            CacheEle<?> ce = CacheContent.get(key);
            cl.add(ce);
        }
        return cl;
    }

    /**
     * 得到系统缓存中的所有内容，并以Json格式
     * @return 缓存中的所有内容
     */
    public String getCacheInfo4Json() throws Exception {
        List<Map<String, String>> cl = new ArrayList<Map<String, String>>();
        for (String key : CacheContent.keySet()) {
            CacheEle<?> ce = CacheContent.get(key);
            Map<String, String> m  = new HashMap<String, String>();
            m.put("cacheEleKey", ce.getKey());
            m.put("cacheEleName", ce.getName());
            m.put("cacheEleContentClass", (ce.getContent()).getClass().getName());
            cl.add(m);
        }
        return JsonUtil.beanToJson(cl);
    }
}