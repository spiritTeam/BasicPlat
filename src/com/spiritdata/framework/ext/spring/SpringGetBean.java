package com.spiritdata.framework.ext.spring;

import java.util.Enumeration;

import javax.servlet.Servlet;
import javax.servlet.ServletContext;

import org.slf4j.LoggerFactory;
import org.springframework.context.ApplicationContext;
import org.springframework.web.context.support.WebApplicationContextUtils;

import com.spiritdata.framework.core.cache.SystemCache;
import com.spiritdata.framework.util.StringUtils;
import com.spiritdata.framework.FConstants;

/**
 * 在B/S开发中，得到Spring中定义的Bean
 * @author wanghui
 */
public class SpringGetBean {
    /**
     * 得到某个Bean
     * @param beanName bean名称
     * @return bean对象
     */
    @SuppressWarnings({ "unchecked", "deprecation" })
    public static  <T> T getBean(String beanName) {
        try {
            //得到上下文
            ServletContext sc=(SystemCache.getCache(FConstants.SERVLET_CONTEXT)==null?null:(ServletContext)SystemCache.getCache(FConstants.SERVLET_CONTEXT).getContent());
            if (sc==null) return null;

            Exception _e=null;
            T retBean=null;
            //从全局得到
            ApplicationContext ac=WebApplicationContextUtils.getWebApplicationContext(sc);
            if (ac!=null) {
                try {
                    retBean=(T)ac.getBean(beanName);
                } catch(Exception e) {
                    _e=e;
                }
            }
            //从MVC中得到
            if (retBean==null) {
                ac=WebApplicationContextUtils.getWebApplicationContext(sc, "org.springframework.web.servlet.FrameworkServlet.CONTEXT.springMvc");
                if (ac!=null) {
                    try {
                        retBean=(T)ac.getBean(beanName);
                    } catch(Exception e) {
                        _e=e;
                    }
                }
            }
            if (retBean==null&&_e!=null) throw _e;
            return retBean;
        } catch(Exception e) {
            LoggerFactory.getLogger(SpringGetBean.class).error("得到名称为[{}]的bean对象出现异常：\n{}", beanName, StringUtils.getAllMessage(e));
        }
        return null;
    }

}