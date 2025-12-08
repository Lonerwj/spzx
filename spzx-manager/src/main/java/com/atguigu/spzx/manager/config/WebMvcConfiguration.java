package com.atguigu.spzx.manager.config;

import com.atguigu.spzx.manager.interceptor.LoginAuthInterceptor;
import com.atguigu.spzx.manager.properties.UserProperties;
import org.jetbrains.annotations.NotNull;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Component;
import org.springframework.web.servlet.config.annotation.CorsRegistry;
import org.springframework.web.servlet.config.annotation.InterceptorRegistry;
import org.springframework.web.servlet.config.annotation.WebMvcConfigurer;

@Component
public class WebMvcConfiguration implements WebMvcConfigurer {

    @Autowired
    private LoginAuthInterceptor loginAuthInterceptor ;

    @Autowired
    private UserProperties userProperties ;

    @Override
    public void addInterceptors(@NotNull InterceptorRegistry registry) {
        // 调试：打印加载的排除路径
        System.out.println("=== 加载的排除路径列表 ===");
        if (userProperties.getNoAuthUrls() != null) {
            userProperties.getNoAuthUrls().forEach(System.out::println);
        } else {
            System.out.println("警告：noAuthUrls 为 null!");
        }
        System.out.println("=======================");

        registry.addInterceptor(loginAuthInterceptor)
                .excludePathPatterns(userProperties.getNoAuthUrls())
                .excludePathPatterns("/doc.html")
                .addPathPatterns("/**");
    }

    @Override
    public void addCorsMappings(CorsRegistry registry) {
        registry.addMapping("/**")      // 添加路径规则
                .allowCredentials(true)               // 是否允许在跨域的情况下传递Cookie
                .allowedOriginPatterns("*")           // 允许请求来源的域规则
                .allowedMethods("*")
                .allowedHeaders("*") ;                // 允许所有的请求头
    }

}
