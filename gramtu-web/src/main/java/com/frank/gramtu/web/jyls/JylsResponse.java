package com.frank.gramtu.web.jyls;

import com.frank.gramtu.core.response.BaseResponse;
import lombok.Getter;
import lombok.Setter;

import java.util.List;
import java.util.Map;

@Getter
@Setter
public class JylsResponse extends BaseResponse {

    // 交易流水列表
    private List<Map<String, String>> jylslist;
}
