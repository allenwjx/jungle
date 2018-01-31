package com.zeh.jungle.utils.common;

import java.text.DateFormat;
import java.text.ParseException;
import java.text.SimpleDateFormat;
import java.util.Calendar;
import java.util.Date;
import java.util.GregorianCalendar;

import org.apache.commons.lang3.StringUtils;
import org.apache.commons.lang3.time.DateUtils;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

/**
 * 日期工具类
 * 
 * @author allen
 * @version $Id: DateUtil.java, v 0.1 2016年2月29日 下午6:32:24 allen Exp $
 */
public class DateUtil {
    /** logger */
    private static final Logger logger               = LoggerFactory.getLogger(DateUtil.class);

    /** 23 */
    private final static int    MC1                  = 23;

    /** 59 */
    private final static int    MC2                  = 59;

    /** seconds of day */
    public final static long    ONE_DAY_SECONDS      = 86400;

    /** millseconds of day */
    public final static long    ONE_DAY_MILL_SECONDS = 86400000;

    /** yyyyMMdd日期格式 */
    public final static String  SHORT_FORMAT         = "yyyyMMdd";

    /** yyyyMMddHHmmss日期格式 */
    public final static String  LONG_FORMAT          = "yyyyMMddHHmmss";

    /** yyyy-MM-dd日期格式 */
    public final static String  WEB_FORMAT           = "yyyy-MM-dd";

    /** HHmmss日期格式 */
    public final static String  TIME_FORMAT          = "HHmmss";

    /** yyyyMM日期格式 */
    public final static String  MONTH_FORMAT         = "yyyyMM";

    /** yyyy年MM月dd日 日期格式 */
    public final static String  CHINESE_DT_FORMAT    = "yyyy年MM月dd日";

    /** yyyy-MM-dd HH:mm:ss日期格式 */
    public final static String  STANDARD_FORMAT      = "yyyy-MM-dd HH:mm:ss";

    /** yyyy-MM-dd HH:mm日期格式 */
    public final static String  NO_SECOND_FORMAT     = "yyyy-MM-dd HH:mm";

    /**
     * 私有构造函数
     */
    private DateUtil() {
    }

    /**
     * 获取日期格式对象
     *
     * @param pattern 日期格式
     * @return
     */
    public static DateFormat getNewDateFormat(String pattern) {
        DateFormat df = new SimpleDateFormat(pattern);
        df.setLenient(false);
        return df;
    }

    /**
     * 日期对象转换成字符串日期
     *
     * @param date 日期对象
     * @param format 日期格式
     * @return 字符串形态日期
     */
    public static String format(Date date, String format) {
        if (date == null) {
            return null;
        }
        return new SimpleDateFormat(format).format(date);
    }

    /**
     * 字符串日期转换为日期对象，字符串格式"yyyyMMdd"
     *
     * @param sDate 字符串形态日期
     * @return 日期对象
     * @throws ParseException 日期格式解析异常
     */
    public static Date parseDateNoTime(String sDate) throws ParseException {
        DateFormat dateFormat = new SimpleDateFormat(SHORT_FORMAT);
        if ((sDate == null) || (sDate.length() < SHORT_FORMAT.length())) {
            throw new ParseException("length too little", 0);
        }
        if (!StringUtils.isNumeric(sDate)) {
            throw new ParseException("not all digit", 0);
        }
        return dateFormat.parse(sDate);
    }

    /**
     * 字符串日期转换为日期对象
     *
     * @param sDate 字符串形态日期
     * @param format 日期字符串格式
     * @return 日期对象
     * @throws ParseException 日期格式解析异常
     */
    public static Date parseDateNoTime(String sDate, String format) throws ParseException {
        if (StringUtils.isBlank(format)) {
            throw new ParseException("Null format. ", 0);
        }
        DateFormat dateFormat = new SimpleDateFormat(format);
        if ((sDate == null) || (sDate.length() < format.length())) {
            throw new ParseException("length too little", 0);
        }
        return dateFormat.parse(sDate);
    }

