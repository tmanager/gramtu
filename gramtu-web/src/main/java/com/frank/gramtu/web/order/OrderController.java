package com.frank.gramtu.web.order;


import com.alibaba.fastjson.JSON;
import com.alibaba.fastjson.TypeReference;
import com.frank.gramtu.core.request.WebRequest;
import lombok.extern.slf4j.Slf4j;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.web.bind.annotation.CrossOrigin;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;

/**
 * 订单查询Controller.
 *
 * @author 张孝党 2020/01/14.
 * @version V1.00.
 * <p>
 * 更新履历： V1.00 2020/01/14 张孝党 创建.
 */
@Slf4j
@RestController
@CrossOrigin(origins = "*")
@RequestMapping("/order")
public class OrderController {

    @Autowired
    private OrderService orderService;

    /**
     * 订单查询.
     */
    @RequestMapping("/query")
    public String query(@RequestBody String requestParam) {
        log.info("查询订单一览开始..................");

        log.info("请求参数为：{}", requestParam);
        WebRequest<OrderRequest> requestData = JSON.parseObject(requestParam, new TypeReference<WebRequest<OrderRequest>>() {
        });

        String responseData = this.orderService.queryService(requestData.getRequest());

        log.info("查询订单一览结束..................");
        log.info("返回值为:{}", responseData);
        return responseData;
    }
}
