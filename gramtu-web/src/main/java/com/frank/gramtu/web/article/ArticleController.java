package com.frank.gramtu.web.article;

import com.alibaba.fastjson.JSON;
import com.alibaba.fastjson.TypeReference;
import com.frank.gramtu.core.request.WebRequest;
import lombok.extern.slf4j.Slf4j;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.web.bind.annotation.CrossOrigin;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;

/**
 * 推荐阅读Controller.
 *
 * @author 张孝党 2019/12/16.
 * @version V1.00.
 * <p>
 * 更新履历： V1.00 2019/12/16 张孝党 创建.
 */
@Slf4j
@RestController
@CrossOrigin(origins = "*")
@RequestMapping("/article")
public class ArticleController {

    @Autowired
    private ArticleService articleService;

    /**
     * 查询文章一览.
     */
    @RequestMapping("/query")
    public String query(@RequestBody String requestParam) {

        log.info("查询文章一览开始..................");

        WebRequest<ArticleRequest> requestData = JSON.parseObject(requestParam, new TypeReference<WebRequest<ArticleRequest>>() {
        });
        log.info("请求参数为：{}", requestData);

        String responseData = this.articleService.queryService(requestData.getRequest());

        log.info("查询文章一览结束..................");
        log.info("返回值为:{}", responseData);
        return responseData;
    }
}
