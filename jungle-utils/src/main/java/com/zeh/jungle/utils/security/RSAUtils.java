package com.zeh.jungle.utils.security;

import com.zeh.jungle.utils.common.FastBase64;
import com.zeh.jungle.utils.net.PacketUtils;

import java.security.*;
import java.security.interfaces.RSAPrivateKey;
import java.security.interfaces.RSAPublicKey;
import java.security.spec.PKCS8EncodedKeySpec;
import java.security.spec.X509EncodedKeySpec;
import java.util.HashMap;
import java.util.Map;

import javax.crypto.Cipher;

/**
 * @author allen
 * @version $Id: RSAUtils.java, v 0.1 2016年2月29日 下午1:31:14 allen Exp $
 */
public abstract class RSAUtils {
    /**
     * 非对称加密密钥算法
     */
    public static final String  KEY_ALGORITHM       = "RSA";

    /**
     * 签名算法
     */
    public static final String  SIGNATURE_ALGORITHM = "SHA1withRSA";

    /**
     * 公钥
     */
    private static final String PUBLIC_KEY          = "RSAPublicKey";

    /**
     * 私钥
     */
    private static final String PRIVATE_KEY         = "RSAPrivateKey";

    /**
     * RSA密钥长度,密钥长度是64的整数倍数,范围在512~65536之间
     * 默认1024位
     */
    private static final int    KEY_SIZE            = 512;

    /**********************************************************************************
     * 私钥解密
     **********************************************************************************/

    /**
     * 私钥解密
     * @param data	待解密数据
     * @param key	私钥
     * @return byte[] 解密后的数据
     * @throws Exception
     */
    public static byte[] decryptByPrivateKey(byte[] data, byte[] key) throws Exception {
        // 获取私钥
        PKCS8EncodedKeySpec pkcs8KeySpec = new PKCS8EncodedKeySpec(key);
        KeyFactory keyFactory = KeyFactory.getInstance(KEY_ALGORITHM);

        // 生成私钥
        PrivateKey privateKey = keyFactory.generatePrivate(pkcs8KeySpec);

        // 使用私钥对数据解密
        Cipher cipher = Cipher.getInstance(keyFactory.getAlgorithm());
        cipher.init(Cipher.DECRYPT_MODE, privateKey);
        return cipher.doFinal(data);
    }

    /**
     * 私钥解密
     * @param data	待解密数据
     * @param hexKey	十六进制私钥
     * @return byte[] 解密后的数据
     * @throws Exception
     */
    public static byte[] decryptByPrivateKeyHex(byte[] data, String hexKey) throws Exception {
        byte[] byteKey = PacketUtils.hexEncode(hexKey);
        return decryptByPrivateKey(data, byteKey);
    }

    /**
     * 私钥解密
     * @param data	待解密数据
     * @param base64Key	Base64编码私钥
     * @return byte[] 解密后的数据
     * @throws Exception
     */
    public static byte[] decryptByPrivateKeyBase64(byte[] data, String base64Key) throws Exception {
        byte[] byteKey = FastBase64.decode(base64Key);
        return decryptByPrivateKey(data, byteKey);
    }

    /**********************************************************************************
     * 公钥解密
     **********************************************************************************/

    /**
     * 公钥解密
     * @param data	待解密的数据
     * @param key	公钥
     * @return byte[] 解密后的数据
     * @throws Exception
     */
    public static byte[] decryptByPublicKey(byte[] data, byte[] key) throws Exception {
        // 获取公钥
        X509EncodedKeySpec x509KeySpec = new X509EncodedKeySpec(key);
        KeyFactory keyFactory = KeyFactory.getInstance(KEY_ALGORITHM);

        // 生成公钥
        PublicKey publicKey = keyFactory.generatePublic(x509KeySpec);

        // 使用公钥对数据解密
        Cipher cipher = Cipher.getInstance(keyFactory.getAlgorithm());
        cipher.init(Cipher.DECRYPT_MODE, publicKey);
        return cipher.doFinal(data);
    }

    /**
     * 公钥解密
     * @param data	待解密数据
     * @param hexKey	十六进制公钥
     * @return byte[] 解密后的数据
     * @throws Exception
     */
    public static byte[] decryptByPublicKeyHex(byte[] data, String hexKey) throws Exception {
        byte[] byteKey = PacketUtils.hexEncode(hexKey);
        return decryptByPublicKey(data, byteKey);
    }

    /**
     * 公钥解密
     * @param data	待解密数据
     * @param base64Key	Base64编码公钥
     * @return byte[] 解密后的数据
     * @throws Exception
     */
    public static byte[] decryptByPublicKeyBase64(byte[] data, String base64Key) throws Exception {
        byte[] byteKey = FastBase64.decode(base64Key);
        return decryptByPublicKey(data, byteKey);
    }

