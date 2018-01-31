package com.zeh.jungle.client;

import java.io.IOException;
import java.io.Serializable;
import java.io.UnsupportedEncodingException;
import java.lang.reflect.Field;
import java.lang.reflect.Modifier;
import java.lang.reflect.Type;
import java.net.URLEncoder;
import java.nio.charset.Charset;
import java.util.ArrayList;
import java.util.List;
import java.util.Map;

import com.zeh.jungle.client.exception.HttpInvokeException;
import com.zeh.jungle.client.model.HttpRequest;
import org.apache.commons.lang3.StringUtils;

import com.alibaba.fastjson.annotation.JSONField;
import com.google.common.collect.Lists;
import com.google.common.collect.Maps;
import com.thoughtworks.xstream.annotations.XStreamAlias;
import com.zeh.jungle.client.factory.MessageConverterFactory;
import com.zeh.jungle.client.model.HttpResponse;
import com.zeh.jungle.client.support.ResponseEntityExtractor;
import org.springframework.beans.factory.InitializingBean;
import org.springframework.http.*;
import org.springframework.http.client.ClientHttpRequest;
import org.springframework.http.converter.GenericHttpMessageConverter;
import org.springframework.http.converter.HttpMessageConverter;
import org.springframework.util.Assert;
import org.springframework.util.LinkedMultiValueMap;
import org.springframework.util.MultiValueMap;
import org.springframework.web.client.RequestCallback;
import org.springframework.web.client.ResponseExtractor;
import org.springframework.web.client.RestClientException;
import org.springframework.web.client.RestTemplate;

/**
 * 
 * @author allen
 * @version $Id: SpringRestTemplateClient.java, v 0.1 2016年5月19日 下午1:21:58 allen Exp $
 */
public class SpringRestTemplateClient implements RestTemplateClient, InitializingBean {

    private static final List<Class<?>> clazzs                  = Lists.newArrayList();

    /** Spring rest template */
    private RestTemplate                restTemplate;

    /** Message converter factory */
    private MessageConverterFactory     messageConverterFactory = MessageConverterFactory.getInstance();

    static {
        clazzs.add(String.class);
        clazzs.add(Integer.class);
        clazzs.add(Long.class);
        clazzs.add(Double.class);
        clazzs.add(Float.class);
        clazzs.add(Short.class);
        clazzs.add(Character.class);
        clazzs.add(Byte.class);
    }

    /** 
     * @see org.springframework.beans.factory.InitializingBean#afterPropertiesSet()
     */
    @Override
    public void afterPropertiesSet() throws Exception {
        Assert.notNull(restTemplate, "RestTemplate instance is required");
    }

