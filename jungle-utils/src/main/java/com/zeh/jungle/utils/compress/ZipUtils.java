package com.zeh.jungle.utils.compress;

import java.io.BufferedInputStream;
import java.io.BufferedOutputStream;
import java.io.File;
import java.io.FileInputStream;
import java.io.FileOutputStream;
import java.util.zip.CRC32;
import java.util.zip.CheckedInputStream;
import java.util.zip.CheckedOutputStream;
import java.util.zip.ZipEntry;
import java.util.zip.ZipInputStream;
import java.util.zip.ZipOutputStream;

import org.apache.commons.io.IOUtils;
import org.apache.commons.lang3.StringUtils;

/**
 * @author allen
 * @version $Id: ZipUtils.java, v 0.1 2016年2月29日 下午1:31:14 allen Exp $
 */
public abstract class ZipUtils {
    public static final String  EXT      = ".zip";
    private static final String BASE_DIR = "";
    private static final int    BUFF     = 1024;

    /**********************************************************************************
     * 压缩
     **********************************************************************************/

    /**
     * 压缩
     * @param srcPath	源文件路径
     * @throws Exception
     */
    public static void compress(String srcPath) throws Exception {
        File srcFile = new File(srcPath);
        compress(srcFile);
    }

    /**
     * 压缩
     * @param srcPath	源文件路径
     * @param destPath	目标文件路径
     * @throws Exception
     */
    public static void compress(String srcPath, String destPath) throws Exception {
        File srcFile = new File(srcPath);

        compress(srcFile, destPath);
    }

    /**
     * 压缩
     * @param srcFile	待压缩文件
     * @throws Exception
     */
    public static void compress(File srcFile) throws Exception {
        String name = srcFile.getName();
        String basePath = srcFile.getParent();
        String dstPath = getDestinationPath(basePath, name, EXT);
        compress(srcFile, dstPath);
    }

    /**
     * 压缩
     * @param srcFile	待压缩文件
     * @param destPath	压缩包内文件相对路径
     * @throws Exception
     */
    public static void compress(File srcFile, String destPath) throws Exception {
        compress(srcFile, new File(destPath));
    }

    /**
     * 压缩
     * @param srcFile	待压缩文件
     * @param destFile	压缩文件
     * @throws Exception
     */
    public static void compress(File srcFile, File destFile) throws Exception {
        // 文件CRC32校验
        CheckedOutputStream cos = new CheckedOutputStream(new FileOutputStream(destFile), new CRC32());
        ZipOutputStream zos = new ZipOutputStream(cos);
        compress(srcFile, zos, BASE_DIR);
        IOUtils.closeQuietly(zos);
    }

    /**
     * 压缩
     * @param srcFile	待压缩文件
     * @param zos		ZipOutputStream
     * @param basePath	压缩包内文件相对路径
     * @throws Exception
     */
    public static void compress(File srcFile, ZipOutputStream zos, String basePath) throws Exception {
        if (srcFile.isDirectory()) {
            compressDir(srcFile, zos, basePath);
        } else {
            compressFile(srcFile, zos, basePath);
        }
    }

    private static void compressDir(File dir, ZipOutputStream zos, String basePath) throws Exception {

        File[] files = dir.listFiles();

        // 构建空目录  
        if (files.length < 1) {
            //            ZipEntry entry = new ZipEntry(StringUtils.join(new String[]{basePath, dir.getName(), PATH}));  
            ZipEntry entry = new ZipEntry(getDestinationPath(basePath, dir.getName(), File.separator));
            zos.putNextEntry(entry);
            zos.closeEntry();
        }

        for (File file : files) {

            // 递归压缩  
            compress(file, zos, basePath + dir.getName() + File.separator);

        }
    }

