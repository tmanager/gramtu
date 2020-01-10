package com.frank.gramtu.mini.crontab;

import com.alibaba.fastjson.JSON;
import com.alibaba.fastjson.JSONObject;
import com.alibaba.fastjson.serializer.SerializerFeature;
import com.frank.gramtu.core.bean.RequestTurnitinBean;
import com.frank.gramtu.core.bean.ResponseTurnitinBean;
import com.frank.gramtu.core.bean.TurnitinConst;
import com.frank.gramtu.core.fdfs.FdfsUtil;
import com.frank.gramtu.core.redis.RedisService;
import com.frank.gramtu.core.utils.SocketClient;
import lombok.extern.slf4j.Slf4j;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.scheduling.annotation.Async;
import org.springframework.scheduling.annotation.AsyncResult;
import org.springframework.stereotype.Component;
import org.springframework.stereotype.Service;

import java.io.File;
import java.util.Calendar;
import java.util.HashMap;
import java.util.Map;
import java.util.concurrent.Future;

/**
 * 提交论文Service.
 *
 * @author 张孝党 2020/01/10.
 * @version V1.00.
 * <p>
 * 更新履历： V1.00 2020/01/10 张孝党 创建.
 */
@Slf4j
@Service
@Component
public class TaskService {

    @Autowired
    private FdfsUtil fdfsUtil;

    @Autowired
    private RedisService redisService;

    @Autowired
    private TaskRepository taskRepository;

    /**
     * 异步下载国际版报告.
     */
    @Async("taskExecutor")
    public Future<String> getReport(String id, String thesisId) throws Exception {

        log.info("国际版检测报告开始下载,论文ID[{}].....................", thesisId);
        Map<String, String> param = new HashMap<>();

        // 增加线程名称
        Thread.currentThread().setName(thesisId);
        long start = Calendar.getInstance().getTimeInMillis();

        // 将状态更新为3:报告下载中
        param.put("id", id);
        param.put("status", "3");
        param.put("comment", "报告下载中");
        this.updOrderStatusById(param);

        RequestTurnitinBean turnBean = JSONObject.parseObject(this.redisService.getStringValue(TurnitinConst.TURN_IN_KEY),
                RequestTurnitinBean.class);
        log.info("查询出的国际版账户信息为：\n{}", JSON.toJSONString(turnBean, SerializerFeature.PrettyFormat));

        // 设置参数
        turnBean.setThesisId(thesisId);
        // 调用Socket
        String result = SocketClient.callServer(TurnitinConst.SOCKET_SERVER, TurnitinConst.SOCKET_PORT,
                "03" + JSONObject.toJSONString(turnBean));
        ResponseTurnitinBean responseTurnitinBean = JSONObject.parseObject(result, ResponseTurnitinBean.class);
        log.info("调用Socket Server返回的结果为：\n{}", JSON.toJSONString(responseTurnitinBean, SerializerFeature.PrettyFormat));

        // 输出日志
        for (String logInfo : responseTurnitinBean.getLogList()) {
            log.info("turnitin-api>>>>>>" + logInfo);
        }
        // 提交失败
        if (!responseTurnitinBean.getRetcode().equals("0000")) {
            log.error("下载失败,等待下载次下载!");
            // 下载失败后状态修改02：检测中等待下次
            param.put("status", "2");
            param.put("comment", "下载报告时失败，等待下次下载");
            this.updOrderStatusById(param);
            log.info("国际版检测报告下载结束,论文ID[{}].....................", thesisId);

            long end = Calendar.getInstance().getTimeInMillis();
            return new AsyncResult<>(String.format("国际版检测报告下载,论文ID[%s],共耗时[%s]毫秒", thesisId, end - start));
        }

        // html报告路径
        String htmlReport = turnBean.getReportVpnPath() + File.separator + thesisId + ".html";
        // pdf报告路径
        String pdfReport = turnBean.getReportVpnPath() + File.separator + thesisId + ".pdf";
        // 重复率
        String rate = responseTurnitinBean.getRate();

        log.info("上传html报告至FDFS");
        String htmlreporturl = this.fdfsUtil.uploadLocalFile(new File(htmlReport));

        log.info("上传pdf报告至FDFS");
        String pdfreporturl = this.fdfsUtil.uploadLocalFile(new File(pdfReport));

        // 更新订单状态
        param.put("status", "4");
        param.put("comment", "检测完成");
        param.put("rate", rate);
        param.put("htmlreporturl", htmlreporturl);
        param.put("pdfreporturl", pdfreporturl);
        this.updOrderStatusById(param);

        // TODO:发送小程序模版消息

        long end = Calendar.getInstance().getTimeInMillis();
        return new AsyncResult<>(String.format("国际版检测报告下载并上传,论文ID[%s],共耗时[%s]毫秒", thesisId, end - start));
    }

    /**
     * 更新订单报告下载状态
     */
    private void updOrderStatusById(Map<String, String> paramIn) {

        Map<String, String> param = new HashMap<>();
        param.put("id", paramIn.get("id"));

        if (paramIn.containsKey("status")) {
            param.put("status", paramIn.get("status"));
        }

        if (paramIn.containsKey("comment")) {
            param.put("comment", paramIn.get("comment"));
        }

        if (paramIn.containsKey("rate")) {
            param.put("repetrate", paramIn.get("rate"));
        }

        if (paramIn.containsKey("htmlreporturl")) {
            param.put("htmlreporturl", paramIn.get("htmlreporturl"));
        }

        if (paramIn.containsKey("pdfreporturl")) {
            param.put("pdfreporturl", paramIn.get("pdfreporturl"));
        }

        // 更新状态
        int cnt = this.taskRepository.updOrderStatusById(param);
        log.info("更新订单状态结果为：[{}]", cnt);
    }
}