    /**
     * @see RestTemplateClient#invoke(HttpRequest, Class)
     */
    @Override
    public <Request extends Serializable, Response extends Serializable> HttpResponse<Response> invoke(HttpRequest<Request> request,
                                                                                                       Class<Response> clazz) throws HttpInvokeException {
        validate(request);

        // Endpoint
        String url = request.getEndpoint();
        HttpMethod httpMethod = request.getRequestMethod();

        // Get http headers
        HttpHeaders httpHeaders = getHttpHeaders(request.getHeaders());

        // call remote method
        ResponseEntity<String> responseEntity = null;
        try {
            switch (request.getParameterType()) {
                case URL_PIAR: {
                    MultiValueMap<String, String> urlPairsParams = getUrlPairsParameters(request);
                    url = getGetRequestUrl(urlPairsParams, url, request.isEncoded(), request.getCharset());
                    RequestCallback requestCallback = new HttpEntityRequestCallback(null, String.class);
                    ResponseExtractor<ResponseEntity<String>> responseExtractor = getResponseEntity(String.class, request.getrCharset());
                    responseEntity = restTemplate.execute(url, httpMethod, requestCallback, responseExtractor);
                    break;
                }
                case URL_OBJECT: {
                    MultiValueMap<String, String> urlObjectParams = getUrlObjectParameters(request);
                    url = getGetRequestUrl(urlObjectParams, url, request.isEncoded(), request.getCharset());
                    RequestCallback requestCallback = new HttpEntityRequestCallback(null, String.class);
                    ResponseExtractor<ResponseEntity<String>> responseExtractor = getResponseEntity(String.class, request.getrCharset());
                    responseEntity = restTemplate.execute(url, httpMethod, requestCallback, responseExtractor);
                    break;
                }
                case BODY: {
                    String bodyRequestStr = getBodyParameters(request);
                    HttpEntity<String> httpEntity = new HttpEntity<String>(bodyRequestStr, httpHeaders);
                    ResponseExtractor<ResponseEntity<String>> responseExtractor = getResponseEntity(String.class, request.getrCharset());
                    RequestCallback requestCallback = new HttpEntityRequestCallback(httpEntity, String.class);
                    responseEntity = restTemplate.execute(url, httpMethod, requestCallback, responseExtractor);

                    break;
                }
                case REST: {
                    HttpEntity<String> httpEntity = new HttpEntity<String>(StringUtils.EMPTY, httpHeaders);
                    ResponseExtractor<ResponseEntity<String>> responseExtractor = getResponseEntity(String.class, request.getrCharset());
                    RequestCallback requestCallback = new HttpEntityRequestCallback(httpEntity, String.class);
                    responseEntity = restTemplate.execute(url, httpMethod, requestCallback, responseExtractor);
                    //responseEntity = restTemplate.exchange(url, httpMethod, httpEntity, String.class);
                    break;
                }
                default:
                    throw new HttpInvokeException("Parameter type is not supported, type=" + request.getParameterType().name());
            }
        } catch (Exception e) {
            throw new HttpInvokeException(e);
        }

        if (responseEntity == null) {
            throw new HttpInvokeException("Response is null");
        }

        //        if (responseEntity.getStatusCode() != HttpStatus.OK) {
        //            throw new HttpInvokeException("Response error, http error code: " + responseEntity.getStatusCode());
        //        }

        switch (responseEntity.getStatusCode()) {
            case OK:
                break;
            case CREATED:
                break;
            case ACCEPTED:
                break;
            default:
                throw new HttpInvokeException("Response error, http error code: " + responseEntity.getStatusCode());
        }

        HttpResponse<Response> response = createResponse(request, responseEntity, clazz);
        return response;
    }

    /**
     * 生成响应处理器
     * 
     * @param type
     * @return
     */
    private ResponseExtractor<ResponseEntity<String>> getResponseEntity(Type type, String rcharsetStr) {
        Charset rcharset = null;
        if (!StringUtils.isBlank(rcharsetStr) && Charset.isSupported(rcharsetStr)) {
            rcharset = Charset.forName(rcharsetStr);
        }
        ResponseEntityExtractor<String> responseExtractor = new ResponseEntityExtractor<String>(type, rcharset);
        return responseExtractor;
    }

    /**
     * 构造GET方法request url
     * 
     * @param params
     * @param url
     * @return
     * @throws UnsupportedEncodingException 
     */
    private String getGetRequestUrl(MultiValueMap<String, String> params, String url, boolean encode, String charset) throws UnsupportedEncodingException {
        String encodedParams = "";
        StringBuilder sb = new StringBuilder();
        int i = 0;
        for (Map.Entry<String, String> param : params.toSingleValueMap().entrySet()) {
            sb.append(param.getKey()).append("=").append(param.getValue());
            if (i < params.toSingleValueMap().entrySet().size() - 1) {
                sb.append("&");
            }
            i += 1;
        }
        encodedParams = sb.toString();
        if (encode) {
            encodedParams = URLEncoder.encode(encodedParams, charset);
        }
        String trueUrl = url + "?" + encodedParams;
        return trueUrl;
    }

    /**
     * 创建response
     * 
     * @param responseEntity
     * @return
     */
    @SuppressWarnings("unchecked")
    private <Request extends Serializable, Response extends Serializable> HttpResponse<Response> createResponse(HttpRequest<Request> request, ResponseEntity<String> responseEntity,
                                                                                                                Class<Response> clazz) {
        String responseBodyStr = responseEntity.getBody();

        if (responseBodyStr == null) {
            return null;
        }

        HttpResponse<Response> response = new HttpResponse<Response>();
        if (request.getResponseSerializeMethod() == null) {
            response.setResponse((Response) responseBodyStr);
        } else {
            Response responseObj = request.getResponseSerializeMethod().getSerializer().deserialize(responseBodyStr, clazz, request.getDateFormat());
            response.setResponse(responseObj);
        }
        response.getHeaders().putAll(responseEntity.getHeaders().toSingleValueMap());
        return response;
    }

