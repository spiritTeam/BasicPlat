package com.gmteam.framework.component.UGA.service;

import java.util.ArrayList;
import java.util.Collection;
import java.util.HashMap;
import java.util.List;
import java.util.Map;

import javax.annotation.PostConstruct;
import javax.annotation.Resource;

import org.springframework.stereotype.Service;

import com.gmteam.framework.UGA.UgaConstants;
import com.gmteam.framework.component.UGA.pojo.User;
import com.gmteam.framework.component.module.pojo.Module;
import com.gmteam.framework.core.cache.CacheEle;
import com.gmteam.framework.core.cache.SystemCache;
import com.gmteam.framework.core.dao.mybatis.MybatisDAO;
import com.gmteam.framework.core.model.tree.TreeNode;
import com.gmteam.framework.util.TreeUtils;

@Service
public class UgaCacheService {
    @Resource(name="defaultDAO")
    private MybatisDAO<User> userDao;

    @Resource(name="defaultDAO")
    private MybatisDAO<Module> moduleDao;

    @PostConstruct
    public void initParam() {
        userDao.setNamespace("UGA");
        moduleDao.setNamespace("UGA");
    }

    /**
     * 构造用户缓存，返回值是Map，包括用户列表list，用户ID索引idMap，用户登录名索引loginMap
     * @return 用户缓存
     * @throws Exception
     */
    public Map<String, Object> makeCacheUser() throws Exception {
        Map<String, Object> ret = new HashMap<String, Object>();
//        List<User> list = userDao.queryForList("getUserList");
        List<User> list=null;
        try {
            list = userDao.queryForList("getUserList");
        } catch(Exception e) {
            e.printStackTrace();
            list = userDao.queryForList("getUserList");
        }
        list = userDao.queryForList("getUserList");
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
        List<Module> list = moduleDao.queryForList("getModuleList");
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

    /**
     * 构造用户模块权限缓存，返回值是Map
     * @return 用户模块权限缓存
     * @throws Exception
     */
    public Map<String, TreeNode<Module>> makeCacheUserModule() throws Exception {
        Map<String, TreeNode<Module>> ret = new HashMap<String, TreeNode<Module>>();
        //获取module信息
        CacheEle<?> mc = SystemCache.getCache(UgaConstants.CATCH_UGA_MODULE);
        TreeNode<Module> tnM = null;
        if (mc!=null&&mc.getContent()!=null) tnM = (TreeNode<Module>)((Map<String, Object>)SystemCache.getCache(UgaConstants.CATCH_UGA_MODULE).getContent()).get("tree");
        if (tnM==null||tnM.getAllCount()==0) return ret;
        List<TreeNode<Module>> forest = new ArrayList<TreeNode<Module>>();
        forest.add(tnM);

        List<Map<String, String>> urm = userDao.queryForListAutoTranform("getUserModuleList", null);
        String userId=null;
        Collection<String> moduleIds = null;
        List<TreeNode<Module>> userModuleTree = null;
        if (urm!=null&&urm.size()>0) {
            for (int i=0; i<urm.size(); i++) {
                if (urm.get(i).get("userId").equals(userId)) {
                    moduleIds.add(urm.get(i).get("moduleId"));
                } else {
                    userModuleTree = TreeUtils.restructureTree(forest, moduleIds);
                    if (userModuleTree!=null&&userModuleTree.size()>0) ret.put(userId, userModuleTree.get(0));
                    userId=urm.get(i).get("userId");
                    moduleIds = new ArrayList<String>();
                    moduleIds.add(urm.get(i).get("moduleId"));
                }
            }
            userModuleTree = TreeUtils.restructureTree(forest, moduleIds);
            if (userModuleTree!=null&&userModuleTree.size()>0) ret.put(userId, userModuleTree.get(0));
        }
        return ret;
    }
}