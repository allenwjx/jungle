package com.zeh.jungle.utils.serializer;

import java.lang.reflect.Type;
import java.nio.charset.CharsetDecoder;
import java.util.Date;

import org.apache.commons.lang3.StringUtils;

import com.alibaba.fastjson.JSON;
import com.alibaba.fastjson.TypeReference;
import com.alibaba.fastjson.parser.Feature;
import com.alibaba.fastjson.parser.ParserConfig;
import com.alibaba.fastjson.serializer.SerializeConfig;
import com.alibaba.fastjson.serializer.SerializerFeature;
import com.alibaba.fastjson.serializer.SimpleDateFormatSerializer;

/**
 * FastJson工具类
 * 
 * @author allen
 * @version $Id: FastJsonUtils.java, v 0.1 2016年2月26日 下午1:46:42 allen Exp $
 */
public class FastJsonUtils {

    /** 序列化属性 */
    private static final SerializerFeature[] serializerFeatures   = { SerializerFeature.WriteMapNullValue, SerializerFeature.WriteNullListAsEmpty,
                                                                      SerializerFeature.WriteNullNumberAsZero, SerializerFeature.WriteNullBooleanAsFalse,
                                                                      SerializerFeature.WriteNullStringAsEmpty, SerializerFeature.SortField, SerializerFeature.SkipTransientField };

    /** 反序列化属性 */
    private static final Feature[]           deSerializerFeatures = { Feature.AllowUnQuotedFieldNames, Feature.AllowSingleQuotes, Feature.InternFieldNames,
                                                                      Feature.AllowArbitraryCommas, Feature.IgnoreNotMatch };

    /** 日期类型默认序列化格式 */
    private static final String              DEFAULT_DATE_FORMAT  = "yyyy-MM-dd HH:mm:ss.SSS";

    /** 序列化配置 */
    private static SerializeConfig           mapping              = new SerializeConfig();

    static {
        mapping.put(Date.class, new SimpleDateFormatSerializer(DEFAULT_DATE_FORMAT));
    }

    /**
     * 构造函数
     */
    private FastJsonUtils() {
    }

    /**
     * java对象序列化为字符串
     * 
     * @param obj 待序列化的java对象
     * @return
     */
    public static String toJSONString(Object obj) {
        return toJSONString(obj, null);
    }

    /**
     * Java对象转换为JSON字符串
     * 
     * @param obj Java对象
     * @param dateFormat 日期格式
     * @return 序列化后的JSON字符串
     */
    public static String toJSONString(Object obj, String dateFormat) {
        if (StringUtils.isBlank(dateFormat)) {
            return JSON.toJSONString(obj, mapping, serializerFeatures);
        }
        return JSON.toJSONStringWithDateFormat(obj, dateFormat, serializerFeatures);
    }

    /**
     * Java对象转换为二进制数据
     * 
     * @param obj Java对象
     * @return 序列化后的JSON二进制数据
     */
    public static byte[] toJSONStringBytes(Object obj) {
        return toJSONStringBytes(obj, null);
    }

    /**
     * Java对象转换为二进制数据
     * 
     * @param obj Java对象
     * @param dateFormat 日期格式
     * @return 序列化后的JSON字符串
     */
    public static byte[] toJSONStringBytes(Object obj, String dateFormat) {
        if (!StringUtils.isBlank(dateFormat)) {
            mapping.put(Date.class, new SimpleDateFormatSerializer(dateFormat));
        }
        return JSON.toJSONBytes(obj, mapping, serializerFeatures);
    }

    /**
     * JSON字符串反序列化为Java对象
     * 
     * @param json JSON字符串
     * @param clazz 反序列化对象类型
     * @return 反序列化对象
     */
    public static <T> T fromJSONString(String json, Class<T> clazz) {
        return JSON.parseObject(json, clazz, deSerializerFeatures);
    }

    /**
     * JSON字符串反序列化为Java对象
     * 
     * @param json JSON字符串
     * @param type 反序列化对象类型
     * @return 反序列化对象
     */
    public static <T> T fromJSONString(String json, Type type) {
        return JSON.parseObject(json, type, deSerializerFeatures);
    }

