package com.frank.gramtu.web.article;

import com.alibaba.fastjson.JSON;
import com.alibaba.fastjson.JSONObject;
import com.alibaba.fastjson.TypeReference;
import com.frank.gramtu.core.fdfs.FdfsUtil;
import com.frank.gramtu.core.request.WebRequest;
import lombok.extern.slf4j.Slf4j;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.web.bind.annotation.*;
import org.springframework.web.multipart.MultipartFile;

import javax.servlet.http.HttpServletRequest;
import java.util.List;

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

    /**
     * 上传封面图.
     */
    @RequestMapping(value = "/upload/image", headers = "content-type=multipart/form-data")
    public String uploadImg(@RequestParam("image") MultipartFile file, HttpServletRequest request) {
        log.info("上传封面开始.....................");

        JSONObject result = new JSONObject();
        try {
            List<String> imgPath = this.fdfsUtil.uploadImage(file);
            log.info("上传到文件服务器返回的信息为：[{}]", imgPath);

            result.put("ret", "0000");
            result.put("url", imgPath.get(0));
        } catch (Exception ex) {
            log.error("上传缩略图异常:{}", ex.getMessage());
            result.put("ret", "0004");
            result.put("msg", ex.getMessage());
        }

        log.info("上传封面结束.....................");
        // 上传封面结束
        return result.toJSONString();
    }

    /**
     * 新增文章.
     */
    @RequestMapping(value = "/artadd")
    private String articleAdd(@RequestBody String requestParam) {
        log.info("文章新增开始..................");

        WebRequest<ArticleRequest> requestData = JSON.parseObject(requestParam, new TypeReference<WebRequest<ArticleRequest>>() {
        });
        log.info("请求参数为：{}", requestData);

        // 新增
        String responseData = this.articleService.articleAddService(requestData);
        log.info("文章新增结束..................");
        log.info("返回值为:{}", responseData);
        return responseData;
    }
}
