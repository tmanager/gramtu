package com.frank.gramtu.mini.mark;

import org.apache.ibatis.annotations.Mapper;
import org.springframework.stereotype.Repository;

import java.util.List;
import java.util.Map;

/**
 * 上程序端积分信息Repository.
 *
 * @author 张孝党 2020/01/08.
 * @version V1.00.
 * <p>
 * 更新履历： V1.00 2020/01/08. 张孝党 创建.
 */
@Mapper
@Repository
public interface MarkRepository {

    /**
     * 根据openid获取积分.
     */
    Map<String, String> getMarkByOpenId(Map<String, Object> param);

    /**
     * 根据openid获取积分列表.
     */
    List<Map<String, String>> getMarkListByOpenId(Map<String, Object> param);
}
