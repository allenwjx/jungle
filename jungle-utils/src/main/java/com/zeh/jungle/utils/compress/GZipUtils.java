package com.zeh.jungle.utils.compress;

import java.io.ByteArrayInputStream;
import java.io.ByteArrayOutputStream;
import java.io.File;
import java.io.FileInputStream;
import java.io.FileOutputStream;
import java.io.InputStream;
import java.io.OutputStream;
import java.util.zip.GZIPInputStream;
import java.util.zip.GZIPOutputStream;

import org.apache.commons.io.IOUtils;

/**
 * @author allen
 * @version $Id: GZipUtils.java, v 0.1 2016年2月29日 下午1:31:14 allen Exp $
 */
public abstract class GZipUtils {
    public static final int    BUFF = 1024;
    public static final String EXT  = ".gz";

    /**********************************************************************************
     * 压缩
     **********************************************************************************/

    /**
     * 压缩二进制数据
     * @param data		待压缩数据
     * @return byte[]	压缩数据
     * @throws Exception
     */
    public static byte[] compress(byte[] data) throws Exception {
        ByteArrayInputStream bais = new ByteArrayInputStream(data);
        ByteArrayOutputStream baos = new ByteArrayOutputStream();

        compress(bais, baos);

        data = baos.toByteArray();

        baos.flush();
        IOUtils.closeQuietly(baos);
        IOUtils.closeQuietly(bais);

        return data;
    }

    /**
     * 文件压缩
     * @param path	源文件路径
     * @throws Exception
     */
    public static void compress(String path) throws Exception {
        compress(path, true);
    }

    /**
     * 文件压缩
     * @param path		源文件路径
     * @param delete	是否删除源文件，压缩成功后
     * @throws Exception
     */
    public static void compress(String path, boolean delete) throws Exception {
        File file = new File(path);
        compress(file, delete);
    }

    /**
     * 压缩文件
     * @param file	源文件
     * @throws Exception
     */
    public static void compress(File file) throws Exception {
        compress(file, true);
    }

    /**
     * 压缩文件
     * @param file		源文件
     * @param delete	是否删除源文件，压缩成功后
     * @throws Exception
     */
    public static void compress(File file, boolean delete) throws Exception {
        FileInputStream fis = new FileInputStream(file);
        FileOutputStream fos = new FileOutputStream(file.getPath() + EXT);

        compress(fis, fos);

        fos.flush();
        IOUtils.closeQuietly(fos);
        IOUtils.closeQuietly(fis);

        if (delete) {
            file.delete();
        }
    }

    /**
     * 二进制数据压缩
     * @param in	输入流
     * @param out	输出流
     * @throws Exception
     */
    public static void compress(InputStream in, OutputStream out) throws Exception {
        GZIPOutputStream gos = new GZIPOutputStream(out);

        byte[] data = new byte[BUFF];
        for (int count = in.read(data, 0, BUFF); count != -1; count = in.read(data, 0, BUFF)) {
            gos.write(data, 0, count);
        }

        gos.finish();
        gos.flush();
        IOUtils.closeQuietly(gos);
    }

    /**********************************************************************************
     * 解压缩
     **********************************************************************************/

    /**
     * 解压缩二进制数据
     * @param data		压缩的二进制数据
     * @return byte[] 	解压缩的二进制数据
     * @throws Exception
     */
    public static byte[] decompress(byte[] data) throws Exception {
        ByteArrayInputStream bais = new ByteArrayInputStream(data);
        ByteArrayOutputStream baos = new ByteArrayOutputStream();

        decompress(bais, baos);

        data = baos.toByteArray();

        baos.flush();
        IOUtils.closeQuietly(baos);
        IOUtils.closeQuietly(bais);
        return data;
    }

    /**
     * 解压缩文件
     * @param file	源文件
     * @throws Exception
     */
    public static void decompress(File file) throws Exception {
        decompress(file, true);
    }

    /**
     * 解压缩文件
     * @param path	源文件路径
     * @throws Exception
     */
    public static void decompress(String path) throws Exception {
        decompress(path, true);
    }

    /**
     * 解压缩文件
     * @param path		源文件路径
     * @param delete	是否删除源文件，解压缩完成
     * @throws Exception
     */
    public static void decompress(String path, boolean delete) throws Exception {
        decompress(new File(path), delete);
    }

    /**
     * 解压缩文件
     * @param file		源文件
     * @param delete	解压缩完成后，是否删除源文件
     * @throws Exception
     */
    public static void decompress(File file, boolean delete) throws Exception {
        FileInputStream in = new FileInputStream(file);
        FileOutputStream out = new FileOutputStream(file.getPath().replace(EXT, ""));

        decompress(in, out);

        out.flush();
        IOUtils.closeQuietly(out);
        IOUtils.closeQuietly(in);

        if (delete) {
            file.delete();
        }
    }

    /**
     * 解压缩文件
     * @param in	输入流
     * @param out	输出流
     * @throws Exception
     */
    public static void decompress(InputStream in, OutputStream out) throws Exception {
        GZIPInputStream gis = new GZIPInputStream(in);

        byte[] data = new byte[BUFF];
        for (int count = gis.read(data, 0, BUFF); count != -1; count = gis.read(data, 0, BUFF)) {
            out.write(data, 0, count);
        }

        IOUtils.closeQuietly(gis);
    }
}
