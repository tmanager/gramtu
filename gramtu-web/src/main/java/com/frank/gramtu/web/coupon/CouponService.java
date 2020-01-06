package com.frank.gramtu.web.coupon;

import com.alibaba.fastjson.JSON;
import com.frank.gramtu.core.request.WebRequest;
import com.frank.gramtu.core.response.SysResponse;
import com.frank.gramtu.core.response.WebResponse;
import com.frank.gramtu.core.utils.CommonUtil;
import com.frank.gramtu.core.utils.DateTimeUtil;
import lombok.extern.slf4j.Slf4j;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import java.util.HashMap;
import java.util.List;
import java.util.Map;

/**
 * 优惠券Service.
 *
 * @author 张孝党 2020/01/07.
 * @version V1.00.
 * <p>
 * 更新履历： V1.00 2020/01/07 张孝党 创建.
 */
@Slf4j
@Service
public class CouponService {

    @Autowired
    private CouponRepository couponRepository;

    /**
     * 查询文章一览.
     */
    public String queryService(CouponRequest requestData) {

        String title = requestData.getTitle();

        // 查询参数
        Map<String, Object> param = new HashMap<>();
        param.put("title", title);
        param.put("startindex", requestData.getStartindex());
        param.put("pagesize", requestData.getPagesize());
        param.put("pagingOrNot", "1");

        // 查询结果
        List<Map<String, String>> dataList = this.couponRepository.getCouponList(param);
        log.info("查询出的结果为：{}", dataList);
        // 条数
        int cnt = this.couponRepository.getCnt(param);
        log.info("查询出的数据条数为：{}", cnt);

        WebResponse<CouponReponse> responseData = new WebResponse<>();
        CouponReponse couponReponse = new CouponReponse();
        couponReponse.setDraw(0);
        couponReponse.setTotalcount(cnt);
        couponReponse.setCouponList(dataList);
        responseData.setResponse(couponReponse);

        return JSON.toJSONString(responseData);
    }

    /**
     * 新增文章.
     */
    @Transactional(rollbackFor = Exception.class)
    public String couponAddService(WebRequest<CouponRequest> requestData) {

        // 参数
        Map<String, String> param = new HashMap<>();
        // id主键
        param.put("id", CommonUtil.getUUid());
        // 用户id
        param.put("userid", requestData.getUserid());
        // 更新时间
        param.put("updtime", DateTimeUtil.getTimeformat());
        // 标题
        param.put("title", requestData.getRequest().getTitle());
        // 封面
        param.put("cover", requestData.getRequest().getCoverimage());
        // 文章内容
        param.put("article", requestData.getRequest().getContent());

        // 新增
        int cnt = this.couponRepository.addCoupon(param);
        log.info("新增数据条数为：[{}]", cnt);

        // 返回
        return new SysResponse().toJsonString();
    }

    /**
     * 根据文章ID获取文章内容.
     */
    public String artDetailService(String artId) {

        // 查询参数
        Map<String, String> param = new HashMap<>();
        param.put("artid", artId);

        // 获取文章详细内容
        Map<String, String> artDetail = this.couponRepository.getCouponDetail(param);

        WebResponse<CouponReponse> responseData = new WebResponse<>();
        CouponReponse couponReponse = new CouponReponse();
        // 标题
        couponReponse.setTitle(artDetail.get("title"));
        // 发布时间
        couponReponse.setTime(artDetail.get("updtime"));
        // 发布者
        couponReponse.setEditor(artDetail.get("uname"));
        // 文章内容
        couponReponse.setContent(artDetail.get("article"));
        responseData.setResponse(couponReponse);

        // 返回
        return JSON.toJSONString(responseData);
    }

    /**
     * 删除文章.
     */
    @Transactional(rollbackFor = Exception.class)
    public String delCouponService(String[] artidList) {

        for (String artId :artidList) {
            Map<String, String> param = new HashMap<>();
            param.put("artid", artId);
            this.couponRepository.deleteCoupon(param);
            log.info("文章[{}]被删除", artId);
        }

        // 返回
        return new SysResponse().toJsonString();
    }

    /**
     * 编辑保存文章.
     */
    @Transactional(rollbackFor = Exception.class)
    public String editCouponService(WebRequest<CouponRequest> requestData) {

        // 参数
        Map<String, String> param = new HashMap<>();
        // ID
        param.put("artid", requestData.getRequest().getArtid());
        // 标题
        param.put("title",requestData.getRequest().getTitle());
        // 封面
        if(!requestData.getRequest().getCoverimage().equals(requestData.getRequest().getOldimage())) {
            param.put("cover", requestData.getRequest().getCoverimage());
        }
        // 内容
        param.put("article",requestData.getRequest().getContent());
        // 更新时间
        param.put("updtime", DateTimeUtil.getTimeformat());
        // 更新人
        param.put("upduid", requestData.getUserid());
        this.couponRepository.updCoupon(param);

        // 返回
        return new SysResponse().toJsonString();
    }
}
