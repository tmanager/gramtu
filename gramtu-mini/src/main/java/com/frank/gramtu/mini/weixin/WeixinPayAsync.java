package com.frank.gramtu.mini.weixin;

import com.alibaba.fastjson.JSON;
import com.alibaba.fastjson.serializer.SerializerFeature;
import com.frank.gramtu.core.utils.CommonUtil;
import com.frank.gramtu.core.utils.DateTimeUtil;
import com.github.binarywang.wxpay.bean.notify.WxPayOrderNotifyResult;
import lombok.extern.slf4j.Slf4j;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Component;
import org.springframework.transaction.annotation.Transactional;

import java.util.HashMap;
import java.util.Map;

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

    @Autowired
    private WeixinPayRepository weixinPayRepository;

    /**
     * 根据商户订单号核销优惠券.
     */
    @Transactional(rollbackFor = Exception.class)
    public void writeOffCoupon(String tradeNo) {
        log.info("商户订单号[{}]核销优惠券开始.................", tradeNo);

        try {
            Map<String, String> param = new HashMap<>();
            param.put("tradeno", tradeNo);

            // 查询优惠券信息
            Map<String, String> result = this.weixinPayRepository.getCouponByTradeNo(param);
            log.info("查询出的优惠券信息为：[{}]", JSON.toJSONString(result, SerializerFeature.PrettyFormat));

            if (result == null || result.isEmpty()) {
                log.info("商户订单号[{}]没有使用优惠券!", tradeNo);
            } else {
                // 更新优惠券状态为1:已使用
                Map<String, String> param2 = new HashMap<>();
                param2.put("couponid", result.get("couponid"));
                param2.put("couponstatus", "1");
                param2.put("usetime", DateTimeUtil.getTimeformat());
                int cnt = this.weixinPayRepository.updCouponStatus(param2);
                log.info("优惠券[{}]更新为已使用!", result.get("couponid"));
            }

        } catch (Exception ex) {
            log.info("商户订单号[{}]核销优惠券时异常,信息：\n{}", tradeNo, ex.getMessage());
        }

        log.info("商户订单号[{}]核销优惠券结束.................", tradeNo);
    }

    /**
     * 根据商户订单号增加用户积分.
     */
    @Transactional(rollbackFor = Exception.class)
    public void writeOffMark(WxPayOrderNotifyResult notifyResult) {

        Map<String, String> param = new HashMap<>();
        // ID
        param.put("id", CommonUtil.getUUid());
        // openid
        param.put("openid", notifyResult.getOpenid());
        // 微信支付商户流水号
        param.put("tradeno", notifyResult.getOutTradeNo());
        // 支付金额,单位为分
        param.put("fee", String.valueOf(notifyResult.getTotalFee()));
        // 分录，0:增加积分,1:积分兑换,2:转增他人,3:他人赠与
        param.put("entry", "0");

        // 本次消费积分
        int mark = notifyResult.getTotalFee() / 100;
        log.info("增加的个人积分为:[{}]", mark);
        param.put("mark", String.valueOf(mark));

        // 更新时间
        param.put("updtime", DateTimeUtil.getTimeformat());
        int cnt = this.weixinPayRepository.insMarkHis(param);
        log.info("新增个人积分结果为：[{}]", cnt);
    }

    /**
     * 发起提交论文异步任务.
     *
     * @param tradeNo 商户订单号.
     */
    public void submitThesis(String tradeNo) {

    }

}
