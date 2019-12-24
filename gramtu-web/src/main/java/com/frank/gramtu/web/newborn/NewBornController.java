package com.frank.gramtu.web.newborn;

import com.alibaba.fastjson.JSON;
import com.alibaba.fastjson.TypeReference;
import com.frank.gramtu.core.request.WebRequest;
import com.frank.gramtu.web.feature.FeatureRequest;
import lombok.extern.slf4j.Slf4j;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.web.bind.annotation.CrossOrigin;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;

/**
 * 新人专区管理Controller.
 *
 * @author 张孝党 2019/12/24.
 * @version V1.00.
 * <p>
 * 更新履历： V1.00 2019/12/24 张孝党 创建.
 */
@Slf4j
@RestController
@CrossOrigin(origins = "*")
@RequestMapping("/newborn")
public class NewBornController {

    @Autowired
    private NewBornService newBornService;

    /**
     * 查询一览.
     */
    @RequestMapping("/query")
    public String query(@RequestBody String requestParam) {
        log.info("查询新人专区一览开始..................");

        log.info("请求参数为：{}", requestParam);
        WebRequest<NewBornRequest> requestData = JSON.parseObject(requestParam, new TypeReference<WebRequest<NewBornRequest>>() {
        });

        // 查询
        String responseData = this.newBornService.queryService(requestData.getRequest());

        log.info("查询新人专区结束..................");
        log.info("返回值为:{}", responseData);
        return responseData;
    }
}