    /**
     * 字符串日期转换为日期对象，字符串格式"yyyyMMdd"
     *
     * @param sDate 字符串形态日期
     * @param delimit 分隔符
     * @return 日期对象
     * @throws ParseException 日期格式解析异常
     */
    public static Date parseDateNoTimeWithDelimit(String sDate, String delimit) throws ParseException {
        sDate = sDate.replaceAll(delimit, "");
        DateFormat dateFormat = new SimpleDateFormat(SHORT_FORMAT);
        if ((sDate == null) || (sDate.length() != SHORT_FORMAT.length())) {
            throw new ParseException("length not match", 0);
        }
        return dateFormat.parse(sDate);
    }

    /**
     * 字符串日期转换为日期对象，字符串格式"yyyyMMddHHmmss"
     *
     * @param sDate 字符串形态日期
     * @return 日期对象
     */
    public static Date parseDateLongFormat(String sDate) {
        DateFormat dateFormat = new SimpleDateFormat(LONG_FORMAT);
        Date d = null;
        if ((sDate != null) && (sDate.length() == LONG_FORMAT.length())) {
            try {
                d = dateFormat.parse(sDate);
            } catch (ParseException ex) {
                return null;
            }
        }
        return d;
    }

    /**
     * 字符串日期转换为日期对象，字符串格式"yyyy-MM-dd HH:mm:ss"
     *
     * @param sDate 字符串形态日期
     * @return 日期对象
     */
    public static Date parseDateNewFormat(String sDate) {
        DateFormat dateFormat = new SimpleDateFormat(STANDARD_FORMAT);
        Date d = null;
        dateFormat.setLenient(false);
        if ((sDate != null) && (sDate.length() == STANDARD_FORMAT.length())) {
            try {
                d = dateFormat.parse(sDate);
            } catch (ParseException ex) {
                return null;
            }
        }
        return d;
    }

    /**
     * 计算当前时间几小时之后的时间
     *
     * @param date 日期
     * @param hours 增加小时数
     * @return 增加后日期
     */
    public static Date addHours(Date date, long hours) {
        return addMinutes(date, hours * 60);
    }

    /**
     * 计算当前时间几分钟之后的时间
     *
     * @param date 日期
     * @param minutes 增加分钟数
     * @return 增加后日期
     */
    public static Date addMinutes(Date date, long minutes) {
        return addSeconds(date, minutes * 60);
    }

    /**
     * 计算当前时间几秒之后的时间
     *
     * @param date 日期
     * @param secs 增加秒数
     * @return 增加后日期
     */
    public static Date addSeconds(Date date, long secs) {
        return new Date(date.getTime() + (secs * 1000));
    }

    /**
     * 判断输入的字符串是否为合法的小时
     *
     * @param hourStr 小时字符串
     * @return true/false
     */
    public static boolean isValidHour(String hourStr) {
        if (!StringUtils.isEmpty(hourStr) && StringUtils.isNumeric(hourStr)) {
            int hour = Integer.parseInt(hourStr);
            return hour >= 0 && hour <= MC1;
        }
        return false;
    }

    /**
     * 判断输入的字符串是否为合法的分或秒
     * 
     * @param str 分钟字符串
     * @return true/false
     */
    public static boolean isValidMinuteOrSecond(String str) {
        if (!StringUtils.isEmpty(str) && StringUtils.isNumeric(str)) {
            int hour = Integer.parseInt(str);
            return hour >= 0 && hour <= MC2;
        }

        return false;
    }

    /**
     * 取得新的日期
     *
     * @param date1 日期
     * @param days 天数
     * @return 新的日期
     */
    public static Date addDays(Date date1, long days) {
        return addSeconds(date1, days * ONE_DAY_SECONDS);
    }

