package com.frank.gramtu.mini.mark;

import com.alibaba.fastjson.JSON;
import com.alibaba.fastjson.TypeReference;
import com.frank.gramtu.core.request.WebRequest;
import com.frank.gramtu.mini.coupon.CouponRequest;
import lombok.extern.slf4j.Slf4j;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.web.bind.annotation.CrossOrigin;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;

import java.util.HashMap;
import java.util.Map;

/**
 * 上程序端积分信息Controller.
 *
 * @author 张孝党 2020/01/08.
 * @version V1.00.
 * <p>
 * 更新履历： V1.00 2020/01/08. 张孝党 创建.
 */
@Slf4j
@RestController
@CrossOrigin(value = "*")
@RequestMapping(value = "/mark")
public class MarkController {

    @Autowired
    private MarkService markService;

    /**
     * 小程序端个人积分查询.
     */
    @RequestMapping("/query")
    public String queryCoupon(@RequestBody String requestParam) {
        log.info("小程序端查询个人积分开始............");

        log.info("请求参数为：{}", requestParam);
        WebRequest<MarkRequest> requestData = JSON.parseObject(requestParam, new TypeReference<WebRequest<MarkRequest>>() {
        });

        // 调用服务
        String responseData = this.markService.queryService(requestData.getRequest());
        log.info("返回小程序端查询个人积分的数据为:\n{}", responseData);

        log.info("小程序端查询个人积分结束............");
        return responseData;
    }

    /**
     * 积分转赠.
     */
    @RequestMapping("/given")
    public String given(@RequestBody String requestParam) {
        log.info("小程序端优惠券转赠开始............");

        log.info("请求参数为：{}", requestParam);
        WebRequest<MarkRequest> requestData = JSON.parseObject(requestParam, new TypeReference<WebRequest<MarkRequest>>() {
        });

        // 调用服务
        //String responseData = this.couponService.givenService(requestData.getRequest());
        String responseData = "";
        log.info("返回小程序端优惠券转赠的数据为:\n{}", responseData);

        log.info("小程序端优惠券转赠结束............");
        return responseData;
    }
}
