package com.frank.gramtu.mini.login;

import com.alibaba.fastjson.JSON;
import com.alibaba.fastjson.JSONObject;
import com.alibaba.fastjson.serializer.SerializerFeature;
import com.frank.gramtu.core.response.SysResponse;
import com.frank.gramtu.core.response.WebResponse;
import com.frank.gramtu.core.utils.CommonUtil;
import com.frank.gramtu.mini.weixin.WechatDecryptDataUtil;
import com.frank.gramtu.mini.weixin.WxApi;
import lombok.extern.slf4j.Slf4j;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

import java.util.HashMap;
import java.util.Map;

/**
 * 小程序登录/注册Service.
 *
 * @author 张孝党 2019/12/27.
 * @version V1.00.
 * <p>
 * 更新履历： V1.00 2019/12/27. 张孝党 创建.
 */
@Slf4j
@Service
public class LoginService {

    @Autowired
    private LoginRepository loginRepository;

    /**
     * 账号校验.
     */
    public String checkService(LoginRequest requestData) {

        String openId = "";
        String sessionKey = "";
        //String unionid = "";
        // 是否已注册
        String isRegister = "0";
        // 积分
        String mark = "0";

        // 获取微信用户openid
        WxApi api = new WxApi();
        JSONObject result = JSON.parseObject(api.getAuthCode2Session(requestData.getCode()));
        // 成功
        if (!result.containsKey("errcode")) {
            openId = result.getString("openid");
            sessionKey = result.getString("session_key");
            //unionid = result.getString("unionid");

            // 判断用户是否注册
            Map<String, String> param = new HashMap<>();
            param.put("openid", openId);
            Map<String, String> wxUserInfo = this.loginRepository.getWxUserCnt(param);
            if (wxUserInfo == null || wxUserInfo.isEmpty()) {
                // 未注册
                isRegister = "0";
            } else {
                // 已注册
                isRegister = "1";
            }
        }

        // 返回报文
        WebResponse<LoginResponse> responseData = new WebResponse<>();
        LoginResponse loginResponse = new LoginResponse();
        responseData.setResponse(loginResponse);

        // 返回数据
        loginResponse.setOpenid(openId);
        loginResponse.setSession_key(sessionKey);
        //loginResponse.setUnionid(unionid);
        loginResponse.setRegister(isRegister);
        loginResponse.setMark(mark);

        // 返回
        log.info("返回的数据为：{}", JSON.toJSONString(responseData, SerializerFeature.PrettyFormat));
        return JSON.toJSONString(responseData);
    }

    /**
     * 更新用户信息.
     */
    public String updUserInfoService(LoginRequest requestData) {

        // 参数
        Map<String, String> param = new HashMap<>();
        param.put("openid", requestData.getOpenId());
        param.put("avatarurl", requestData.getAvatarUrl());
        param.put("nick_name", requestData.getNickName());
        param.put("gender", requestData.getGender());
        param.put("city", requestData.getCity());
        param.put("province", requestData.getProvince());
        param.put("country", requestData.getCountry());
        param.put("openid", requestData.getOpenId());

        // 用户未注册时
        if (requestData.getIsRegister().equals("0")) {
            // 手机号数据解密
            String phoneInfo = WechatDecryptDataUtil.decryptData(requestData.getEncryptedData(), requestData.getSessionKey(), requestData.getIv());
            log.info("手机号数据解密为：{}", phoneInfo);
            JSONObject jsonPhoneInfo = JSONObject.parseObject(phoneInfo);

            param.put("phonenumber", jsonPhoneInfo.getString("phoneNumber"));
            param.put("pure_phone_number", jsonPhoneInfo.getString("purePhoneNumber"));
            param.put("country_code", jsonPhoneInfo.getString("countryCode"));
        }

        // 未注册
        if (requestData.getIsRegister().equals("0")) {
            // 新增
            param.put("id", CommonUtil.getUUid());
            int cnt = this.loginRepository.insWxUserInfo(param);
            ;
            log.info("新增微信用户条数：{}", cnt);
        } else {
            // 已注册时更新
            int cnt = this.loginRepository.updWxUserInfo(param);
            log.info("更新用户信息条数：{}", cnt);
        }

        // 返回
        return new SysResponse().toJsonString();
    }
}