    /**********************************************************************************
     * 公钥加密
     **********************************************************************************/

    /**
     * 公钥加密
     * @param data	待加密的数据
     * @param key	公钥
     * @return byte[] 加密后的数据
     * @throws Exception
     */
    public static byte[] encryptByPublicKey(byte[] data, byte[] key) throws Exception {
        // 获取公钥
        X509EncodedKeySpec x509KeySpec = new X509EncodedKeySpec(key);
        KeyFactory keyFactory = KeyFactory.getInstance(KEY_ALGORITHM);

        // 生成公钥
        PublicKey publicKey = keyFactory.generatePublic(x509KeySpec);

        // 使用公钥加密数据
        Cipher cipher = Cipher.getInstance(keyFactory.getAlgorithm());
        cipher.init(Cipher.ENCRYPT_MODE, publicKey);
        return cipher.doFinal(data);
    }

    /**
     * 公钥加密
     * @param data	待加密的数据
     * @param hexKey	十六进制公钥
     * @return byte[] 加密后的数据
     * @throws Exception
     */
    public static byte[] encryptByPublicKeyHex(byte[] data, String hexKey) throws Exception {
        byte[] byteKey = PacketUtils.hexEncode(hexKey);
        return encryptByPublicKey(data, byteKey);
    }

    /**
     * 公钥加密
     * @param data	待加密的数据
     * @param base64Key	base64编码公钥
     * @return byte[] 加密后的数据
     * @throws Exception
     */
    public static byte[] encryptByPublicKeyBase64(byte[] data, String base64Key) throws Exception {
        byte[] byteKey = FastBase64.decode(base64Key);
        return encryptByPublicKey(data, byteKey);
    }

    /**********************************************************************************
     * 私钥加密
     **********************************************************************************/

    /**
     * 私钥加密
     * @param data	待加密的数据
     * @param key	私钥
     * @return byte[] 加密后的数据
     * @throws Exception
     */
    public static byte[] encryptByPrivateKey(byte[] data, byte[] key) throws Exception {
        // 获取私钥
        PKCS8EncodedKeySpec pkcs8KeySpec = new PKCS8EncodedKeySpec(key);
        KeyFactory keyFactory = KeyFactory.getInstance(KEY_ALGORITHM);

        // 生成私钥
        PrivateKey privateKey = keyFactory.generatePrivate(pkcs8KeySpec);

        // 使用私钥对数据加密
        Cipher cipher = Cipher.getInstance(keyFactory.getAlgorithm());
        cipher.init(Cipher.ENCRYPT_MODE, privateKey);
        return cipher.doFinal(data);
    }

    /**
     * 私钥加密
     * @param data	待加密的数据
     * @param hexKey	十六进制私钥
     * @return byte[] 加密后的数据
     * @throws Exception
     */
    public static byte[] encryptByPrivateKeyHex(byte[] data, String hexKey) throws Exception {
        byte[] byteKey = PacketUtils.hexEncode(hexKey);
        return encryptByPrivateKey(data, byteKey);
    }

    /**
     * 私钥加密
     * @param data	待加密的数据
     * @param base64Key	base64编码私钥
     * @return byte[] 加密后的数据
     * @throws Exception
     */
    public static byte[] encryptByPrivateKeyBase64(byte[] data, String base64Key) throws Exception {
        byte[] byteKey = FastBase64.decode(base64Key);
        return encryptByPrivateKey(data, byteKey);
    }

    /**********************************************************************************
     * 数字签名，私钥签名，公钥验证
     **********************************************************************************/

    /**
     * RSA数字签名
     * @param data	待签名数据
     * @param privateKey	签名私钥
     * @return byte[] 数字签名
     * @throws Exception
     */
    public static byte[] sign(byte[] data, byte[] privateKey) throws Exception {
        PKCS8EncodedKeySpec pkcs8KeySpec = new PKCS8EncodedKeySpec(privateKey);
        KeyFactory keyFactory = KeyFactory.getInstance(KEY_ALGORITHM);
        PrivateKey priKey = keyFactory.generatePrivate(pkcs8KeySpec);
        Signature signature = Signature.getInstance(SIGNATURE_ALGORITHM);
        signature.initSign(priKey);
        signature.update(data);
        return signature.sign();
    }

    /**
     * RSA数字签名
     * @param data	待签名数据
     * @param hexKey	十六进制签名私钥
     * @return byte[] 数字签名
     * @throws Exception
     */
    public static byte[] signHex(byte[] data, String hexKey) throws Exception {
        byte[] byteKey = PacketUtils.hexEncode(hexKey);
        return sign(data, byteKey);
    }

