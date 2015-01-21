package com.spiritdata.framework.core.web;

import java.io.File;
import java.io.FileOutputStream;
import java.io.IOException;
import java.io.InputStream;
import java.nio.ByteBuffer;
import java.nio.channels.FileChannel;
import java.util.ArrayList;
import java.util.Collections;
import java.util.Date;
import java.util.HashMap;
import java.util.Iterator;
import java.util.List;
import java.util.Map;

import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;

import org.apache.commons.fileupload.FileItem;
import org.springframework.web.multipart.MultipartFile;
import org.springframework.web.multipart.MultipartHttpServletRequest;
import org.springframework.web.multipart.commons.CommonsMultipartFile;
import org.springframework.web.servlet.HandlerExceptionResolver;
import org.springframework.web.servlet.ModelAndView;

import com.spiritdata.framework.FConstants;
import com.spiritdata.framework.core.cache.CacheEle;
import com.spiritdata.framework.core.cache.SystemCache;
import com.spiritdata.framework.exceptionC.Plat0201CException;
import com.spiritdata.framework.ext.io.StringPrintWriter;
import com.spiritdata.framework.util.FileNameUtils;
import com.spiritdata.framework.util.JsonUtils;
import com.spiritdata.framework.util.ReflectUtils;
import com.spiritdata.framework.util.StringUtils;

import org.springframework.web.servlet.mvc.Controller;
import org.springframework.web.servlet.view.json.MappingJackson2JsonView;

/**
 * 文件上传的虚类，文件上传的功能都应该继承自此类。
 * 此类完成了基本的文件上传的功能，并以json格式返回给http协议的调用者。
 * 可以通过beforeUploadOneFileOnSuccess和befowUploadAllFile两个虚方法来扩展此类
 *
 * @author mht, wh
 */
public abstract class AbstractFileUploadController implements Controller, HandlerExceptionResolver {
    private String appOSPath = ((CacheEle<String>)(SystemCache.getCache(FConstants.APPOSPATH))).getContent();

    private final String defaultPath="\\uploadFiles";//默认路径，今后写到配置文件中，配置文件用Json方式

    private String savePath=null;//保存路径
    /**
     * 设置保存路径
     * @param savePath 保存路径
     */
    public void setSavePath(String savePath) {
        this.savePath = savePath;
    }

    private String storeFileNameFieldName=null;//上传文件保存时的名称的字段名称，与界面中的内容相互匹配
    /**
     * 保存上传文件存储文件名的界面字段名称，与前台页面中的内容相互匹配。<br/>
     * 若不设置，则按照默认的字段名称"storeFilename"从前台页面获取保存文件的名称。<br/>
     * 若通过前台取不到文件名称，则采用源文件的名称进行存储
     * @param storeFileNameFieldName 保存上传文件存储文件名的界面字段名称
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

    private int datePathModel=0;//是否采用Date规则生成路径
    /**
     * 设置是否采用Date规则生成路径。默认不按日期规则生成路径<br/>
     * =true按日期规则生成路径；=false，不按日期规则生成路径
     * @param isDatePath 是否采用Date规则生成路径
     */
    public void setDatePathModel(int datePathModel) {
        this.datePathModel = datePathModel;
    }

    
    private int conflictType=0;//解决冲突的方法
    /**
     * 设置文件名冲突时的解决办法<br/>
     * =0命名规则顺序加1，类似windows，这是默认方式，若进行10次仍然重名，则随机删除重名文件删除掉；=1，覆盖同名的文件
     * @param conflictType 冲突解决方法
     */
    public void setconflictType(int conflictType) {
        this.conflictType = conflictType;
    }

    private boolean breakOnOneFaild=false;//当一个文件上传失败后，是否结束后需的所有上传的文件(有点事务的味道)
    /**
     * 设置某一文件上传失败后的处理模式
     * @param breakOnOneFaild 某一文件上传失败后的处理模式 
     */
    public void setBreakOnOneFaild(boolean breakOnOneFaild) {
        this.breakOnOneFaild = breakOnOneFaild;
    }
    
    @Override
    public ModelAndView resolveException(HttpServletRequest request, HttpServletResponse response, Object handler, Exception ex) {
        StringPrintWriter strintPrintWriter = new StringPrintWriter();
        ex.printStackTrace(strintPrintWriter);

        MappingJackson2JsonView mjjv = new MappingJackson2JsonView();
        response.setHeader("Cache-Control", "no-cache");
        mjjv.setContentType("text/html; charset=UTF-8");
        mjjv.setAttributesMap(JsonUtils.obj2AjaxMap(strintPrintWriter.getString(), 1));
        ModelAndView mav = new ModelAndView();
        mav.setView(mjjv);
        return mav;
    }

