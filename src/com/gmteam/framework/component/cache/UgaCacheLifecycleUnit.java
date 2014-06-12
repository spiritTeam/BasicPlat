package com.gmteam.framework.component.cache;

import java.util.List;
import java.util.Map;

import javax.annotation.Resource;

import org.apache.log4j.Logger;
import org.springframework.stereotype.Component;

import com.gmteam.framework.IConstants;
import com.gmteam.framework.UGA.UgaConstants;
import com.gmteam.framework.component.UGA.service.UgaCacheService;
import com.gmteam.framework.component.module.pojo.Module;
import com.gmteam.framework.component.module.service.ModuleCacheService;
import com.gmteam.framework.core.cache.AbstractCacheLifecycleUnit;
import com.gmteam.framework.core.cache.CacheEle;
import com.gmteam.framework.core.cache.SystemCache;
@Component
public class UgaCacheLifecycleUnit extends AbstractCacheLifecycleUnit {
    /**
     * 日志
     */
    private Logger logger = Logger.getLogger(UgaCacheLifecycleUnit.class);

    @Resource
    private ModuleCacheService moduleCacheService;

    @Resource
    private UgaCacheService ugaCacheService;
    
    @Override
    public void init() {
        try {
            //装载模块信息
            loadModule();
            //装载用户信息
            loadUser();
        } catch (Exception e) {
            logger.info("启动时加载{UGA}缓存出错", e);
        }
    }

    /**
     * 刷新缓存中指定的缓存单元(CacheEle)
     * @param key 缓存单元的标识
     */
    @Override
    public void refresh(String key) {
        try {
            CacheEle<?> rce;
            if (key.equals(IConstants.CATCH_MODULE)) {
                rce = (CacheEle<?>)SystemCache.remove(IConstants.CATCH_MODULE);
                key = rce.getName();
                loadModule();
            } else if (key.equals(UgaConstants.CATCH_UGA_USER)) {
                rce = SystemCache.remove(IConstants.CATCH_MODULE);
                key = rce.getName();
                loadModule();
            }
        } catch (Exception e) {
            logger.info("加载缓存项{UGA["+key+"]}失败：", e);
        }
    }

    /**
     * 装载模块缓存
     * @throws Exception
     */
    @SuppressWarnings("unchecked")
    public void loadModule() throws Exception {
        try {
            Map<String, Object> mo = moduleCacheService.makeCacheModule();
            if (mo==null) throw new Exception("没有[模块]数据。");
            List<Module> el = (List<Module>)mo.get("errors");
            for (Module m: el) {
                logger.debug("结点没有对应的根结点：{id="+m.getId()+"; name="+m.getNodeName()+"; parentId="+m.getParentId()+"}");
            }
            mo.remove("errors");
            SystemCache.setCache(new CacheEle<Map<String, Object>>(IConstants.CATCH_MODULE, "模块", mo));
        } catch(Exception e) {
            throw new Exception("加载缓存项{UGA[模块]}失败：", e);
        }
    }

    /**
     * 装载uga用户缓存
     * @throws Exception
     */
    public void loadUser() throws Exception {
        try {
            Map<String, Object> uo = ugaCacheService.makeCacheUser();
            if (uo==null) throw new Exception("没有[uga用户]数据。");
            SystemCache.setCache(new CacheEle<Map<String, Object>>(UgaConstants.CATCH_UGA_USER, "uga用户", uo));
        } catch(Exception e) {
            throw new Exception("加载缓存项{UGA[uga用户]}失败：", e);
        }
    }
}