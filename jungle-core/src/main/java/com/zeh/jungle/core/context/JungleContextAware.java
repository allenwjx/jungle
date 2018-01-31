package com.zeh.jungle.core.context;

/**
 * 上下文感知接口，业务代码通过实现该接口来获得Jungle上下文{@link JungleContext}
 * 系统会将所有实现了该接口的实现类，在系统启动时注入Jungle上下文给实现类
 * 
 * @author allen
 * @version $Id: JungleContextAware.java, v 0.1 2016年2月28日 上午12:21:33 allen Exp $
 */
public interface JungleContextAware {

    /**
     * 注入Jungle上下文
     * 
     * @param context 上下文
     */
    void setJungleContext(JungleContext context);
}
