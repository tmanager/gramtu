package com.frank.gramtu.web.article;

import com.alibaba.fastjson.JSON;
import com.alibaba.fastjson.TypeReference;
import com.frank.gramtu.core.fdfs.FdfsUtil;
import com.frank.gramtu.core.request.WebRequest;
import com.frank.gramtu.core.response.SysErrResponse;
import lombok.extern.slf4j.Slf4j;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.web.bind.annotation.*;
import org.springframework.web.multipart.MultipartFile;

import javax.servlet.http.HttpServletRequest;

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

    @Autowired
    private FdfsUtil fdfsUtil;

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


    @RequestMapping(value = "/upload/image", headers = "content-type=multipart/form-data")
    public String uploadImg(@RequestParam("file")MultipartFile file, HttpServletRequest request) {

        try {
            this.fdfsUtil.upload(file);
        } catch (Exception ex) {
            log.error("上传缩略图异常:{}", ex.getMessage());
            new SysErrResponse("上传缩略图异常!").toJsonString();
        }


        return "index";
    }
}
