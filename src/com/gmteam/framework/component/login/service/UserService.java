package com.gmteam.framework.component.login.service;

import javax.annotation.PostConstruct;
import javax.annotation.Resource;

import org.springframework.stereotype.Service;

import com.gmteam.framework.component.login.pojo.User;
import com.gmteam.framework.core.dao.mybatis.MybatisDAO;
@Service
public class UserService {
    @Resource(name="defaultDAO")
    private MybatisDAO<User> dao;

    @PostConstruct
    public void initParam() {
        dao.setNamespace("Plat_User");
    }
    public User getPlatUserByLoginName(String loginName){
        try {
            return dao.getInfoObject("selectUserByLoginName", loginName);
        } catch (Exception e) {
            e.printStackTrace();
        }
        return null;
    }
}
