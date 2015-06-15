package com.hoyo.paobar.util;

import java.io.BufferedInputStream;
import java.io.File;
import java.io.FileInputStream;
import java.io.FileOutputStream;
import java.io.InputStream;
import java.util.ArrayList;
import java.util.List;
import java.util.zip.ZipEntry;
import java.util.zip.ZipOutputStream;

import android.util.Log;

public class ZipUtil {
	private static final String TAG = "ZipUtil";
	/**
     * zip压缩功能
     * @param baseDir 所要压缩的目录名（包含绝对路径）
     * @param objFileName 压缩后的文件名
     * @throws Exception
     */
    public static void createZip(String baseDir, String objFileName) throws Exception {
            File folderObject = new File(baseDir);
            if (folderObject.exists()){
                    List<File> fileList = getSubFiles(new File(baseDir));
                    //压缩文件名
                    ZipOutputStream zos = new ZipOutputStream(new FileOutputStream(objFileName));
                    ZipEntry ze = null;
                    byte[] buf = new byte[1024];
                    int readLen = 0;
                    for (int i = 0; i < fileList.size(); i++) {
                            File f = fileList.get(i);
                            Log.i(TAG, "Adding: " + f.getPath() + f.getName());
                            //创建一个ZipEntry，并设置Name和其它的一些属性
                            ze = new ZipEntry(getAbsFileName(baseDir, f));
                            ze.setSize(f.length());
                            ze.setTime(f.lastModified());
                            //将ZipEntry加到zos中，再写入实际的文件内容
                            zos.putNextEntry(ze);
                            InputStream is = new BufferedInputStream(new FileInputStream(f));
                            while ((readLen = is.read(buf, 0, 1024)) != -1) {
                                    zos.write(buf, 0, readLen);
                            }
                            is.close();
                            Log.i(TAG, "done...");
                    }
                    zos.close();
            }else{
                    throw new Exception("this folder isnot exist!");
            }
    }
    /**
     * 取得指定目录下的所有文件列表，包括子目录.
     *
     * @param baseDir
     *            File 指定的目录
     * @return 包含java.io.File的List
     */
    private static List<File> getSubFiles(File baseDir) {
            List<File> ret = new ArrayList<File>();
            File[] tmp = baseDir.listFiles();
            for (File element : tmp) {
                    if (element.isFile()) {
                            ret.add(element);
                    }
                    if (element.isDirectory()) {
                            ret.addAll(getSubFiles(element));
                    }
            }
            return ret;
    }
    /**
     * 给定根目录，返回另一个文件名的相对路径，用于zip文件中的路径.
     *
     * @param baseDir
     *            java.lang.String 根目录
     * @param realFileName
     *            java.io.File 实际的文件名
     * @return 相对文件名
     */
    private static String getAbsFileName(String baseDir, File realFileName) {
            File real = realFileName;
            File base = new File(baseDir);
            String ret = real.getName();
            while (true) {
                    real = real.getParentFile();
                    if (real == null) {
                        break;
                    }
                    if (real.equals(base)) {
                        break;
                    } else {
                            ret = real.getName() + "/" + ret;
                    }
            }
            Log.i(TAG, "K " + ret);
            return ret;
    }
    /**
     * 删除文件夹
     * @param folderPath 文件夹完整绝对路径
     */
    public static void delFolder(String folderPath) {
        try {
           delAllFile(folderPath); //删除完里面所有内容
           String filePath = folderPath;
           java.io.File myFilePath = new java.io.File(filePath);
           myFilePath.delete(); //删除空文件夹
        } catch (Exception e) {
          e.printStackTrace();
        }
   }
    /**
     * 删除指定文件夹下所有文件
     * @param path 文件夹完整绝对路径
     * @return
     */
     public static boolean delAllFile(String path) {
         boolean flag = false;
         File file = new File(path);
         if (!file.exists()) {
           return flag;
         }
         if (!file.isDirectory()) {
           return flag;
         }
         String[] tempList = file.list();
         File temp = null;
         for (String element : tempList) {
            if (path.endsWith(File.separator)) {
               temp = new File(path + element);
            } else {
                temp = new File(path + File.separator + element);
            }
            if (temp.isFile()) {
               temp.delete();
            }
            if (temp.isDirectory()) {
               delAllFile(path + "/" + element);//先删除文件夹里面的文件
               delFolder(path + "/" + element);//再删除空文件夹
               flag = true;
            }
         }
         return flag;
       }
}
