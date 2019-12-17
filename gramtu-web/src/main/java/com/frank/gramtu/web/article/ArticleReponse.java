package com.frank.gramtu.web.article;

import com.frank.gramtu.core.response.BaseResponse;
import lombok.Getter;
import lombok.Setter;

import java.util.List;
import java.util.Map;

/**
 * 推荐阅读返回报文.
 *
 * @author 张孝党 2019/12/16.
 * @version V0.0.1.
 * <p>
 * 更新履历： V0.0.1 2019/12/16 张孝党 创建.
 */
@Getter
@Setter
public class ArticleReponse extends BaseResponse {

    // 数据列表
    private List<Map<String, String>> artlist;

}
