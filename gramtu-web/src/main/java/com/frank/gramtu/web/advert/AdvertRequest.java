package com.frank.gramtu.web.advert;

import com.frank.gramtu.core.response.BaseRequest;
import lombok.Getter;
import lombok.Setter;

@Getter
@Setter
public class AdvertRequest extends BaseRequest {

    // 广告ID
    private String adid = "";

    // 广告名称
    private String title = "";

    // 预览图片
    private String adimage = "";

    // 广告类型
    private String adtype = "";

    // 文章id
    private String article;

    // 排序号
    private String sort = "";

    // 旧图片
    private String oldadimage = "";

    // 删除的广告id列表
    private String[] advertIdlist;
}
