package com.zeh.jungle.fsm.annotation;

import java.lang.annotation.ElementType;
import java.lang.annotation.Retention;
import java.lang.annotation.RetentionPolicy;
import java.lang.annotation.Target;

/**
 * 状态机注解，添加该注解的类将别系统识别为参与状态机流转和执行的节点组件
 * 
 * @author allen
 * @version $Id: State.java, v 0.1 2017年9月3日 下午8:18:32 allen Exp $
 */
@Retention(RetentionPolicy.RUNTIME)
@Target({ ElementType.TYPE })
public @interface State {
    /** 状态委托名称 */
    String delegate();

    /** 原始业务类型 */
    String fromBiz();

    /** 目标业务类型 */
    String toBiz();

    /** 原始业务的状态 */
    String from();

    /** 目标的业务状态 */
    String to();
}
