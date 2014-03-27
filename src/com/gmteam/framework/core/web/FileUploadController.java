package com.gmteam.framework.core.web;

import java.io.File;
import java.io.FileOutputStream;
import java.nio.ByteBuffer;
import java.nio.channels.FileChannel;
import java.util.Iterator;
import java.util.Map;

import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;

import org.springframework.web.multipart.MultipartFile;
import org.springframework.web.multipart.MultipartHttpServletRequest;
import org.springframework.web.servlet.HandlerExceptionResolver;
import org.springframework.web.servlet.ModelAndView;

import com.gmteam.framework.ext.io.StringPrintWriter;
import com.gmteam.framework.utils.FileNameUtil;
import com.gmteam.framework.utils.JsonUtil;

import org.springframework.web.servlet.mvc.Controller;
import org.springframework.web.servlet.view.json.MappingJackson2JsonView;

/**
 * 文件上传，本功能只是实现简单的上传文件功能。
 * 此控制类中的所有返回到前台界面的内容都是json格式.
 * @author mht, wh
 */
public class FileUploadController implements Controller, HandlerExceptionResolver {
    private final String defaultPath="\\files";//默认路径，今后写到配置文件中，配置文件用Json方式

    private String savePath=null;//保存路径
	/**
	 * 设置保存路径
	 * @param savePath 保存路径
	 */
    public void setSavePath(String savePath) {
        this.savePath = savePath;
    }

    public void setConflictType(int conflictType) {
        ConflictType = conflictType;
    }

    private String storeFileNameFieldName=null;//上传文件保存时的名称的字段名称，与界面中的内容相互匹配
    /**
     * 保存上传文件存储文件名的界面字段名称，与前台页面中的内容相互匹配。<br/>
     * 若不设置，则按照默认的字段名称"storeFileName"从前台页面获取保存文件的名称。<br/>
     * 若通过前台取不到文件名称，则采用源文件的名称进行存储
     * @param savePath 保存上传文件存储文件名的界面字段名称
     */
    public void setStoreFileNameFieldName(String storeFileNameFieldName) {
        this.storeFileNameFieldName = storeFileNameFieldName;
    }

    
    private String filePrefix=null;//保存文件的前缀名
    /**
     * 设置文件前缀
     * @param filePrefix 文件前缀
     */
    public void setFilePrefix(String filePrefix) {
        this.filePrefix = filePrefix;
    }

    private boolean isDatePath=false;//是否采用Date规则生成路径
    /**
     * 是否采用Date规则生成路径。默认不按日期规则生成路径<br/>
     * =true按日期规则生成路径；=false，不按日期规则生成路径
     * @param isDatePath 是否采用Date规则生成路径
     */
    public void setDatePath(boolean isDatePath) {
        this.isDatePath = isDatePath;
    }

    private int ConflictType=1;//解决冲突的方法

    @Override
    public ModelAndView resolveException(HttpServletRequest request, HttpServletResponse response, Object handler, Exception ex) {
        StringPrintWriter strintPrintWriter = new StringPrintWriter();
        ex.printStackTrace(strintPrintWriter);

        MappingJackson2JsonView mjjv = new MappingJackson2JsonView();
        response.setHeader("Cache-Control", "no-cache");
        mjjv.setContentType("text/html; charset=UTF-8");
        mjjv.setAttributesMap(JsonUtil.Obj2AjaxMap(strintPrintWriter.getString(), 0) );
        ModelAndView mav = new ModelAndView();
        mav.setView(mjjv);
        return mav;
    }

    /**
     * 文件上传，并以Json的形式返回内容
     */
    @Override
    public ModelAndView handleRequest(HttpServletRequest request, HttpServletResponse response) throws Exception {
        MultipartHttpServletRequest multipartRequest = (MultipartHttpServletRequest)request;
        Map<String, MultipartFile> files = multipartRequest.getFileMap();
        String _path = defaultPath;
        File f;
        //处理路径
        if (this.savePath!=null&&this.savePath.trim().length()>0) {//有路径
            f = new File(this.savePath);
            if (f.isAbsolute()) _path=this.savePath;
            else _path = FileNameUtil.concatPath(_path, this.savePath);
        }
        if (this.isDatePath) _path=FileNameUtil.getDateRulePath(_path);
        //处理文件名称字段
        String[] storeFileNames = null;
        if (this.storeFileNameFieldName!=null) {
            storeFileNames = multipartRequest.getParameterValues(this.storeFileNameFieldName);
        } else {
            storeFileNames = multipartRequest.getParameterValues("storeFileName");
        }
        if (storeFileNames!=null) if (storeFileNames.length==0) storeFileNames=null;

        multipartRequest.getParameterValues("filename");
        Iterator<String> iterator=files.keySet().iterator();
        while (iterator.hasNext()) {
            MultipartFile file = files.get(iterator.next());
            //处理文件名
            String storeFileName = null;
            if (storeFileNames!=null) {
                storeFileName = getStoreFileName(storeFileNames, "");
            }
            if (storeFileName==null) storeFileName = file.getName();

            if (this.filePrefix!=null&&this.filePrefix.trim().length()>0) storeFileName += this.filePrefix+"_";
            storeFileName = FileNameUtil.concatPath(_path, storeFileName);
        	//拷贝文件
            saveMultipartFile2File(file, storeFileName);
        }
        

        MappingJackson2JsonView mjjv = new MappingJackson2JsonView();
        response.setHeader("Cache-Control", "no-cache");
        mjjv.setContentType("text/html; charset=UTF-8");
        mjjv.setAttributesMap(null);
        ModelAndView mav = new ModelAndView();
        mav.setView(mjjv);
        return mav;
    }

    /*
     * 保存MultipartFile类型文件到文件系统，采用了NIO的方法
     * @param file 传过来的MultipartFile对象
     * @param fileName 保存路径，包括文件名
     * @return 保存file属性的bean
     */
    private Map<String, Object> saveMultipartFile2File(MultipartFile file,String fileName) {
        try {
            FileOutputStream fileOS = new FileOutputStream(new File(fileName));
            //获取一个通道
            FileChannel fc = fileOS.getChannel();
            //建立一个缓冲区
            ByteBuffer buffer = ByteBuffer.allocate(52428800);
            byte[] message = file.getBytes();
            for (int i=0; i<message.length; i++) {
                buffer.put(message[i]);
            }
            buffer.flip();
            fc.write(buffer);
            fileOS.close();
        } catch (Exception e) {
            e.printStackTrace();
        }
        return null;
    }

    private String getStoreFileName(String[] s, String fileFieldName) {
        for (String aFileName: s) {
            if (aFileName.startsWith(fileFieldName+":")) return aFileName.substring(aFileName.indexOf(":"));
        }
        return null;
    }
}