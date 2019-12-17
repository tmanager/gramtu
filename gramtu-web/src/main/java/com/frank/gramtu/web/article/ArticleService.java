package com.frank.gramtu.web.article;

import com.alibaba.fastjson.JSON;
import com.frank.gramtu.core.request.WebRequest;
import com.frank.gramtu.core.response.SysResponse;
import com.frank.gramtu.core.response.WebResponse;
import com.frank.gramtu.core.utils.SdyfCommonUtil;
import com.frank.gramtu.core.utils.SdyfDateTimeUtil;
import lombok.extern.slf4j.Slf4j;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

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
        ArticleReponse articleReponse = new ArticleReponse();
        articleReponse.setDraw(0);
        articleReponse.setTotalcount(cnt);
        articleReponse.setArtlist(dataList);
        responseData.setResponse(articleReponse);

        return JSON.toJSONString(responseData);
    }

    /**
     * 新增文章.
     */
    @Transactional(rollbackFor = Exception.class)
    public String articleAddService(WebRequest<ArticleRequest> requestData) {

        // 参数
        Map<String, String> param = new HashMap<>();
        // id主键
        param.put("id", SdyfCommonUtil.getUUid());
        // 用户id
        param.put("userid", requestData.getUserid());
        // 更新时间
        param.put("updtime", SdyfDateTimeUtil.getTimeformat());
        // 标题
        param.put("title", requestData.getRequest().getTitle());
        // 封面
        param.put("cover", requestData.getRequest().getCoverimage());
        // 文章内容
        param.put("article", requestData.getRequest().getContent());

        // 新增
        int cnt = this.articleRepository.addArticle(param);
        log.info("新增数据条数为：[{}]", cnt);

        // 返回
        return new SysResponse().toJsonString();
    }

    /**
     * 根据文章ID获取文章内容.
     */
    public String artDetailService(String artId) {

        // 查询参数
        Map<String, String> param = new HashMap<>();
        param.put("artid", artId);

        // 获取文章详细内容
        Map<String, String> artDetail = this.articleRepository.getArtDetail(param);

        WebResponse<ArticleReponse> responseData = new WebResponse<>();
        ArticleReponse articleReponse = new ArticleReponse();
        // 标题
        articleReponse.setTitle(artDetail.get("title"));
        // 发布时间
        articleReponse.setTime(artDetail.get("updtime"));
        // 发布者
        articleReponse.setEditor(artDetail.get("uname"));
        // 文章内容
        articleReponse.setContent(artDetail.get("article"));
        responseData.setResponse(articleReponse);

        // 返回
        return JSON.toJSONString(responseData);
    }

    /**
     * 删除文章.
     */
    @Transactional(rollbackFor = Exception.class)
    public String delArticleService(String[] artidList) {

        for (String artId :artidList) {
            Map<String, String> param = new HashMap<>();
            param.put("artid", artId);
            this.articleRepository.deleteArticle(param);
            log.info("文章[{}]被删除", artId);
        }

        // 返回
        return new SysResponse().toJsonString();
    }
}
