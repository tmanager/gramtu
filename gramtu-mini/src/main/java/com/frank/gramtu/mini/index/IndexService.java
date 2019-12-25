package com.frank.gramtu.mini.index;

import com.alibaba.fastjson.JSON;
import com.alibaba.fastjson.serializer.SerializerFeature;
import com.frank.gramtu.core.response.WebResponse;
import lombok.extern.slf4j.Slf4j;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

import java.util.List;
import java.util.Map;

/**
 * 小程序首页Service.
 *
 * @author 张孝党 2019/12/25.
 * @version V1.00.
 * <p>
 * 更新履历： V1.00 2019/12/25. 张孝党 创建.
 */
@Slf4j
@Service
public class IndexService {

    @Autowired
    private IndexRepository indexRepository;

    /**
     * 首页数据查询.
     */
    public String queryService() {

        // 查询广告数据
        List<Map<String, String>> advertDataList = this.indexRepository.getAdvertList();
        log.info("查询出的广告数据为：{}", advertDataList);

        // 返回报文
        WebResponse<IndexResponse> responseData = new WebResponse<>();
        IndexResponse indexResponse = new IndexResponse();
        responseData.setResponse(indexResponse);

        // 广告数据
        if(!advertDataList.equals(null) && advertDataList.size() > 0) {
            indexResponse.setAdlist(advertDataList);
        }

        // 返回
        //return JSON.toJSONString(responseData, SerializerFeature.PrettyFormat);
        return JSON.toJSONString(responseData);
    }
}
