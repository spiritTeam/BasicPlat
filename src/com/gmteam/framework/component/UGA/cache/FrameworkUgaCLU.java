package com.gmteam.framework.component.UGA.cache;

import java.util.List;
import java.util.Map;

import javax.annotation.Resource;

import org.apache.log4j.Logger;
import org.springframework.stereotype.Component;

import com.gmteam.framework.UGA.UgaConstants;
import com.gmteam.framework.component.UGA.service.UgaCacheService;
import com.gmteam.framework.component.module.pojo.Module;
import com.gmteam.framework.core.cache.AbstractCacheLifecycleUnit;
import com.gmteam.framework.core.cache.CacheEle;
import com.gmteam.framework.core.cache.SystemCache;
import com.gmteam.framework.core.model.tree.TreeNode;
import com.gmteam.framework.exceptionC.Plat0301CException;
@Component
public class FrameworkUgaCLU extends AbstractCacheLifecycleUnit {
    /**
     * 日志
     */
    private Logger logger = Logger.getLogger(FrameworkUgaCLU.class);

    @Resource
    private UgaCacheService ugaCacheService;
    
    @Override
    public void init() {
        try {
            //装载用户信息
            loadUser();
            //装载模块信息
            loadModule();
            //装载用户模块信息
            loadUserModule();
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
        String keyName=null;
        try {
            CacheEle<?> rce = (CacheEle<?>)SystemCache.remove(key);
            if (rce!=null) keyName = rce.getName();
            else throw new Exception("没有值为<"+key+">的缓存");

            if (key.equals(UgaConstants.CATCH_UGA_USER)) loadUser();
            else if (key.equals(UgaConstants.CATCH_UGA_MODULE)) loadModule();
            else if (key.equals(UgaConstants.CATCH_UGA_USERMODULE)) loadUserModule();

        } catch (Exception e) {
            logger.info("重新加载缓存项{UGA["+keyName+"]}失败：", e);
        }
    }

    /**
     * 装载模块缓存
     * @throws Exception
     */
    @SuppressWarnings("unchecked")
    public void loadModule() throws Exception {
        try {
            Map<String, Object> mo = ugaCacheService.makeCacheModule();
            if (mo==null) throw new Exception("没有[模块]数据。");
            List<Module> el = (List<Module>)mo.get("errors");
            for (Module m: el) {
                logger.debug("结点没有对应的根结点：{id="+m.getId()+"; name="+m.getNodeName()+"; parentId="+m.getParentId()+"}");
            }
            mo.remove("errors");
            SystemCache.setCache(new CacheEle<Map<String, Object>>(UgaConstants.CATCH_UGA_MODULE, "模块", mo));
        } catch(Exception e) {
            throw new Plat0301CException("[模块]失败", e);
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
            throw new Plat0301CException("[用户]失败", e);
        }
    }

    /**
     * 装载uga用户模块关联缓存
     * @throws Exception
     */
    public void loadUserModule() throws Exception {
        try {
            Map<String, TreeNode<Module>> umo = ugaCacheService.makeCacheUserModule();
            if (umo==null) throw new Exception("没有[用户模块关联]数据。");
            SystemCache.setCache(new CacheEle<Map<String, TreeNode<Module>>>(UgaConstants.CATCH_UGA_USERMODULE, "用户模块关联", umo));
        } catch(Exception e) {
            throw new Plat0301CException("[用户模块权限]失败", e);
        }
    }
}