package com.zeh.jungle.utils.security;

/**
 * @author allen
 * @version $Id: GZipUtil.java, v 0.1 2016年2月29日 下午1:31:14 allen Exp $
 */
public interface PasswordEncoder {

    /**
     * 加密
     * 
     * @param rawPass		原始密码
     * @param salt			盐值
     * @return encoded password
     */
    String encodePassword(String rawPass, Object salt);

    /**
     * 验证密码
     * 
     * @param encPass	加密密码
     * @param rawPass	原始密码	
     * @param salt		盐值
     * @return check the password is valid
     */
    boolean isPasswordValid(String encPass, String rawPass, Object salt);
}
