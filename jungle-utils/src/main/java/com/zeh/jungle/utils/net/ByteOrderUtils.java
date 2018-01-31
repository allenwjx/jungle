package com.zeh.jungle.utils.net;

/**
 * @author allen
 * @version $Id: ByteOrderUtils.java, v 0.1 2016年2月29日 下午1:31:14 allen Exp $
 */
public class ByteOrderUtils {

    /**
     * 将字节数组转换为String
     * @param b byte[]
     * @return string
     */
    public static String bytesToString(byte[] b) {
        StringBuffer result = new StringBuffer("");
        int length = b.length;
        for (int i = 0; i < length; i++) {
            result.append((char) (b[i] & 0xff));
        }
        return result.toString();
    }

    /**
     * 高字节数组转换为float, 即数组中的字节顺序是由字节在内存中由高到低的顺序排列,
     * @param b byte[]
     * @return float
     */
    public static float highBytesToFloat(byte[] b) {
        int i = ((((b[0] & 0xff) << 8 | (b[1] & 0xff)) << 8) | (b[2] & 0xff)) << 8 | (b[3] & 0xff);
        return Float.intBitsToFloat(i);
    }

    /**
     * 低字节数组转换为float, 即数组中的字节顺序是由字节在内存中由低到高的顺序排列
     * @param b byte[]
     * @return float
     */
    public static float lowBytesToFloat(byte[] b) {
        int i = 0;
        i = ((((b[3] & 0xff) << 8 | (b[2] & 0xff)) << 8) | (b[1] & 0xff)) << 8 | (b[0] & 0xff);
        return Float.intBitsToFloat(i);
    }

    /**
     * 将byte数组中的元素倒序排列
     * @param b byte
     * @return byte[]
     */
    public static byte[] bytesReverseOrder(byte[] b) {
        int length = b.length;
        byte[] result = new byte[length];
        for (int i = 0; i < length; i++) {
            result[length - i - 1] = b[i];
        }
        return result;
    }

    /**
     * 将Java short类型转为byte数组，字节顺序由<code>isHL</code>指定
     * @param s 待转换short数据
     * @param isHL 各字节进入数组顺序，是否是高字节在前，低字节在后. <code>true</code>为高字节在前, <code>false</code>为低字节在前
     * @return byte[]
     */
    public final static byte[] getBytes(short s, boolean isHL) {
        byte[] buf = new byte[2];
        if (isHL) {
            for (int i = buf.length - 1; i >= 0; i--) {
                buf[i] = (byte) (s & 0x00ff);
                s >>= 8;
            }
        } else {
            for (int i = 0; i < buf.length; i++) {
                buf[i] = (byte) (s & 0x00ff);
                s >>= 8;
            }
        }
        return buf;
    }

    /**
     * 将Java int类型转为byte数组，字节顺序由<code>isHL</code>指定
     * @param s 待转换short数据
     * @param isHL 各字节进入数组顺序，是否是高字节在前，低字节在后. <code>true</code>为高字节在前, <code>false</code>为低字节在前
     * @return byte[]
     */
    public final static byte[] getBytes(int s, boolean isHL) {
        byte[] buf = new byte[4];
        if (isHL) {
            // 高字节在前，低字节在后
            for (int i = buf.length - 1; i >= 0; i--) {
                buf[i] = (byte) (s & 0x000000ff);
                s >>= 8;
            }
        } else {
            // 低字节在前，高字节在后
            for (int i = 0; i < buf.length; i++) {
                buf[i] = (byte) (s & 0x000000ff);
                s >>= 8;
            }
        }
        return buf;
    }

    /**
     * 将Java long类型转为byte数组，字节顺序由<code>isHL</code>指定
     * @param s 待转换short数据
     * @param isHL 各字节进入数组顺序，是否是高字节在前，低字节在后. <code>true</code>为高字节在前, <code>false</code>为低字节在前
     * @return byte[]
     */
    public final static byte[] getBytes(long s, boolean isHL) {
        byte[] buf = new byte[8];
        if (isHL) {
            for (int i = buf.length - 1; i >= 0; i--) {
                buf[i] = (byte) (s & 0x00000000000000ff);
                s >>= 8;
            }
        } else {
            for (int i = 0; i < buf.length; i++) {
                buf[i] = (byte) (s & 0x00000000000000ff);
                s >>= 8;
            }
        }
        return buf;
    }

    /**
     * 将byte数组转为Java short类型, 字节顺序由<code>isHL</code>指定
     * @param buf 待转换byte数组
     * @param isHL 数组中的字节顺序是否是高字节在前，低字节在后. <code>true</code>为高字节在前, <code>false</code>为低字节在前
     * @return short
     */
    public final static short getShort(byte[] buf, boolean isHL) {
        if (buf == null) {
            throw new IllegalArgumentException("byte array is null!");
        }
        if (buf.length > 2) {
            throw new IllegalArgumentException("byte array size > 2 !");
        }

        short r = 0;
        if (!isHL) {
            for (int i = buf.length - 1; i >= 0; i--) {
                r <<= 8;
                r |= (buf[i] & 0x00ff);
            }
        } else {
            for (int i = 0; i < buf.length; i++) {
                r <<= 8;
                r |= (buf[i] & 0x00ff);
            }
        }
        return r;
    }

    /**
     * 将byte数组转为Java int类型, 字节顺序由<code>isHL</code>指定
     * @param buf 待转换byte数组
     * @param isHL 数组中的字节顺序是否是高字节在前，低字节在后. <code>true</code>为高字节在前, <code>false</code>为低字节在前
     * @return int
     */
    public final static int getInt(byte[] buf, boolean isHL) {
        if (buf == null) {
            throw new IllegalArgumentException("byte array is null!");
        }
        if (buf.length > 4) {
            throw new IllegalArgumentException("byte array size > 4 !");
        }
        int r = 0;
        if (!isHL) {
            for (int i = buf.length - 1; i >= 0; i--) {
                r <<= 8;
                r |= (buf[i] & 0x000000ff);
            }
        } else {
            for (int i = 0; i < buf.length; i++) {
                r <<= 8;
                r |= (buf[i] & 0x000000ff);
            }
        }
        return r;
    }

    /**
     * 将byte数组转为Java long类型, 字节顺序由<code>isHL</code>指定
     * @param buf 待转换byte数组
     * @param isHL 数组中的字节顺序是否是高字节在前，低字节在后. <code>true</code>为高字节在前, <code>false</code>为低字节在前
     * @return long
     */
    public final static long getLong(byte[] buf, boolean isHL) {
        if (buf == null) {
            throw new IllegalArgumentException("byte array is null!");
        }
        if (buf.length > 8) {
            throw new IllegalArgumentException("byte array size > 8 !");
        }
        long r = 0;
        if (!isHL) {
            for (int i = buf.length - 1; i >= 0; i--) {
                r <<= 8;
                r |= (buf[i] & 0x00000000000000ff);
            }
        } else {
            for (int i = 0; i < buf.length; i++) {
                r <<= 8;
                r |= (buf[i] & 0x00000000000000ff);
            }
        }
        return r;
    }
}
