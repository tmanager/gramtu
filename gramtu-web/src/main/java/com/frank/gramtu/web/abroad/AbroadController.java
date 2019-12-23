package com.frank.gramtu.web.abroad;

import com.alibaba.fastjson.JSON;
import com.alibaba.fastjson.TypeReference;
import com.frank.gramtu.core.request.WebRequest;
import lombok.extern.slf4j.Slf4j;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.web.bind.annotation.CrossOrigin;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;

/**
 * 海外招聘管理Controller.
 *
 * @author 张孝党 2019/12/23.
 * @version V1.00.
 * <p>
 * 更新履历： V1.00 2019/12/23 张孝党 创建.
 */
@Slf4j
@RestController
@CrossOrigin(origins = "*")
@RequestMapping("/abroad")
public class AbroadController {

    @Autowired
    private AbroadService abroadService;

    /**
     * 查询一览.
     */
    @RequestMapping("/query")
    public String query(@RequestBody String requestParam) {
        log.info("查询特色服务一览开始..................");

        log.info("请求参数为：{}", requestParam);
        WebRequest<AbroadRequest> requestData = JSON.parseObject(requestParam, new TypeReference<WebRequest<AbroadRequest>>() {
        });

        // 查询
        String responseData = this.abroadService.queryService(requestData.getRequest());

        log.info("查询特色服务结束..................");
        log.info("返回值为:{}", responseData);
        return responseData;
    }
}
