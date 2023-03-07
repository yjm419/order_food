package com.yjm.interceptor;

import org.springframework.util.CollectionUtils;
import org.springframework.web.servlet.HandlerInterceptor;

import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;
import javax.servlet.http.HttpSession;
import java.util.Map;
import java.util.Set;

public class LoginInterceptor implements HandlerInterceptor {
    @Override
    public boolean preHandle(HttpServletRequest request, HttpServletResponse response, Object handler) throws Exception {
        HttpSession session = request.getSession();
        Object user = session.getAttribute("user");
        Object admin = session.getAttribute("admin");

        if (user!=null || admin!=null){
        return true;
        }

        String uri = request.getRequestURI();
        StringBuffer params = new StringBuffer();
        if(!uri.equals("/js/axios.min.map") && !uri.equals("/favicon.ico") && !uri.equals("/del/timg1.jpg")){
            Map<String, String[]> parameterMap = request.getParameterMap();
            if(!CollectionUtils.isEmpty(parameterMap)){
                Set<String> strings = parameterMap.keySet();
                for (String key : strings) {
                    params.append("&").append(key).append("=").append(parameterMap.get(key)[0]);
                }
                params.setCharAt(0,'?');
                uri += params;
            }
            if (!uri.equals("/error")) {
                session.setAttribute("toHtmlURI", uri);
            }
        }
        response.sendRedirect("/view/tologin");
        return false;
    }
}
