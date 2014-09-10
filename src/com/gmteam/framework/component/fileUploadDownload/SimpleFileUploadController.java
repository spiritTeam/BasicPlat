package com.gmteam.framework.component.fileUploadDownload;

import java.util.List;
import java.util.Map;

import com.gmteam.framework.core.web.AbstractFileUploadController;

/**
 * 简单文件上传，完全用父类中的逻辑进行处理。
 * 对单个文件上传
 * @author wh
 */
public class SimpleFileUploadController extends AbstractFileUploadController{

    @Override
    public Map<String, Object> afterUploadOneFileOnSuccess(Map<String, Object> m, Map<String, Object> a, Map<String, Object> p) {
        //System.out.println(m.toString());
        //判断类型
        if (doc) importDoc()
        if (excel) importExcel();
        if (db) ？？
        return null;
    }

    @Override
    public void afterUploadAllFiles(List<Map<String, Object>> fl, Map<String, Object> a, Map<String, Object> p) {
        //System.out.println(fl.toString());
    }
}