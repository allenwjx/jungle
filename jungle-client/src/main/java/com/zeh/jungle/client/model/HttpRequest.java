package com.zeh.jungle.client.model;

import java.io.Serializable;
import java.util.Map;

import org.springframework.http.HttpMethod;

import com.google.common.collect.Maps;

/**
 * 
 * @author allen
 * @version $Id: HttpRequest.java, v 0.1 2016年5月19日 下午4:37:22 allen Exp $
 */
public final class HttpRequest<T extends Serializable> {

    /** Http请求数据 */
    private T                   request;

    /** Http请求地址 */
    private String              endpoint;

    /** Http method */
    private HttpMethod          requestMethod;

    /** 请求参数序列化方法 */
    private SerializeMethod     requestSerializeMethod;

    /** 应答数据序列化方法 */
    private SerializeMethod     responseSerializeMethod;

    /** 参数传递方式 */
    private ParameterType       parameterType;

    /** Object url入参的Key */
    private String              urlObjectKey;

    /** 日期格式 */
    private String              dateFormat;

    /** URL参数是否需要编码*/
    private boolean             encoded;

    /** URL参数编码字符集 ，默认UTF-8*/
    private String              charset = "UTF-8";

    /** 返回体编码字符集,默认使用响应头编码 */
    private String              rCharset;

    /** Http头信息*/
    private Map<String, String> headers = Maps.newHashMap();

    public void addHeader(String name, String value) {
        headers.put(name, value);
    }

    /**
     * Getter method for property <tt>request</tt>.
     * 
     * @return property value of request
     */
    public T getRequest() {
        return request;
    }

    /**
     * Setter method for property <tt>request</tt>.
     * 
     * @param request value to be assigned to property request
     */
    public void setRequest(T request) {
        this.request = request;
    }

    /**
     * Getter method for property <tt>endpoint</tt>.
     * 
     * @return property value of endpoint
     */
    public String getEndpoint() {
        return endpoint;
    }

    /**
     * Setter method for property <tt>endpoint</tt>.
     * 
     * @param endpoint value to be assigned to property endpoint
     */
    public void setEndpoint(String endpoint) {
        this.endpoint = endpoint;
    }

    /**
     * Getter method for property <tt>headers</tt>.
     * 
     * @return property value of headers
     */
    public Map<String, String> getHeaders() {
        return headers;
    }

    /**
     * Getter method for property <tt>requestMethod</tt>.
     * 
     * @return property value of requestMethod
     */
    public HttpMethod getRequestMethod() {
        return requestMethod;
    }

    /**
     * Setter method for property <tt>requestMethod</tt>.
     * 
     * @param requestMethod value to be assigned to property requestMethod
     */
    public void setRequestMethod(HttpMethod requestMethod) {
        this.requestMethod = requestMethod;
    }

    /**
     * Getter method for property <tt>requestSerializeMethod</tt>.
     * 
     * @return property value of requestSerializeMethod
     */
    public SerializeMethod getRequestSerializeMethod() {
        return requestSerializeMethod;
    }

    /**
     * Setter method for property <tt>requestSerializeMethod</tt>.
     * 
     * @param requestSerializeMethod value to be assigned to property requestSerializeMethod
     */
    public void setRequestSerializeMethod(SerializeMethod requestSerializeMethod) {
        this.requestSerializeMethod = requestSerializeMethod;
    }

    /**
     * Getter method for property <tt>responseSerializeMethod</tt>.
     * 
     * @return property value of responseSerializeMethod
     */
    public SerializeMethod getResponseSerializeMethod() {
        return responseSerializeMethod;
    }

    /**
     * Setter method for property <tt>responseSerializeMethod</tt>.
     * 
     * @param responseSerializeMethod value to be assigned to property responseSerializeMethod
     */
    public void setResponseSerializeMethod(SerializeMethod responseSerializeMethod) {
        this.responseSerializeMethod = responseSerializeMethod;
    }

    /**
     * Getter method for property <tt>parameterType</tt>.
     * 
     * @return property value of parameterType
     */
    public ParameterType getParameterType() {
        return parameterType;
    }

    /**
     * Setter method for property <tt>parameterType</tt>.
     * 
     * @param parameterType value to be assigned to property parameterType
     */
    public void setParameterType(ParameterType parameterType) {
        this.parameterType = parameterType;
    }

    /**
     * Getter method for property <tt>urlObjectKey</tt>.
     * 
     * @return property value of urlObjectKey
     */
    public String getUrlObjectKey() {
        return urlObjectKey;
    }

    /**
     * Setter method for property <tt>urlObjectKey</tt>.
     * 
     * @param urlObjectKey value to be assigned to property urlObjectKey
     */
    public void setUrlObjectKey(String urlObjectKey) {
        this.urlObjectKey = urlObjectKey;
    }

    /**
     * Getter method for property <tt>dateFormat</tt>.
     * 
     * @return property value of dateFormat
     */
    public String getDateFormat() {
        return dateFormat;
    }

    /**
     * Setter method for property <tt>dateFormat</tt>.
     * 
     * @param dateFormat value to be assigned to property dateFormat
     */
    public void setDateFormat(String dateFormat) {
        this.dateFormat = dateFormat;
    }

    /**
     * Getter method for property <tt>encoded</tt>.
     * 
     * @return property value of encoded
     */
    public boolean isEncoded() {
        return encoded;
    }

    /**
     * Setter method for property <tt>encoded</tt>.
     * 
     * @param encoded value to be assigned to property encoded
     */
    public void setEncoded(boolean encoded) {
        this.encoded = encoded;
    }

    /**
     * Getter method for property <tt>charset</tt>.
     * 
     * @return property value of charset
     */
    public String getCharset() {
        return charset;
    }

    /**
     * Setter method for property <tt>charset</tt>.
     * 
     * @param charset value to be assigned to property charset
     */
    public void setCharset(String charset) {
        this.charset = charset;
    }

    /**
     * Getter method for property <tt>rCharset</tt>.
     * 
     * @return property value of rCharset
     */
    public String getrCharset() {
        return rCharset;
    }

    /**
     * Setter method for property <tt>rCharset</tt>.
     * 
     * @param rCharset value to be assigned to property rCharset
     */
    public void setrCharset(String rCharset) {
        this.rCharset = rCharset;
    }

}
