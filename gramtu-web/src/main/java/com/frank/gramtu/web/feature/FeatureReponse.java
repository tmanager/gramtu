package com.frank.gramtu.web.feature;

import com.frank.gramtu.core.response.BaseResponse;
import lombok.Getter;
import lombok.Setter;

import java.util.List;
import java.util.Map;

/**
 * 特色服务管理返回报文.
 *
 * @author 张孝党 2019/12/23.
 * @version V1.00.
 * <p>
 * 更新履历： V1.00 2019/12/23 张孝党 创建.
 */
@Getter
@Setter
public class FeatureReponse extends BaseResponse {

    // 数据列表
    private List<Map<String, String>> servlist;
}
