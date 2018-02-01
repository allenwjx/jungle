package com.zeh.jungle.web;

import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;

import org.apache.commons.lang3.StringUtils;
import org.springframework.web.servlet.ModelAndView;
import org.springframework.web.servlet.handler.HandlerInterceptorAdapter;

/**
 * The type String to date type converter.
 *
 * @author huzhongyuan
 * @version $Id : NoFrameInterceptor.java, v 0.1 16/4/17 18:17 huzhongyuan Exp $
 */
public class NoFrameInterceptor extends HandlerInterceptorAdapter {

    @Override
    public void postHandle(HttpServletRequest httpServletRequest, HttpServletResponse httpServletResponse, Object o, ModelAndView modelAndView) throws Exception {
        if (modelAndView != null) {
            boolean noframe = false;
            String queryString = httpServletRequest.getQueryString();
            if (StringUtils.isNotBlank(queryString)) {
                String[] args = queryString.split("&");
                for (String arg : args) {
                    if (arg != null && arg.toLowerCase().equals("noframe")) {
                        noframe = true;
                    }
                }
            }
            modelAndView.addObject("noframe", noframe);
        }
    }

}
