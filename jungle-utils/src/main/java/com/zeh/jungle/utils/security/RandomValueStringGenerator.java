package com.zeh.jungle.utils.security;

import java.security.SecureRandom;
import java.util.Random;

/**
 * 
 * @author allen
 * @version $Id: RandomValueStringGenerator.java, v 0.1 2016年4月22日 下午3:29:50 allen Exp $
 */
public class RandomValueStringGenerator {

    private static final char[] DEFAULT_CODEC = "1234567890ABCDEFGHIJKLMNOPQRSTUVWXYZ".toCharArray();

    private Random              random        = new SecureRandom();

    private int                 length;

    public RandomValueStringGenerator() {
        this(8);
    }

    public RandomValueStringGenerator(int length) {
        this.length = length;
    }

    public String generate() {
        byte[] verifierBytes = new byte[length];
        random.nextBytes(verifierBytes);
        return getCodeString(verifierBytes);
    }

    protected String getCodeString(byte[] verifierBytes) {
        char[] chars = new char[verifierBytes.length];
        for (int i = 0; i < verifierBytes.length; i++) {
            chars[i] = DEFAULT_CODEC[((verifierBytes[i] & 0xFF) % DEFAULT_CODEC.length)];
        }
        return new String(chars);
    }

    public void setRandom(Random random) {
        this.random = random;
    }

    public void setLength(int length) {
        this.length = length;
    }
}
