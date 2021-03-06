package com.frank.gramtu.mini.grammarly;

import com.alibaba.fastjson.JSON;
import com.alibaba.fastjson.serializer.SerializerFeature;
import com.frank.gramtu.core.response.SysResponse;
import com.frank.gramtu.core.response.WebResponse;
import lombok.extern.slf4j.Slf4j;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import java.text.DecimalFormat;
import java.util.ArrayList;
import java.util.HashMap;
import java.util.List;
import java.util.Map;

/**
 * 上程序语法检测列表Controller.
 *
 * @author 张孝党 2020/01/07.
 * @version V1.00.
 * <p>
 * 更新履历： V1.00 2020/01/07. 张孝党 创建.
 */
@Slf4j
@Service
public class GOrderService {

    @Autowired
    private GOrderRepository gOrderRepository;

    /**
     * 查询语法检测列表.
     */
    public String queryService(GOrderRequest requestData) {
        log.info("语法检测列表查询开始..............");

        // 查询参数
        Map<String, Object> param = new HashMap<>();
        // openid
        param.put("openid", requestData.getOpenId());

        // 状态
        List<String> status = new ArrayList<>();
        // 待支付
        if (requestData.getType().equals("0")) {
            // ADD BY zhangxd ON 202002113 START
            // 待支付列表中增加新建状态的数据，小程序端显示为正在解析中
            status.add("0");
            status.add("1");
            // ADD BY zhangxd ON 202002113 END
        } else {
            status.add("2");
            status.add("3");
            status.add("4");
            status.add("9");
        }
        param.put("statusList", status);

        // 分页信息
        param.put("startindex", requestData.getStartindex());
        param.put("pagesize", requestData.getPagesize());
        param.put("pagingOrNot", "1");

        // 查询
        List<Map<String, String>> dataList = this.gOrderRepository.getGrammarlyList(param);
        log.info("查询出的价格信息为：\n{}", JSON.toJSONString(dataList, SerializerFeature.PrettyFormat));

        // 查询费用信息
        Map<String, String> feeInfo = this.gOrderRepository.getFeeInfo();
        log.info("查询出的价格信息为：\n{}", JSON.toJSONString(feeInfo, SerializerFeature.PrettyFormat));

        WebResponse<GOrderResponse> responseData = new WebResponse<>();
        GOrderResponse tOrderResponse = new GOrderResponse();
        responseData.setResponse(tOrderResponse);

        if (requestData.getType().equals("0")) {
            tOrderResponse.setOrderlist(dataList);
        } else {
            tOrderResponse.setPaylist(dataList);
        }

        // 价格信息
        DecimalFormat df1 = new DecimalFormat("#.00");
        tOrderResponse.setPrice(String.valueOf(df1.format(Double.valueOf(feeInfo.get("t_price")) / 100)));

        DecimalFormat df2 = new DecimalFormat("#.0");
        tOrderResponse.setDiscount(String.valueOf(df2.format(Double.valueOf(feeInfo.get("t_discount")) / 10)));

        tOrderResponse.setWordnum(feeInfo.get("t_wordnum"));

        log.info("返回信息为：\n{}", JSON.toJSONString(responseData, SerializerFeature.PrettyFormat));

        // 返回
        return JSON.toJSONString(responseData);
    }

    /**
     * 删除订单.
     */
    @Transactional(rollbackFor = Exception.class)
    public String deleteService(GOrderRequest requestData) {
        log.info("删除语法检测列表订单开始......................");

        // 删除
        requestData.getOrderidlist().forEach(
                orderid -> {
                    // 参数
                    Map<String, String> param = new HashMap<>();
                    param.put("orderid", orderid);
                    int cnt = this.gOrderRepository.delGrammarlyOrder(param);
                    log.info("删除订单的结果为：[{}]", cnt);
                }
        );

        // 返回
        return new SysResponse("删除成功!").toJsonString();
    }
}
