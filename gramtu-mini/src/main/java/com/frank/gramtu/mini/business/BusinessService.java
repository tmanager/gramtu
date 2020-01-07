package com.frank.gramtu.mini.business;

import com.alibaba.fastjson.JSON;
import com.alibaba.fastjson.JSONObject;
import com.alibaba.fastjson.serializer.SerializerFeature;
import com.frank.gramtu.core.bean.RequestTurnitinBean;
import com.frank.gramtu.core.bean.ResponseTurnitinBean;
import com.frank.gramtu.core.bean.TurnitinConst;
import com.frank.gramtu.core.fdfs.FdfsUtil;
import com.frank.gramtu.core.redis.RedisService;
import com.frank.gramtu.core.response.SysErrResponse;
import com.frank.gramtu.core.response.WebResponse;
import com.frank.gramtu.core.utils.*;
import com.frank.gramtu.mini.constant.CheckType;
import lombok.extern.slf4j.Slf4j;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;
import org.springframework.web.multipart.MultipartFile;

import java.text.DecimalFormat;
import java.util.HashMap;
import java.util.Map;

/**
 * 上传文件及价格计算Service.
 *
 * @author 张孝党 2020/01/02.
 * @version V1.00.
 * <p>
 * 更新履历： V1.00 2020/01/02. 张孝党 创建.
 */
@Slf4j
@Service
public class BusinessService {

    @Autowired
    private RedisService redisService;

    @Autowired
    private BusinessRepository businessRepository;

    /**
     * 上传文件.
     */
    @Transactional(rollbackFor = Exception.class)
    public String uploadService(BusinessRequest requestData, String filePath) throws Exception {
        log.info("上传论文服务开始..........................");

        // 保存至数据库
        Map<String, String> param = new HashMap<>();
        // id
        param.put("id", CommonUtil.getUUid());
        // 订单号
        IdGeneratorUtils idGeneratorUtils = new IdGeneratorUtils(0, 0);
        String orderId = String.valueOf(idGeneratorUtils.nextId());
        log.info("订单编号为[{}]", orderId);
        param.put("orderid", orderId);
        // openId
        param.put("openid", requestData.getOpenId());
        // 姓
        param.put("firstname", requestData.getFirstName());
        // 名
        param.put("lastname", requestData.getLastName());
        // 标题
        param.put("titile", requestData.getSubTitle());
        // 文件名称
        param.put("filename", requestData.getOrgFileName());
        // 原始文件URL
        param.put("originalurl", filePath);
        // 检测类型
        param.put("checktype", requestData.getCheckType());
        // 更新时间
        param.put("updtime", DateTimeUtil.getTimeformat());

        // 文件扩展名
        String fileExt = requestData.getOrgFileName().
                substring(requestData.getOrgFileName().lastIndexOf(".") + 1);
        log.info("文档的后缀为：[{}]", fileExt);
        param.put("ext", fileExt);

        // 新增数据
        int cnt = this.businessRepository.insOrderInfo(param);
        log.info("新增订单信息结果为：[{}]", cnt);

        // 返回值
        WebResponse<BusinessResponse> responseData = new WebResponse<>();
        BusinessResponse businessResponse = new BusinessResponse();
        businessResponse.setOrderid(orderId);
        responseData.setResponse(businessResponse);

        responseData.setResponse(businessResponse);
        log.info("返回信息为：\n{}", JSON.toJSONString(responseData, SerializerFeature.PrettyFormat));

        log.info("上传论文服务结束..........................");

        // 返回
        return JSON.toJSONString(responseData);
    }

