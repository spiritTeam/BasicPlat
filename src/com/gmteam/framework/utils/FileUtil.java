package com.gmteam.framework.utils;

import java.io.File;
import java.io.FileInputStream;
import java.io.FileOutputStream;
import java.io.IOException;
import java.io.InputStream;
import java.io.OutputStream;
import java.util.List;

/**
 * @author zhuhua
 * 封装了文件操作
 */
public abstract class FileUtil {
    /**
     * 删除文件或文件夹下的文件，包括子目录中的文件，但不删除文件夹
     * 
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
     * 
     * @param sourcePath 源目录
     * @param desPath 目标目录
     * @return 若成功返回true
     */
    public static boolean copyPath(String sourcePath, String desPath) {
        File file = new File(sourcePath);
        String desFile = FileNameUtil.concatPath(desPath, file.getName());
        if (file.isDirectory()) {
            File desfile = new File(desFile);
            if (desfile.isFile()) return false;
            if (!desfile.isDirectory()) {
                if (!desfile.mkdirs()) return false;
            }
            String[] fileArray = file.list();
            for (int i = 0; i < fileArray.length; i++) {
                String subFile = fileArray[i];
                if (!copyPath(FileNameUtil.concatPath(sourcePath, subFile), desFile))
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
     * 将单个文件复制到指定位置
     * 
     * @param sourceFile 源文件
     * @param desFile 目标文件
     * @return
     */
    public static boolean copyFile(String sourceFile, String desFile) {
        try {
            File desfile = new File(desFile);
            desfile.delete();
            FileOutputStream fout = new FileOutputStream(desFile);
            FileInputStream fin = new FileInputStream(sourceFile);
            copy(fin, fout);
            fin.close();
            fout.close();
            return true;
        } catch (IOException e) {
            e.printStackTrace();
            return false;
        }
    }

    /**
     * 将输入流pull到输出流中
     * 
     * @param in
     * @param out
     * @throws IOException
     */
    private static void copy(InputStream in, OutputStream out)
            throws IOException {
        byte[] buffer = new byte[1024];
        while (true) {
            int bytesRead = in.read(buffer);
            if (bytesRead == -1)
                break;
            out.write(buffer, 0, bytesRead);
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
     * 
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
     * 
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
}