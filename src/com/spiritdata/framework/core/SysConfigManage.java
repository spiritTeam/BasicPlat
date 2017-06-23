package com.spiritdata.framework.core;

import java.io.FileInputStream;
import java.io.IOException;
import java.util.HashMap;
import java.util.Iterator;
import java.util.Map;
import java.util.Properties;

import com.spiritdata.framework.FConstants;
import com.spiritdata.framework.core.cache.CacheEle;
import com.spiritdata.framework.core.cache.SystemCache;

/**
 * 系统配置文件参数处理
 * @author wh
 */
public class SysConfigManage {
    private static Map<String, String> sysConfigMap = null;
    /**
     * 把配置文件信息读入缓存
     * @param proFName 参数Map
     * @throws IOException
     */
    public static Map<String, String> load(String proFName) {
        FileInputStream in = null;
        try {
            in = new FileInputStream(proFName);
            Properties pro = new Properties();
            pro.load(in);
            Map<String, String> cfgKvMap = new HashMap<String, String>();
            Iterator<String> it=pro.stringPropertyNames().iterator();
            while(it.hasNext()){
                String key=it.next();
                cfgKvMap.put(key, pro.getProperty(key));
            }
            if (cfgKvMap.size()>0) return cfgKvMap;
        } catch (IOException e) {
        } finally {
            if (in!=null)
                try {
                    in.close();
                } catch (IOException e) {
                    e.printStackTrace();
                }
        }
        return null;
    }

    /**
     * 根据key值获取系统配置信息
     * @param key 系统配置项名称
     * @return 系统配置项值
     */
    @SuppressWarnings("unchecked")
    public static String getValue(String key) {
        if (SysConfigManage.sysConfigMap==null) {
            if (SystemCache.getCache(FConstants.SYS_CONFIG)!=null) {
                try {
                    SysConfigManage.sysConfigMap = ((CacheEle<Map<String, String>>)SystemCache.getCache(FConstants.SYS_CONFIG)).getContent();
                } catch(Exception e) {
                }
            }
        }
        if (SysConfigManage.sysConfigMap==null) return null;
        return SysConfigManage.sysConfigMap.get(key);
    }
}