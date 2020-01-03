package com.frank.gramtu.mini.business;

import com.frank.gramtu.core.response.BaseRequest;
import lombok.Getter;
import lombok.Setter;
import org.springframework.beans.factory.annotation.Value;

@Getter
@Setter
public class BusinessRequest extends BaseRequest {

    // OpenID
    @Value("${openid}")
    private String openId = "";

    // 姓
    @Value("${firstname}")
    private String firstName = "";

    // 名
    @Value("${lastname}")
    private String lastName = "";

    // 标题
    @Value("${subtitle}")
    private String subTitle = "";

    // 原始文件名称
    @Value("${filename}")
    private String orgFileName = "";

    // 上传类型
    @Value("${checktype}")
    private String checkType = "";

    // 订单编号
    @Value("${orderid}")
    private String orderId = "";
}
