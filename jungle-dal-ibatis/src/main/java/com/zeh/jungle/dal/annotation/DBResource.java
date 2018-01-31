package com.zeh.jungle.dal.annotation;

import java.lang.annotation.ElementType;
import java.lang.annotation.Retention;
import java.lang.annotation.RetentionPolicy;
import java.lang.annotation.Target;

/**
 * 
 * @author allen
 * @version $Id: DBResource.java, v 0.1 2016年4月29日 下午2:27:39 wb30644 Exp $
 */
@Retention(RetentionPolicy.RUNTIME)
@Target({ ElementType.METHOD, ElementType.TYPE })
public @interface DBResource {
    /**
     * 数据源的id标志
     * 
     * @return
     */
    String value() default "";
}
