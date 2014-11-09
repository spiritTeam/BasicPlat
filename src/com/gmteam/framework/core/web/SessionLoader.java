package com.gmteam.framework.core.web;

import javax.servlet.http.HttpSession;

/**
 * Session加载
 * @author wh
 */
public interface SessionLoader {
    /**
     * 设置Session
     * @param session
     */
    public void setSession(HttpSession session);
    /**
     * Session的加载过程
     */
    public void loader();
}