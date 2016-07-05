package com.spiritdata.framework.component.fileUploadDownload;

import java.util.Map;

import org.springframework.stereotype.Controller;

import com.spiritdata.framework.core.web.AbstractFileUploadController;

/**
 * 简单文件上传，完全用父类中的逻辑进行处理。
 * 对单个文件上传
 * @author wh
 */
@Controller
public class SimpleFileUploadController extends AbstractFileUploadController{
    @Override
    public Map<String, Object> afterUploadOneFileOnSuccess(Map<String, Object> m, Map<String, Object> a, Map<String, Object> p) {
        //System.out.println(m.toString());
        return null;
    }

    @Override
    public void afterUploadAllFiles(Map<String, Object> fl, Map<String, Object> a, Map<String, Object> p) {
        //System.out.println(fl.toString());
    }
}