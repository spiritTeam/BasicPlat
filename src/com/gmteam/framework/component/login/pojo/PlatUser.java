package com.gmteam.framework.component.login.pojo;

import com.gmteam.framework.model.BaseObject;

public class PlatUser extends BaseObject{
    
    private static final long serialVersionUID = 6013739951194460125L;
    private String id;
    private String loginName;
    private String userName;
    private String password;

    public String getId() {
        return id;
    }
    public void setId(String id) {
        this.id = id;
    }
    public String getLoginName() {
        return loginName;
    }
    public void setLoginName(String loginName) {
        this.loginName = loginName;
    }
    public String getUserName() {
    	return userName;
	}
    public void setUserName(String userName) {
        this.userName = userName;
    }
    public String getPassword() {
        return password;
    }
    public void setPassword(String password) {
        this.password = password;
    }
}
