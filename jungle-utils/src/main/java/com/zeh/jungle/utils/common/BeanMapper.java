package com.zeh.jungle.utils.common;

import java.beans.BeanInfo;
import java.beans.IntrospectionException;
import java.beans.Introspector;
import java.beans.PropertyDescriptor;
import java.lang.reflect.InvocationTargetException;
import java.lang.reflect.Method;
import java.util.Map;

import org.apache.commons.beanutils.BeanUtils;
import org.apache.commons.lang3.StringUtils;

import com.google.common.collect.Maps;

/**
 * Java bean映射工具
 * 
 * @author allen
 * @version $Id: BeanMapper.java, v 0.1 2016年2月27日 下午12:45:42 allen Exp $
 */
public class BeanMapper {

    /**
     * 构造函数
     */
    private BeanMapper() {
    }

    /**
     * Transform java bean to map
     * 
     * @param bean java bean object
     * @return map
     */
    public static Map<String, Object> bean2Map(Object bean) {
        Map<String, Object> properties = Maps.newHashMap();
        if (bean == null) {
            return properties;
        }
        try {
            BeanInfo beanInfo = Introspector.getBeanInfo(bean.getClass());
            PropertyDescriptor[] propertyDescriptors = beanInfo.getPropertyDescriptors();
            for (PropertyDescriptor pd : propertyDescriptors) {
                String beanName = pd.getName();
                if (!StringUtils.equals(beanName, "class")) {
                    Method getter = pd.getReadMethod();
                    Object beanValue = getter.invoke(bean);
                    properties.put(beanName, beanValue);
                }
            }
        } catch (IntrospectionException | IllegalAccessException | IllegalArgumentException | InvocationTargetException e) {
            throw new RuntimeException(e);
        }
        return properties;
    }

    /**
     * Transform map to java bean
     * 
     * @param properties map
     * @param clazz class type                 
     * @return class instance
     */
    public static <T> T map2Bean(Map<String, Object> properties, Class<T> clazz) {
        if (properties == null) {
            return null;
        }
        try {
            T bean = clazz.newInstance();
            BeanUtils.populate(bean, properties);
            return bean;
        } catch (IllegalAccessException | InvocationTargetException | InstantiationException e) {
            throw new RuntimeException(e);
        }
    }

}
