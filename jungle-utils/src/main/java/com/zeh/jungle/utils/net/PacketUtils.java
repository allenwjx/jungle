package com.zeh.jungle.utils.net;

import java.io.ByteArrayInputStream;
import java.io.ByteArrayOutputStream;
import java.io.IOException;
import java.io.ObjectInputStream;
import java.io.ObjectOutputStream;
import java.io.Serializable;

import org.apache.commons.lang3.StringUtils;

/**
 * @author allen
 * @version $Id: PacketUtils.java, v 0.1 2016年2月29日 下午1:31:14 allen Exp $
 */
public class PacketUtils {
    public final static char[] B_TO_A = "0123456789abcdef".toCharArray();

    /**
     * 把16进制字符串转换成字节数组
     * 
     * @param hex
     * @return
     */
    public static byte[] hexEncode(String hex) {
        int len = (hex.length() / 2);
        byte[] result = new byte[len];
        char[] achar = hex.toCharArray();
        for (int i = 0; i < len; i++) {
            int pos = i * 2;
            result[i] = (byte) (toByte(achar[pos]) << 4 | toByte(achar[pos + 1]));
        }
        return result;
    }

    private static byte toByte(char c) {
        byte b = (byte) "0123456789ABCDEF".indexOf(c);
        return b;
    }

    /**
     * 把字节数组转换成16进制字符串
     * 
     * @param bArray
     * @return
     */
    public static final String hexDump(byte[] bArray) {
        StringBuffer sb = new StringBuffer(bArray.length);
        String sTemp;
        for (int i = 0; i < bArray.length; i++) {
            sTemp = Integer.toHexString(0xFF & bArray[i]);
            if (sTemp.length() < 2) {
                sb.append(0);
            }
            sb.append(sTemp.toUpperCase());
        }
        return sb.toString();
    }

    /**
     * 把字节数组转换成16进制字符串, 以提供的分隔符分隔
     * 
     * @param bArray
     * @return
     */
    public static final String hexDump(byte[] bArray, String splitSymbol) {
        StringBuffer sb = new StringBuffer(bArray.length);
        String sTemp;
        for (int i = 0; i < bArray.length; i++) {
            sTemp = Integer.toHexString(0xFF & bArray[i]);
            if (sTemp.length() < 2) {
                sb.append(0);
            }
            sb.append(sTemp.toUpperCase());
            if (i < bArray.length - 1) {
                sb.append(splitSymbol);
            }
        }
        return sb.toString();
    }

    /**
     * 把字节数组转换为对象
     * 
     * @param bytes
     * @return
     * @throws IOException
     * @throws ClassNotFoundException
     */
    public static final Object objectDump(byte[] bytes) throws IOException, ClassNotFoundException {
        ByteArrayInputStream in = new ByteArrayInputStream(bytes);
        ObjectInputStream oi = new ObjectInputStream(in);
        Object o = oi.readObject();
        oi.close();
        return o;
    }

    /**
     * 把可序列化对象转换成字节数组
     * 
     * @param s
     * @return
     * @throws IOException
     */
    public static final byte[] objectEncode(Serializable s) throws IOException {
        ByteArrayOutputStream out = new ByteArrayOutputStream();
        ObjectOutputStream ot = new ObjectOutputStream(out);
        ot.writeObject(s);
        ot.flush();
        ot.close();
        return out.toByteArray();
    }

    /**
     * 把可序列化对象转换为Hex字符串
     * @param s
     * @return
     * @throws IOException
     */
    public static final String hexDump(Serializable s) throws IOException {
        return hexDump(objectEncode(s));
    }

    /**
     * Hex字符串转为可序列化对象
     * @param hex
     * @return
     * @throws IOException
     * @throws ClassNotFoundException
     */
    public static final Object objectEncode(String hex) throws IOException, ClassNotFoundException {
        return objectDump(hexEncode(hex));
    }

    /**
     * BCD码转为10进制串(阿拉伯数据)
     * @param bytes BCD码
     * @return String 10进制字符串
     */
    public static String bcdDump(byte[] bytes) {
        StringBuffer temp = new StringBuffer(bytes.length * 2);

        for (int i = 0; i < bytes.length; i++) {
            temp.append((byte) ((bytes[i] & 0xf0) >>> 4));
            temp.append((byte) (bytes[i] & 0x0f));
        }
        return temp.toString().substring(0, 1).equalsIgnoreCase("0") ? temp.toString().substring(1) : temp.toString();
    }

