package com.frank.gramtu.ac;

import lombok.extern.slf4j.Slf4j;
import org.mybatis.spring.annotation.MapperScan;
import org.springframework.boot.SpringApplication;
import org.springframework.boot.autoconfigure.SpringBootApplication;
import org.springframework.boot.web.servlet.ServletComponentScan;
import org.springframework.transaction.annotation.EnableTransactionManagement;
import org.springframework.web.bind.annotation.CrossOrigin;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;

/**
 * Copyright(C) ShanDongYinFang 2019.
 * <p>
 * 认证中心系统应用入口.
 *
 * @author 张明亮 2019/08/08.
 * @version V0.0.1.
 * <p>
 * 更新履历： V0.0.1 2019/08/08. 张明亮 创建.
 */

@Slf4j
@RestController
@CrossOrigin(origins = "*")
@ServletComponentScan
@EnableTransactionManagement
@MapperScan("com.frank.gramtu.ac.*.")
@SpringBootApplication(scanBasePackages = {"com.frank.gramtu.ac", "com.frank.gramtu.core"})
public class GramtuAcApplication {

    public static void main(String[] args) {
        SpringApplication.run(GramtuAcApplication.class, args);
    }

    /**
     * 主页.
     *
     * @return欢迎语
     */
    @RequestMapping("/")
    public String index() {

        return "欢迎访问gramtu微信小程序后台系统";
    }
}
