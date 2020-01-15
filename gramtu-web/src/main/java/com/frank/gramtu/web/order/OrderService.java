package com.frank.gramtu.web.order;

import com.alibaba.fastjson.JSON;
import com.alibaba.fastjson.serializer.SerializerFeature;
import com.frank.gramtu.core.response.WebResponse;
import com.frank.gramtu.web.coupon.CouponReponse;
import com.frank.gramtu.web.coupon.CouponRequest;
import lombok.extern.slf4j.Slf4j;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

import java.util.HashMap;
import java.util.List;
import java.util.Map;

/**
 * 订单查询Controller.
 *
 * @author 张孝党 2020/01/14.
 * @version V1.00.
 * <p>
 * 更新履历： V1.00 2020/01/14 张孝党 创建.
 */
@Slf4j
@Service
public class OrderService {

    @Autowired
    private OrderRepository orderRepository;

    /**
     * 查询优惠券一览.
     */
    public String queryService(OrderRequest requestData) {

        // 查询参数
        Map<String, Object> param = new HashMap<>();
        param.put("startindex", requestData.getStartindex());
        param.put("pagesize", requestData.getPagesize());
        param.put("pagingOrNot", "1");

        // 查询结果
        List<Map<String, String>> dataList = this.orderRepository.getOrderList(param);
        log.info("查询出的结果为：{}", dataList);
        // 条数
        int cnt = this.orderRepository.getOrderCnt(param);
        log.info("查询出的数据条数为：{}", cnt);

        WebResponse<OrderResponse> responseData = new WebResponse<>();
        OrderResponse orderResponse = new OrderResponse();
        orderResponse.setDraw(0);
        orderResponse.setTotalcount(cnt);
        orderResponse.setOrderlist(dataList);
        responseData.setResponse(orderResponse);
        log.info("订单查询返回的数据为：\n", JSON.toJSONString(responseData, SerializerFeature.PrettyFormat));

        // 返回
        return JSON.toJSONString(responseData, SerializerFeature.WriteMapNullValue);
    }
}
