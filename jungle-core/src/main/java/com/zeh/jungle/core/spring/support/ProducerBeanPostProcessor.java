package com.zeh.jungle.core.spring.support;

import com.zeh.jungle.mq.exception.MQException;
import com.zeh.jungle.mq.producer.UniformEventPublisher;
import com.zeh.jungle.utils.common.LoggerUtils;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.beans.BeansException;
import org.springframework.beans.factory.access.BootstrapException;
import org.springframework.beans.factory.config.BeanPostProcessor;

/**
 * 
 * @author allen
 * @version $Id: ProducerBeanPostProcessor.java, v 0.1 2016年3月3日 下午4:48:07 allen Exp $
 */
public class ProducerBeanPostProcessor implements BeanPostProcessor {

    private static final Logger logger = LoggerFactory.getLogger(ProducerBeanPostProcessor.class);

    /** 
     * @see BeanPostProcessor#postProcessBeforeInitialization(Object, String)
     */
    @Override
    public Object postProcessBeforeInitialization(Object bean, String beanName) throws BeansException {
        return bean;
    }

    /** 
     * @see BeanPostProcessor#postProcessAfterInitialization(Object, String)
     */
    @Override
    public Object postProcessAfterInitialization(Object bean, String beanName) throws BeansException {
        if (UniformEventPublisher.class.isAssignableFrom(bean.getClass())) {
            try {
                UniformEventPublisher.class.cast(bean).start();
            } catch (MQException e) {
                LoggerUtils.error(logger, e.getMessage(), e);
                throw new BootstrapException(e.getMessage(), e);
            }
        }
        return bean;
    }

}
