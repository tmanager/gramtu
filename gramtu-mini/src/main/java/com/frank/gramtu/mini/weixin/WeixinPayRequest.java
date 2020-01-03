package com.frank.gramtu.mini.weixin;

import com.frank.gramtu.core.response.BaseRequest;
import lombok.Getter;
import lombok.Setter;
import org.springframework.beans.factory.annotation.Value;

@Getter
@Setter
public class WeixinPayRequest extends BaseRequest {

    // OpenID
    @Value("${openid}")
    private String openId = "";

    // 支付金额
    @Value("${amount}")
    private String amount = "";

    // 订单ID
    @Value("${orderid}")
    private String orderId = "";

    // 优惠券ID
    @Value("${coupid}")
    private String coupId = "";
}