    /**
     * 构建url key-value请求参数
     * 
     * @param request
     * @return
     * @throws IllegalArgumentException
     * @throws IllegalAccessException
     */
    private <Request extends Serializable> MultiValueMap<String, String> getUrlPairsParameters(HttpRequest<Request> request) throws IllegalArgumentException,
                                                                                                                             IllegalAccessException {
        MultiValueMap<String, String> mvm = new LinkedMultiValueMap<String, String>();
        if (!clazzs.contains(request.getRequest().getClass())) {
            Map<String, Object> beanProps = convertBean(request.getRequest());
            for (Map.Entry<String, Object> beanProp : beanProps.entrySet()) {
                mvm.add(beanProp.getKey(), StringUtils.defaultIfBlank(String.valueOf(beanProp.getValue()), ""));
            }
        } else {
            mvm.add(request.getUrlObjectKey(), String.valueOf(request.getRequest()));
        }

        return mvm;
    }

    /**
     * 构建url key-value请求参数，参数为JSON对象，或XML对象
     * 
     * @param request
     * @return
     */
    private <Request extends Serializable> MultiValueMap<String, String> getUrlObjectParameters(HttpRequest<Request> request) {
        Assert.notNull(request.getRequestSerializeMethod(), "Request serializer is invalid");
        String requestParamStr = request.getRequestSerializeMethod().getSerializer().serialize(request.getRequest(), request.getDateFormat());
        String paramKey = request.getUrlObjectKey();
        MultiValueMap<String, String> mvm = new LinkedMultiValueMap<String, String>();
        mvm.add(paramKey, requestParamStr);
        return mvm;
    }

    /**
     * 构建post body请求参数
     * 
     * @param request
     * @return
     */
    private <Request extends Serializable> String getBodyParameters(HttpRequest<Request> request) {
        Assert.notNull(request.getRequestSerializeMethod(), "Request serializer is invalid");
        String requestParamStr = request.getRequestSerializeMethod().getSerializer().serialize(request.getRequest(), request.getDateFormat());
        return requestParamStr;
    }

    /**
     * 转换bean为map类型
     * 
     * @param bean
     * @return
     * @throws IllegalArgumentException
     * @throws IllegalAccessException
     */
    private Map<String, Object> convertBean(Object bean) throws IllegalArgumentException, IllegalAccessException {
        Map<String, Object> params = Maps.newHashMap();

        Field[] fields = bean.getClass().getDeclaredFields();
        for (Field field : fields) {
            field.setAccessible(true);
            int modifiers = field.getModifiers();
            if (Modifier.isTransient(modifiers)) {
                continue;
            }
            if (Modifier.isStatic(modifiers)) {
                continue;
            }
            if (Modifier.isVolatile(modifiers)) {
                continue;
            }

            if (field.isAnnotationPresent(JSONField.class)) {
                JSONField anno = field.getAnnotation(JSONField.class);
                String name = anno.name();
                Object value = field.get(bean);
                params.put(name, value);
            } else if (field.isAnnotationPresent(XStreamAlias.class)) {
                XStreamAlias anno = field.getAnnotation(XStreamAlias.class);
                String name = anno.value();
                Object value = field.get(bean);
                params.put(name, value);
            } else {
                String name = field.getName();
                Object value = field.get(bean);
                params.put(name, value);
            }
        }
        return params;
    }

    /**
     * 构建HTTP头信息
     * 
     * @param httpHeaders
     * @return
     */
    private HttpHeaders getHttpHeaders(Map<String, String> httpHeaders) {
        HttpHeaders hh = new HttpHeaders();
        for (Map.Entry<String, String> header : httpHeaders.entrySet()) {
            hh.add(header.getKey(), header.getValue());
        }
        return hh;
    }

    /**
     * 入参校验
     * 
     * @param request
     */
    private <Request extends Serializable> void validate(HttpRequest<Request> request) {
        Assert.notNull(request, "Request is invalid");
        Assert.notNull(request.getRequest(), "Request parameter is required");
        Assert.isTrue(StringUtils.isNotBlank(request.getEndpoint()), "Endpoint url is required");
        Assert.notNull(request.getRequestMethod(), "Http request method is required");
        Assert.notNull(request.getParameterType(), "Request parameter type is required");
    }

    /**
     * Setter method for property <tt>restTemplate</tt>.
     * 
     * @param restTemplate value to be assigned to property restTemplate
     */
    public void setRestTemplate(RestTemplate restTemplate) {
        this.restTemplate = restTemplate;
    }

