package com.frank.gramtu.mini.weixin;

import com.alibaba.fastjson.JSON;
import com.alibaba.fastjson.JSONObject;
import com.alibaba.fastjson.serializer.SerializerFeature;
import com.frank.gramtu.core.bean.RequestTurnitinBean;
import com.frank.gramtu.core.bean.ResponseTurnitinBean;
import com.frank.gramtu.core.bean.TurnitinConst;
import com.frank.gramtu.core.redis.RedisService;
import com.frank.gramtu.core.utils.CommonUtil;
import com.frank.gramtu.core.utils.DateTimeUtil;
import com.frank.gramtu.core.utils.FileUtils;
import com.frank.gramtu.core.utils.SocketClient;
import com.frank.gramtu.mini.constant.CheckType;
import com.github.binarywang.wxpay.bean.notify.WxPayOrderNotifyResult;
import lombok.extern.slf4j.Slf4j;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.scheduling.annotation.Async;
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
    private RedisService redisService;

    @Autowired
    private WeixinPayRepository weixinPayRepository;

    /**
     * 根据商户订单号核销优惠券.
     */
    @Async
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
    @Async
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
    @Async
    @Transactional(rollbackFor = Exception.class)
    public void submitThesis(String tradeNo) {

        // 查询订单信息
        Map<String, String> param = new HashMap<>();
        param.put("tradeno", tradeNo);
        Map<String, String> orderInfo = this.weixinPayRepository.getOrderInfoByTradeno(param);
        if (orderInfo == null) {
            log.error(">>>>>>>>>>>>>>商户订单号[{}]未找到,请联系管理员!!!>>>>>>>>>>>>>>", tradeNo);
        }
        log.info("商户订单号[{}]的信息为：\n{}", tradeNo, JSON.toJSONString(orderInfo, SerializerFeature.PrettyFormat));

        // 判断提交的类型
        String checkType = orderInfo.get("checktype");
        if (checkType.equals(CheckType.TURNITIN.getValue())) {
            // 国际版
            this.submitInThesis(orderInfo);
        } else if (checkType.equals(CheckType.TURNITINUK.getValue())) {
            // UK版
            this.submitUKThesis(orderInfo);
        } else if (checkType.equals(CheckType.GRAMMARLY.getValue())) {
            // Grammarly
        } else {
            log.error("提交类型不符合,无法检测!!!");
        }
    }

    /**
     * 提交国际版.
     */
    private void submitInThesis(Map<String, String> orderInfo) {
        log.info("提交国际版论文开始......................");

        try {
            RequestTurnitinBean turnBean = JSONObject.parseObject(this.redisService.getStringValue(TurnitinConst.TURN_IN_KEY),
                    RequestTurnitinBean.class);
            log.info("查询出的国际版账户信息为：\n{}", JSON.toJSONString(turnBean, SerializerFeature.PrettyFormat));

            // 保存论文信息
            String thesisName = orderInfo.get("orderid") + "." + orderInfo.get("ext");
            log.info("保存的论文名称为：[{}]", thesisName);
            boolean dowloadResult = FileUtils.downloadFromHttpUrl(orderInfo.get("originalurl"), turnBean.getThesisVpnPath(), thesisName);
            if (!dowloadResult) {
                log.error("从FDFS下载论文时异常");
                return;
            }

            // 设计参数
            turnBean.setFirstName(orderInfo.get("firstname"));
            turnBean.setLastName(orderInfo.get("lastname"));
            turnBean.setTitle(orderInfo.get("titile"));
            turnBean.setThesisName(thesisName);

            // 调用Socket
            String result = SocketClient.callServer(TurnitinConst.SOCKET_SERVER, TurnitinConst.SOCKET_PORT,
                    "02" + JSONObject.toJSONString(turnBean));
            ResponseTurnitinBean responseTurnitinBean = JSONObject.parseObject(result, ResponseTurnitinBean.class);
            log.info("调用Socket Server返回的结果为：\n{}", JSON.toJSONString(responseTurnitinBean, SerializerFeature.PrettyFormat));

            // 输出日志
            for (String logInfo : responseTurnitinBean.getLogList()) {
                log.info("turnitin-api>>>>>>" + logInfo);
            }
            // 提交失败
            if (!responseTurnitinBean.getRetcode().equals("0000")) {
                log.error("提交论文失败");
            }

            // 论文ID
            String thesisId = responseTurnitinBean.getThesisId();
            // 更新论文ID
            this.updThesisId(orderInfo.get("tradeno"), thesisId);
        } catch (Exception ex) {
            log.error("提交国际版论文异常结束，异常信息为：\n{}", ex.getMessage());
        }
    }

    /**
     * 提交UK版.
     */
    private void submitUKThesis(Map<String, String> orderInfo) {

    }

    /**
     * 提交Grammarly语法检测.
     */
    private void submitGrammarly(Map<String, String> orderInfo) throws Exception {

    }

    /**
     * 根据支付商户订单号更新论文ID.
     */
    private void updThesisId(String tradeNo, String thesisId) {

        Map<String, String> param = new HashMap<>();
        param.put("tradeno", tradeNo);
        param.put("thesisid", thesisId);
        int cnt = this.weixinPayRepository.updThesisIdByTradeNo(param);
        log.info("更新论文ID的结果为：[{}]", cnt);
    }
}