    /**
     * RSA数字签名
     * @param data	待签名数据
     * @param base64Key	base64编码签名私钥
     * @return byte[] 数字签名
     * @throws Exception
     */
    public static byte[] signBase64(byte[] data, String base64Key) throws Exception {
        byte[] byteKey = FastBase64.decode(base64Key);
        return sign(data, byteKey);
    }

    /**
     * RSA数字签名验证
     * @param data	待校验数据
     * @param publicKey	二进制公钥
     * @param sign	数字签名
     * @return boolean 数字签名校验结果
     * @throws Exception
     */
    public static boolean verify(byte[] data, byte[] publicKey, byte[] sign) throws Exception {
        X509EncodedKeySpec x509KeySpec = new X509EncodedKeySpec(publicKey);
        KeyFactory keyFactory = KeyFactory.getInstance(KEY_ALGORITHM);
        PublicKey pubKey = keyFactory.generatePublic(x509KeySpec);
        Signature signature = Signature.getInstance(SIGNATURE_ALGORITHM);
        signature.initVerify(pubKey);
        signature.update(data);
        return signature.verify(sign);
    }

    /**
     * RSA数字签名验证
     * @param data	待校验数据
     * @param hexKey	十六进制公钥
     * @param sign	数字签名
     * @return boolean 数字签名校验结果
     * @throws Exception
     */
    public static boolean verifyHex(byte[] data, String hexKey, byte[] sign) throws Exception {
        byte[] byteKey = PacketUtils.hexEncode(hexKey);
        return verify(data, byteKey, sign);
    }

    /**
     * RSA数字签名验证
     * @param data	待校验数据
     * @param base64Key	base64编码公钥
     * @param sign	数字签名
     * @return boolean 数字签名校验结果
     * @throws Exception
     */
    public static boolean verifyBase64(byte[] data, String base64Key, byte[] sign) throws Exception {
        byte[] byteKey = FastBase64.decode(base64Key);
        return verify(data, byteKey, sign);
    }

    /**
     * 取得二进制私钥
     * @param keyMap	密钥Map
     * @return	byte[] 私钥二进制数据
     * @throws Exception
     */
    public static byte[] getPrivateKey(Map<String, Key> keyMap) throws Exception {
        Key key = keyMap.get(PRIVATE_KEY);
        return key.getEncoded();
    }

    /**
     * 取得十六进制私钥
     * @param keyMap 密钥Map
     * @return String 十六进制私钥
     * @throws Exception
     */
    public static String getPrivateKeyHex(Map<String, Key> keyMap) throws Exception {
        byte[] key = getPrivateKey(keyMap);
        return PacketUtils.hexDump(key);
    }

    /**
     * 取得Base64编码的私钥,base64编码每76个字符不添加换行符 (不遵循RFC 822规定)
     * @param keyMap
     * @return String Base64编码的私钥
     * @throws Exception
     */
    public static String getPrivateKeyBase64(Map<String, Key> keyMap) throws Exception {
        byte[] key = getPrivateKey(keyMap);
        return FastBase64.encodeToString(key, false);
    }

    /**
     * 取得公钥
     * @param keyMap 密钥Map
     * @return byte[] 公钥二进制数据
     * @throws Exception
     */
    public static byte[] getPublicKey(Map<String, Key> keyMap) throws Exception {
        Key key = keyMap.get(PUBLIC_KEY);
        return key.getEncoded();
    }

    /**
     * 取得十六进制公钥
     * @param keyMap	密钥Map
     * @return String	十六进制公钥
     * @throws Exception
     */
    public static String getPublicKeyHex(Map<String, Key> keyMap) throws Exception {
        byte[] key = getPublicKey(keyMap);
        return PacketUtils.hexDump(key);
    }

    /**
     * 取得base64编码公钥
     * @param keyMap	密钥Map
     * @return String	base64编码公钥
     * @throws Exception
     */
    public static String getPublicKeyBase64(Map<String, Key> keyMap) throws Exception {
        byte[] key = getPublicKey(keyMap);
        return FastBase64.encodeToString(key, false);
    }

    /**
     * 初始化密钥
     * @return Map 密钥Map
     * @throws Exception
     */
    public static Map<String, Key> initKey() throws Exception {
        KeyPairGenerator keyPairGen = KeyPairGenerator.getInstance(KEY_ALGORITHM);
        keyPairGen.initialize(KEY_SIZE);

        // 生成密钥对
        KeyPair keyPair = keyPairGen.generateKeyPair();
        // 获取公钥
        RSAPublicKey publicKey = (RSAPublicKey) keyPair.getPublic();
        // 获取私钥
        RSAPrivateKey privateKey = (RSAPrivateKey) keyPair.getPrivate();

        // 返回密钥Map
        Map<String, Key> keyMap = new HashMap<>(2);
        keyMap.put(PUBLIC_KEY, publicKey);
        keyMap.put(PRIVATE_KEY, privateKey);
        return keyMap;
    }
}
