package com.zeh.jungle.web;

import java.util.ArrayList;
import java.util.List;

import org.springframework.core.MethodParameter;
import org.springframework.data.domain.Sort;
import org.springframework.data.web.SortHandlerMethodArgumentResolver;
import org.springframework.web.bind.support.WebDataBinderFactory;
import org.springframework.web.context.request.NativeWebRequest;
import org.springframework.web.method.support.ModelAndViewContainer;

import com.alibaba.fastjson.JSON;

/**
 * The type Customer sort handler method argument resolver.
 *
 * @author huzhongyuan
 * @version $Id : JGSortHandlerMethodArgumentResolver, v 0.1 16/4/17 18:23 huzhongyuan Exp $
 */
public class JGSortHandlerMethodArgumentResolver extends SortHandlerMethodArgumentResolver {

    /**
     * Resolve argument sort.
     *
     * @param parameter     the parameter
     * @param mavContainer  the mav container
     * @param webRequest    the web request
     * @param binderFactory the binder factory
     * @return the sort
     */
    @Override
    public Sort resolveArgument(MethodParameter parameter, ModelAndViewContainer mavContainer, NativeWebRequest webRequest, WebDataBinderFactory binderFactory) {
        String directionParameter = webRequest.getParameter(getSortParameter(parameter));
        List<SortBean> jsonArray = JSON.parseArray(directionParameter, SortBean.class);

        if (directionParameter != null && jsonArray.size() != 0) {
            return parseParameterIntoSort(jsonArray);
        }
        return null;

    }

    /**
     * Parse parameter into sort sort.
     *
     * @param jsonArray the json array
     * @return the sort
     */
    private Sort parseParameterIntoSort(List<SortBean> jsonArray) {

        List<Sort.Order> allOrders = new ArrayList<>();
        for (SortBean vo : jsonArray) {
            allOrders.add(new Sort.Order(Sort.Direction.fromString(vo.getDirection()), vo.getProperty()));
        }

        return allOrders.isEmpty() ? null : new Sort(allOrders);
    }

}