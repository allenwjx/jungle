package com.zeh.jungle.utils.security;

import java.security.Key;
import java.security.SecureRandom;

import javax.crypto.Cipher;
import javax.crypto.SecretKey;
import javax.crypto.SecretKeyFactory;
import javax.crypto.spec.PBEKeySpec;
import javax.crypto.spec.PBEParameterSpec;

/**
 * @author allen
 * @version $Id: PBEUtils.java, v 0.1 2016年2月29日 下午1:31:14 allen Exp $
 */
public abstract class PBEUtils {
    public static final String ALGORITHM       = "PBEWITHMD5andDES";
    public static final int    ITERATION_COUNT = 100;
    public static final int    SALT_LENGTH     = 8;

    /**
     * 盐初始化
     * 盐长度必须为8字节
     * @return byte[] 盐
     * @throws Exception
     */
    public static byte[] initSalt() throws Exception {
        SecureRandom random = new SecureRandom();
        return random.generateSeed(SALT_LENGTH);
    }

    /**
     * 转换密钥
     * @param password 密码
     * @return Key 迷药
     * @throws Exception
     */
    private static Key toKey(String password) throws Exception {
        PBEKeySpec keySpec = new PBEKeySpec(password.toCharArray());
        SecretKeyFactory keyFactory = SecretKeyFactory.getInstance(ALGORITHM);
        SecretKey secretKey = keyFactory.generateSecret(keySpec);
        return secretKey;
    }

    /**
     * 加密
     * @param data	待加密数据
     * @param password	密码
     * @param salt	盐
     * @return byte[] 加密后的数据
     * @throws Exception
     */
    public static byte[] encrypt(byte[] data, String password, byte[] salt) throws Exception {
        Key key = toKey(password);
        PBEParameterSpec paramSpec = new PBEParameterSpec(salt, ITERATION_COUNT);
        Cipher cipher = Cipher.getInstance(ALGORITHM);
        cipher.init(Cipher.ENCRYPT_MODE, key, paramSpec);
        return cipher.doFinal(data);
    }

    /**
     * 解密
     * @param data	待解密数据
     * @param password	密码
     * @param salt	盐
     * @return byte[] 解密后的数据
     * @throws Exception
     */
    public static byte[] decrypt(byte[] data, String password, byte[] salt) throws Exception {
        Key key = toKey(password);
        PBEParameterSpec paramSpec = new PBEParameterSpec(salt, ITERATION_COUNT);
        Cipher cipher = Cipher.getInstance(ALGORITHM);
        cipher.init(Cipher.DECRYPT_MODE, key, paramSpec);
        return cipher.doFinal(data);

    }
}
