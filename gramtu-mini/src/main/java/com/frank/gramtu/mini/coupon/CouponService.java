package com.frank.gramtu.mini.coupon;

import com.alibaba.fastjson.JSON;
import com.alibaba.fastjson.serializer.SerializerFeature;
import com.frank.gramtu.core.response.WebResponse;
import com.frank.gramtu.mini.constant.CheckType;
import lombok.extern.slf4j.Slf4j;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

import java.util.HashMap;
import java.util.List;
import java.util.Map;

/**
 * 上程序端优惠券Service.
 *
 * @author 张孝党 2020/01/06.
 * @version V1.00.
 * <p>
 * 更新履历： V1.00 2020/01/06. 张孝党 创建.
 */
@Slf4j
@Service
public class CouponService {

    @Autowired
    private CouponRepository couponRepository;

    /**
     * 小程序端个人优惠券查询.
     */
    public String queryCouponService(CouponRequest requestData) {

        // 根据openid查询优惠券列表
        Map<String, String> param = new HashMap<>();
        param.put("openid", requestData.getOpenId());
        // 语法检测
        if (requestData.getCheckType().equals(CheckType.GRAMMARLY.getValue())) {
            param.put("type", "1");
        } else {
            // 查重
            param.put("type", "0");
        }

        List<Map<String, String>> couponList = this.couponRepository.getCouponList(param);
        log.info("查询出的个人优惠券信息为：{}", couponList);

        WebResponse<CouponResponse> responseData = new WebResponse<>();
        CouponResponse couponResponse = new CouponResponse();
        responseData.setResponse(couponResponse);
        // 优惠券列表
        couponResponse.setCouponlist(couponList);

        log.info("返回信息为：\n{}", JSON.toJSONString(responseData, SerializerFeature.PrettyFormat));

        // 返回
        return JSON.toJSONString(responseData);
    }
}