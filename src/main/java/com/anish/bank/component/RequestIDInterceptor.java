package com.anish.bank.component;

import java.util.UUID;

import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;

import org.slf4j.MDC;
import org.springframework.stereotype.Component;
import org.springframework.web.servlet.handler.HandlerInterceptorAdapter;

@Component
public class RequestIDInterceptor extends HandlerInterceptorAdapter {
    
    private static final String REQUEST_ID_VAR_NAME = "requestID";

    @Override
    public boolean preHandle (final HttpServletRequest request, final HttpServletResponse response , final Object handler) throws Exception {
        final String requestID = UUID.randomUUID().toString();
        MDC.put(REQUEST_ID_VAR_NAME, requestID);
        return true;
    }

    @Override
    public void afterCompletion (final HttpServletRequest request, final HttpServletResponse response , final Object handler, final Exception ex) throws Exception {
        MDC.remove(REQUEST_ID_VAR_NAME);
    }

}
