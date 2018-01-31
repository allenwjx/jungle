package com.zeh.jungle.utils.common;

import com.zeh.jungle.utils.security.RandomValueStringGenerator;

import java.util.Date;

/**
 * 
 * @author allen
 * @version $Id: SerialNoGenerator.java, v 0.1 2016年4月22日 下午3:38:11 allen Exp $
 */
public class SerialNoGenerator {

    private SerialNoGenerator() {
    }

    /**
     * 生成流水号
     * 
     * @param tag 流水号打头字母
     * @param num 随机数位数
     * @return
     */
    public static String generateSerialNo(String tag, int num) {
        RandomValueStringGenerator g = new RandomValueStringGenerator(num);
        String randomCode = g.generate();
        String ser = DateUtil.format(new Date(), DateUtil.LONG_FORMAT);
        StringBuilder sb = new StringBuilder();
        sb.append(tag).append(ser).append(randomCode);
        return sb.toString();
    }
}
