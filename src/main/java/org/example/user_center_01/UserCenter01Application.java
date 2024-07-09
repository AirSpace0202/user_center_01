package org.example.user_center_01;

import org.mybatis.spring.annotation.MapperScan;
import org.springframework.boot.SpringApplication;
import org.springframework.boot.autoconfigure.SpringBootApplication;

@SpringBootApplication                  // 复合注解
@MapperScan("org.example.user_center_01.mapper")            // 加了当前注解，SpringBoot就能扫描Mapper下的文件夹
public class UserCenter01Application {

    public static void main(String[] args) {
        SpringApplication.run(UserCenter01Application.class, args);
    }

}
