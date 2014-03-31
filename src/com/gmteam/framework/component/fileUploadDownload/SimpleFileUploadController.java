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
    public Map<String, Object> beforeUploadOneFileOnSuccess(Map<String, Object> m) {
        return null;
    }

    @Override
    public void beforeUploadAllFiles(List<Map<String, Object>> fl) {
    }
}