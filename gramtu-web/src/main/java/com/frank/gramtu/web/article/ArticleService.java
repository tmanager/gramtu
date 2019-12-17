package com.frank.gramtu.web.article;

import com.alibaba.fastjson.JSON;
import com.frank.gramtu.core.response.WebResponse;
import lombok.extern.slf4j.Slf4j;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

import java.util.HashMap;
import java.util.List;
import java.util.Map;

/**
 * 推荐阅读Service.
 *
 * @author 张孝党 2019/12/16.
 * @version V1.00.
 * <p>
 * 更新履历： V1.00 2019/12/16 张孝党 创建.
 */
@Slf4j
@Service
public class ArticleService {

    @Autowired
    private ArticleRepository articleRepository;

    /**
     * 查询文章一览.
     */
    public String queryService(ArticleRequest requestData) {

        String title = requestData.getTitle();

        // 查询参数
        Map<String, Object> param = new HashMap<>();
        param.put("title", title);
        param.put("startindex", requestData.getStartindex());
        param.put("pagesize", requestData.getPagesize());
        param.put("pagingOrNot", "1");

        // 查询结果
        List<Map<String, String>> dataList = this.articleRepository.getArticleList(param);
        log.info("查询出的结果为：{}", dataList);
        // 条数
        int cnt = this.articleRepository.getCnt(param);
        log.info("查询出的数据条数为：{}", cnt);

        WebResponse<ArticleReponse> responseData = new WebResponse<>();
        ArticleReponse articleReponse=new ArticleReponse();
        articleReponse.setDraw(0);
        articleReponse.setTotalcount(cnt);
        articleReponse.setArtlist(dataList);
        responseData.setResponse(articleReponse);

        return JSON.toJSONString(responseData);
    }
}
