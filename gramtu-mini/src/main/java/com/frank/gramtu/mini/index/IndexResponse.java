package com.frank.gramtu.mini.index;

import com.frank.gramtu.core.response.BaseResponse;
import lombok.Getter;
import lombok.Setter;

import java.util.List;
import java.util.Map;

/**
 * 小程序首页返回报文.
 *
 * @author 张孝党 2019/12/25.
 * @version V1.00.
 * <p>
 * 更新履历： V1.00 2019/12/25. 张孝党 创建.
 */
@Getter
@Setter
public class IndexResponse extends BaseResponse {

    // 广告列表
    private List<Map<String, String>> adlist;

    // 特色服务列表
    private List<Map<String, String>> servlist;

    // 海外招聘
    private List<Map<String, String>> abroadlist;
}
