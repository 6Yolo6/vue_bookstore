package com.example.bookstore.config;

import com.example.bookstore.interceptor.AuthInterceptor;
import org.springframework.context.annotation.Configuration;
import org.springframework.web.servlet.config.annotation.*;

import java.util.ArrayList;
import java.util.List;

@Configuration
public class InterceptorConfig implements WebMvcConfigurer {
    @Override
    public void addCorsMappings(CorsRegistry registry) {
        registry.addMapping("/**")
                .allowedOriginPatterns("*")
                .allowedMethods("GET","HEAD","POST","PUT","DELETE","OPTIONS")
                .allowCredentials(true)
                .maxAge(3600)
                .allowedHeaders("*");
    }

    @Override
    public void addInterceptors(InterceptorRegistry reg) {
        reg.addInterceptor(new AuthInterceptor())
                .addPathPatterns("/**")
                .excludePathPatterns(excludePattern());
    }

    public List<String> excludePattern(){
        List<String> ret = new ArrayList<String>();
        ret.add("/user/login");
        ret.add("/user/register");
        ret.add("/book/*");
        ret.add("/comment/*");
        ret.add("/category/*");
//        ret.add("/favor/*");
        return ret;
    }

    /**
     * 资源映射到本地路径
     * @param registry
     */
//    @Override
//    public void addResourceHandlers(ResourceHandlerRegistry registry) {
//        registry.addResourceHandler("/book/**")
//                .addResourceLocations("file:D:/vue/bookstore_app/bookstore_app/public/image/");
//    }
}