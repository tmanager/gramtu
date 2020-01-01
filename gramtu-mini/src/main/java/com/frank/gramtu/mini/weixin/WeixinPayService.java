package com.frank.gramtu.mini.weixin;

import com.frank.gramtu.core.utils.CommonUtil;
import com.frank.gramtu.core.utils.DateTimeUtil;
import com.frank.gramtu.mini.config.WxPayConfigBean;
import com.github.binarywang.wxpay.bean.request.WxPayUnifiedOrderRequest;
import com.github.binarywang.wxpay.bean.result.WxPayUnifiedOrderResult;
import com.github.binarywang.wxpay.config.WxPayConfig;
import com.github.binarywang.wxpay.service.WxPayService;
import com.github.binarywang.wxpay.service.impl.WxPayServiceImpl;
import lombok.extern.slf4j.Slf4j;
import org.springframework.beans.factory.InitializingBean;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

import java.util.HashMap;
import java.util.Map;

/**
 * 微信支付Service.
 *
 * @author 张孝党 2020.01/01.
 * @version V1.00.
 * <p>
 * 更新履历： V1.00 2020.01/01. 张孝党 创建.
 */
@Slf4j
@Service
public class WeixinPayService implements InitializingBean {

    @Autowired
    private WxPayConfigBean wxPayConfigBean;

    private WxPayConfig wxPayConfig;

    @Override
    public void afterPropertiesSet() throws Exception {
        this.wxPayConfig = new WxPayConfig();
        this.wxPayConfig.setAppId(this.wxPayConfigBean.getAppID());
        this.wxPayConfig.setMchId(this.wxPayConfigBean.getMchId());
        this.wxPayConfig.setMchKey(this.wxPayConfigBean.getMchKey());
        this.wxPayConfig.setKeyPath(this.wxPayConfigBean.getKeyPath());
    }

    /**
     * 统一下单.
     */
    public void unifiedOrderService() throws Exception {

        // 返回报文.
        Map<String, String> rspMap = new HashMap<>();

        // 请求参数
        WxPayUnifiedOrderRequest orderRequest = new WxPayUnifiedOrderRequest();
        // 公众账号ID
        orderRequest.setAppid(this.wxPayConfigBean.getAppID());
        // 商户号
        orderRequest.setMchId(this.wxPayConfigBean.getMchId());
        // 随机字符串
        orderRequest.setNonceStr(CommonUtil.getUUid());
        // 商品描述
        orderRequest.setBody("查重费用");
        // 商户订单号
        String tradeNo = DateTimeUtil.getCurrentTime();
        log.info("微信支付下单商户订单号为：" + tradeNo);
        orderRequest.setOutTradeNo(tradeNo);
        // 标价金额
        orderRequest.setTotalFee(Integer.valueOf("1"));
        // 终端IP
        String ip = "127.0.0.1";
        log.info("取得的终端IP为：" + ip);
        orderRequest.setSpbillCreateIp(ip);
        // 通知地址
        orderRequest.setNotifyUrl(this.wxPayConfigBean.getNotifyUrl());
        // 交易类型
        orderRequest.setTradeType("JSAPI");
        // 用户标识
        orderRequest.setOpenid("oroId5J-0u5KQYS6EgMyDgPNDGHY");

        log.info("签名>>>>>>>>>>[{}]", orderRequest.getSign());

        // 调用服务
        WxPayService wxPayService = new WxPayServiceImpl();
        wxPayService.setConfig(this.wxPayConfig);
        // 后台请求
        WxPayUnifiedOrderResult orderResult = wxPayService.unifiedOrder(orderRequest);

        if (orderResult.getReturnCode().equals("SUCCESS") && orderResult.getResultCode().equals("SUCCESS")) {
            // 下单成功
            log.info("微信支付统一下单成功");
            log.info(orderResult.getSign());
            log.info(orderResult.getPrepayId());
        } else {
            // 下单失败
            log.info("微信支付统一下单失败");
        }
    }

}
