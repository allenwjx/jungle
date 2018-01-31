package com.zeh.jungle.core.degrade;

import java.lang.annotation.Documented;
import java.lang.annotation.ElementType;
import java.lang.annotation.Retention;
import java.lang.annotation.RetentionPolicy;
import java.lang.annotation.Target;

/**
 * 
 * @author allen
 * @version $Id: Degrade.java, v 0.1 2017年12月7日 下午4:49:12 allen Exp $
 */
@Retention(RetentionPolicy.RUNTIME)
@Target({ ElementType.METHOD })
@Documented
public @interface Degrade {
    /**
     * 降级名称
     * 
     * @return
     */
    String name();
}
