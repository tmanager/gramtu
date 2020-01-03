package com.frank.gramtu.mini.business;

import com.alibaba.fastjson.JSON;
import com.alibaba.fastjson.TypeReference;
import com.frank.gramtu.core.request.WebRequest;
import com.frank.gramtu.core.response.SysErrResponse;
import lombok.extern.slf4j.Slf4j;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.web.bind.annotation.*;
import org.springframework.web.multipart.MultipartFile;

import javax.servlet.http.HttpServletRequest;

/**
 * 上传文件及价格计算Controller.
 *
 * @author 张孝党 2020/01/02.
 * @version V1.00.
 * <p>
 * 更新履历： V1.00 2020/01/02. 张孝党 创建.
 */
@Slf4j
@RestController
@CrossOrigin(value = "*")
@RequestMapping(value = "/busi")
public class BusinessController {

    @Autowired
    private BusinessService businessService;

    /**
     * 上传文件.
     */
    @RequestMapping(value = "/upload", headers = "content-type=multipart/form-data")
    public String uploadFile(@RequestParam("file") MultipartFile file, HttpServletRequest request) {
        log.info("小程序上传文件开始................");

        String responseData = "";

        // 接收到的参数
        String openId = request.getParameter("openid");
        String firstName = request.getParameter("firstname");
        String lastName = request.getParameter("lastname");
        String subTitle = request.getParameter("subtitle");
        String checkType = request.getParameter("checktype");
        String fileName = request.getParameter("filename");
        log.info("接收到的参数为：{}-{}-{},上传类型为：{}", firstName, lastName, subTitle, checkType);

        BusinessRequest requestData = new BusinessRequest();
        requestData.setOpenId(openId);
        requestData.setFirstName(firstName);
        requestData.setLastName(lastName);
        requestData.setSubTitle(subTitle);
        requestData.setCheckType(checkType);
        requestData.setOrgFileName(fileName);

        try {
            // 调用服务
            responseData = this.businessService.uploadService(requestData, file);
            log.info("返回小程序的数据为:\n{}", responseData);
        } catch (Exception ex) {
            log.error("上传文档时异常结束,异常信息：\n{}", ex.getMessage());
            return new SysErrResponse("上传文档时异常结束").toJsonString();
        }

        log.info("小程序上传文件结束................");

        // 返回
        return responseData;
    }

    /**
     * 解析论文字数.
     */
    @RequestMapping(value = "/analyse")
    public String analyse(@RequestBody String requestParam) {
        log.info("小程序解析论文字数开始................");

        log.info("请求参数为：{}", requestParam);
        WebRequest<BusinessRequest> requestData = JSON.parseObject(requestParam, new TypeReference<WebRequest<BusinessRequest>>() {
        });

        // 调用服务
        String responseData = this.businessService.analyseService(requestData.getRequest().getOrderId());
        log.info("返回小程序解析论文字的数据为:\n{}", responseData);

        log.info("小程序解析论文字数结束................");

        // 返回
        return responseData;
    }
}