    /**
     * 文件上传，并以Json的形式返回内容
     */
    @Override
    public ModelAndView handleRequest(HttpServletRequest request, HttpServletResponse response) {
        try {
            MultipartHttpServletRequest multipartRequest = (MultipartHttpServletRequest)request;
            Map<String, MultipartFile> files = multipartRequest.getFileMap();
            //得到其他的属性
            Map<String, Object> rqtParams = new HashMap<String, Object>();
            List<String> paramNameL = (List<String>)Collections.list(request.getParameterNames());
            for (String n: paramNameL) rqtParams.put(n, request.getParameter(n));
            Map<String, Object> rqtAttrs = new HashMap<String, Object>();
            List<String> attrNameL = (List<String>)Collections.list(request.getAttributeNames());
            for (String n: attrNameL) rqtAttrs.put(n, request.getAttribute(n));

            List<Map<String, Object>> retl = new ArrayList<Map<String, Object>>();
            if (files!=null&&files.size()>0) {//返回空
                //处理路径
                String _path = FileNameUtils.concatPath(this.appOSPath, this.defaultPath);
                File f;
                if (this.savePath!=null&&this.savePath.trim().length()>0) {//有路径
                    f = new File(this.savePath);
                    if (f.isAbsolute()) _path=this.savePath;
                    else _path = FileNameUtils.concatPath(_path, this.savePath);
                }
                if (this.datePathModel==1||this.datePathModel==3) _path=FileNameUtils.getDateRulePath(_path);
                //处理文件名称字段
                String[] storeFileNames = null;
                if (this.storeFileNameFieldName!=null) storeFileNames = multipartRequest.getParameterValues(this.storeFileNameFieldName);
                else storeFileNames = multipartRequest.getParameterValues("storeFilename");

                if (storeFileNames!=null) if (storeFileNames.length==0) storeFileNames=null;
                //处理每个文件
                Iterator<String> iterator=files.keySet().iterator();
                int fIndex=0;
                while (iterator.hasNext()) {
                    CommonsMultipartFile file = (CommonsMultipartFile)files.get(iterator.next());
                    if (file.getOriginalFilename()==null||file.getOriginalFilename().trim().equals("")) continue;
                    //处理文件名
                    String storeFilename = null;
                    if (storeFileNames==null) storeFilename = file.getOriginalFilename();
                    else {
                        storeFilename = storeFileNames[fIndex];
                        if (storeFilename==null||storeFilename.trim().equals("")) storeFilename = file.getOriginalFilename();
                        else storeFilename += FileNameUtils.getExt(file.getOriginalFilename());
                    }

                    if (this.filePrefix!=null&&this.filePrefix.trim().length()>0) storeFilename = this.filePrefix+"_"+storeFilename;
                    if (this.datePathModel==2||this.datePathModel==3) storeFilename = FileNameUtils.getDateRuleFileName(storeFilename);
                    storeFilename = FileNameUtils.concatPath(_path, storeFilename);
                    //拷贝文件
                    Map<String, Object> oneFileDealRetMap = saveMultipartFile2File(file, storeFilename);
                    boolean isBreak=false;
                    if ((""+oneFileDealRetMap.get("success")).equalsIgnoreCase("TRUE")) {//处理成功
                        //删除临时文件
                        delTempFile(file.getFileItem());
                        /*
                         *调用虚方法，处理每个文件的后续部分
                         */
                        oneFileDealRetMap.remove("success");
                        Map<String, Object> myDealRetMap = afterUploadOneFileOnSuccess(oneFileDealRetMap, rqtAttrs, rqtParams);
                        if (myDealRetMap!=null) {
                            boolean mySuccess = true;
                            try {
                                mySuccess = Boolean.parseBoolean((String)myDealRetMap.get("success"));
                            } catch(Exception e) {
                                mySuccess = true;
                            }
                            oneFileDealRetMap.put("success", "TRUE");
                            if (!mySuccess) {
                                boolean myOnFaildBreak=false;
                                try {
                                    myOnFaildBreak = Boolean.parseBoolean((String)myDealRetMap.get("onFaildBreak"));
                                } catch(Exception e) {
                                    myOnFaildBreak=false;
                                }
                                isBreak = myOnFaildBreak;
                            }
                        }
                    } else {//处理失败
                        isBreak = this.breakOnOneFaild;
                    }
                    retl.add(oneFileDealRetMap);
                    if (isBreak) break;
                    fIndex++;
                }
                if (retl.size()==0) retl=null;
                afterUploadAllFiles(retl, rqtAttrs, rqtParams);
            } else {
                Map<String, Object> nullM = new HashMap<String, Object>();
                nullM.put("warn", "没有文件可以处理。");
                retl.add(nullM);
            }
            //json处理
            MappingJackson2JsonView mjjv = new MappingJackson2JsonView();
            response.setHeader("Cache-Control", "no-cache");
            mjjv.setContentType("text/html; charset=UTF-8");
            mjjv.setAttributesMap(JsonUtils.obj2AjaxMap(retl, 0));
            ModelAndView mav = new ModelAndView();
            mav.setView(mjjv);
            return mav;
        } catch(Exception e) {
            throw new Plat0201CException(e);
        }
    }

