package com.frank.gramtu.web.turninparam;

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
 * 国际版turnitin参数Service.
 *
 * @author 张孝党 2019/12/09.
 * @version V1.00.
 * <p>
 * 更新履历： V1.00 2019/12/09 张孝党 创建.
 */
@Slf4j
@Service
public class TurninParamService {

    @Autowired
    private TurninParamRepository turninParamRepository;

    /**
     * 获取国际版参数.
     */
    public String getParamService() {

        TurninParamResponse response = new TurninParamResponse();

        List<Map<String, String>> dataList = this.turninParamRepository.getTurnitParam();
        log.info(">>>>>>>>>>>>>>>>>>{}", dataList.size());

        // 将数据存储到json中
        dataList.forEach(
                data -> {
                    // 用户名
                    if (data.get("dict_key").equals("in_uname")) {
                        response.setUname(data.get("dict_value"));
                    }
                    // 密码
                    if (data.get("dict_key").equals("in_passwd")) {
                        response.setPassword(data.get("dict_value"));
                    }
                    // classid
                    if (data.get("dict_key").equals("in_classid")) {
                        response.setClassid(data.get("dict_value"));
                    }
                    // aid
                    if (data.get("dict_key").equals("in_aid")) {
                        response.setAid(data.get("dict_value"));
                    }
                    // sub aid
                    if (data.get("dict_key").equals("in_sub_aid")) {
                        response.setSubaid(data.get("dict_value"));
                    }
                    // 论文路径
                    if (data.get("dict_key").equals("in_thesis_path")) {
                        response.setThesispath(data.get("dict_value"));
                    }
                    // 报告路径
                    if (data.get("dict_key").equals("in_report_path")) {
                        response.setReportpath(data.get("dict_value"));
                    }
                }
        );

        WebResponse<TurninParamResponse> webResponse = new WebResponse<>();
        webResponse.setResponse(response);
        // 返回
        return JSONObject.toJSONString(webResponse);
    }

    /**
     * 更新国际版参数.
     */
    @Transactional(rollbackFor = Exception.class)
    public String updParamService(TurninParamRequest requestData) {

        // 更新
        this.updDict("in_uname", requestData.getUname());
        this.updDict("in_passwd", requestData.getPassword());
        this.updDict("in_classid", requestData.getClassid());
        this.updDict("in_aid", requestData.getAid());
        this.updDict("in_sub_aid", requestData.getSubaid());
        this.updDict("in_thesis_path", requestData.getThesispath());
        this.updDict("in_report_path", requestData.getReportpath());

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
        this.turninParamRepository.updTurnitParam(param);
    }
}
