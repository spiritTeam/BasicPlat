package com.gmteam.framework.util;

import java.io.BufferedReader;
import java.io.File;
import java.io.FileInputStream;
import java.io.FileNotFoundException;
import java.io.FileOutputStream;
import java.io.IOException;
import java.io.InputStreamReader;
import java.nio.channels.FileChannel;
import java.util.List;
import java.util.StringTokenizer;

/**
 * 封装了文件操作
 * @author wh,zhuhua
 */
public abstract class FileUtils {
    /**
     * 删除文件或文件夹下的文件，包括子目录中的文件，但不删除文件夹
     * @param file 欲删除的文件或文件夹
     * @return 若删除成功返回true
     */
    public static boolean deleteFile(File file) {
        if (file.isDirectory()) {
            String[] children = file.list();
            for (int i = 0; i < children.length; i++) {
                if (!deleteFile(new File(file, children[i])))
                    return false;
            }
        }
        return file.delete();
    }

    /**
     * 将一个目录copy到指定目录，包括其下的所有子目录和文件
     * @param sourcePath 源目录
     * @param desPath 目标目录
     * @return 若成功返回true
     */
    public static boolean copyPath(String sourcePath, String desPath) {
        File file = new File(sourcePath);
        String desFile = FileNameUtils.concatPath(desPath, file.getName());
        if (file.isDirectory()) {
            File desfile = new File(desFile);
            if (desfile.isFile()) return false;
            if (!desfile.isDirectory()) {
                if (!desfile.mkdirs()) return false;
            }
            String[] fileArray = file.list();
            for (int i = 0; i < fileArray.length; i++) {
                String subFile = fileArray[i];
                if (!copyPath(FileNameUtils.concatPath(sourcePath, subFile), desFile))
                    return false;
            }
            return true;
        } else {
            File desfile = new File(desFile);
            desfile.delete();
            return copyFile(sourcePath, desFile);
        }
    }

    /**
     * 将单个文件复制到指定位置，采用NIO方式
     * @param sourceFile 源文件
     * @param desFile 目标文件
     * @return 若拷贝成功返回true，若拷贝失败返回false
     * @throws IOException 
     */
    public static boolean copyFile(String sourceFile, String desFile) {
        File desfile = new File(desFile);
        desfile.delete();

        FileChannel inChannel=null, outChannel = null;
        FileInputStream fileInputStream = null;
        FileOutputStream fileOutputStream = null;
        try {
            fileInputStream = new FileInputStream(new File(sourceFile));
            inChannel = fileInputStream.getChannel();
            fileOutputStream = new FileOutputStream(desfile);
            outChannel = fileOutputStream.getChannel();

            int maxCount = (64*1024*1024)-(32*1024);//一次传输大小
            long size = inChannel.size();
            long position = 0;
            while (position < size) {
                position += inChannel.transferTo(position, maxCount, outChannel);
            }
            return true;
        } catch (FileNotFoundException e) {
            e.printStackTrace();
            return false;
        } catch (IOException e) {
            e.printStackTrace();
            return false;
        } finally {
            if (inChannel!=null) {
                try {inChannel.close();}catch(IOException e) {e.printStackTrace();}
            }
            if (outChannel!=null) {
                try {outChannel.close();}catch(IOException e) {e.printStackTrace();}
            }
            if (fileInputStream!=null) {
                try {fileInputStream.close();}catch(IOException e) {e.printStackTrace();}
            }
            if (fileOutputStream!=null) {
                try {fileOutputStream.close();}catch(IOException e) {e.printStackTrace();}
            }
        }
    }

    /**
     * 给文件更名
     * @param sourceFileName 源文件名
     * @param desFileName 更新后的文件名
     * @return 若成功返回true
     */
    public static boolean rename(String sourceFileName, String desFileName) {
        File sourceFile = new File(sourceFileName);
        File desFile = new File(desFileName);
        if (desFile.exists()) desFile.delete();
        return sourceFile.renameTo(desFile);
    }

    /**
     * 根据列表，删除多个文件
     * @param fileList 文件路径列表
     * @return
     */
    public static boolean delete(List<String> fileNameList) {
        boolean ret = false;
        for (int i=0; i<fileNameList.size(); i++) {
            String fileName = (String) fileNameList.get(i);
            ret = (new File(fileName)).delete();
            if (!ret)
                break;
        }
        return ret;
    }

    /**
     * 删除多个文件
     * @param fileList 文件路径数组
     * @return
     */
    public static boolean delete(String[] fileList) {
        boolean ret = false;
        for (int i = 0; i < fileList.length; i++) {
            String fileName = fileList[i];
            ret = (new File(fileName)).delete();
            if (!ret)
                break;
        }
        return ret;
    }

    /**
     * 获取文件创建时间，目前只支持windows操作系统
     * @param f
     * @return 创建时间的串，若出现异常，返回null
     */
    public static long getFileCreateTime4Win(File f) {
        try {
            Process ls_proc = Runtime.getRuntime().exec("cmd.exe /c dir " + f.getAbsolutePath() + " /tc");
            BufferedReader br = new BufferedReader(new InputStreamReader(ls_proc.getInputStream()));
            for (int i=0; i<5; i++) {
                br.readLine();
            }
            String stuff = br.readLine();
            br.close();

            StringTokenizer st = new StringTokenizer(stuff);
            String dateC = (st.nextToken()+" ");
            String time = st.nextToken()+":00";
            dateC = dateC.concat(time);
            return (DateUtils.getDateTime("yyyy-MM-dd HH:mm:ss", dateC)).getTime();
        } catch (Exception e) {
            return -1;
        }
    }
}