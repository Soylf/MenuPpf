package com.example.demo.api.config;

import org.springframework.context.annotation.Configuration;
import org.springframework.web.servlet.config.annotation.ResourceHandlerRegistry;
import org.springframework.web.servlet.config.annotation.WebMvcConfigurer;

@Configuration
public class StaticResourceConfig implements WebMvcConfigurer {

    @Override
    public void addResourceHandlers(ResourceHandlerRegistry registry) {
        registry.addResourceHandler("/imgIns/itemImg/**")
                .addResourceLocations("file:///D:/eat/dev/demo(3)/demo/src/main/resources/static/imgIns/itemImg/")
                .setCachePeriod(0);
    }
}

