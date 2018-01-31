package com.zeh.jungle.utils.common;

import org.apache.commons.lang3.StringUtils;

/**
 * 校验类中所有方法,如果参数为空和null,则返回false
 * 
 * @author allen
 * @version $Id: RegexUtils.java, v 0.1 2016年2月29日 下午1:31:14 allen Exp $
 */
public class RegexUtils {
    private static String MOBILE    = "^((\\d{3}))?1[3,5,8,7,4][0-9]\\d{8}";

    private static String EMAIL     = "\\w+([-+.]\\w+)*@\\w+([-.]\\w+)*\\.\\w+([-.]\\w+)*";

    private static String IP        = "([1-9]|[1-9]\\d|1\\d{2}|2[0-4]\\d|25[0-5])(\\.(\\d|[1-9]\\d|1\\d{2}|2[0-4]\\d|25[0-5])){3}";

    private static String TELEPHONE = "0\\d{2,3}-\\d{7,8}|0\\d{4}-\\d{7,8}";

    private static String DATE      = "(19|20)\\d\\d-(0[1-9]|1[0-2])-([0][1-9]|[1,2][0-9]|3[0-1])";

    private static String FULL_DATE = "((^((1[8-9]\\d{2})|([2-9]\\d{3}))(-)(10|12|0?[13578])(-)(3[01]|[12][0-9]|0?[1-9])$)"
                                      + "|(^((1[8-9]\\d{2})|([2-9]\\d{3}))(-)(11|0?[469])(-)(30|[12][0-9]|0?[1-9])$)|(^((1[8-9]"
                                      + "\\d{2})|([2-9]\\d{3}))(-)(0?2)(-)(2[0-8]|1[0-9]|0?[1-9])$)|(^([2468][048]00)(-)(0?2)(-)"
                                      + "(29)$)|(^([3579][26]00)(-)(0?2)(-)(29)$)|(^([1][89][0][48])(-)(0?2)(-)(29)$)|(^([2-9][0-9]"
                                      + "[0][48])(-)(0?2)(-)(29)$)|(^([1][89][2468][048])(-)(0?2)(-)(29)$)|(^([2-9][0-9][2468][048])"
                                      + "(-)(0?2)(-)(29)$)|(^([1][89][13579][26])(-)(0?2)(-)(29)$)|(^([2-9][0-9][13579][26])(-)(0?2)" + "(-)(29)$))";

    private static String AGE       = "120|((1[0-1]|\\d)?\\d)";

    private static String CERT      = "[\\d]{6}(19)?[\\d]{2}((0[1-9])|(10|11|12))([012][\\d]|(30|31))[\\d]{3}[xX\\d]*";

    private static String MONEY     = "^(([1-9]\\d*)|0)(\\.\\d{1,2})?$";

    /**
     * @return 不正确的格式,参数为null和空都返回false
     */
    public static boolean emailMatch(String email) {
        if (StringUtils.isNotEmpty(email)) {
            return email.matches(EMAIL);
        }
        return false;
    }

    /**
     * 校验手机.
     * 
     * @return 不正确的格式,参数为null和空都返回false
     */
    public static boolean mobileMacth(String mobile) {
        if (StringUtils.isNotEmpty(mobile)) {
            return mobile.matches(MOBILE);
        }
        return false;
    }

    /**
     * @return 不正确的格式,参数为null和空都返回false
     */
    public static boolean ipMacth(String ip) {
        if (StringUtils.isNotEmpty(ip)) {
            return ip.matches(IP);
        }
        return false;
    }

    /**
     * 校验固定电话号码.
     * 
     * @return 不正确的格式,参数为null和空都返回false
     */
    public static boolean telMacth(String tel) {
        if (StringUtils.isNotEmpty(tel)) {
            return tel.matches(TELEPHONE);
        }
        return false;
    }

    /**
     * 年份从1900-2099 该方法只能校验简单的日期.无法校验闰年,平年. 也无法校验大月和小月以及二月的月末
     * 
     * @return 不正确的格式,参数为null和空都返回false
     */
    public static boolean simpleDateMatch(String date) {
        if (StringUtils.isNotEmpty(date)) {
            return date.matches(DATE);
        }
        return false;
    }

    /**
     * 年份从1800-9999 该方法支持闰年平年的检验.以及各月的月末
     * 
     * @return 不正确的格式,参数为null和空都返回false
     */
    public static boolean dateMatch(String date) {
        if (StringUtils.isNotEmpty(date)) {
            return date.matches(FULL_DATE);
        }
        return false;
    }

    /**
     * 年龄范围0-120
     * 
     * @return 不正确的格式,参数为null和空都返回false
     */
    public static boolean ageMatch(String age) {
        if (StringUtils.isNotEmpty(age)) {
            return age.matches(AGE);
        }
        return false;
    }

    /**
     * 身份证为15或18位,且生日在1900-1999年之间,支持最后位是x的.大小写都可以
     * 
     * @return 不正确的格式,参数为null和空都返回false
     */
    public static boolean simpleCertMatch(String cert) {
        if (StringUtils.isNotEmpty(cert)) {
            return cert.matches(CERT);
        }
        return false;
    }

    /**
     * 校验金额.
     * 
     * @return 不正确的格式,参数为null和空都返回false
     */
    public static boolean moneyMatch(String money) {
        if (StringUtils.isNotEmpty(money)) {
            return money.matches(MONEY);
        }
        return false;
    }
}