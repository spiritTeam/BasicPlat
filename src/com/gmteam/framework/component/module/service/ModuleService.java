package com.gmteam.framework.component.module.service;

import java.util.Map;
import java.util.List;

import javax.annotation.PostConstruct;
import javax.annotation.Resource;

import org.springframework.stereotype.Service;

import com.gmteam.framework.UGA.UgaConstants;
import com.gmteam.framework.component.module.pojo.Module;
import com.gmteam.framework.core.cache.CacheEle;
import com.gmteam.framework.core.cache.SystemCache;
import com.gmteam.framework.core.dao.mybatis.MybatisDAO;
import com.gmteam.framework.core.model.tree.TreeNode;
/**
 * moduleService：
 * 对module的基本操作(insert,update,delete,select)，
 * 以及对module缓存的一些方法(getModuleTreeById,getModuleById,getModuleListgetModuleRoot)
 * @author mht
 */
@Service
public class ModuleService {
    @Resource(name="defaultDAO")
    private MybatisDAO<Module> dao;
    @PostConstruct
    public void initParam() {
        dao.setNamespace("Module");
    }

    /**
     * insert
     * @return
     * @throws Exception 
     */
    public int insertModule(Module m) throws Exception{
        return dao.insert(m);
    }
    /**
     * update
     * @param m
     * @return
     * @throws Exception 
     */
    public int updateModule(Module m) throws Exception{
        return dao.update(m);
    }
    /**
     * delete
     * @param id
     * @return
     * @throws Exception 
     * @throws NumberFormatException 
     */
    public int deleteModule(String id) throws NumberFormatException, Exception{
        return dao.delete(id);
    }
    /**
     * 根据模块ID，得到所对应的模块树
     * @param id 模块ID
     * @return id所对应的模块树
     */
    @SuppressWarnings("unchecked")
    public TreeNode<Module> getModuleTreeById(String id) {
        if (id==null||id.trim().length()==0) return null;
        TreeNode<Module> tnM = null;
        CacheEle<?> mc = SystemCache.getCache(UgaConstants.CATCH_UGA_MODULE);
        if (mc!=null&&mc.getContent()!=null) {
            Map<String, TreeNode<Module>> im = (Map<String, TreeNode<Module>>)((Map<String, Object>)SystemCache.getCache(UgaConstants.CATCH_UGA_MODULE).getContent()).get("treeIndxMap");
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
    @SuppressWarnings("unchecked")
    public List<Module> getModuleList() {
        List<Module> lM = null;
        CacheEle<?> mc = SystemCache.getCache(UgaConstants.CATCH_UGA_MODULE);
        if (mc!=null&&mc.getContent()!=null) {
            lM = (List<Module>)((Map<String, Object>)SystemCache.getCache(UgaConstants.CATCH_UGA_MODULE).getContent()).get("list");
        }
        return lM;
    }

    /**
     * 得到模块树的根，注意，此根是造出的结点，名称为“模块”;ID=-1;
     * @return 模块树的根
     */
    @SuppressWarnings("unchecked")
    public TreeNode<Module> getModuleRoot() {
        TreeNode<Module> tnM = null;
        CacheEle<?> mc = SystemCache.getCache(UgaConstants.CATCH_UGA_MODULE);
        if (mc!=null&&mc.getContent()!=null) {
            tnM = (TreeNode<Module>)((Map<String, Object>)SystemCache.getCache(UgaConstants.CATCH_UGA_MODULE).getContent()).get("tree");
        }
        return tnM;
    }
}
