package com.gmteam.framework.component.module.service;

import java.util.ArrayList;
import java.util.HashMap;
import java.util.List;
import java.util.Map;

import javax.annotation.PostConstruct;
import javax.annotation.Resource;

import org.junit.Test;
import org.springframework.stereotype.Service;

import com.gmteam.framework.IConstants;
import com.gmteam.framework.component.login.pojo.UserLogin;
import com.gmteam.framework.component.module.pojo.Module;
import com.gmteam.framework.core.cache.CacheEle;
import com.gmteam.framework.core.cache.SystemCache;
import com.gmteam.framework.core.dao.mybatis.MybatisDAO;
import com.gmteam.framework.core.model.tree.TreeNode;

@Service
public class ModuleService {
    @Resource(name="defaultDAO")
    private MybatisDAO<Module> dao;

    @PostConstruct
    public void initParam() {
        dao.setNamespace("Module");
    }

    public List<Module> getList() {
        try {
            return dao.queryForList("getList");
        } catch (Exception e) {
            e.printStackTrace();
        }
        return null;
    }

    public List<TreeNode<Module>> getRoot() {
        List<Module> list = getList();
        List<TreeNode<Module>> root = new ArrayList<TreeNode<Module>>();
        for (Module m : list) {
            TreeNode<Module> oneNode = new TreeNode<Module>();
            oneNode.setTnEntity(m);
            if (root.size() == 0) {
                root.add(oneNode);
            } else {
                // 判断roots连有没有oneNode的子节点或者父节点
                for (TreeNode<Module> node : root) {
                    // 判断node是不是oneNode的子节点
                    if (node.getParentId().equals(oneNode.getId())) {
                        oneNode.addChild(node);
                    }
                    // 判断node是不是oneNode的父节点
                    else if (node.getId().equals(oneNode.getParentId())) {
                        node.addChild(oneNode);
                    }
                }
                root.add(oneNode);
            }
        }
        List<TreeNode<Module>> roots = new ArrayList<TreeNode<Module>>();
        for (TreeNode<Module> node : root) {
            if (node.getParentId().equals("0")) {
                roots.add(node);
            }
        }
        // 返回的roots是一个List，也就是一个森林(这个list里面可能也就只有一棵树)
        return roots;
    }

    /**
     * 给一个树的数据List,返回一个组装好的TreeList,也就是一个roots(森林)
     * 
     * @param list
     * @return
     */
    public List<TreeNode<Module>> getRoots(List<Module> list) {
        List<TreeNode<Module>> root = new ArrayList<TreeNode<Module>>();
        for (Module m : list) {
            TreeNode<Module> oneNode = new TreeNode<Module>();
            oneNode.setTnEntity(m);
            if (root.size() == 0) {
                root.add(oneNode);
            } else {
                // 判断roots连有没有oneNode的子节点或者父节点
                for (TreeNode<Module> node : root) {
                    // 判断node是不是oneNode的子节点
                    if (node.getParentId().equals(oneNode.getId())) {
                        oneNode.addChild(node);
                    }
                    // 判断node是不是oneNode的父节点
                    else if (node.getId().equals(oneNode.getParentId())) {
                        node.addChild(oneNode);
                    }
                }
                root.add(oneNode);
            }
        }
        List<TreeNode<Module>> roots = new ArrayList<TreeNode<Module>>();
        for (TreeNode<Module> node : root) {
            if (node.getParentId().equals("0")) {
                roots.add(node);
            }
        }
        // 返回的roots是一个List，也就是一个森林(这个list里面可能也就只有一棵树)
        return roots;
    }
}