    /**
     * 取得第二天日期
     *
     * @param sDate 日期
     * @return 隔天日期
     * @throws ParseException 解析异常
     */
    public static String getTomorrowDateString(String sDate) throws ParseException {
        Date aDate = parseDateNoTime(sDate);
        aDate = addSeconds(aDate, ONE_DAY_SECONDS);
        return getDateString(aDate);
    }

    /**
     * 取得"yyyyMMddHHmmss"格式日期
     * @param date 日期
     * @return 日期字符串
     */
    public static String getLongDateString(Date date) {
        DateFormat dateFormat = new SimpleDateFormat(LONG_FORMAT);
        return getDateString(date, dateFormat);
    }

    /**
     * 取得"yyyy-MM-dd HH:mm:ss"格式日期
     *
     * @param date 日期
     * @return 日期字符串
     */
    public static String getNewFormatDateString(Date date) {
        DateFormat dateFormat = new SimpleDateFormat(STANDARD_FORMAT);
        return getDateString(date, dateFormat);
    }

    /**
     * 获取日期字符串
     *
     * @param date 日期
     * @param dateFormat 日期字符串格式
     * @return 日期字符串
     */
    public static String getDateString(Date date, DateFormat dateFormat) {
        if (date == null || dateFormat == null) {
            return null;
        }
        return dateFormat.format(date);
    }

    /**
     * 取得前一天日期字符串
     *
     * @param sDate 日期
     * @return 前一天日期
     * @throws ParseException 解析异常
     */
    public static String getYesterDayDateString(String sDate) throws ParseException {
        Date aDate = parseDateNoTime(sDate);
        aDate = addSeconds(aDate, -ONE_DAY_SECONDS);
        return getDateString(aDate);
    }

    /**
     * 取得"yyyyMMdd"字符串日期
     *
     * @param date 日期
     * @return 当天的时间格式化为"yyyyMMdd"
     */
    public static String getDateString(Date date) {
        DateFormat df = getNewDateFormat(SHORT_FORMAT);
        return df.format(date);
    }

    /**
     * 取得"yyyy-MM-dd"字符串日期
     *
     * @param date 日期
     * @return yyyy-MM-dd日期字符串
     */
    public static String getWebDateString(Date date) {
        DateFormat dateFormat = getNewDateFormat(WEB_FORMAT);
        return getDateString(date, dateFormat);
    }

    /**
     * 取得“X年X月X日”的日期格式
     *
     * @param date 日期
     * @return X年X月X日日期字符串
     */
    public static String getChineseDateString(Date date) {
        DateFormat dateFormat = getNewDateFormat(CHINESE_DT_FORMAT);
        return getDateString(date, dateFormat);
    }

    /**
     * 获取格式"yyyyMMdd"当前日期字符串
     *
     * @return yyyyMMdd当前日期
     */
    public static String getTodayString() {
        DateFormat dateFormat = getNewDateFormat(SHORT_FORMAT);
        return getDateString(new Date(), dateFormat);
    }

    /**
     * 获取"HHmmss"当前时间
     *
     * @param date 日期
     * @return HHmmss当前时间
     */
    public static String getTimeString(Date date) {
        DateFormat dateFormat = getNewDateFormat(TIME_FORMAT);
        return getDateString(date, dateFormat);
    }

    /**
     * 获取当前时间推荐N天的时间
     *
     * @param days 推前天数
     * @return 推前N天的日期
     */
    public static String getBeforeDayString(int days) {
        Date date = new Date(System.currentTimeMillis() - (ONE_DAY_MILL_SECONDS * days));
        DateFormat dateFormat = getNewDateFormat(SHORT_FORMAT);
        return getDateString(date, dateFormat);
    }

