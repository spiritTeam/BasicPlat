package com.spiritdata.framework.core.web;

import javax.servlet.http.HttpSession;

/**
 * Session加载
 * @author wh
 */
public interface SessionLoader {
    /**
     * 向Session中加载
     */
    public void loader(HttpSession session) throws Exception;
}