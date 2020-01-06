package com.frank.gramtu.web.coupon;

import com.frank.gramtu.core.response.BaseResponse;
import lombok.Getter;
import lombok.Setter;

import java.util.List;
import java.util.Map;

/**
 * 优惠券返回报文.
 *
 * @author 张孝党 2020/01/07.
 * @version V0.0.1.
 * <p>
 * 更新履历： V0.0.1 2020/01/07 张孝党 创建.
 */
@Getter
@Setter
public class CouponReponse extends BaseResponse {

    // 数据列表
    private List<Map<String, String>> couponList;

    // 标题
    private String title = "";

    // 发布时间
    private String time = "";

    // 发布人
    private String editor = "";

    // 内容
    private String content = "";
}