    /**
     * 取得两个日期间隔秒数（日期1-日期2）
     * 
     * @param one 日期1
     * @param two 日期2
     * @return 间隔秒数
     */
    public static long getDiffSeconds(Date one, Date two) {
        Calendar sysDate = new GregorianCalendar();
        sysDate.setTime(one);
        Calendar failDate = new GregorianCalendar();
        failDate.setTime(two);
        return (sysDate.getTimeInMillis() - failDate.getTimeInMillis()) / 1000;
    }

    /**
     * 取得两个日期间隔分钟（日期1-日期2）
     *
     * @param one 日期1
     * @param two 日期2
     * @return 间隔分钟
     */
    public static long getDiffMinutes(Date one, Date two) {
        Calendar sysDate = new GregorianCalendar();
        sysDate.setTime(one);
        Calendar failDate = new GregorianCalendar();
        failDate.setTime(two);
        return (sysDate.getTimeInMillis() - failDate.getTimeInMillis()) / (60 * 1000);
    }

    /**
     * 取得两个日期的间隔天数
     *
     * @param one 日期1
     * @param two 日期2
     * @return 间隔天数
     */
    public static long getDiffDays(Date one, Date two) {
        Calendar sysDate = new GregorianCalendar();
        sysDate.setTime(one);
        Calendar failDate = new GregorianCalendar();
        failDate.setTime(two);
        return (sysDate.getTimeInMillis() - failDate.getTimeInMillis()) / (24 * 60 * 60 * 1000);
    }

    /**
     * 获取前N天日期字符串
     *
     * @param dateString 日期字符串
     * @param days 推前N天
     * @return 前N天日期字符串
     */
    public static String getBeforeDayString(String dateString, int days) {
        Date date;
        DateFormat df = getNewDateFormat(SHORT_FORMAT);
        try {
            date = df.parse(dateString);
        } catch (ParseException e) {
            date = new Date();
        }
        date = new Date(date.getTime() - (ONE_DAY_MILL_SECONDS * days));
        return df.format(date);
    }

    /**
     * 是否是有效的yyyyMMdd格式的日期
     * @param strDate 日期
     * @return true / false
     */
    public static boolean isValidShortDateFormat(String strDate) {
        if (strDate.length() != SHORT_FORMAT.length()) {
            return false;
        }

        try {
            // 避免日期中输入非数字
            Integer.parseInt(strDate);
        } catch (Exception numberFormatException) {
            return false;
        }

        DateFormat df = getNewDateFormat(SHORT_FORMAT);
        try {
            df.parse(strDate);
        } catch (ParseException e) {
            return false;
        }
        return true;
    }

    /**
     * 是否是有效的yyyyMMdd格式的日期
     * 
     * @param strDate 日期
     * @param delimiter 分隔符
     * @return true / false
     */
    public static boolean isValidShortDateFormat(String strDate, String delimiter) {
        String temp = strDate.replaceAll(delimiter, "");
        return isValidShortDateFormat(temp);
    }

    /**
     * 判断表示时间的字符是否为符合yyyyMMddHHmmss格式
     * 
     * @param strDate 日期
     * @return true / false
     */
    public static boolean isValidLongDateFormat(String strDate) {
        if (strDate.length() != LONG_FORMAT.length()) {
            return false;
        }

        try {
            // 避免日期中输入非数字 
            Long.parseLong(strDate);
        } catch (Exception numberFormatException) {
            return false;
        }

        DateFormat df = getNewDateFormat(LONG_FORMAT);
        try {
            df.parse(strDate);
        } catch (ParseException e) {
            return false;
        }
        return true;
    }

    /**
     * 判断表示时间的字符是否为符合yyyyMMddHHmmss格式
     * 
     * @param strDate 日期
     * @param delimiter 分隔符
     * @return true / false
     */
    public static boolean isValidLongDateFormat(String strDate, String delimiter) {
        String temp = strDate.replaceAll(delimiter, "");
        return isValidLongDateFormat(temp);
    }