    private static void compressFile(File file, ZipOutputStream zos, String dir) throws Exception {

        /** 
         * 压缩包内文件名定义 
         *  
         * <pre> 
         * 如果有多级目录，那么这里就需要给出包含目录的文件名 
         * 如果用WinRAR打开压缩包，中文名将显示为乱码 
         * </pre> 
         */
        ZipEntry entry = new ZipEntry(dir + file.getName());
        zos.putNextEntry(entry);

        BufferedInputStream bis = new BufferedInputStream(new FileInputStream(file));

        byte data[] = new byte[BUFF];
        for (int count = bis.read(data, 0, BUFF); count != -1; count = bis.read(data, 0, BUFF)) {
            zos.write(data, 0, count);
        }
        IOUtils.closeQuietly(bis);

        zos.closeEntry();
    }

    /**********************************************************************************
     * 解压缩
     **********************************************************************************/

    /** 
     * 文件解压缩 
     *  
     * @param srcPath	源文件路径 
     * @throws Exception 
     */
    public static void decompress(String srcPath) throws Exception {
        File srcFile = new File(srcPath);
        decompress(srcFile);
    }

    /** 
     * 文件解压缩 
     * @param srcPath 	源文件路径 
     * @param destPath	目标文件路径 
     * @throws Exception 
     */
    public static void decompress(String srcPath, String destPath) throws Exception {
        File srcFile = new File(srcPath);
        decompress(srcFile, destPath);
    }

    /** 
     * 解压缩 
     * @param srcFile	源文件
     * @throws Exception 
     */
    public static void decompress(File srcFile) throws Exception {
        String basePath = srcFile.getParent();
        decompress(srcFile, basePath);
    }

    /** 
     * 解压缩 
     *  
     * @param srcFile	源文件
     * @param destPath	目标文件路径
     * @throws Exception 
     */
    public static void decompress(File srcFile, String destPath) throws Exception {
        decompress(srcFile, new File(destPath));
    }

    /** 
     * 解压缩 
     * @param srcFile	源文件
     * @param destFile	目标文件
     * @throws Exception 
     */
    public static void decompress(File srcFile, File destFile) throws Exception {
        CheckedInputStream cis = new CheckedInputStream(new FileInputStream(srcFile), new CRC32());
        ZipInputStream zis = new ZipInputStream(cis);
        decompress(destFile, zis);
        IOUtils.closeQuietly(zis);
    }

    /** 
     * 文件 解压缩 
     * @param destFile	目标文件 
     * @param zis		ZipInputStream 
     * @throws Exception 
     */
    private static void decompress(File destFile, ZipInputStream zis) throws Exception {

        for (ZipEntry entry = zis.getNextEntry(); entry != null; entry = zis.getNextEntry()) {
            // 文件  
            //            String dir = StringUtils.join(new String[]{destFile.getPath(), File.separator, entry.getName()});
            String dir = getDestinationPath(destFile.getParent(), destFile.getName(), File.separator + entry.getName());
            File dirFile = new File(dir);

            // 文件检查  
            fileProber(dirFile);

            if (entry.isDirectory()) {
                dirFile.mkdirs();
            } else {
                decompressFile(dirFile, zis);
            }

            zis.closeEntry();
        }
    }

    /** 
     * 文件检查 
     * 当父目录不存在时，创建目录！ 
     * @param dirFile 
     */
    private static void fileProber(File dirFile) {
        File parentFile = dirFile.getParentFile();
        if (!parentFile.exists()) {
            // 递归寻找上级目录  
            fileProber(parentFile);
            parentFile.mkdir();
        }
    }

    /** 
     * 文件解压缩 
     * @param destFile	目标文件 
     * @param zis		ZipInputStream 
     * @throws Exception 
     */
    private static void decompressFile(File destFile, ZipInputStream zis) throws Exception {

        BufferedOutputStream bos = new BufferedOutputStream(new FileOutputStream(destFile));
        byte data[] = new byte[BUFF];
        for (int count = zis.read(data, 0, BUFF); count != -1; count = zis.read(data, 0, BUFF)) {
            bos.write(data, 0, count);
        }

        IOUtils.closeQuietly(bos);
    }

    private static String getDestinationPath(String basePath, String fileName, String ext) {
        char lastChar = basePath.charAt(basePath.length() - 1);
        if (!File.separator.equals(lastChar)) {
            basePath += File.separator;
        }
        return StringUtils.join(new String[] { basePath, fileName, ext });
    }
}
