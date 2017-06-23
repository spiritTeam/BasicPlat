package com.spiritdata.framework.UGA;

import java.sql.Timestamp;

import com.spiritdata.framework.core.model.BaseObject;

/**
 * UGA框架。
 * 用户对象的基础类，用户信息需继承此类。
 * 否则本框架登录会出现问题。
 * @author wanghui
 */
public abstract class UgaUser extends BaseObject {
    private static final long serialVersionUID=-2467050195321143589L;

    protected String userId;//用户Id
    protected String loginName;//用户登录名称
    protected String password;//密码
    protected String salt;//密码
    protected int isValidate;//是否生效
    protected Timestamp CTime;//创建时间
    protected Timestamp lmTime;//最后修改时间
    protected String userName;//用户实名

    public String getUserId() {
        return userId;
    }
    public void setUserId(String userId) {
        this.userId=userId;
    }
    public String getLoginName() {
        return loginName;
    }
    public void setLoginName(String loginName) {
        this.loginName=loginName;
    }
    public String getPassword() {
        return password;
    }
    public void setPassword(String password) {
        this.password=password;
    }
    public String getSalt() {
        return salt;
    }
    public void setSalt(String salt) {
        this.salt=salt;
    }
    public int getIsValidate() {
        return isValidate;
    }
    public void setIsValidate(int isValidate) {
        this.isValidate=isValidate;
    }
    public Timestamp getCTime() {
        return CTime;
    }
    public void setCTime(Timestamp cTime) {
        CTime=cTime;
    }
    public Timestamp getLmTime() {
        return lmTime;
    }
    public void setLmTime(Timestamp lmTime) {
        this.lmTime=lmTime;
    }
    public String getUserName() {
        return userName;
    }
    public void setUserName(String userName) {
        this.userName=userName;
    }
}