    /**
     * 
     * Request callback implementation that prepares the request's accept headers.
     */
    private class HttpEntityRequestCallback extends AcceptHeaderRequestCallback {

        private final HttpEntity<?> requestEntity;

        private HttpEntityRequestCallback(Object requestBody) {
            this(requestBody, null);
        }

        private HttpEntityRequestCallback(Object requestBody, Type responseType) {
            super(responseType);
            if (requestBody instanceof HttpEntity) {
                this.requestEntity = (HttpEntity<?>) requestBody;
            } else if (requestBody != null) {
                this.requestEntity = new HttpEntity<Object>(requestBody);
            } else {
                this.requestEntity = HttpEntity.EMPTY;
            }
        }

        @Override
        @SuppressWarnings("unchecked")
        public void doWithRequest(ClientHttpRequest httpRequest) throws IOException {
            super.doWithRequest(httpRequest);
            if (!requestEntity.hasBody()) {
                HttpHeaders httpHeaders = httpRequest.getHeaders();
                HttpHeaders requestHeaders = requestEntity.getHeaders();
                if (!requestHeaders.isEmpty()) {
                    httpHeaders.putAll(requestHeaders);
                }
                if (httpHeaders.getContentLength() == -1) {
                    httpHeaders.setContentLength(0L);
                }
            } else {
                Object requestBody = requestEntity.getBody();
                Class<?> requestType = requestBody.getClass();
                HttpHeaders requestHeaders = requestEntity.getHeaders();
                MediaType requestContentType = requestHeaders.getContentType();
                for (HttpMessageConverter<?> messageConverter : messageConverterFactory.getMessageConverters()) {
                    if (messageConverter.canWrite(requestType, requestContentType)) {
                        if (!requestHeaders.isEmpty()) {
                            httpRequest.getHeaders().putAll(requestHeaders);
                        }
                        ((HttpMessageConverter<Object>) messageConverter).write(requestBody, requestContentType, httpRequest);
                        return;
                    }
                }
                String message = "Could not write request: no suitable HttpMessageConverter found for request type [" + requestType.getName() + "]";
                if (requestContentType != null) {
                    message += " and content type [" + requestContentType + "]";
                }
                throw new RestClientException(message);
            }
        }
    }

    /**
     * Request callback implementation that prepares the request's accept headers.
     */
    private class AcceptHeaderRequestCallback implements RequestCallback {

        private final Type responseType;

        private AcceptHeaderRequestCallback(Type responseType) {
            this.responseType = responseType;
        }

        @Override
        public void doWithRequest(ClientHttpRequest request) throws IOException {
            if (responseType != null) {
                Class<?> responseClass = null;
                if (responseType instanceof Class) {
                    responseClass = (Class<?>) responseType;
                }

                List<MediaType> allSupportedMediaTypes = new ArrayList<MediaType>();
                for (HttpMessageConverter<?> converter : messageConverterFactory.getMessageConverters()) {
                    if (responseClass != null) {
                        if (converter.canRead(responseClass, null)) {
                            allSupportedMediaTypes.addAll(getSupportedMediaTypes(converter));
                        }
                    } else if (converter instanceof GenericHttpMessageConverter) {

                        GenericHttpMessageConverter<?> genericConverter = (GenericHttpMessageConverter<?>) converter;
                        if (genericConverter.canRead(responseType, null, null)) {
                            allSupportedMediaTypes.addAll(getSupportedMediaTypes(converter));
                        }
                    }

                }
                if (!allSupportedMediaTypes.isEmpty()) {
                    MediaType.sortBySpecificity(allSupportedMediaTypes);
                    request.getHeaders().setAccept(allSupportedMediaTypes);
                }
            }
        }

        private List<MediaType> getSupportedMediaTypes(HttpMessageConverter<?> messageConverter) {
            List<MediaType> supportedMediaTypes = messageConverter.getSupportedMediaTypes();
            List<MediaType> result = new ArrayList<MediaType>(supportedMediaTypes.size());
            for (MediaType supportedMediaType : supportedMediaTypes) {
                if (supportedMediaType.getCharSet() != null) {
                    supportedMediaType = new MediaType(supportedMediaType.getType(), supportedMediaType.getSubtype());
                }
                result.add(supportedMediaType);
            }
            return result;
        }
    }

}
