package com.mc.security.Configurations;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.context.annotation.Configuration;
import org.springframework.web.servlet.config.annotation.InterceptorRegistry;
import org.springframework.web.servlet.config.annotation.WebMvcConfigurer;

import com.mc.security.Interceptors.SecurityInterceptor;





@Configuration

public class WebConfig implements WebMvcConfigurer {
    @Autowired
    private SecurityInterceptor securityInterceptor;


    public void addInterceptors(InterceptorRegistry registry) {


        // registry.addInterceptor(securityInterceptor)
        //         .addPathPatterns("/api/**")
        //         .excludePathPatterns("/api/public/**");


    }
}