    /**
     * 解析论文字数.
     */
    @Transactional(rollbackFor = Exception.class)
    public String analyseService(String orderId) {
        log.info("解析论文字数服务开始,订单号为[{}]", orderId);

        // 查询订单信息
        Map<String, String> param = new HashMap<>();
        param.put("orderid", orderId);
        Map<String, String> orderInfo = this.businessRepository.getOrderInfoByOrderId(param);
        log.info("查询出的订单信息为：\n{}", JSON.toJSONString(orderInfo, SerializerFeature.PrettyFormat));

        // 查询费用信息
        Map<String, String> feeInfo = this.businessRepository.getFeeInfo();
        log.info("查询出的价格信息为：\n{}", JSON.toJSONString(feeInfo, SerializerFeature.PrettyFormat));

        // 加载turnitin国际版账户信息
        RequestTurnitinBean turnBean = JSONObject.parseObject(this.redisService.getStringValue(TurnitinConst.TURN_IN_KEY),
                RequestTurnitinBean.class);
        log.info("查询出的国际版账户信息为：\n{}", JSON.toJSONString(turnBean, SerializerFeature.PrettyFormat));

        // 保存论文信息
        String thesisName = orderInfo.get("orderid") + "." + orderInfo.get("ext");
        log.info("保存的论文名称为：[{}]", thesisName);
        boolean dowloadResult = FileUtils.downloadFromHttpUrl(orderInfo.get("originalurl"), turnBean.getThesisVpnPath(), thesisName);
        if (!dowloadResult) {
            this.removeOrderByOrderId(param);
            return new SysErrResponse("从FDFS下载论文时异常").toJsonString();
        }

        // 设计参数
        turnBean.setFirstName(orderInfo.get("firstname"));
        turnBean.setLastName(orderInfo.get("lastname"));
        turnBean.setTitle(orderInfo.get("titile"));
        turnBean.setThesisName(thesisName);

        // 调用Socket
        String result = SocketClient.callServer(TurnitinConst.SOCKET_SERVER, TurnitinConst.SOCKET_PORT,
                "04" + JSONObject.toJSONString(turnBean));
        ResponseTurnitinBean responseTurnitinBean = JSONObject.parseObject(result, ResponseTurnitinBean.class);
        log.info("调用Socket Server返回的结果为：\n{}", JSON.toJSONString(responseTurnitinBean, SerializerFeature.PrettyFormat));

        // 输出日志
        for (String logInfo : responseTurnitinBean.getLogList()) {
            log.info("turnitin-api>>>>>>" + logInfo);
        }
        // 提交失败
        if (!responseTurnitinBean.getRetcode().equals("0000")) {
            this.removeOrderByOrderId(param);
            return new SysErrResponse("计算文字字数时异常").toJsonString();
        }

        // 保存文字信息
        // 大小
        param.put("filesize", responseTurnitinBean.getFileSize());
        // 页数
        param.put("pagesize", responseTurnitinBean.getPageCount());
        // 字数
        param.put("wordcnt", responseTurnitinBean.getWordCount());
        // 字符数
        param.put("charcnt", responseTurnitinBean.getCharCount());
        // 状态
        param.put("status", "1");
        this.updOrderByOrderId(param);

        // 返回值
        WebResponse<BusinessResponse> responseData = new WebResponse<>();
        BusinessResponse businessResponse = new BusinessResponse();
        businessResponse.setOrderid(orderId);
        // 大小
        businessResponse.setFilesize(responseTurnitinBean.getFileSize());
        // 页数
        businessResponse.setPagecount(responseTurnitinBean.getPageCount());
        // 字数
        businessResponse.setWordcount(responseTurnitinBean.getWordCount());
        // 字符数
        businessResponse.setCharcount(responseTurnitinBean.getCharCount());

        // 价格信息
        if (orderInfo.get("checktype").equals(CheckType.GRAMMARLY.getValue())) {
            DecimalFormat df1 = new DecimalFormat("#.00");
            businessResponse.setPrice(String.valueOf(df1.format(Double.valueOf(feeInfo.get("g_price")) / 100)));

            DecimalFormat df2 = new DecimalFormat("#.0");
            businessResponse.setDiscount(String.valueOf(df2.format(Double.valueOf(feeInfo.get("g_discount")) / 10)));

            businessResponse.setWordnum(feeInfo.get("g_wordnum"));
        } else {
            DecimalFormat df1 = new DecimalFormat("#.00");
            businessResponse.setPrice(String.valueOf(df1.format(Double.valueOf(feeInfo.get("t_price")) / 100)));

            DecimalFormat df2 = new DecimalFormat("#.0");
            businessResponse.setDiscount(String.valueOf(df2.format(Double.valueOf(feeInfo.get("t_discount")) / 10)));

            businessResponse.setWordnum(feeInfo.get("t_wordnum"));
        }
        responseData.setResponse(businessResponse);

        responseData.setResponse(businessResponse);
        log.info("返回信息为：\n{}", JSON.toJSONString(responseData, SerializerFeature.PrettyFormat));

        log.info("解析论文字数服务结束..........................");

        // 返回
        return JSON.toJSONString(responseData);
    }

    /**
     * 删除订单信息.
     */
    private void removeOrderByOrderId(Map<String, String> param) {
        this.businessRepository.deleteOrderInfo(param);
    }

    /**
     * 更新订单信息.
     */
    private void updOrderByOrderId(Map<String, String> param) {
        this.businessRepository.updOrderWordInfo(param);
    }
}
