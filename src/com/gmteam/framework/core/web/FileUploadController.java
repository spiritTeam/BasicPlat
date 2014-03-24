package com.gmteam.framework.core.web;

import java.util.HashMap;
import java.util.Map;
import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;
import org.springframework.web.multipart.MultipartFile;
import org.springframework.web.multipart.MultipartHttpServletRequest;
import org.springframework.web.servlet.HandlerExceptionResolver;
import org.springframework.web.servlet.ModelAndView;
import com.gmteam.framework.model.StringPrintWriter;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RequestMethod;
import org.springframework.web.bind.annotation.ResponseBody;

/**
 * Controller的父类，用于处理异常，目前用在上传文件的功能中.
 * 所有需要抛出异常的Controller都应继承此类
 * 另：此类由于可能需要扩展，不打入Jar包中，或在子controller类中再次
 * @author mht, wh
 */
public class FileUploadController implements HandlerExceptionResolver {
	@Override
	public ModelAndView resolveException(HttpServletRequest request, HttpServletResponse response, Object handler, Exception ex) {
        Map<String,Object> map = new HashMap<String,Object>();
        StringPrintWriter strintPrintWriter = new StringPrintWriter();
        ex.printStackTrace(strintPrintWriter);
        map.put("errorMsg", strintPrintWriter.getString());
        return new ModelAndView("error.jsp", map);
	}

    @RequestMapping(value="/ajaxUpload.do",method=RequestMethod.POST)
    @ResponseBody
    public Map<String, String> uploadFiles(MultipartHttpServletRequest multipartRequest,HttpServletResponse response)throws Exception {
    	Map<String, String> m=null;
        //获取页面上传的文件key为对应的文本框Id，value为文件
        Map<String, MultipartFile> files = multipartRequest.getFileMap();
        //得到路径
        String path = multipartRequest.getSession().getServletContext().getRealPath("/tmp/");
        //指定路径，此路径在tmp下,customPath为自定义路径，依然在tmp下
        String customPath = multipartRequest.getParameter("customPath");
        //保存
//        list = FileUpload.saveFiles(files, path,customPath);
        return m;
    }
}