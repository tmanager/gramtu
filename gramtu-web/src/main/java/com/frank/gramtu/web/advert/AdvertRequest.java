package com.frank.gramtu.web.advert;

import com.frank.gramtu.core.response.BaseRequest;
import lombok.Getter;
import lombok.Setter;

@Getter
@Setter
public class AdvertRequest extends BaseRequest {

    // 广告名称
    private String title = "";
}
