package com.zeh.jungle.client.model;

import com.google.common.collect.Maps;

import java.io.Serializable;
import java.util.Map;

/**
 * 
 * @author allen
 * @version $Id: HttpResponse.java, v 0.1 2016年5月19日 下午4:37:39 allen Exp $
 */
public final class HttpResponse<T extends Serializable> {

    /** Http请求数据 */
    private T                   response;

    /** Http头信息*/
    private Map<String, String> headers = Maps.newHashMap();

    /**
     * 
     */
    public HttpResponse() {
    }

    /**
     * @param response
     * @param headers
     */
    public HttpResponse(T response, Map<String, String> headers) {
        super();
        this.response = response;
        this.headers = headers;
    }

    /**
     * Getter method for property <tt>response</tt>.
     * 
     * @return property value of response
     */
    public T getResponse() {
        return response;
    }

    /**
     * Setter method for property <tt>response</tt>.
     * 
     * @param response value to be assigned to property response
     */
    public void setResponse(T response) {
        this.response = response;
    }

    public void addHeader(String name, String value) {
        headers.put(name, value);
    }

    /**
     * Getter method for property <tt>headers</tt>.
     * 
     * @return property value of headers
     */
    public Map<String, String> getHeaders() {
        return headers;
    }

}
