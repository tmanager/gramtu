package com.frank.gramtu.web.advert;

import com.alibaba.fastjson.JSON;
import com.frank.gramtu.core.response.WebResponse;
import lombok.extern.slf4j.Slf4j;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

import java.util.HashMap;
import java.util.List;
import java.util.Map;

/**
 * 轮播广告管理Service.
 *
 * @author 张孝党 2019/12/21.
 * @version V1.00.
 * <p>
 * 更新履历： V1.00 2019/12/21 张孝党 创建.
 */
@Slf4j
@Service
public class AdvertService {

    @Autowired
    private AdvertRepository advertRepository;

    /**
     * 查询数据.
     */
    public String queryService(AdvertRequest requestData) {

        // 广告名称
        String title = requestData.getTitle();

        // 查询参数
        Map<String, Object> param = new HashMap<>();
        param.put("title", title);
        param.put("startindex", requestData.getStartindex());
        param.put("pagesize", requestData.getPagesize());
        param.put("pagingOrNot", "1");

        // 查询结果
        List<Map<String, String>> dataList = this.advertRepository.getAdvertList(param);
        log.info("查询出的结果为：{}", dataList);
        // 条数
        int cnt = this.advertRepository.getCnt(param);
        log.info("查询出的数据条数为：{}", cnt);


        WebResponse<AdvertResponse> responseData = new WebResponse<>();
        AdvertResponse advertReponse = new AdvertResponse();
        advertReponse.setDraw(0);
        advertReponse.setTotalcount(cnt);
        advertReponse.setAdlist(dataList);
        responseData.setResponse(advertReponse);

        // 返回
        return JSON.toJSONString(responseData);
    }
}