    /**
     * JSON字符串反序列化为Java对象
     * @param json JSON字符串
     * @param clazz 反序列化对象类型
     * @param dateFormat 日期格式
     * @return 反序列化对象
     */
    public static <T> T fromJSONString(String json, Class<T> clazz, String dateFormat) {
        if (StringUtils.isNotBlank(dateFormat)) {
            ParserConfig config = new ParserConfig();
            config.getDerializers().put(Date.class, new JGDateFormatDeserializer(dateFormat));
            return JSON.parseObject(json, clazz, config, JSON.DEFAULT_PARSER_FEATURE, deSerializerFeatures);
        }
        return JSON.parseObject(json, clazz, deSerializerFeatures);
    }

    /**
     * JSON字符串反序列化为Java对象
     * 
     * @param json JSON字符串
     * @param ref 反序列化对象类型
     * @return 反序列化对象
     */
    public static <T> T fromJSONString(String json, TypeReference<T> ref) {
        return JSON.parseObject(json, ref, deSerializerFeatures);
    }

    /**
     *  JSON字符串反序列化为Java对象
     * 
     * @param json JSON字符串
     * @param ref 反序列化对象类型
     * @param dateFormat 日期格式
     * @return 反序列化对象
     */
    public static <T> T fromJSONString(String json, TypeReference<T> ref, String dateFormat) {
        if (StringUtils.isNotBlank(dateFormat)) {
            ParserConfig config = new ParserConfig();
            config.getDerializers().put(Date.class, new JGDateFormatDeserializer(dateFormat));
            return JSON.parseObject(json, ref.getType(), config, JSON.DEFAULT_PARSER_FEATURE, deSerializerFeatures);
        }
        return JSON.parseObject(json, ref, deSerializerFeatures);
    }

    /**
     * JSON二进制数据反序列化为Java对象
     * 
     * @param bytes JSON二进制数据
     * @param clazz 反序列化对象类型
     * @return 反序列化对象
     */
    public static <T> T fromJSONString(byte[] bytes, Class<T> clazz) {
        return JSON.<T> parseObject(bytes, clazz, deSerializerFeatures);
    }

    /**
     * JSON二进制数据反序列化为Java对象
     * 
     * @param bytes JSON二进制数据
     * @param clazz 反序列化对象类型
     * @param dateFormat 日期格式
     * @return 反序列化对象
     */
    public static <T> T fromJSONString(byte[] bytes, Class<T> clazz, String dateFormat) {
        return JSON.parseObject(bytes, clazz, deSerializerFeatures);
    }

    /**
     * JSON二进制数据反序列化为Java对象
     * 
     * @param bytes JSON二进制数据
     * @param off 二进制数据起始字节数组下标
     * @param len 需要反序列化二进制数据的长度
     * @param charsetDecoder 反序列化数据的字符编码类型
     * @param clazz 反序列化对象类型
     * @return 反序列化对象
     */
    public static <T> T fromJSONString(byte[] bytes, int off, int len, CharsetDecoder charsetDecoder, Class<T> clazz) {
        return JSON.<T> parseObject(bytes, off, len, charsetDecoder, clazz, deSerializerFeatures);
    }

    /**
     * JSON字符串简单添加Entry功能，只能再第一次数据添加key-value，不支持嵌套类型
     * @param json JSON字符串
     * @param key 添加的Entry key
     * @param value 添加的Entry value
     * @param addDelimiter 是否需要添加逗号分隔符
     * @return 添加Entry后的JSON字符串
     */
    public static String addEntry(String json, String key, String value, boolean addDelimiter) {
        StringBuilder buff = new StringBuilder();
        buff.append("{").append("\"").append(key).append("\"").append(":").append("\"").append(value).append("\"");
        if (addDelimiter) {
            buff.append(",");
        }
        buff.append(json.substring(1));
        return buff.toString();
    }
}
