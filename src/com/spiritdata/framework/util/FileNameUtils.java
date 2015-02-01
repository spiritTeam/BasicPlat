package com.spiritdata.framework.util;

import java.io.File;

public abstract class FileNameUtils {
    /**
     * 用系统文件分割符号连接文件路径
     * @param path1 第一个路径
     * @param path2 第二个路径
     * @return 连接好的路径
     */
    public static String concatPath(String path1, String path2) {
        String firstPath = path1.replace("/", File.separator);
        firstPath = firstPath.replace("\\", File.separator);
        String secondPath = path2.replace("/", File.separator);
        secondPath = secondPath.replace("\\", File.separator);

        if (!firstPath.endsWith(File.separator)) firstPath+=File.separator;
        if (secondPath.startsWith(File.separator)) secondPath = secondPath.substring(1);

        return firstPath+secondPath;
    }

    /**
     * 转换为Unix格式目录分割符的文件名串
     * @param strPath 文件名串
     * @return 转换为Unix格式目录分割符的文件名串
     */
    public static String toUnixFormat(String strPath){
        return strPath.replaceAll("\\", "/");
    }
    
    /**
     * 根据当前日期 生成文件路径
     * @param filePath 生成路径的原路径
     * @return 日期路径
     */
    public static String getDateRulePath(String filePath) {
        String strRulePath = "";
        strRulePath = DateUtils.convert2DateStr(new java.util.Date());
        String[] tmpArr = strRulePath.split("-");
        strRulePath = "";
        for (String tmp: tmpArr) strRulePath += tmp + File.separator;
        return concatPath(filePath, strRulePath);
    }

    /**
     * 根据当前日期 生成文件名
     * @param fileName 生成文件名的原文件名
     * @return 日期文件名
     */
    public static String getDateRuleFileName(String fileName) {
        String tempFileName = getPureFileName(fileName);
        String strRulePath = "";
        strRulePath = DateUtils.convert2DateStr(new java.util.Date());
        String[] tmpArr = strRulePath.split("-");
        strRulePath = "";
        for (String tmp: tmpArr) tempFileName += "_"+tmp;
        return tempFileName+getExt(fileName);
    }

    /**
     * 得到文件的路径，默认认为，文件的最后一个分割符前是文件路径
     * @param localFullPath 文件名称
     * @return 文件路径
     */
    public static String getFilePath(String localFullPath){
        int lastDirPos = localFullPath.lastIndexOf(File.separator);
        if (lastDirPos==-1) lastDirPos = localFullPath.lastIndexOf("/");
        if (lastDirPos!=-1) return localFullPath.substring(0, lastDirPos);
        else return "";
    }

    /**
     * 得到不包括路径的文件名称，默认认为，文件的最后一个分割符前是文件路径
     * @param localFullPath 文件名称
     * @return 文件名称(包括扩展名)
     */
    public static String getFileName(String localFullPath){
        int lastDirPos = localFullPath.lastIndexOf(File.separator);
        if (lastDirPos==-1) lastDirPos = localFullPath.lastIndexOf("/");
        if (lastDirPos!=-1) return localFullPath.substring(lastDirPos + 1);
        return localFullPath;
    }

    /**
     * 得到纯文件名，不包括路径和扩展名
     * @param localFullPath 文件名称
     * @return 纯文件名
     */
    public static String getPureFileName(String localFullPath){
        String fileName = FileNameUtils.getFileName(localFullPath);
        int lastDirPos = fileName.lastIndexOf(".");
        if (lastDirPos!=-1) return fileName.substring(0, lastDirPos);
        else return fileName;
    }

    /**
     * 得到文件的扩展名
     * @param localFullPath 文件名称
     * @return 文件扩展名
     */
    public static String getExt(String localFullPath){
        if (localFullPath==null) return "";
        int lastDirPos = localFullPath.lastIndexOf(".");
        if (lastDirPos!=-1) return localFullPath.substring(lastDirPos);
        else return "";
    }
}