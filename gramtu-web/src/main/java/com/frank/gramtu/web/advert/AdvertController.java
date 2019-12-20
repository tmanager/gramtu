package com.frank.gramtu.web.advert;

import com.alibaba.fastjson.JSON;
import com.alibaba.fastjson.TypeReference;
import com.frank.gramtu.core.fdfs.FdfsUtil;
import com.frank.gramtu.core.request.WebRequest;
import lombok.extern.slf4j.Slf4j;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.web.bind.annotation.CrossOrigin;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;

@Slf4j
@RestController
@CrossOrigin(origins = "*")
@RequestMapping("/advert")
public class AdvertController {

    @Autowired
    private FdfsUtil fdfsUtil;

    /**
     * 查询一览.
     */
    @RequestMapping("/query")
    public String query(@RequestBody String requestParam) {
        log.info("查询广告一览开始..................");

        log.info("请求参数为：{}", requestParam);
        WebRequest<AdvertRequest> requestData = JSON.parseObject(requestParam, new TypeReference<WebRequest<AdvertRequest>>() {
        });

        String responseData = "";

        log.info("查询广告一览结束..................");
        log.info("返回值为:{}", responseData);
        return responseData;
    }
}
