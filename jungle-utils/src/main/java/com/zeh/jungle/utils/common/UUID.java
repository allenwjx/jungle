package com.zeh.jungle.utils.common;

import org.safehaus.uuid.UUIDGenerator;

/**
 * UUID生成器
 * 
 * @author allen
 * @version $Id: UUID.java, v 0.1 2016年2月26日 下午2:24:26 allen Exp $
 */
public class UUID {

    /***/
    private static final UUIDGenerator GENERATOR = UUIDGenerator.getInstance();

    /**
     * 构造方法
     */
    private UUID() {
    }

    /**
     * 生成基于时间生成的UUID (格式化)
     * 
     * @return UUID
     */
    public static String generateFormatedTimeBasedUUID() {
        return GENERATOR.generateTimeBasedUUID().toString();
    }

    /**
     * 生成基于时间生成的UUID (非格式化)
     * 
     * @return UUID
     */
    public static String generateTimeBasedUUID() {
        return generateFormatedTimeBasedUUID().replace("-", "");
    }

    /**
     * 生成基于随机数生成的UUID (格式化)
     * 
     * @return UUID
     */
    public static String generateFormatedRandomUUID() {

        return GENERATOR.generateRandomBasedUUID().toString();
    }

    /**
     * 生成基于随机数生成的UUID (非格式化)
     * 
     * @return UUID
     */
    public static String generateRandomUUID() {
        return generateFormatedRandomUUID().replace("-", "");
    }
}
