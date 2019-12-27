package com.frank.gramtu.mini.crontab;

import com.alibaba.fastjson.JSONObject;
import com.frank.gramtu.core.utils.HttpUtil;
import com.frank.gramtu.mini.config.WeixinMiniConfigBean;
import lombok.extern.slf4j.Slf4j;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.scheduling.annotation.Scheduled;
import org.springframework.stereotype.Component;

import java.text.MessageFormat;

/**
 * 获取AccessToken定时任务.
 *
 * @author 张孝党 2019/12/27.
 * @version V0.0.1.
 * <p>
 * 更新履历： V0.0.1 2019/12/27 张孝党 创建.
 */
@Slf4j
@Component
public class AccessTokenCrontab {

    @Autowired
    private WeixinMiniConfigBean weixinMiniConfigBean;

    /**
     * 每90分钟执行一次.
     */
    @Scheduled(fixedDelay = 90 * 60 * 1000)
    public void getAccessToken() {
        log.info("定时任务获取access token开始........................");

        String accessToken = "";
        try {
            // 拼接URL
            String url = MessageFormat.format(weixinMiniConfigBean.getAccessTokenUrl(), weixinMiniConfigBean.getAppID(), weixinMiniConfigBean.getAppSecret());
            log.info("获取accessToken的URL为：{}", url);

            // 发送GET请求
            JSONObject result = JSONObject.parseObject(HttpUtil.getResponseWithGET(url));
            log.info("获取accesstoken的结果为：{}", result);

            if (!result.containsKey("errcode")) {
                accessToken = result.getString("access_token");
            } else {
                log.info("返回异常，信息为：{}", result.getString("errmsg"));
            }
        } catch (Exception ex) {
            log.error("定时任务获取access token时异常:" + ex.getMessage());
        }
        log.info("定时任务获取access token结束........................");
    }
}
