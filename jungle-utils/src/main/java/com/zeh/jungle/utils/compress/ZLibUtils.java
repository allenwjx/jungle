package com.zeh.jungle.utils.compress;

import java.io.ByteArrayOutputStream;
import java.io.InputStream;
import java.io.OutputStream;
import java.util.zip.Deflater;
import java.util.zip.DeflaterOutputStream;
import java.util.zip.Inflater;
import java.util.zip.InflaterInputStream;

import org.apache.commons.io.IOUtils;

/**
 * @author allen
 * @version $Id: ZLibUtils.java, v 0.1 2016年2月29日 下午1:31:14 allen Exp $
 */
public abstract class ZLibUtils {
    private static int BUFF_SIZE = 1024;

    /**
     * 压缩二进制数据
     * @param data	待压缩数据
     * @return byte[] 压缩数据
     * @throws Exception
     */
    public static byte[] compress(byte[] data) throws Exception {
        Deflater compressor = new Deflater();
        compressor.reset();
        compressor.setInput(data);
        compressor.finish();

        ByteArrayOutputStream out = new ByteArrayOutputStream(BUFF_SIZE);
        byte[] buff = new byte[BUFF_SIZE];
        while (!compressor.finished()) {
            out.write(buff, 0, compressor.deflate(buff));
        }

        byte[] compressedData = out.toByteArray();
        compressor.end();
        IOUtils.closeQuietly(out);
        return compressedData;
    }

    /**
     * 压缩二进制数据
     * @param data	待压缩的数据
     * @param out	输出流
     * @throws Exception
     */
    public static void compress(byte[] data, OutputStream out) throws Exception {
        DeflaterOutputStream dos = new DeflaterOutputStream(out);
        dos.write(data, 0, data.length);
        dos.finish();
        dos.flush();
        IOUtils.closeQuietly(dos);
    }

    /**
     * 解压缩二进制数据
     * @param compressedData	压缩数据
     * @return byte[]			解压数据
     * @throws Exception
     */
    public static byte[] decompress(byte[] compressedData) throws Exception {
        Inflater decompressor = new Inflater();
        decompressor.reset();
        decompressor.setInput(compressedData);

        ByteArrayOutputStream out = new ByteArrayOutputStream(BUFF_SIZE);
        byte[] buff = new byte[BUFF_SIZE];
        while (!decompressor.finished()) {
            out.write(buff, 0, decompressor.inflate(buff));
        }

        byte[] data = out.toByteArray();
        decompressor.end();
        IOUtils.closeQuietly(out);
        return data;
    }

    /**
     * 解压缩二进制数据
     * @param in	输入流
     * @return byte[] 解压数据
     * @throws Exception
     */
    public static byte[] decompress(InputStream in) throws Exception {
        InflaterInputStream iis = new InflaterInputStream(in);
        ByteArrayOutputStream out = new ByteArrayOutputStream(BUFF_SIZE);
        byte[] buff = new byte[BUFF_SIZE];
        for (int i = iis.read(buff); i > 0; i = iis.read(buff)) {
            out.write(buff, 0, i);
        }
        byte[] data = out.toByteArray();
        IOUtils.closeQuietly(out);
        IOUtils.closeQuietly(iis);
        return data;
    }
}
