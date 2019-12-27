package com.frank.gramtu.mini.login;

import org.apache.ibatis.annotations.Mapper;
import org.springframework.stereotype.Repository;

import java.util.Map;

/**
 * 小程序登录/注册Repository.
 *
 * @author 张孝党 2019/12/27.
 * @version V1.00.
 * <p>
 * 更新履历： V1.00 2019/12/27. 张孝党 创建.
 */
@Mapper
@Repository
public interface LoginRepository {

    /**
     * 根据openId判断是否已注册.
     */
    Map<String, String> getWxUserCnt(Map<String, String> param);

    /**
     * 新增微信用户信息.
     */
    int insWxUserInfo(Map<String, String> param);

    /**
     * 更新微信用户信息.
     */
    int updWxUserInfo(Map<String, String> param);
}
