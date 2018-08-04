package com.spiritdata.framework.util;

import java.io.BufferedReader;
import java.io.IOException;
import java.io.InputStreamReader;
import java.io.UnsupportedEncodingException;
import java.net.URLDecoder;
import java.util.Enumeration;
import java.util.HashMap;
import java.util.Map;
import javax.servlet.ServletInputStream;
import javax.servlet.http.HttpServletRequest;

/**
 * 完成参数获取，这里参数获取不能获取file类型的数据。<br/>
 * 若要进行文件上传功能的处理，请参照FileUpload进行处理。
 * @author wanghui
 */
public abstract class RequestUtils {
    /**
     * 从Request获得所有参数，包括流中的json参数。
     * <pre>
     * 可以支持post/get方式。若stream和parameter都有，则要merge两个参数。
     * merge规则为:
     * 1-以parameter参数为准(parameter中参数替换stream中同名参数的值)
     * 2-若出现_JSON_DATA的参数，则不进行任何合并，以_JSON_DATA为准
     * </pre>
     * @param req 请求内容
     * @return 返回参数对象，用Map和List的相互嵌套为形式
     */
    @SuppressWarnings("unchecked")
    public static Map<String, Object> getDataFromRequest(HttpServletRequest req) {
        Map<String, Object> retM=null;
        String line=null;
        //1-从数据流中获得参数：这种参数必须是json格式的
        InputStreamReader isr=null;
        BufferedReader br=null;
        try {
            isr=new InputStreamReader((ServletInputStream)req.getInputStream(), "UTF-8");
            br=new BufferedReader(isr);
            StringBuilder sb=new StringBuilder();
            while ((line=br.readLine())!=null) sb.append(line);
            line=sb.toString().trim();
            if (line!=null&&line.length()>0) {
                try {
                    retM=(Map<String, Object>)JsonUtils.jsonToObj(line, Map.class);
                } catch(Exception e) {
                    retM=getDataFromUrlQueryStr(line);
                    if (retM!=null&&!retM.isEmpty()&&retM.get("_JSON_DATA")!=null) {
                        line=retM.get("_JSON_DATA")+"";
                        try {
                            retM=(Map<String, Object>)JsonUtils.jsonToObj(line, Map.class);
                            return retM;
                        } catch(Exception e1) {
                            return null;
                        }
                    }
                }
            }
        } catch (IOException e) {
            e.printStackTrace();
        } finally {
            if (isr!=null) try {isr.close();} catch(Exception e) {}
            if (br!=null) try {br.close();} catch(Exception e) {}
        }

        //2-从parameter获得参数
        Map<String, Object> tempM=new HashMap<String, Object>();
        Enumeration<String> enu=req.getParameterNames();
        while(enu.hasMoreElements()) {
            String name=(String)enu.nextElement();
            tempM.put(name, req.getParameter(name));
        }
        if (tempM!=null&&!tempM.isEmpty()) {
            //2.1-参数处理，读取_JSON_DATA，若有此参数，则其他任何参数都无用
            if (tempM.get("_JSON_DATA")!=null) {
                line=tempM.get("_JSON_DATA")+"";
                try {
                    retM=(Map<String, Object>)JsonUtils.jsonToObj(line, Map.class);
                    return retM;
                } catch(Exception e) {
                    return null;
                }
            }
            //2.2-处理参数内容，合并内容
            if (retM==null) retM=new HashMap<String, Object>();
            Object mPoint=null;
            Object newMap=null;
            for (String key:tempM.keySet()) {
                if (key.indexOf("[")==-1) retM.put(key, tempM.get(key));
                else {//这个合并比较复杂？？？
                    mPoint=null;
                    newMap=null;
                    String[] ks=parseKey(key);
                    if (ks!=null) {
                        for (int i=0; i<ks.length; i++) {
                            if (i==0) {
                                mPoint=retM.get(ks[i]);
                                if (mPoint==null||!(mPoint instanceof Map)) {
                                    newMap=new HashMap<String, Object>();
                                    retM.put(ks[i], newMap);
                                    mPoint=newMap;
                                }
                            } else if (i<ks.length-1) {
                                newMap=((Map<String, Object>)mPoint).get(ks[i]);
                                if (newMap==null||!(newMap instanceof Map)) {
                                    newMap=new HashMap<String, Object>();
                                    ((Map<String, Object>)mPoint).put(ks[i], newMap);
                                }
                                mPoint=newMap;
                            } else {
                                ((Map<String, Object>)mPoint).put(ks[i], tempM.get(key));
                            }
                        }
                    }
                }
            }
        }

//        //3-从?号参数中获得数据
//        if (retM==null) retM=new HashMap<String, Object>();
//        String urlParamStr=req.getQueryString();
//        if (!StringUtils.isNullOrEmptyOrSpace(urlParamStr)) {
//            String[] paramArray=urlParamStr.split("&");
//            if (paramArray!=null&&paramArray.length>0) {
//                for (String oneParam: paramArray) {
//                    String[] paramKeyValArray=oneParam.split("=");
//                    String key=null, val=null;
//                    if (paramKeyValArray.length==1) {
//                        try {
//                            key=URLDecoder.decode(oneParam, "utf-8");
//                        } catch(Exception e) {};
//                    } else if (paramKeyValArray.length==2) {
//                        try {
//                            key=URLDecoder.decode(paramKeyValArray[0], "utf-8");
//                        } catch(Exception e) {};
//                        try {
//                            val=URLDecoder.decode(paramKeyValArray[1], "utf-8");
//                        } catch(Exception e) {};
//                    }
//                    if (StringUtils.isNullOrEmptyOrSpace(key)) {
//                        //合并参数，若已经存在，此参数作废
//                        if (retM.get(key)!=null) {
//                            retM.put(key, val);
//                        }
//                    }
//                }
//            }
//        }
        if (retM==null||retM.isEmpty()) return null;
        return retM;
    }

    private static Map<String, Object> getDataFromUrlQueryStr(String queryStr) throws UnsupportedEncodingException {
        Map<String, Object> retM=new HashMap<String, Object>();
        String pkvs[]=queryStr.split("&");
        for (String kv: pkvs) {
            String kvD[]=kv.trim().split("=");
            if (kvD.length==2) retM.put(URLDecoder.decode(kvD[0],"UTF-8"), URLDecoder.decode(kvD[1],"UTF-8"));
        }
        return retM;
    }
    private static String[] parseKey(String keyStr) {
        String[] sp=keyStr.split("\\[");
        if (sp[sp.length-1].equals("]")) return null;
        for (int i=1; i<sp.length; i++) {
            sp[i]=sp[i].substring(0, sp[i].length()-1);
        }
        return sp.length>1?sp:null;
    }
}