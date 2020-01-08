package com.frank.gramtu.mini.mark;

import com.alibaba.fastjson.JSON;
import com.alibaba.fastjson.serializer.SerializerFeature;
import com.frank.gramtu.core.response.WebResponse;
import lombok.extern.slf4j.Slf4j;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

import java.util.HashMap;
import java.util.List;
import java.util.Map;

/**
 * 上程序端积分信息Service.
 *
 * @author 张孝党 2020/01/08.
 * @version V1.00.
 * <p>
 * 更新履历： V1.00 2020/01/08. 张孝党 创建.
 */
@Slf4j
@Service
public class MarkService {

    @Autowired
    private MarkRepository markRepository;

    /**
     * 个人积分列表查询.
     */
    public String queryService(MarkRequest requestData) {
        log.info("个人积分列表查询开始...............");

        String mark = "0";

        // 查询参数
        Map<String, Object> param = new HashMap<>();
        param.put("openid", requestData.getOpenId());

        // 分页信息
        param.put("startindex", requestData.getStartindex());
        param.put("pagesize", requestData.getPagesize());
        param.put("pagingOrNot", "1");

        // 获取积分信息
        Map<String, String> markMap = this.markRepository.getMarkByOpenId(param);
        log.info("获取到的个人积分信息为：[{}]", JSON.toJSONString(markMap, SerializerFeature.PrettyFormat));
        if (markMap == null || markMap.isEmpty()) {
            mark = "0";
        } else {
            mark = String.valueOf(markMap.get("mark"));
        }

        // 获取个人积分列表
        List<Map<String, String>> markList = this.markRepository.getMarkListByOpenId(param);
        log.info("查询出的个人积分列表为：{}", markList);

        WebResponse<MarkResponse> responseData = new WebResponse<>();
        MarkResponse markResponse = new MarkResponse();
        responseData.setResponse(markResponse);
        // 积分
        markResponse.setMark(mark);
        // 积分列表
        markResponse.setMarklist(markList);

        // 返回
        log.info("返回的个人积分信息为：\n{}", JSON.toJSONString(responseData, SerializerFeature.PrettyFormat));
        return JSON.toJSONString(responseData);
    }

}
