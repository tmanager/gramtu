package com.frank.gramtu.mini.weixin;

import lombok.extern.slf4j.Slf4j;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.web.bind.annotation.CrossOrigin;
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
    public String unifiedorder() {

        try {
            this.weixinPayService.unifiedOrderService();
            return "success";
        } catch (Exception ex) {
            log.info("下单异常:\n{}", ex.getMessage());
            return "error";
        }
    }

}
