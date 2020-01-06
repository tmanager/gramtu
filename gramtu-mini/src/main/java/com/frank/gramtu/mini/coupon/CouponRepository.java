package com.frank.gramtu.mini.coupon;

import org.apache.ibatis.annotations.Mapper;
import org.springframework.stereotype.Repository;

import java.util.List;
import java.util.Map;

/**
 * 上程序端优惠券Repository.
 *
 * @author 张孝党 2020/01/06.
 * @version V1.00.
 * <p>
 * 更新履历： V1.00 2020/01/06. 张孝党 创建.
 */
@Mapper
@Repository
public interface CouponRepository {

    /**
     * 根据openid查询可用优惠券.
     */
    List<Map<String, String>> getCouponList(Map<String, String> param);
}
