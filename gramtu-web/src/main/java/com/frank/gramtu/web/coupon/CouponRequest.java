package com.frank.gramtu.web.coupon;

import com.frank.gramtu.core.response.BaseRequest;
import lombok.Getter;
import lombok.Setter;

/**
 * 优惠券请求报文.
 *
 * @author 张孝党 2020/01/07.
 * @version V0.0.1.
 * <p>
 * 更新履历： V0.0.1 2020/01/07 张孝党 创建.
 */
@Getter
@Setter
public class CouponRequest extends BaseRequest {

    // 文章ID
    private String artid = "";

    // 文章标题
    private String title = "";

    // 封面url
    private String coverimage = "";

    // 文章内容
    private String content = "";

    // 删除的文章id列表
    private String[] artidlist;

    // 旧封面
    private String oldimage = "";
}
