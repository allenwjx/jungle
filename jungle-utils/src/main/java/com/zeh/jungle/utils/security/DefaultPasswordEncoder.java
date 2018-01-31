package com.zeh.jungle.utils.security;

/**
 * 加密(盐值)
 * 
 * @author allen
 * @version $Id: DefaultPasswordEncoder.java, v 0.1 2016年2月29日 下午1:31:14 allen Exp $
 */
public class DefaultPasswordEncoder implements PasswordEncoder {

    /**
     * 加密
     * 
     * @see com.zeh.jungle.utils.security.PasswordEncoder#encodePassword(String, Object)
     */
    @Override
    public String encodePassword(String rawPass, Object salt) {
        Long aid = (Long) salt;
        if (rawPass == null) {
            rawPass = "";
        }

        String md5 = MD5Utils.encrypt(rawPass);
        int idx = (int) (aid % 32);
        String s_aid = String.valueOf(aid);
        String r = new StringBuilder(md5.length() + s_aid.length()).append(substring(md5, 0, idx)).append(s_aid).append(substring(md5, idx)).toString();
        return MD5Utils.encrypt(r);
    }

    /**
     * 密码是否有效
     * 
     * @see com.zeh.jungle.utils.security.PasswordEncoder#isPasswordValid(String, String, Object)
     */
    @Override
    public boolean isPasswordValid(String encPass, String rawPass, Object salt) {
        String pass1 = "" + encPass;
        String pass2 = encodePassword(rawPass, salt);
        return pass1.equals(pass2);
    }

    private String substring(String str, int start) {
        if (str == null) {
            return null;
        }

        // handle negatives, which means last n characters
        if (start < 0) {
            // remember start is negative
            start = str.length() + start;
        }

        if (start < 0) {
            start = 0;
        }
        if (start > str.length()) {
            return "";
        }

        return str.substring(start);
    }

    private String substring(String str, int start, int end) {
        if (str == null) {
            return null;
        }

        // handle negatives
        if (end < 0) {
            // remember end is negative
            end = str.length() + end;
        }
        if (start < 0) {
            // remember start is negative
            start = str.length() + start;
        }

        // check length next
        if (end > str.length()) {
            end = str.length();
        }

        // if start is greater than end, return ""
        if (start > end) {
            return "";
        }

        if (start < 0) {
            start = 0;
        }
        if (end < 0) {
            end = 0;
        }

        return str.substring(start, end);
    }
}
