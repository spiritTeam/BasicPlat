package com.gmteam.framework.component.login.pojo;

import java.io.Serializable;
import java.util.Date;

import com.gmteam.framework.model.BaseObject;

public class UserLoginInfo extends BaseObject {
    private static final long serialVersionUID = 796788171985762931L;
    
    public String loginname;
    public String password;
    private String sessionId;//SessionID
    private String clientIp;//客户端IP
    private String clientMacAddr;//客户端网卡Mac地址
    private String browser;//客户端浏览器信息
    private long time = (new Date()).getTime();//创建时间，此信息在创建时就确定了，不能进行修改

    public String getLoginname() {
        return loginname;
    }
    public void setLoginname(String loginname) {
        this.loginname = loginname;
    }
    public String getPassword() {
        return password;
    }
    public void setPassword(String password) {
        this.password = password;
    }
    public String getSessionId() {
        return sessionId;
    }
    public void setSessionId(String sessionId) {
        this.sessionId = sessionId;
    }
    public String getClientIp() {
        return clientIp;
    }
    public void setClientIp(String clientIp) {
        this.clientIp = clientIp;
    }
    public String getClientMacAddr() {
        return clientMacAddr;
    }
    public void setClientMacAddr(String clientMacAddr) {
        this.clientMacAddr = clientMacAddr;
    }
    public String getBrowser() {
        return browser;
    }
    public void setBrowser(String browser) {
        this.browser = browser;
    }
    public long getTime() {
        return time;
    }

    public void setId(Serializable id) {
    }
}