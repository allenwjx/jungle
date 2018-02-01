package com.zeh.jungle.web.converter;

import java.text.ParseException;
import java.util.Date;
import java.util.Locale;

import org.apache.commons.lang3.StringUtils;
import org.joda.time.DateTime;
import org.springframework.core.convert.ConversionFailedException;
import org.springframework.core.convert.TypeDescriptor;
import org.springframework.format.Formatter;

import com.zeh.jungle.utils.common.DateUtil;

/**
 * @author hzy24985
 * @version $Id: DateFormatter.java, v 0.1 2016/10/31 13:52 hzy24985 Exp $
 */
public class DateFormatter implements Formatter<Date> {

    /**
     * Print string.
     *
     * @param object the object
     * @param locale the locale
     * @return the string
     */
    @Override
    public String print(Date object, Locale locale) {
        return null;
    }

    /**
     * Parse date.
     *
     * @param text   the text
     * @param locale the locale
     * @return the date
     * @throws ParseException the parse exception
     */
    @Override
    public Date parse(String text, Locale locale) throws ParseException {

        if (StringUtils.isEmpty(text)) {
            return null;
        }
        Date date;
        try {
            if (text.length() == 10) {
                date = DateTime.parse(text).toDate();
            } else if (text.length() == 13) {
                //yyyy-MM-dd hh
                date = DateUtil.parseDateNoTime(text, "yyyy-MM-dd HH");
            } else if (text.length() == 16) {
                //yyyy-MM-dd hh
                date = DateUtil.parseDateNoTime(text, DateUtil.NO_SECOND_FORMAT);
            } else if (text.length() == 19) {
                //yyyy-MM-dd hh
                date = DateUtil.parseDateNoTime(text, DateUtil.STANDARD_FORMAT);
            } else {
                date = DateTime.parse(text).toDate();
            }
        } catch (Exception e) {
            throw new ConversionFailedException(TypeDescriptor.valueOf(String.class), TypeDescriptor.valueOf(Date.class), text, null);
        }
        return date;
    }

}
