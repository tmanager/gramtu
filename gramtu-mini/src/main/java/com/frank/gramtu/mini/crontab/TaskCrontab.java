package com.frank.gramtu.mini.crontab;

import com.alibaba.fastjson.JSON;
import com.alibaba.fastjson.serializer.SerializerFeature;
import lombok.extern.slf4j.Slf4j;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.scheduling.annotation.Scheduled;
import org.springframework.stereotype.Component;

import java.util.List;
import java.util.Map;
import java.util.concurrent.Future;

/**
 * 获取报告定时任务.
 *
 * @author 张孝党 2020/01/10.
 * @version V1.00.
 * <p>
 * 更新履历： V1.00 2020/01/10 张孝党 创建.
 */
@Slf4j
@Component
public class TaskCrontab {

    @Autowired
    private TaskService taskService;

    @Autowired
    private TaskRepository taskRepository;

    /**
     * 获取国际版报告.
     */
    @Scheduled(cron = "0 0/3 * * * ?")
    public void getGjReport() {
        log.info("..................开始执行获国际版取报告定时任务..................");

        try {
            // 从数据库中取出10条记录获取报告
            List<Map<String, String>> dataList = this.taskRepository.getCheckingReports();
            if (dataList.size() == 0) {
                log.info("国际版没有需要获取报告的论文!");
            } else {
                log.info("国际版共有{}个论文报告需要下载...........", dataList.size());
                log.info("获取的国际版下载报告的信息为：\n{}", JSON.toJSONString(dataList, SerializerFeature.PrettyFormat));
                for (Map<String, String> dataMap : dataList) {
                    // 异步执行下载报告并上传
                    if (!dataMap.get("thesisid").isEmpty()) {
                        Future<String> task = this.taskService.getReport(dataMap.get("id"), dataMap.get("thesisid"));
                        Thread.sleep(1000L);
                    }
                }
            }
        } catch (Exception ex) {
            log.info("..................异常结束执行国际版获取报告定时任务..................");
            log.info(ex.getMessage());
        }
    }

    /**
     * 获取UK版报告.
     */
    @Scheduled(cron = "0 0/5 * * * ?")
    public void getUkReport() {
        log.info("..................开始执行获UK版取报告定时任务..................");

        try {
            // 从数据库中取出10条记录获取报告
            List<Map<String, String>> dataList = this.taskRepository.getCheckingReportsUK();
            if (dataList.size() == 0) {
                log.info("UK版没有需要获取报告的论文!");
            } else {
                log.info("UK版共有{}个论文报告需要下载...........", dataList.size());
                log.info("获取的UK版下载报告的信息为：\n{}", JSON.toJSONString(dataList, SerializerFeature.PrettyFormat));
                for (Map<String, String> dataMap : dataList) {
                    // 异步执行下载报告并上传
                    if (!dataMap.get("thesisid").isEmpty()) {
                        Future<String> task = this.taskService.getReportUK(dataMap.get("id"), dataMap.get("thesisid"));
                        Thread.sleep(1000L);
                    }
                }
            }
        } catch (Exception ex) {
            log.info("..................异常结束执行UK版获取报告定时任务..................");
            log.info(ex.getMessage());
        }
    }

}