    /**
     * 获取yyyyMMdd日期
     *
     * @param strDate 日期
     * @return yyyyMMdd日期
     */
    public static String getShortDateString(String strDate) {
        return getShortDateString(strDate, "-|/");
    }

    /**
     * 获取yyyyMMdd日期
     *
     * @param strDate 日期
     * @param delimiter 分隔符
     * @return yyyyMMdd日期
     */
    public static String getShortDateString(String strDate, String delimiter) {
        if (StringUtils.isBlank(strDate)) {
            return null;
        }

        String temp = strDate.replaceAll(delimiter, "");
        if (isValidShortDateFormat(temp)) {
            return temp;
        }
        return null;
    }

    /**
     * 获取当前yyyy-MM-dd日期
     *
     * @return 当前yyyy-MM-dd日期
     */
    public static String getWebTodayString() {
        DateFormat df = getNewDateFormat(WEB_FORMAT);
        return df.format(new Date());
    }

    /**
     * 获取每月第一天日期
     *
     * @param format 日期格式
     * @return 每月第一天日期
     */
    public static String getFirstDayOfMonth(String format) {
        Calendar cal = Calendar.getInstance();
        Date dt = new Date();
        cal.setTime(dt);
        cal.set(Calendar.DAY_OF_MONTH, 1);
        DateFormat df = getNewDateFormat(format);
        return df.format(cal.getTime());
    }

    /**
     * 日期格式转换
     *
     * @param dateString 日期字符串
     * @param formatIn 转入的日期格式
     * @param formatOut 转出的日期格式
     * @return 转出的日期格式的日期字符串
     */
    public static String convert(String dateString, DateFormat formatIn, DateFormat formatOut) {
        try {
            Date date = formatIn.parse(dateString);
            return formatOut.format(date);
        } catch (ParseException e) {
            logger.warn("convert() --- orign date error: " + dateString);
            return "";
        }
    }

    /**
     * web日期格式字符串转换
     *
     * @param dateString 日期字符串
     * @return yyyy-MM-dd格式日期
     */
    public static String convert2WebFormat(String dateString) {
        DateFormat df1 = getNewDateFormat(SHORT_FORMAT);
        DateFormat df2 = getNewDateFormat(WEB_FORMAT);
        return convert(dateString, df1, df2);
    }

    /**
     * yyyy年MM月dd日日期格式字符串转换
     *
     * @param dateString 日期字符串
     * @return yyyy年MM月dd日格式日期
     */
    public static String convert2ChineseDtFormat(String dateString) {
        DateFormat df1 = getNewDateFormat(SHORT_FORMAT);
        DateFormat df2 = getNewDateFormat(CHINESE_DT_FORMAT);
        return convert(dateString, df1, df2);
    }

    /**
     * web日期格式转换为yyyyMMdd
     *
     * @param dateString 日期字符串
     * @return yyyyMMdd格式日期
     */
    public static String convertFromWebFormat(String dateString) {
        DateFormat df1 = getNewDateFormat(SHORT_FORMAT);
        DateFormat df2 = getNewDateFormat(WEB_FORMAT);
        return convert(dateString, df2, df1);
    }

    /**
     * 日期比较
     * 
     * @param date1 yyyy-MM-dd格式日期1
     * @param date2 yyyy-MM-dd格式日期2
     * @return date1是否小于date2
     */
    public static boolean webDateNotLessThan(String date1, String date2) {
        DateFormat df = getNewDateFormat(WEB_FORMAT);
        return dateNotLessThan(date1, date2, df);
    }

    /**
     * 日期比较
     * @param date1 日期1
     * @param date2 日期2
     * @param format 日期格式
     * @return date1是否小于date2
     */
    public static boolean dateNotLessThan(String date1, String date2, DateFormat format) {
        try {
            Date d1 = format.parse(date1);
            Date d2 = format.parse(date2);

            if (d1.before(d2)) {
                return false;
            } else {
                return true;
            }
        } catch (ParseException e) {
            logger.debug("dateNotLessThan() --- ParseException(" + date1 + ", " + date2 + ")");
            return false;
        }
    }

