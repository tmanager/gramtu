package com.frank.gramtu.web.turninukparam;

import com.alibaba.fastjson.JSONObject;
import com.frank.gramtu.core.response.SysResponse;
import com.frank.gramtu.core.response.WebResponse;
import lombok.extern.slf4j.Slf4j;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import java.util.HashMap;
import java.util.List;
import java.util.Map;

/**
 * UK版turnitin参数Service.
 *
 * @author 张孝党 2019/12/09.
 * @version V1.00.
 * <p>
 * 更新履历： V1.00 2019/12/09 张孝党 创建.
 */
@Slf4j
@Service
public class TurninUKParamService {

    @Autowired
    private TurninUKParamRepository turninUKParamRepository;

    /**
     * 获取UK版参数.
     */
    public String getParamService() {

        TurninUKParamResponse response = new TurninUKParamResponse();

        List<Map<String, String>> dataList = this.turninUKParamRepository.getTurnitParam();
        log.info(">>>>>>>>>>>>>>>>>>{}", dataList.size());

        // 将数据存储到json中
        dataList.forEach(
                data -> {
                    // 用户名
                    if (data.get("dict_key").equals("uk_uname")) {
                        response.setUname(data.get("dict_value"));
                    }
                    // 密码
                    if (data.get("dict_key").equals("uk_passwd")) {
                        response.setPassword(data.get("dict_value"));
                    }
                    // classid
                    if (data.get("dict_key").equals("uk_classid")) {
                        response.setClassid(data.get("dict_value"));
                    }
                    // aid
                    if (data.get("dict_key").equals("uk_aid")) {
                        response.setAid(data.get("dict_value"));
                    }
                    // sub aid
                    if (data.get("dict_key").equals("uk_sub_aid")) {
                        response.setSubaid(data.get("dict_value"));
                    }
                    // 论文路径
                    if (data.get("dict_key").equals("uk_thesis_path")) {
                        response.setThesispath(data.get("dict_value"));
                    }
                    // 报告路径
                    if (data.get("dict_key").equals("uk_report_path")) {
                        response.setReportpath(data.get("dict_value"));
                    }
                }
        );

        WebResponse<TurninUKParamResponse> webResponse = new WebResponse<>();
        webResponse.setResponse(response);
        // 返回
        return JSONObject.toJSONString(webResponse);
    }

    /**
     * 更新UK版参数.
     */
    @Transactional(rollbackFor = Exception.class)
    public String updParamService(TurninUKParamRequest requestData) {

        // 更新
        this.updDict("uk_uname", requestData.getUname());
        this.updDict("uk_passwd", requestData.getPassword());
        this.updDict("uk_classid", requestData.getClassid());
        this.updDict("uk_aid", requestData.getAid());
        this.updDict("uk_sub_aid", requestData.getSubaid());
        this.updDict("uk_thesis_path", requestData.getThesispath());
        this.updDict("uk_report_path", requestData.getReportpath());

        return new SysResponse().toJsonString();
    }

    /**
     * 执行更新操作.
     */
    private void updDict(String key, String value) {
        Map<String, String> param = new HashMap<>();
        // 用户名
        param.put("key", key);
        param.put("value", value);
        this.turninUKParamRepository.updTurnitParam(param);
    }
}