    /*
     * 保存MultipartFile类型文件到文件系统，采用NIO的方法
     * @param file 传过来的MultipartFile对象
     * @param fileName 保存路径，包括文件名
     * @return 保存file属性的Map，主要是是否成功和文件名称
     */
    private Map<String, Object> saveMultipartFile2File(MultipartFile file,String fileName) {
        FileOutputStream fileOut = null;
        InputStream in = null;
        FileChannel fcOut=null;
        Map<String, String> em = new HashMap<String, String>();
        Map<String, Object> m = new HashMap<String, Object>();

        m.put("uploadTime", (new Date()).getTime());
        m.put("size", file.getSize());
        //处理文件名
        try {
            String dirName = FileNameUtils.getFilePath(fileName);
            File storeFilePath = new File(dirName);
            if (!storeFilePath.isDirectory()) storeFilePath.mkdirs();

            File storeFile = new File(fileName);
            if (storeFile.isDirectory()) {//如果文件名是一个路径，报错
                em.put("errCode", "FUE003");
                em.put("errMsg", "指定的上传文件名称已经作为目录存在了");
                em.put("errInfo", "目录["+storeFile+"]已经存在，无法拷贝。");
                m.put("error", em);
                m.put("storeFilename", fileName);
            } else if (storeFile.isFile()) {//如果文件已经存在
                if (this.conflictType==1) {//如果采用覆盖模式处理同名文件
                    if (!storeFile.delete()) {
                        em.put("errCode", "FUE001");
                        em.put("errMsg", "有重名文件，采用'覆盖同名文件模式'处理，由于不能删除原来的同名文件，导致不能上传文件");
                        em.put("errInfo", "文件["+fileName+"]不能覆盖拷贝");
                        m.put("error", em);
                    } else {
                        em.put("warnCode", "FUW002");
                        em.put("warnMsg", "有重名文件，采用'覆盖同名文件模式'处理，删除原同名文件");
                        em.put("warnInfo", "文件["+fileName+"]已被覆盖");
                        m.put("warn", em);
                    }
                } else {
                    String _fPath, _fPureName, _fExt, _orgFPureName=FileNameUtils.getPureFileName(fileName);
                    int i=10;
                    while (i>0) {
                        _fPath = FileNameUtils.getFilePath(fileName);
                        _fPureName = FileNameUtils.getPureFileName(fileName)+"(1)";
                        _fExt = FileNameUtils.getExt(fileName);
                        fileName = FileNameUtils.concatPath(_fPath, _fPureName+_fExt);
                        storeFile = new File(fileName);
                        if (!storeFile.isFile()) break;
                        i--;
                    }
                    if (i<=1) {//10此重名任然有问题
                        double d = Math.random();
                        long l = Math.round(d*10);
                        for (int j=0; j<l; j++) {
                            _orgFPureName = _orgFPureName+"(1)";
                        }
                        _fPath = FileNameUtils.getFilePath(fileName);
                        _fExt = FileNameUtils.getExt(fileName);
                        fileName = FileNameUtils.concatPath(_fPath, _orgFPureName+_fExt);
                        storeFile = new File(fileName);
                        if (storeFile.isFile()) {
                            if (!storeFile.delete()) {
                                em.put("errCode", "FUE002");
                                em.put("errMsg", "有重名文件，采用'重命名同名文件模式'处理，由于重命名已经嵌套10次，名称仍然重复，而随机匹配的重命名文件不能删除，导致不能上传文件");
                                em.put("errInfo", "文件["+fileName+"]不能删除，无法拷贝");
                                m.put("error", em);
                            } else {
                                em.put("warnCode", "FUW001");
                                em.put("warnMsg", "有重名文件，采用'重命名同名文件模式'处理，由于重命名已经嵌套10次，名称仍然重复，于是随机匹配的重命名文件");
                                em.put("warnInfo", "文件["+fileName+"]已被删除");
                                m.put("warn", em);
                            }
                        }
                    } else {
                        em.put("warnCode", "FUW003");
                        em.put("warnMsg", "有重名文件，采用'重命名同名文件模式'处理");
                        em.put("warnInfo", "文件已被存储为["+fileName+"]");
                        m.put("warn", em);
                    }
                }
            }
        } catch(Exception e) {
            em.put("errCode", "FUE_E");
            em.put("errMsg", e.getMessage());
            m.put("error", em);
        }

        if (m.get("error")!=null) {
            m.put("success", false);
            return m;
        }
        //填充返回信息
        Class<?> clazz = file.getClass();
        Map<String, String> _m = ReflectUtils.Object2Map(clazz, file);
        StringBuffer _fileItem = new StringBuffer(_m.get("fileItem"));
        m.put("orglFilename", _fileItem.substring(5, _fileItem.indexOf("StoreLocation")-2));
        m.put("FieldName", _fileItem.substring(_fileItem.indexOf("FieldName=")+10));
        
        try {
            File outputFile = new File(fileName);
            if (!outputFile.isFile()) {
                if (!outputFile.createNewFile()) {
                    em.put("errCode", "FUE004");
                    em.put("errMsg", "创建文件失败");
                    em.put("errInfo", "文件["+fileName+"]创建失败");
                    m.put("error", em);
                    m.put("storeFilename", fileName);
                    m.put("success", false);
                    return m;
                }
            }
            fileOut = new FileOutputStream(outputFile);
            fcOut = fileOut.getChannel();

            ByteBuffer buffer = ByteBuffer.allocate(1024);
            in = file.getInputStream();
            byte[] inbf = new byte[1024];
            while(in.read(inbf)!=-1) {
                buffer.put(inbf);
                buffer.flip();
                fcOut.write(buffer);
                buffer.clear();
            }
            m.put("success", true);
            m.put("storeFilename", fileName);
            m.put("timeConsuming", (new Date()).getTime()-Long.parseLong(""+m.get("uploadTime")));
            return m;
        } catch (Exception e) {
            em.put("errCode", "FUE_E");
            em.put("errMsg", e.getMessage());
            m.put("success", false);
            m.put("storeFilename", fileName);
            m.put("timeConsuming", (new Date()).getTime()-Long.parseLong(""+m.get("uploadTime")));
            m.put("error", em);
            return m;
        } finally {
            if (fileOut!=null) {
                try{fileOut.close();}catch(IOException e){e.printStackTrace();}
            }
            if (fcOut!=null) {
                try{fcOut.close();}catch(IOException e){e.printStackTrace();}
            }
            if (in!=null) {
                try{in.close();}catch(IOException e){e.printStackTrace();}
            }
            //删除临时文件
            file.getOriginalFilename();
        }
    }