    /**
     * 获取邮件格式日期
     * 
     * @param today 日期
     * @return 邮件格式日期
     */
    public static String getEmailDate(Date today) {
        String todayStr;
        SimpleDateFormat sdf = new SimpleDateFormat("yyyy年MM月dd日HH:mm:ss");
        todayStr = sdf.format(today);
        return todayStr;
    }

    /**
     * 获取SMS格式日期
     * 
     * @param today 日期
     * @return SMS格式日期
     */
    public static String getSmsDate(Date today) {
        String todayStr;
        SimpleDateFormat sdf = new SimpleDateFormat("MM月dd日HH:mm");
        todayStr = sdf.format(today);
        return todayStr;
    }

    /**
     * 范围日期
     * 
     * @param startDate 起始日期
     * @param endDate 截止日期
     * @param format 日期格式
     * @return 范围格式日期字符串
     */
    public static String formatTimeRange(Date startDate, Date endDate, String format) {
        if ((endDate == null) || (startDate == null)) {
            return null;
        }

        long range = endDate.getTime() - startDate.getTime();
        long day = range / DateUtils.MILLIS_PER_DAY;
        long hour = (range % DateUtils.MILLIS_PER_DAY) / DateUtils.MILLIS_PER_HOUR;
        long minute = (range % DateUtils.MILLIS_PER_HOUR) / DateUtils.MILLIS_PER_MINUTE;

        if (range < 0) {
            day = 0;
            hour = 0;
            minute = 0;
        }
        String rt = format.replaceAll("dd", String.valueOf(day));
        rt = rt.replaceAll("hh", String.valueOf(hour));
        rt = rt.replaceAll("mm", String.valueOf(minute));
        return rt;
    }

    /**
     * 获取月
     * 
     * @param date 日期
     * @return 月
     */
    public static String formatMonth(Date date) {
        if (date == null) {
            return null;
        }
        return new SimpleDateFormat(MONTH_FORMAT).format(date);
    }

    /**
     * 获取系统日期的前一天日期，返回Date
     *
     * @return 系统日期的前一天日期
     */
    public static Date getBeforeDate() {
        Date date = new Date();
        return new Date(date.getTime() - (ONE_DAY_MILL_SECONDS));
    }

    /**
     * 获得指定时间当天起点时间
     * 
     * @param date 日期
     * @return 指定时间当天起点时间
     */
    public static Date getDayBegin(Date date) {
        DateFormat df = new SimpleDateFormat("yyyyMMdd");
        df.setLenient(false);

        String dateString = df.format(date);
        try {
            return df.parse(dateString);
        } catch (ParseException e) {
            return date;
        }
    }

    /**
     * 判断参date上min分钟后，是否小于当前时间
     * 
     * @param date 日期
     * @param min 分钟
     * @return 是否小于N分钟
     */
    public static boolean dateLessThanNowAddMin(Date date, long min) {
        return addMinutes(date, min).before(new Date());

    }

    /**
     * 是否小于当前时间
     * @param date 日期
     * @return 指定的日期是否小于当前时间
     */
    public static boolean isBeforeNow(Date date) {
        return date != null && date.compareTo(new Date()) < 0;
    }

    /**
     * 解析yyyy-MM-dd HH:mm格式日期
     * 
     * @param sDate 日期
     * @return yyyy-MM-dd HH:mm格式日期字符串
     * @throws ParseException 解析异常
     */
    public static Date parseNoSecondFormat(String sDate) throws ParseException {
        DateFormat dateFormat = new SimpleDateFormat(NO_SECOND_FORMAT);

        if ((sDate != null) && (sDate.length() == NO_SECOND_FORMAT.length())) {
            return dateFormat.parse(sDate);
        }
        return null;
    }
}
