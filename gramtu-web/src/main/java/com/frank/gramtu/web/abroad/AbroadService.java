package com.frank.gramtu.web.abroad;

import com.alibaba.fastjson.JSON;
import com.frank.gramtu.core.response.WebResponse;
import com.frank.gramtu.web.feature.FeatureReponse;
import com.frank.gramtu.web.feature.FeatureRequest;
import lombok.extern.slf4j.Slf4j;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

import java.util.HashMap;
import java.util.List;
import java.util.Map;

/**
 * 海外招聘管理Service.
 *
 * @author 张孝党 2019/12/23.
 * @version V1.00.
 * <p>
 * 更新履历： V1.00 2019/12/23 张孝党 创建.
 */
@Slf4j
@Service
public class AbroadService {

    @Autowired
    private AbroadRepository abroadRepository;

    /**
     * 查询数据.
     */
    public String queryService(AbroadRequest requestData) {

        // 名称
        String title = requestData.getTitile();

        // 查询参数
        Map<String, Object> param = new HashMap<>();
        param.put("title", title);
        param.put("startindex", requestData.getStartindex());
        param.put("pagesize", requestData.getPagesize());
        param.put("pagingOrNot", "1");

        // 查询结果
        List<Map<String, String>> dataList = this.abroadRepository.getAbroadList(param);
        log.info("查询出的结果为：{}", dataList);
        // 条数
        int cnt = this.abroadRepository.getCnt(param);
        log.info("查询出的数据条数为：{}", cnt);


        WebResponse<AbroadResponse> responseData = new WebResponse<>();
        AbroadResponse abroadResponse = new AbroadResponse();
        abroadResponse.setDraw(0);
        abroadResponse.setTotalcount(cnt);
        abroadResponse.setAbroadlist(dataList);
        responseData.setResponse(abroadResponse);

        // 返回
        return JSON.toJSONString(responseData);
    }
}
