package com.frank.gramtu.web.feature;

import com.alibaba.fastjson.JSON;
import com.frank.gramtu.core.response.WebResponse;
import com.frank.gramtu.web.advert.AdvertResponse;
import lombok.extern.slf4j.Slf4j;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

import java.util.HashMap;
import java.util.List;
import java.util.Map;

/**
 * 特色服务管理Service.
 *
 * @author 张孝党 2019/12/23.
 * @version V1.00.
 * <p>
 * 更新履历： V1.00 2019/12/23 张孝党 创建.
 */
@Slf4j
@Service
public class FeatureService {

    @Autowired
    private FeatureRepository featureRepository;

    /**
     * 查询数据.
     */
    public String queryService(FeatureRequest requestData) {

        // 服务名称
        String serviceName = requestData.getServname();

        // 查询参数
        Map<String, Object> param = new HashMap<>();
        param.put("servname", serviceName);
        param.put("startindex", requestData.getStartindex());
        param.put("pagesize", requestData.getPagesize());
        param.put("pagingOrNot", "1");

        // 查询结果
        List<Map<String, String>> dataList = this.featureRepository.getServiceList(param);
        log.info("查询出的结果为：{}", dataList);
        // 条数
        int cnt = this.featureRepository.getCnt(param);
        log.info("查询出的数据条数为：{}", cnt);


        WebResponse<FeatureReponse> responseData = new WebResponse<>();
        FeatureReponse advertReponse = new FeatureReponse();
        advertReponse.setDraw(0);
        advertReponse.setTotalcount(cnt);
        advertReponse.setServlist(dataList);
        responseData.setResponse(advertReponse);

        // 返回
        return JSON.toJSONString(responseData);
    }
}
