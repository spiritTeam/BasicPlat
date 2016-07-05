package com.spiritdata.framework.core.web;

import java.util.HashMap;
import java.util.Map;
import java.util.TreeMap;

import javax.servlet.ServletContext;
import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;

import org.springframework.web.context.WebApplicationContext;
import org.springframework.web.context.support.WebApplicationContextUtils;
import org.springframework.web.servlet.HandlerExceptionResolver;
import org.springframework.web.servlet.ModelAndView;
import org.springframework.web.servlet.mvc.Controller;
import org.springframework.web.servlet.view.json.MappingJackson2JsonView;

import com.spiritdata.framework.core.cache.CachePool;
import com.spiritdata.framework.core.cache.CatchLifecycle;
import com.spiritdata.framework.ext.io.StringPrintWriter;

/**
 * 刷新所有缓存
 * @author wh
 */
public class CacheFreshAllControllor implements Controller, HandlerExceptionResolver {

    @Override
    public ModelAndView resolveException(HttpServletRequest request, HttpServletResponse response, Object handler, Exception ex) {
        StringPrintWriter strintPrintWriter = new StringPrintWriter();
        ex.printStackTrace(strintPrintWriter);

        MappingJackson2JsonView mjjv = new MappingJackson2JsonView();
        response.setHeader("Cache-Control", "no-cache");
        mjjv.setContentType("text/html; charset=UTF-8");
        Map<String, Object> m = new HashMap<String, Object>();
        m.put("message", strintPrintWriter.getString());
        mjjv.setAttributesMap(m);
        ModelAndView mav = new ModelAndView();
        mav.setView(mjjv);
        return mav;
    }

    @Override
    public ModelAndView handleRequest(HttpServletRequest request, HttpServletResponse response) throws Exception {
        Map<String, Object> retObj = new HashMap<String, Object>();
        try {
            ServletContext sc = request.getSession().getServletContext();
            WebApplicationContext context = WebApplicationContextUtils.getWebApplicationContext(sc);
            CachePool cachePool = (CachePool)context.getBean("cachePool");
            //缓存框架存储
            if (cachePool!=null) {
                Map<String,CatchLifecycle> catchMap = new TreeMap<String,CatchLifecycle>();
                catchMap.putAll(cachePool.getCaches());
                for (String key : catchMap.keySet()) {
                    CatchLifecycle ic = catchMap.get(key);
                    ic.init();
                }
            }
            retObj.put("jsonType", "1");
            retObj.put("data", null);
        } catch(Exception e) {
            retObj.put("jsonType", "0");
            retObj.put("data", e.getMessage());
        }
        //json处理
        MappingJackson2JsonView mjjv = new MappingJackson2JsonView();
        response.setHeader("Cache-Control", "no-cache");
        mjjv.setContentType("text/html; charset=UTF-8");
        mjjv.setAttributesMap(retObj);
        ModelAndView mav = new ModelAndView();
        mav.setView(mjjv);
        return mav;
    }
}