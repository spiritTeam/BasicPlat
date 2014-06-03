package com.gmteam.framework.component.module.service;

import java.util.HashMap;
import java.util.List;
import java.util.Map;

import javax.annotation.PostConstruct;
import javax.annotation.Resource;

import org.springframework.stereotype.Service;

import com.gmteam.framework.component.module.pojo.Module;
import com.gmteam.framework.core.dao.mybatis.MybatisDAO;
import com.gmteam.framework.core.model.tree.TreeNode;
import com.gmteam.framework.util.TreeUtils;

/**
 * 从数据库中得到列表，并构造缓存结构
 * @author wh
 */
@Service
public class ModuleCacheService {

    @Resource(name="defaultDAO")
    private MybatisDAO<Module> dao;

    @PostConstruct
    public void initParam() {
        dao.setNamespace("Module");
    }

    @SuppressWarnings("unchecked")
    public Map<String, Object> makeCacheObject() throws Exception {
        Map<String, Object> ret = new HashMap<String, Object>();
        List<Module> list = dao.queryForList();
        ret.put("list", list);
        Map<String, Object> m = TreeUtils.convertFromList(list);
        List<TreeNode<Module>> mf = (List<TreeNode<Module>>)m.get("forest");
        if (mf!=null) {
            Module rootB = new Module();
            rootB.setId("-1");
            rootB.setParentId("0");
            rootB.setNodeName("模块");
            TreeNode<Module> root = new TreeNode<Module>(rootB);
            root.setChildren(mf);
            ret.put("tree", root);
            Map<String, TreeNode<Module>> treeIndexMap = new HashMap<String, TreeNode<Module>>();
            TreeUtils.setTreeIndexMap(mf, treeIndexMap);
            ret.put("treeIndexMap", treeIndexMap);
        }
        ret.put("errors", m.get("errors"));
        return ret;
    }
}