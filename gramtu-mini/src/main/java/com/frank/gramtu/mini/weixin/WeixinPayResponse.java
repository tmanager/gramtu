package com.frank.gramtu.mini.weixin;

import com.frank.gramtu.core.response.BaseResponse;
import lombok.Getter;
import lombok.Setter;

@Getter
@Setter
public class WeixinPayResponse extends BaseResponse {

    // 时间戳
    private String timestamp = "";

    // 随机字符串
    private String noncestr = "";

    // package
    private String paypackage = "";

    // paySign
    private String paysign = "";
}
