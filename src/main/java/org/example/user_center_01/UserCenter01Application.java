package org.example.user_center_01;

import org.mybatis.spring.annotation.MapperScan;
import org.springframework.boot.SpringApplication;
import org.springframework.boot.autoconfigure.SpringBootApplication;
import org.springframework.context.annotation.Bean;
import org.springframework.web.servlet.config.annotation.CorsRegistry;
import org.springframework.web.servlet.config.annotation.WebMvcConfigurer;

@SpringBootApplication                  // 复合注解
@MapperScan("org.example.user_center_01.mapper")            // 加了当前注解，SpringBoot就能扫描Mapper下的文件夹
public class UserCenter01Application {

    public static void main(String[] args) {
        SpringApplication.run(UserCenter01Application.class, args);
    }

    @Bean
    public WebMvcConfigurer corsConfigurer() {
        return new WebMvcConfigurer() {
            @Override
            public void addCorsMappings(CorsRegistry registry) {
                registry.addMapping("/**")
                        .allowedOrigins("http://localhost:3000")  // 允许来自 React 前端的跨域请求
                        .allowedMethods("GET", "POST", "PUT", "DELETE", "OPTIONS")  // 允许的请求方法
                        .allowedHeaders("*")  // 允许的请求头
                        .allowCredentials(true);  // 允许带上认证信息
            }
        };
    }

}
