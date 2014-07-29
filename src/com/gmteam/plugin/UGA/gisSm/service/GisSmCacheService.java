package com.gmteam.plugin.UGA.gisSm.service;

import java.util.HashMap;
import java.util.List;
import java.util.Map;

import javax.annotation.PostConstruct;
import javax.annotation.Resource;

import org.springframework.stereotype.Service;

import com.gmteam.framework.core.dao.mybatis.MybatisDAO;
import com.gmteam.framework.core.model.tree.TreeNode;
import com.gmteam.framework.util.TreeUtils;
import com.gmteam.plugin.UGA.gisSm.pojo.Function;
import com.gmteam.plugin.UGA.gisSm.pojo.User;

@Service
public class GisSmCacheService {
    @Resource(name="defaultDAO_dcSys")
    private MybatisDAO<User> userDao;
    @Resource(name="defaultDAO_dcSys")
    private MybatisDAO<Function> functionDao;
    @PostConstruct
    public void initParam() {
        userDao.setNamespace("SM");
    }

    /**
     * 构造用户缓存，返回值是Map，包括用户列表list，用户ID索引idMap，用户登录名索引loginMap
     * @return 用户缓存
     * @throws Exception
     */
    public Map<String, Object> makeCacheUser() throws Exception {
        Map<String, Object> ret = new HashMap<String, Object>();
        List<User> list = userDao.queryForList("getUserList");
        ret.put("list", list);
        Map<String, User> idMap = new HashMap<String, User>();
        Map<String, User> loginMap = new HashMap<String, User>();
        for (User user : list) {
            idMap.put(user.getUserId(), user);
            loginMap.put(user.getLoginName(), user);
        }
        ret.put("idMap", idMap);
        ret.put("loginMap", loginMap);
        return ret;
    }

    /**
     * 构造模块缓存，返回值是Map
     * @return 模块缓存
     * @throws Exception
     */
    @SuppressWarnings("unchecked")
    public Map<String, Object> makeCacheModule() throws Exception {
        Map<String, Object> ret = new HashMap<String, Object>();
        List<Function> list = functionDao.queryForList("getFunctionList");
        ret.put("list", list);
        Map<String, Object> m = TreeUtils.convertFromList(list);
        List<TreeNode<Function>> mf = (List<TreeNode<Function>>)m.get("forest");
        if (mf!=null) {
            Function rootB = new Function();
            rootB.setId("-1");
            rootB.setParentId("0");
            rootB.setNodeName("模块");
            TreeNode<Function> root = new TreeNode<Function>(rootB);
            root.setChildren(mf);
            ret.put("tree", root);
            Map<String, TreeNode<Function>> treeIndexMap = new HashMap<String, TreeNode<Function>>();
            TreeUtils.setTreeIndexMap(mf, treeIndexMap);
            ret.put("treeIndexMap", treeIndexMap);
        }
        ret.put("errors", m.get("errors"));
        return ret;
    }
}