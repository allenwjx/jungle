package com.zeh.jungle.web.basic;

import java.util.List;
import java.util.Map;

import org.apache.commons.lang3.StringUtils;

import com.alibaba.fastjson.JSON;
import com.google.common.collect.Lists;
import com.google.common.collect.Maps;

/**
 * 枚举工具类
 *
 * @author hxy43938
 * @version $Id : EnumUtil.java, v 0.1 2017年02月17 hxy43938 Exp $
 */
public class EnumUtil {

    /**
     * The Enum data.
     */
    private static final Map<String, List<TextValue>> ENUM_DATA = Maps.newConcurrentMap();

    /**
     * 枚举类型转换 必选为特定类型 枚举中必选提供getCode 和 getDesc方法 -> 对应放回的json数据的value为 code, text 为 desc
     *
     * @param <T> the type parameter
     * @param t   the t
     * @return json String or null
     * @throws RuntimeException
     */
    public static <T> String enumToJson(Class<T> t) {
        try {
            if (t.isEnum()) {
                T[] emArr = t.getEnumConstants();
                TextValue[] tvalues = new TextValue[emArr.length];
                int i = 0;
                for (T em : emArr) {
                    Enum<?> e = (Enum<?>) em;
                    tvalues[i++] = new TextValue((Object) (e.getDeclaringClass().getDeclaredMethod("getCode").invoke(e)),
                        (String) (e.getDeclaringClass().getDeclaredMethod("getDesc").invoke(e)));
                }
                return JSON.toJSONString(tvalues);
            }
        } catch (Exception e) {
            throw new RuntimeException("enum 解析错误!");
        }
        return null;
    }

    /**
     * values 需要转换的枚举值
     * @param t
     * @param values
     * @param <T>
     * @return
     */
    public static <T> String enumToJson(Class<T> t, List<T> values) {
        try {
            if (t.isEnum()) {
                T[] emArr = t.getEnumConstants();
                TextValue[] tvalues = new TextValue[values.size()];
                int i = 0;
                for (T em : emArr) {
                    Enum<?> e = (Enum<?>) em;
                    if (!values.contains(e)) {
                        continue;
                    }
                    tvalues[i++] = new TextValue((Object) (e.getDeclaringClass().getDeclaredMethod("getCode").invoke(e)),
                        (String) (e.getDeclaringClass().getDeclaredMethod("getDesc").invoke(e)));
                }
                return JSON.toJSONString(tvalues);
            }
        } catch (Exception e) {
            throw new RuntimeException("enum 解析错误!");
        }
        return null;
    }

    /**
     * 枚举类型转换 必选为特定类型 枚举中必选提供getDesc方法 -> 对应放回的json数据的value为 name, text 为 desc
     *
     * @param <T> the type parameter
     * @param t   the t
     * @return json String or null
     * @throws RuntimeException
     */
    public static <T> String enumNameToJson(Class<T> t) {
        try {
            if (t.isEnum()) {
                T[] emArr = t.getEnumConstants();
                TextValue[] tvalues = new TextValue[emArr.length];
                int i = 0;
                for (T em : emArr) {
                    Enum<?> e = (Enum<?>) em;
                    tvalues[i++] = new TextValue((Object) (e.name()), (String) (e.getDeclaringClass().getDeclaredMethod("getDesc").invoke(e)));
                }
                return JSON.toJSONString(tvalues);
            }
        } catch (Exception e) {
            throw new RuntimeException("enum 解析错误!");
        }
        return null;
    }

    /**
     * values 需要转换的枚举值
     * @param t
     * @param values
     * @param <T>
     * @return
     */
    public static <T> String enumNameToJson(Class<T> t, List<T> values) {
        try {
            if (t.isEnum()) {
                T[] emArr = t.getEnumConstants();
                TextValue[] tvalues = new TextValue[values.size()];
                int i = 0;
                for (T em : emArr) {
                    Enum<?> e = (Enum<?>) em;
                    if (!values.contains(e)) {
                        continue;
                    }
                    tvalues[i++] = new TextValue((Object) (e.name()), (String) (e.getDeclaringClass().getDeclaredMethod("getDesc").invoke(e)));
                }
                return JSON.toJSONString(tvalues);
            }
        } catch (Exception e) {
            throw new RuntimeException("enum 解析错误!");
        }
        return null;
    }

    /**
     * 枚举类型转换 必选为特定类型 枚举中必选提供getCode 和 getDesc方法 -> 对应放回的json数据的value为 code, text 为 desc
     *
     * @param <T> the type parameter
     * @param t   the t
     * @return the list
     * @throws RuntimeException
     */
    public static <T> List<TextValue> enumToList(Class<T> t) {
        List<TextValue> result = ENUM_DATA.get(t.getName());
        if (result != null) {
            return result;
        }

        try {
            if (t.isEnum()) {
                T[] emArr = t.getEnumConstants();
                List<TextValue> tvalues = Lists.newArrayList();
                for (T em : emArr) {
                    Enum<?> e = (Enum<?>) em;
                    tvalues
                        .add(new TextValue(e.getDeclaringClass().getDeclaredMethod("getCode").invoke(e), (String) (e.getDeclaringClass().getDeclaredMethod("getDesc").invoke(e))));
                }
                ENUM_DATA.put(t.getName(), tvalues);
                return tvalues;
            }
        } catch (Exception e) {
            throw new RuntimeException("enum 解析错误!");
        }
        return null;
    }

    /**
     * 获取枚举值
     *
     * @param className the class name
     * @param code      the code
     * @return description
     */
    public static String getDescription(String className, Object code) {
        if (StringUtils.isEmpty(className)) {
            return "";
        }
        List<TextValue> result = ENUM_DATA.get(className);
        if (result == null) {
            try {
                Class<?> clazz = Class.forName("com.ly.flight.refundadmin.biz.constants." + className);
                result = enumToList(clazz);
            } catch (ClassNotFoundException e) {
                e.printStackTrace();
            }
        }
        if (result != null) {
            TextValue textValue = result.stream().filter(item -> StringUtils.equals(item.getValue().toString(), code.toString())).findFirst().orElse(null);
            if (textValue == null) {
                return "";
            }
            return textValue.getText();
        }
        return "";
    }
}
