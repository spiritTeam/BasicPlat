package com.spiritdata.framework.core.web;

import java.util.List;
import javax.servlet.http.HttpSession;

/**
 * session加载器的外壳，以便于spring的配置
 * @author wh
 */
public class SessionLoaderShell {
    /*
     * session加载列表
     */
    private List<SessionLoader> sl;

    public List<SessionLoader> getSessionLoaderList() {
        return sl;
    }

    public void setSessionLoaderList(List<SessionLoader> sl) {
        this.sl = sl;
    }

    /**
     * 执行列表中的所有loader方法，向session加载数据
     * @param session
     */
    public void loader(HttpSession session) {
        if (sl!=null) {
            for (SessionLoader sl :this.sl) {
                try {
                    sl.loader(session);
                } catch(Exception e) {
                    e.printStackTrace();
                }
            }
        }
    }
}