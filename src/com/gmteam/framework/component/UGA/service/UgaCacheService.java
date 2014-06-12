package com.gmteam.framework.component.UGA.service;

import java.util.HashMap;
import java.util.List;
import java.util.Map;

import javax.annotation.PostConstruct;
import javax.annotation.Resource;

import org.springframework.stereotype.Service;

import com.gmteam.framework.component.UGA.pojo.User;
import com.gmteam.framework.core.dao.mybatis.MybatisDAO;

@Service
public class UgaCacheService {
    @Resource(name="defaultDAO")
    private MybatisDAO<User> userDao;

    @Resource(name="defaultDAO")
    private MybatisDAO<User> moduleOrgDao;

    @PostConstruct
    public void initParam() {
        userDao.setNamespace("UGA");
        moduleOrgDao.setNamespace("UGA");
    }

    /**
     * 构造用户缓存，返回值是Map，包括用户列表list，用户ID索引idMap，用户登录名索引loginMap
     * @return 用户缓存
     * @throws Exception
     */
    public Map<String, Object> makeCacheUser() throws Exception {
        Map<String, Object> ret = new HashMap<String, Object>();
        List<User> list = userDao.queryForList("getList");
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
}