    private boolean delTempFile(FileItem fi) {
        String fiStr = fi.toString();
        String[] ss = StringUtils.splitString(fiStr, ",");
        fiStr = null;
        for (int i=0; i<ss.length; i++) {
            if (ss[i].startsWith(" StoreLocation=")) {
                fiStr = ss[i];
                break;
            }
        }
        if (fiStr!=null) {
            ss = StringUtils.splitString(fiStr, "=");
            fiStr = ss[1];
            File f = new File(fiStr);
            return f.delete();
        }
        return false;
    }
    
    /**
     * 当成功上传一个文件后，调用此方法。
     * @param m 成功上传的文件的信息，包括：success——是否上传成功;storeFilename——保存在服务器端的文件名;fileInfo——上传文件的信息，类型为MultipartFile
     * 若上传失败，还会有error信息;<br/>
     * 警告信息会存储在warn信息中。<br/>
     * @param rqtAttrs request的属性
     * @param rqtParams request的参数
     * @return  此方法的返回值是Map，此Map需要有如下两个key:<br/>
     * 1-success:String类型,处理是否成功<br/>
     * 2-onFaildBreak:String类型("true" or "false"),若失败是否退出后需的处理<br/>
     * 如果返回值为空，或没有这些信息，本方法将按照sucess=true进行处理<br/>
     * 若要把自己的处理结果传递到本方法的外面，可以直接修改参数m，在m中加入自己的信息
     */
    public abstract Map<String, Object> afterUploadOneFileOnSuccess(Map<String, Object> m, Map<String, Object> rqtAttrs, Map<String, Object> rqtParams);

    /**
     * 当上传所有文件后，调用此方法。注意，如果没有任何上传文件，也可以调用此方法，处理后续逻辑，此时fl为空。
     * @param fl 上传文件处理结果的说明列表，每个上传文件一个处理结果。（如果只上传一个文件，此列表中只有一个元素，如果没有任何上传文件此值为null）。<br/>
     * 列表中的元素为Map对象，其中的信息如下包括：success——是否上传成功;storeFilename——保存在服务器端的文件名;fileInfo——上传文件的信息，类型为MultipartFile;<br/>
     * 若上传失败，还会有error信息;<br/>
     * 警告信息会存储在warn信息中。
     * 此列表会被以json形式返回到页面中。
     * @param rqtAttrs request的属性
     * @param rqtParams request的参数
     */
    public abstract void afterUploadAllFiles(List<Map<String, Object>> fl, Map<String, Object> rqtAttrs, Map<String, Object> rqtParams);
}