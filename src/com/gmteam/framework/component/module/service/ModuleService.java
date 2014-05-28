package com.gmteam.framework.component.module.service;

import java.util.Map;
import java.util.List;

import org.springframework.stereotype.Service;

import com.gmteam.framework.IConstants;
import com.gmteam.framework.component.module.pojo.Module;
import com.gmteam.framework.core.cache.CacheEle;
import com.gmteam.framework.core.cache.SystemCache;
import com.gmteam.framework.core.model.tree.TreeNode;

@Service
public class ModuleService {
    /**
     * 根据模块ID，得到所对应的模块树
     * @param id 模块ID
     * @return id所对应的模块树
     */
    public TreeNode<Module> getModuleTreeById(String id) {
        if (id==null||id.trim().length()==0) return null;
        TreeNode<Module> tnM = null;
        CacheEle<?> mc = SystemCache.getCache(IConstants.CATCH_MODULE);
        if (mc!=null&&mc.getContent()!=null) {
            Map<String, TreeNode<Module>> im = (Map<String, TreeNode<Module>>)((Map<String, Object>)SystemCache.getCache(IConstants.CATCH_MODULE).getContent()).get("treeIndxMap");
            if (im!=null) tnM = im.get(id);
        }
        return tnM;
    }

    /**
     * 根据模块ID，得到所对应的模块信息
     * @param id 模块ID
     * @return id所对应的模块树
     */
    public Module getModuleById(String id) {
        TreeNode<Module> tnM = getModuleTreeById(id);
        if (tnM==null) return null;
        return tnM.getTnEntity();
    }

    /**
     * 得到模块信息的列表
     * @return 模块信息的列表
     */
    public List<Module> getModuleLis() {
        List<Module> lM = null;
        CacheEle<?> mc = SystemCache.getCache(IConstants.CATCH_MODULE);
        if (mc!=null&&mc.getContent()!=null) {
            lM = (List<Module>)((Map<String, Object>)SystemCache.getCache(IConstants.CATCH_MODULE).getContent()).get("list");
        }
        return lM;
    }

    /**
     * 得到模块树的根，注意，此根是造出的结点，名称为“模块”;ID=-1;
     * @return 模块树的根
     */
    public TreeNode<Module> getModuleRoot() {
        TreeNode<Module> tnM = null;
        CacheEle<?> mc = SystemCache.getCache(IConstants.CATCH_MODULE);
        if (mc!=null&&mc.getContent()!=null) {
            tnM = (TreeNode<Module>)((Map<String, Object>)SystemCache.getCache(IConstants.CATCH_MODULE).getContent()).get("tree");
        }
        return tnM;
    }
}