    /**
     * 10进制字符串转为BCD码
     * @param asc 10进制字符串
     * @return byte[] BCD码
     */
    public static byte[] bcdEncode(String asc) {
        int len = asc.length();
        int mod = len % 2;

        if (mod != 0) {
            asc = "0" + asc;
            len = asc.length();
        }

        byte abt[] = new byte[len];
        if (len >= 2) {
            len = len / 2;
        }

        byte bbt[] = new byte[len];
        abt = asc.getBytes();
        int j, k;

        for (int p = 0; p < asc.length() / 2; p++) {
            if ((abt[2 * p] >= '0') && (abt[2 * p] <= '9')) {
                j = abt[2 * p] - '0';
            } else if ((abt[2 * p] >= 'a') && (abt[2 * p] <= 'z')) {
                j = abt[2 * p] - 'a' + 0x0a;
            } else {
                j = abt[2 * p] - 'A' + 0x0a;
            }

            if ((abt[2 * p + 1] >= '0') && (abt[2 * p + 1] <= '9')) {
                k = abt[2 * p + 1] - '0';
            } else if ((abt[2 * p + 1] >= 'a') && (abt[2 * p + 1] <= 'z')) {
                k = abt[2 * p + 1] - 'a' + 0x0a;
            } else {
                k = abt[2 * p + 1] - 'A' + 0x0a;
            }

            int a = (j << 4) + k;
            byte b = (byte) a;
            bbt[p] = b;
        }
        return bbt;
    }

    /**
     * BCD码转ASC码
     * @param bytes BCD码字节数组
     * @return ASC码字符串
     */
    public static String bcd2ASC(byte[] bytes) {
        StringBuffer temp = new StringBuffer(bytes.length * 2);

        for (int i = 0; i < bytes.length; i++) {
            int h = ((bytes[i] & 0xf0) >>> 4);
            int l = (bytes[i] & 0x0f);
            temp.append(B_TO_A[h]).append(B_TO_A[l]);
        }
        return temp.toString();
    }

    /**
     * 将十六进制数字转换为1十进制数字字符
     * e.g. 0x00, 0x01, 0x02, 0x03, 0x04, 0x05, 0x06, 0x07, 0x08, 0x09
     * 转为 '0', '1', '2', '3', '4', '5', '6', '7', '8', '9'
     * @param ch
     * @return
     * @throws SCException
     */
    private static byte hexNum2DecNumchar(byte ch) {
        if (ch <= '9') {
            return (byte) ('0' + ch);
        }
        return '0';
    }

    /**
     * 将十进制数字字符转换为十六进制数字
     * e.g. '0', '1', '2', '3', '4', '5', '6', '7', '8', '9'
     * 转为 0x00, 0x01, 0x02, 0x03, 0x04, 0x05, 0x06, 0x07, 0x08, 0x09
     * @param ch
     * @return
     * @throws SCException
     */
    private static byte decNumchar2HexNum(byte ch) {
        if (ch >= '0' && ch <= '9') {
            return (byte) (ch - '0');
        }
        return 0;
    }

    /**
     * 将十六进制数字数组转为数字字符串
     * e.g. {0x00, 0x01, 0x02, 0x03, 0x04, 0x05, 0x06, 0x07, 0x08, 0x09}
     * 转为 "0123456789"字符串
     * @param bArray
     * @return
     * @throws SCException
     */
    public static String hexNums2DecNumString(byte[] bArray) {
        byte[] bytes = new byte[bArray.length];
        for (int i = 0; i < bArray.length; i++) {
            bytes[i] = hexNum2DecNumchar(bArray[i]);
        }
        return new String(bytes);
    }

    /**
     * 将十进制数字字符串转为十六进制数字byte数组
     * e.g. "0123456789"字符串
     * 转为 {0x00, 0x01, 0x02, 0x03, 0x04, 0x05, 0x06, 0x07, 0x08, 0x09}数组
     * @param str
     * @return
     * @throws SCException
     */
    public static byte[] decNumString2HexNums(String str) {
        if (StringUtils.isEmpty(str)) {
            return null;
        }
        byte[] bytes = new byte[str.length()];
        for (int i = 0; i < str.length(); i++) {
            bytes[i] = decNumchar2HexNum(str.getBytes()[i]);
        }
        return bytes;
    }

