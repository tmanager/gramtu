package com.frank.gramtu.web;

import lombok.extern.slf4j.Slf4j;
import org.mybatis.spring.annotation.MapperScan;
import org.springframework.boot.SpringApplication;
import org.springframework.boot.autoconfigure.SpringBootApplication;
import org.springframework.boot.web.servlet.ServletComponentScan;
import org.springframework.transaction.annotation.EnableTransactionManagement;
import org.springframework.web.bind.annotation.CrossOrigin;
import org.springframework.web.bind.annotation.RestController;


@Slf4j
@RestController
@CrossOrigin(origins = "*")
@ServletComponentScan
@EnableTransactionManagement
@MapperScan("com.frank.gramtu.mini.*.")
@SpringBootApplication(scanBasePackages = {"com.frank.gramtu.web", "com.frank.gramtu.core"})
public class GramtuWebApplication {

    public static void main(String[] args) {
        SpringApplication.run(GramtuWebApplication.class, args);
    }

}
