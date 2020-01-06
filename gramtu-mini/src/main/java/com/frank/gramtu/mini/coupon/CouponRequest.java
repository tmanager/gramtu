package com.frank.gramtu.mini.coupon;

import com.frank.gramtu.core.response.BaseRequest;
import lombok.Getter;
import lombok.Setter;
import org.springframework.beans.factory.annotation.Value;

/**
 * 上程序端优惠券请求报文.
 *
 * @author 张孝党 2020/01/06.
 * @version V1.00.
 * <p>
 * 更新履历： V1.00 2020/01/06. 张孝党 创建.
 */
@Getter
@Setter
public class CouponRequest extends BaseRequest {

    @Value("${openid}")
    private String openId = "";

    @Value("${checktype}")
    private String checkType = "";
}
