package com.frank.gramtu.web.article;

import org.apache.ibatis.annotations.Mapper;
import org.springframework.stereotype.Repository;

import java.util.List;
import java.util.Map;

/**
 * 推荐阅读Repository.
 *
 * @author 张孝党 2019/12/16.
 * @version V1.00.
 * <p>
 * 更新履历： V1.00 2019/12/16 张孝党 创建.
 */
@Mapper
@Repository
public interface ArticleRepository {

    // 总条数
    int getCnt(Map<String, Object> param);

    // 一览
    List<Map<String, String>> getArticleList(Map<String, Object> param);
}
