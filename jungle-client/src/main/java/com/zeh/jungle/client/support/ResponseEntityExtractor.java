package com.zeh.jungle.client.support;

import java.io.IOException;
import java.lang.reflect.Type;
import java.nio.charset.Charset;

import com.zeh.jungle.client.factory.MessageConverterFactory;
import org.springframework.http.ResponseEntity;
import org.springframework.http.client.ClientHttpResponse;
import org.springframework.web.client.HttpMessageConverterExtractor;
import org.springframework.web.client.ResponseExtractor;

/**
 * 响应处理器，提供转换响应体的功能
 *
 * @author allen
 * @version $Id: ResponseEntityExtractor.java, v 0.1 2016年6月16日 下午8:58:57 wb30644 Exp $
 */
public class ResponseEntityExtractor<T> implements ResponseExtractor<ResponseEntity<T>> {

    private final HttpMessageConverterExtractor<T> delegate;

    private MessageConverterFactory                messageConverterFactory = MessageConverterFactory.getInstance();

    public ResponseEntityExtractor(Type responseType, Charset charset) {
        if (responseType != null && !Void.class.equals(responseType)) {
            this.delegate = new HttpMessageConverterExtractor<T>(responseType, messageConverterFactory.getMessageConverters(charset));
        } else {
            this.delegate = null;
        }
    }

    public ResponseEntity<T> extractData(ClientHttpResponse response) throws IOException {
        if (this.delegate != null) {
            T body = this.delegate.extractData(response);
            return new ResponseEntity<T>(body, response.getHeaders(), response.getStatusCode());
        } else {
            return new ResponseEntity<T>(response.getHeaders(), response.getStatusCode());
        }
    }
}
