package com.frank.gramtu.mini.torder;

import com.frank.gramtu.core.response.BaseRequest;
import lombok.Getter;
import lombok.Setter;
import org.springframework.beans.factory.annotation.Value;

@Getter
@Setter
public class TOrderRequest extends BaseRequest {

    // 用户openid
    @Value("${openid}")
    private String openId = "";

    // 类别，0:待付款，1:已付款
    @Value("${type}")
    private String type = "";
}
