package com.frank.gramtu.mini.weixin;

import com.alibaba.fastjson.JSON;
import com.alibaba.fastjson.TypeReference;
import com.frank.gramtu.core.request.WebRequest;
import com.frank.gramtu.core.response.SysErrResponse;
import lombok.extern.slf4j.Slf4j;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.web.bind.annotation.CrossOrigin;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;

/**
 * 微信支付Controller.
 *
 * @author 张孝党 2020.01/01.
 * @version V1.00.
 * <p>
 * 更新履历： V1.00 2020.01/01. 张孝党 创建.
 */
@Slf4j
@RestController
@CrossOrigin(value = "*")
@RequestMapping(value = "/wxpay")
public class WeixinPayController {

    @Autowired
    private WeixinPayService weixinPayService;

    /**
     * 统一下单.
     */
    @RequestMapping("/unifiedorder")
    public String unifiedorder(@RequestBody String requestParam) {
        log.info("微信支付统一下单开始................");

        log.info("请求参数为：{}", requestParam);
        WebRequest<WeixinPayRequest> requestData = JSON.parseObject(requestParam, new TypeReference<WebRequest<WeixinPayRequest>>() {
        });
        try {
            String responseData = this.weixinPayService.unifiedOrderService(requestData.getRequest());
            log.info("统一下单返回的信息为：{}", requestData);

            log.info("微信支付统一下单结束................");
            return responseData;
        } catch (Exception ex) {
            log.info("微信支付统一下单异常结束................");
            log.info("下单异常:\n{}", ex.getMessage());
            return new SysErrResponse("微信支付统一下单异常").toJsonString();
        }
    }

}
