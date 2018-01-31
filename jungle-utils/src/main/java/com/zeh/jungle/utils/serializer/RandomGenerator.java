package com.zeh.jungle.utils.serializer;

import java.security.SecureRandom;
import java.util.Random;

/**
 * 随机值生成器
 * 
 * @author allen
 * @version $Id: RandomGenerator.java, v 0.1 2016年2月26日 下午2:18:13 allen Exp $
 */
public class RandomGenerator {

    /***/
    private static final char[] DEFAULT_CODEC = "1234567890ABCDEFGHIJKLMNOPQRSTUVWXYZabcdefghijklmnopqrstuvwxyz".toCharArray();

    /** Random */
    private Random              random        = new SecureRandom();

    /** Length of Random*/
    private int                 length;

    /**
     * 构造方法
     */
    public RandomGenerator() {
        this(6);
    }

    /**
     * 构造方法
     * @param length
     */
    public RandomGenerator(int length) {
        this.length = length;
    }

    /**
     * 生成随机字符串
     * 
     * @return
     */
    public String generate() {
        byte[] verifierBytes = new byte[length];
        random.nextBytes(verifierBytes);
        return getAuthorizationCodeString(verifierBytes);
    }

    /**
     * 生成授权码
     * 
     * @param verifierBytes
     * @return
     */
    protected String getAuthorizationCodeString(byte[] verifierBytes) {
        char[] chars = new char[verifierBytes.length];
        for (int i = 0; i < verifierBytes.length; i++) {
            chars[i] = DEFAULT_CODEC[((verifierBytes[i] & 0xFF) % DEFAULT_CODEC.length)];
        }
        return new String(chars);
    }

    /**
     * Setter for Random
     * @param random
     */
    public void setRandom(Random random) {
        this.random = random;
    }

    /**
     * Setter for length
     * 
     * @param length
     */
    public void setLength(int length) {
        this.length = length;
    }
}
