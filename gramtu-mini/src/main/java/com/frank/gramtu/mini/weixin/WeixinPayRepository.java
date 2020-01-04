package com.frank.gramtu.mini.weixin;

import com.alibaba.fastjson.JSON;
import com.alibaba.fastjson.serializer.SerializerFeature;
import com.frank.gramtu.core.response.SysErrResponse;
import com.frank.gramtu.core.response.WebResponse;
import com.frank.gramtu.core.utils.CommonUtil;
import com.frank.gramtu.core.utils.IdGeneratorUtils;
import com.frank.gramtu.mini.config.WxPayConfigBean;
import com.github.binarywang.wxpay.bean.notify.WxPayOrderNotifyResult;
import com.github.binarywang.wxpay.bean.request.WxPayUnifiedOrderRequest;
import com.github.binarywang.wxpay.bean.result.WxPayUnifiedOrderResult;
import com.github.binarywang.wxpay.config.WxPayConfig;
import com.github.binarywang.wxpay.service.WxPayService;
import com.github.binarywang.wxpay.service.impl.WxPayServiceImpl;
import com.github.binarywang.wxpay.util.SignUtils;
import lombok.extern.slf4j.Slf4j;
import org.apache.ibatis.annotations.Mapper;
import org.springframework.beans.factory.InitializingBean;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Repository;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import java.text.DecimalFormat;
import java.util.Arrays;
import java.util.HashMap;
import java.util.Map;

/**
 * 微信支付Repository.
 *
 * @author 张孝党 2020/01/04.
 * @version V1.00.
 * <p>
 * 更新履历： V1.00 2020/01/04. 张孝党 创建.
 */
@Mapper
@Repository
public interface WeixinPayRepository {

    /**
     * 更新订单的商户订单号.
     */
    int updOrderByOrderId(Map<String, String> param);

    /**
     * 新增支付流水.
     */
    int insJyLs(Map<String, String> param);

    /**
     * 更新订单的商户订单号.
     */
    int updOrderByTradeNo(Map<String, String> param);
}
