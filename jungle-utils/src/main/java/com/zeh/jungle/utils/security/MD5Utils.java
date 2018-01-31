package com.zeh.jungle.utils.security;

import java.io.UnsupportedEncodingException;
import java.security.MessageDigest;
import java.security.NoSuchAlgorithmException;

import com.zeh.jungle.utils.net.PacketUtils;
import org.apache.commons.lang3.StringUtils;

/**
 * @author allen
 * @version $Id: MD5Utils.java, v 0.1 2016年2月29日 下午1:31:14 allen Exp $
 */
public class MD5Utils {

    /**
     * 使用MD5加密
     * 
     * @param signature
     *            待加密字符串
     * @return
     * @throws Exception
     */
    public static String encrypt(String signature) throws RuntimeException {
        return encrypt(signature, "", true);
    }

    /**
     * 使用MD5加密 - 带编码，用于中文加密
     * 
     * @param signature
     * @param encode
     * @return
     * @throws RuntimeException
     */
    public static String encrypt(String signature, String encode) throws RuntimeException {
        return encrypt(signature, encode, true);
    }

    /**
     * 使用MD5加密
     * 
     * @param signature
     *            待加密字符串
     * @param encode
     *            字符编码(用于中文加密)
     * @param isUpperCase
     *            返回加密结果是否转为大写
     * @return
     * @throws RuntimeException
     */
    public static String encrypt(String signature, String encode, boolean isUpperCase) throws RuntimeException {
        if (signature == null) {
            return null;
        }
        MessageDigest md5 = null;
        try {
            md5 = MessageDigest.getInstance("MD5");
        } catch (NoSuchAlgorithmException e) {
            throw new RuntimeException("加密算法错误", e);
        }
        try {
            byte[] plainText;
            if (StringUtils.isBlank(encode)) {
                plainText = signature.getBytes();
            } else {
                plainText = signature.getBytes(encode);
            }
            md5.update(plainText);
            return PacketUtils.hexDump(md5.digest());
        } catch (UnsupportedEncodingException e) {
            throw new RuntimeException("MD5加密异常", e);
        }
    }

}
