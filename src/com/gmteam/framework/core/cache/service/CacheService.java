package com.gmteam.framework.core.cache.service;

import java.util.ArrayList;
import java.util.HashMap;
import java.util.Hashtable;
import java.util.List;
import java.util.Map;

import org.springframework.stereotype.Component;

import com.gmteam.framework.core.cache.pojo.PlatModule;
/**
 * 缓存service
 * 其中包含数据库的访问，初始化
 * 并放入缓存中
 * @author mht
 */
@Component
public class CacheService {
    /**
     * 访问数据库
     */
//    private GeneralMybatisDAO<?> smDAO;
//    public GeneralMybatisDAO<?> getSmDAO() {
//        return smDAO;
//    }
//    public void setSmDAO(GeneralMybatisDAO<?> smDAO) {
//        this.smDAO = smDAO;
//    }

    /**
     * 初始化方法，用于设置DAO的nameSpace
     */
    public void init() {
        //smDAO.setNamespace("SM");
    }
    /**
     * 装载用户信息
     */
//    public Map<String, Object> loadUserCache() throws Exception {
//        Map<String, Object> retM = new HashMap<String, Object>();
//        Map<String, User> userMap=new Hashtable<String, User>();
//        List<User> userList = (List<User>)smDAO.queryForList("getUserList", null);
//        for (User u: userList) {
//            userMap.put(u.getId(), u);
//        }
//        retM.put("1", userMap);
//        retM.put("2", userList);
//        return retM;
//    }
    /**
     * 装载模块信息
     */
    public Map<String, Object> loadModuleCache() throws Exception {
        Map<String, Object> retM = new HashMap<String, Object>();
        Map<String, PlatModule> moduleMap=new Hashtable<String, PlatModule>();
        //List<PlatModule> moduleList = (List<PlatModule>)smDAO.queryForList("getModuleList", null);
        List<PlatModule> moduleList = getModuleList();
        for (PlatModule pm: moduleList) {
            moduleMap.put(pm.getId()+"", pm);
        }
        retM.put("1", moduleMap);
        retM.put("2", moduleList);
        return retM;
    }
    /**
     * 模块加载的测试数据
     * @return
     */
    private List<PlatModule> getModuleList() {
        List<PlatModule> l1 = new ArrayList<PlatModule>();
        PlatModule p = new PlatModule();
        for(int i=0;i<10;i++){
            p = new PlatModule();
            p.setId(i);
            p.setModeuleName("module"+i);
            l1.add(p);
        }
        return l1;
    }

    /**
     * 装载组织机构信息
     */
//    public Map<String, Object> loadDepartmentCache() throws Exception {
//        Map<String, Object> retM = new HashMap<String, Object>();
//        Map<String, Department> departmentMap=new Hashtable<String, Department>();
//        List<Department> departmentList = (List<Department>)smDAO.queryForList("getDepartmentList", null);
//        for (Department d: departmentList) {
//            departmentMap.put(d.getV_zzjgid(), d);
//        }
//        retM.put("1", departmentMap);
//        retM.put("2", departmentList);
//        return retM;
//    }

    /**
     * 装载功能信息
//     */
//    public Map<String, Object> loadFunctionCache() throws Exception {
//        Map<String, Object> retM = new HashMap<String, Object>();
//        Map<String, Function> functionMap=new Hashtable<String, Function>();
//        List<Function> functionList = (List<Function>)smDAO.queryForList("getFunctioinList", null);
//        for (Function f: functionList) {
//            functionMap.put(f.getV_functionid(), f);
//        }
//        retM.put("1", functionMap);
//        retM.put("2", functionList);
//        return retM;
//    }

    /**
     * 装载用户功能
     */
//    public Map<String, List<String>> loadUserFunctionCache() throws Exception {
//        Map<String, List<String>> retM = new HashMap<String, List<String>>();
//        List<String> fList = null;
//        String curUserId = "";
//        List ufList = smDAO.queryForListAutoTranform("getUserAuthorityList", null);
//        for (int i=0;i<ufList.size();i++) {
//            Map<String, Object> uf = (HashMap<String, Object>)ufList.get(i);
//            if (!uf.get("USERID").equals(curUserId)) {
//                curUserId = (String)uf.get("USERID");
//                fList = new ArrayList<String>();
//                fList.add((String)uf.get("FID"));
//                retM.put(curUserId, fList);
//            } else {
//                ((List<String>)retM.get(curUserId)).add((String)uf.get("FID"));
//            }
//        }
//        return retM;
//    }
    
}