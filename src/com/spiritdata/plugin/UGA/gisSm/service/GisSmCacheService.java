package com.spiritdata.plugin.UGA.gisSm.service;

import java.util.ArrayList;
import java.util.Collection;
import java.util.HashMap;
import java.util.List;
import java.util.Map;

import javax.annotation.PostConstruct;
import javax.annotation.Resource;

import com.spiritdata.framework.UGA.UgaConstants;
import com.spiritdata.framework.core.cache.CacheEle;
import com.spiritdata.framework.core.cache.SystemCache;
import com.spiritdata.framework.core.dao.mybatis.MybatisDAO;
import com.spiritdata.framework.core.model.tree.TreeNode;
import com.spiritdata.framework.util.TreeUtils;
import com.spiritdata.plugin.UGA.gisSm.pojo.Function;
import com.spiritdata.plugin.UGA.gisSm.pojo.User;

public class GisSmCacheService {
    @Resource(name="defaultDAO_dcSys")
    private MybatisDAO<User> userDao;
    @Resource(name="defaultDAO_dcSys")
    private MybatisDAO<Function> functionDao;
    @PostConstruct
    public void initParam() {
        userDao.setNamespace("SM");
        functionDao.setNamespace("SM");
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

    /**
     * 构造用户模块权限缓存，返回值是Map
     * @return 用户模块权限缓存
     * @throws Exception
     */
    public Map<String, TreeNode<Function>> makeCacheUserModule() throws Exception {
        Map<String, TreeNode<Function>> ret = new HashMap<String, TreeNode<Function>>();
        //获取module信息
        CacheEle<?> mc = SystemCache.getCache(UgaConstants.CATCH_UGA_MODULE);
        TreeNode<Function> tnM = null;
        if (mc!=null&&mc.getContent()!=null) tnM = (TreeNode<Function>)((Map<String, Object>)SystemCache.getCache(UgaConstants.CATCH_UGA_MODULE).getContent()).get("tree");
        if (tnM==null||tnM.getAllCount()==0) return ret;
        List<TreeNode<Function>> forest = new ArrayList<TreeNode<Function>>();
        forest.add(tnM);

        List<Map<String, String>> urm = userDao.queryForListAutoTranform("getUserFunctionList", null);

        String userId=null;
        Collection<String> moduleIds = null;
        List<TreeNode<Function>> userModuleTree = null;
        if (urm!=null&&urm.size()>0) {
            for (int i=0; i<urm.size(); i++) {
                if (urm.get(i).get("USERID").equals(userId)) {
                    moduleIds.add(urm.get(i).get("FID"));
                } else {
                    userModuleTree = TreeUtils.restructureTree(forest, moduleIds);
                    if (userModuleTree!=null&&userModuleTree.size()>0) ret.put(userId, userModuleTree.get(0));
                    userId=urm.get(i).get("USERID");
                    moduleIds = new ArrayList<String>();
                    moduleIds.add(urm.get(i).get("FID"));
                }
            }
            userModuleTree = TreeUtils.restructureTree(forest, moduleIds);
            if (userModuleTree!=null&&userModuleTree.size()>0) ret.put(userId, userModuleTree.get(0));
        }
        return ret;
    }
}