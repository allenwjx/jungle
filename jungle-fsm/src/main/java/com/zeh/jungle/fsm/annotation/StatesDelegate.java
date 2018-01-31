package com.zeh.jungle.fsm.annotation;

import java.lang.annotation.ElementType;
import java.lang.annotation.Retention;
import java.lang.annotation.RetentionPolicy;
import java.lang.annotation.Target;

/**
 * @author allen
 * @create $ ID: StatesDelegate, 18/1/15 15:31 allen Exp $
 * @since 1.0.0
 */
@Retention(RetentionPolicy.RUNTIME)
@Target({ ElementType.TYPE })
public @interface StatesDelegate {
    /**
     * 状态委托名称 
     * 
     * @return 状态委托名称
     */
    String name();

    /**
     * 状态类型
     *
     * @return 状态Class类型
     */
    Class state();

    /**
     * 业务类型
     * 
     * @return 业务Class类型
     */
    Class bizType();
}
