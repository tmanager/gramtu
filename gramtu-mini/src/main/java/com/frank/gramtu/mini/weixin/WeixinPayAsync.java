package com.frank.gramtu.mini.weixin;

import lombok.extern.slf4j.Slf4j;
import org.springframework.stereotype.Component;

/**
 * 微信支付完成后异步任务.
 *
 * @author 张孝党 2020.01/06.
 * @version V1.00.
 * <p>
 * 更新履历： V1.00 2020.01/06. 张孝党 创建.
 */

@Slf4j
@Component
public class WeixinPayAsync {

    /**
     * 发起提交论文异步任务.
     * 
     * @param tradeNo 商户订单号.
     */
    public void submitThesis(String tradeNo) {

    }

}