    /**
     * 将十六进制数字转为Ascii码
     * e.g. 0x00, 0x01, 0x02, 0x03, 0x04, 0x05, 0x06, 0x07, 0x08, 0x09, 0x0A, 0x0B, 0x0C, 0x0D, 0x0E, 0x0F 
     * 转为 '0', '1', '2', '3', '4', '5', '6', '7', '8', '9', 'A', 'B', 'C', 'D', 'E', 'F'
     * @param ch
     * @return
     */
    private static byte hex2ASCIIChar(byte ch) {
        if (ch <= 9) {
            return (byte) ('0' + ch); // handle decimal values     
        }
        if (ch <= 0xf) {
            return (byte) ('A' + ch - 10); // handle hexidecimal specific values     
        }
        return ('X');
    }

    /**
     * 将Ascii转为十六进制数字
     * e.g. '0', '1', '2', '3', '4', '5', '6', '7', '8', '9', 'A', 'B', 'C', 'D', 'E', 'F'
     * 转为 0x00, 0x01, 0x02, 0x03, 0x04, 0x05, 0x06, 0x07, 0x08, 0x09, 0x0A, 0x0B, 0x0C, 0x0D, 0x0E, 0x0F 
     * @param ch
     * @return
     */
    private static byte asciiChar2Hex(byte ch) {
        if (ch >= '0' && ch <= '9') {
            return (byte) (ch - '0'); // Handle numerals     
        }
        if (ch >= 'A' && ch <= 'F') {
            return (byte) (ch - 'A' + 0xA); // Handle capitol hex digits     
        }
        if (ch >= 'a' && ch <= 'f') {
            return (byte) (ch - 'a' + 0xA); // Handle small hex digits     
        }
        return (byte) (255);
    }

    /**
     * 将16进制数字转为ASCII码
     * e.g. {0x00,0x01,0x02,0x03,0x04,0x05,0x06,0x07,0x08,0x09,0x0a,0x0b,0x0c,0x0d,0x0e,0x0f}
     * 转为 "0123456789ABCDEF"
     * @param bArray
     * @return
     */
    public static String hex2ASCIIString(byte[] bArray) {
        byte[] bytes = new byte[bArray.length];
        for (int i = 0; i < bArray.length; i++) {
            bytes[i] = hex2ASCIIChar(bArray[i]);
        }
        return new String(bytes);
    }

    /**
     * 将ASCII码转为16进制数字
     * e.g. "0123456789ABCDEF"
     * 转为 {0x00,0x01,0x02,0x03,0x04,0x05,0x06,0x07,0x08,0x09,0x0a,0x0b,0x0c,0x0d,0x0e,0x0f}
     * @param str
     * @return
     */
    public static byte[] asciiString2Hex(String str) {
        if (StringUtils.isEmpty(str)) {
            return null;
        }
        byte[] bytes = new byte[str.length()];
        for (int i = 0; i < str.length(); i++) {
            bytes[i] = asciiChar2Hex(str.getBytes()[i]);
        }
        return bytes;
    }

    /**
     * Convert byte to ascii
     * e.g. 0x00,0x01,0x02,0x03,0x04,0x05,0x06,0x07,0x08,0x09 -> '0', '1', '2', '3', '4', '5', '6', '7', '8', '9'
     * others keeps the same
     * @param ch
     * @return
     */
    private static byte byte2ASCII(byte ch) {
        if (ch <= 9) {
            return (byte) ('0' + ch); // handle decimal values
        }
        return ch;
    }

    /**
     * Convert ascii to byte
     * e.g. '0', '1', '2', '3', '4', '5', '6', '7', '8', '9' -> 0x00,0x01,0x02,0x03,0x04,0x05,0x06,0x07,0x08,0x09
     * others keeps the same
     * @param ch
     * @return
     */
    private static byte ascii2Byte(byte ch) {
        if (ch >= '0' && ch <= '9') {
            return (byte) (ch - '0'); // Handle numerals
        }
        return ch;
    }

    public static String bytes2ASCIIString(byte[] bArray) {
        byte[] bytes = new byte[bArray.length];
        for (int i = 0; i < bArray.length; i++) {
            bytes[i] = byte2ASCII(bArray[i]);
        }
        return new String(bytes);
    }

    public static byte[] asciiString2Bytes(String str) {
        if (StringUtils.isEmpty(str)) {
            return null;
        }
        byte[] bytes = new byte[str.length()];
        for (int i = 0; i < str.length(); i++) {
            bytes[i] = ascii2Byte(str.getBytes()[i]);
        }
        return bytes;
    }

    /**
     * 获取c/c++ 字符串结束符号
     * @return
     */
    public static String getCppStringTail() {
        byte[] data = new byte[1];
        data[0] = 0x00;
        return new String(data);
    }

}
