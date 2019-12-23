package com.frank.gramtu.web;

import com.alibaba.fastjson.JSON;
import com.alibaba.fastjson.JSONObject;
import com.frank.gramtu.core.rmq.RmqService;
import lombok.extern.slf4j.Slf4j;
import org.mybatis.spring.annotation.MapperScan;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.SpringApplication;
import org.springframework.boot.autoconfigure.SpringBootApplication;
import org.springframework.boot.web.servlet.ServletComponentScan;
import org.springframework.transaction.annotation.EnableTransactionManagement;
import org.springframework.web.bind.annotation.CrossOrigin;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;


@Slf4j
@RestController
@CrossOrigin(origins = "*")
@ServletComponentScan
@EnableTransactionManagement
@MapperScan("com.frank.gramtu.web.*.")
@SpringBootApplication(scanBasePackages = {"com.frank.gramtu.web", "com.frank.gramtu.core"})
public class GramtuWebApplication {

    public static void main(String[] args) {
        SpringApplication.run(GramtuWebApplication.class, args);
    }

    @Autowired
    private RmqService rmqService;

    @RequestMapping(value = "msg")
    public void sendMsg() {

        JSONObject json = new JSONObject();
        json.put("ddbh", "zhangxd");
        json.put("file_url","http://www.gramtu.com/group1/M00/00/00/rBBVI14AmPiARAF9AAAB9Puwo2k741.txt");

        Object result = rmqService.rpcToGrammarly(json.toJSONString());
        log.info("返回值为：{}", result);
    }
}
