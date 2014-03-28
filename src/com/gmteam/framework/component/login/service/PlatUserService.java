package com.gmteam.framework.component.login.service;

import javax.annotation.PostConstruct;
import javax.annotation.Resource;
import org.springframework.stereotype.Service;
import com.gmteam.framework.component.login.pojo.PlatUser;
import com.gmteam.framework.core.dao.mybatis.MybatisDAO;
@Service
public class PlatUserService {
    @Resource(name="defaultDAO")
    private MybatisDAO<PlatUser> dao;

    @PostConstruct
    public void initParam() {
        dao.setNamespace("Plat_User");
	}
    public PlatUser getPlatUserByLoginName(String loginName) throws Exception{
        return dao.getInfoObject("selectPlatUserByLoginName", loginName);
    }
    public void addPlatUser(PlatUser user) throws Exception{
        dao.insert(user);
    }
}
