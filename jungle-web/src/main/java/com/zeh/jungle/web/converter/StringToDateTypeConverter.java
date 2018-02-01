package com.zeh.jungle.web.converter;

import java.util.Date;

import org.apache.commons.lang3.StringUtils;
import org.joda.time.DateTime;
import org.springframework.core.convert.ConversionFailedException;
import org.springframework.core.convert.TypeDescriptor;
import org.springframework.core.convert.converter.Converter;

/**
 * The type String to date type converter.
 *
 * @author huzhongyuan
 * @version $Id : StringToDateTypeConverter.java, v 0.1 16/4/17 18:17 huzhongyuan Exp $
 */
public class StringToDateTypeConverter implements Converter<String, Date> {

    /**
     * Convert date.
     *
     * @param dateStr the date str
     * @return the date
     * @see
     */
    @Override
    public Date convert(String dateStr) {
        if (StringUtils.isEmpty(dateStr)) {
            return null;
        }
        Date date;
        try {
            date = DateTime.parse(dateStr).toDate();
        } catch (Exception e) {
            throw new ConversionFailedException(TypeDescriptor.valueOf(String.class), TypeDescriptor.valueOf(Date.class), dateStr, null);
        }
        return date;
    }
}
