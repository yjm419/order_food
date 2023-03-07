package com.yjm.config;

import com.yjm.interceptor.LoginInterceptor;
import org.springframework.context.annotation.Configuration;
import org.springframework.web.servlet.config.annotation.InterceptorRegistry;
import org.springframework.web.servlet.config.annotation.WebMvcConfigurer;

@Configuration
public class MyInterceptor implements WebMvcConfigurer {
    @Override
    public void addInterceptors(InterceptorRegistry registry) {
        registry.addInterceptor(new LoginInterceptor())
                .addPathPatterns("/**") //拦截
                .excludePathPatterns("/css/**","/js/**","/images/**","/img/**","/del/**","/fonts/**","/font/**")
                .excludePathPatterns("/user/login","/user/islogin","/user/register")
                .excludePathPatterns("/view/**","/admin","/")
                .excludePathPatterns("/food/**")
                .excludePathPatterns("/store/**")
                .excludePathPatterns("/orders/orderwaitcomment")

                .excludePathPatterns("/admin/css/**","/admin/fonts/**","/admin/images/**","/admin/js/**","/admin/layui/**")
                .excludePathPatterns("/admin/adminLogin","/aview/**","/adm");;



    }

}
