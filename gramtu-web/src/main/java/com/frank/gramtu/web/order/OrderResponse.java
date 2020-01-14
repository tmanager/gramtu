package com.frank.gramtu.web.order;

import com.frank.gramtu.core.response.BaseResponse;
import lombok.Getter;
import lombok.Setter;

import java.util.List;
import java.util.Map;

@Getter
@Setter
public class OrderResponse extends BaseResponse {

    // 订单列表
    private List<Map<String, String>> orderlist;
}
