package com.zeh.jungle.utils.security;

import com.zeh.jungle.utils.net.PacketUtils;

import java.security.Key;

import javax.crypto.Cipher;
import javax.crypto.KeyGenerator;
import javax.crypto.SecretKey;
import javax.crypto.spec.SecretKeySpec;

/**
 * AES加解密
 * 
 * @author allen
 * @version $Id: AESUtils.java, v 0.1 2016年2月29日 下午1:31:14 allen Exp $
 */
public abstract class AESUtils {
    public static final String KEY_ALGORITHM    = "AES";
    public static final String CIPHER_ALGORITHM = "AES/ECB/PKCS5Padding";
    public static final int    ALGORITHM_LENGTH = 128;

    /**
     * 转换密钥
     * @param key 二进制密钥
     * @return Key 密钥
     * @throws Exception
     */
    private static Key toKey(byte[] key) throws Exception {
        SecretKey secretKey = new SecretKeySpec(key, KEY_ALGORITHM);
        return secretKey;
    }

    /**
     * 解密
     * @param data 待解密数据
     * @param key 二进制密钥
     * @return byte[] 解密数据
     * @throws Exception
     */
    public static byte[] decrypt(byte[] data, byte[] key) throws Exception {
        Key k = toKey(key);
        Cipher cipher = Cipher.getInstance(CIPHER_ALGORITHM);
        cipher.init(Cipher.DECRYPT_MODE, k);
        return cipher.doFinal(data);
    }

    /**
     * 解密
     * @param data 待加密数据
     * @param keyHex 十六进制密钥
     * @return byte[] 解密数据
     * @throws Exception
     */
    public static byte[] decrypt(byte[] data, String keyHex) throws Exception {
        byte[] byteKey = PacketUtils.hexEncode(keyHex);
        return decrypt(data, byteKey);
    }

    /**
     * 加密
     * @param data 待加密数据
     * @param key 二进制密钥
     * @return byte[] 加密数据
     * @throws Exception
     */
    public static byte[] encrypt(byte[] data, byte[] key) throws Exception {
        Key k = toKey(key);
        Cipher cipher = Cipher.getInstance(CIPHER_ALGORITHM);
        cipher.init(Cipher.ENCRYPT_MODE, k);
        return cipher.doFinal(data);
    }

    /**
     * 加密
     * @param data 待加密数据
     * @param hexKey 十六进制密钥
     * @return byte[] 加密数据
     * @throws Exception
     */
    public static byte[] encrypt(byte[] data, String hexKey) throws Exception {
        byte[] byteKey = PacketUtils.hexEncode(hexKey);
        return encrypt(data, byteKey);
    }

    /**
     * 生成密钥
     * @return byte[] 二进制密钥
     * @throws Exception
     */
    public static byte[] initKey() throws Exception {
        KeyGenerator kg = KeyGenerator.getInstance(KEY_ALGORITHM);
        kg.init(ALGORITHM_LENGTH);
        SecretKey secretKey = kg.generateKey();
        return secretKey.getEncoded();
    }

    /**
     * 生成密钥
     * @return String 十六进制密钥
     * @throws Exception
     */
    public static String initKeyHex() throws Exception {
        byte[] key = initKey();
        return PacketUtils.hexDump(key);
    }
}
