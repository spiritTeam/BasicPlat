package com.gmteam.framework.core.web;

import javax.servlet.http.HttpSession;

public abstract class AbstractSessionLoader implements SessionLoader{

    protected HttpSession session;

    public HttpSession getSession() {
        return session;
    }

    /**
     * 设置Session
     * @param session
     */
    public void setSession(HttpSession session) {
        this.session = session;
    }
}