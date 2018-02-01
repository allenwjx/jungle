package com.zeh.jungle.mq.common;

import com.zeh.jungle.mq.serialize.ByteArraySerialization;
import com.zeh.jungle.mq.serialize.DefaultByteArraySerialization;
import com.zeh.jungle.mq.serialize.DefaultTextSerialization;
import com.zeh.jungle.mq.serialize.TextSerialization;
import org.apache.commons.lang3.StringUtils;

import com.alibaba.dubbo.common.serialize.Serialization;
import com.alibaba.dubbo.common.serialize.support.fst.FstSerialization;
import com.alibaba.dubbo.common.serialize.support.hessian.Hessian2Serialization;
import com.alibaba.dubbo.common.serialize.support.json.FastJsonSerialization;
import com.alibaba.dubbo.common.serialize.support.kryo.KryoSerialization;
import com.alibaba.dubbo.common.serialize.support.nativejava.NativeJavaSerialization;

/**
 * 
 * @author allen
 * @version $Id: SerializeEnum.java, v 0.1 2016年3月2日 上午11:07:38 allen Exp $
 */
public enum SerializeEnum {
	/**
	 * 非UTF文本序列化
	 */
	DEFAULT_TEXT("DEFAULT_TEXT", new DefaultTextSerialization(), "非UTF文本序列化"),
	/**
	 * "非UTF字节数组序列化
	 */
	DEFAULT_BYTEARRAY("DEFAULT_BYTEARRAY", new DefaultByteArraySerialization(), "非UTF字节数组序列化"),
	/**
	 * UTF文本方式
	 */
	TEXT("TEXT", new TextSerialization(), "UTF文本方式"),
	/**
	 * Java序列化
	 */
	NATIVE_JAVA("JAVA", new NativeJavaSerialization(), "Java序列化"),
	/**
	 * Kryo序列化
	 */
	KRYO("Kryo", new KryoSerialization(), "Kryo序列化"),
	/**
	 * Fastjson序列化
	 */
	FASTJSON("Fastjson", new FastJsonSerialization(), "Fastjson序列化"),
	/**
	 * Hessian2序列化
	 */
	HESSIAN2("Hessian2", new Hessian2Serialization(), "Hessian2序列化"),
	/**
	 * FST序列化
	 */
	FST("Fst", new FstSerialization(), "FST序列化"),
	/**
	 * UTF字节数组序列化
	 */
	BYTEARRAY("ByteArray", new ByteArraySerialization(), "UTF字节数组序列化");

	/** 编码 */
	private String code;

	/** 序列化器 */
	private Serialization serialize;

	/** 描述 */
	private String message;

	/**
	 * 构造函数
	 * 
	 * @param code
	 *            编码
	 * @param serialize
	 *            序列化器
	 * @param message
	 *            描述
	 */
	private SerializeEnum(String code, Serialization serialize, String message) {
		this.code = code;
		this.serialize = serialize;
		this.message = message;
	}

	/**
	 * 获取枚举类型
	 * 
	 * @param code
	 *            枚举码
	 * @return
	 */
	public static SerializeEnum getEnumByCode(String code) {
		if (StringUtils.isEmpty(code)) {
			return null;
		}

		for (SerializeEnum e : SerializeEnum.values()) {
			if (code.equals(e.getCode())) {
				return e;
			}
		}
		return null;
	}

	/**
	 * Getter for code
	 * 
	 * @return
	 */
	public String getCode() {
		return code;
	}

	/**
	 * 获取序列化器
	 * 
	 * @return
	 */
	public Serialization getSerialize() {
		return serialize;
	}

	/**
	 * Getter message
	 * 
	 * @return
	 */
	public String getMessage() {
		return message;
	}
}
