package com.frank.gramtu.mini.mark;

import com.frank.gramtu.core.response.BaseRequest;
import lombok.Getter;
import lombok.Setter;
import org.springframework.beans.factory.annotation.Value;

@Getter
@Setter
public class MarkRequest extends BaseRequest {

    @Value("${openid}")
    private String openId = "";
}
