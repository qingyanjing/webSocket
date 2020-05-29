package com.huangpeng.messages.handler;

import com.alibaba.fastjson.JSONArray;
import com.huangpeng.messages.config.ApplicationContextProvider;
import com.huangpeng.messages.constants.WebSocketServer;
import com.huangpeng.messages.entities.EwsSaAreariskindexRt;
import com.huangpeng.messages.service.impl.AreariskindexRtService;
import lombok.extern.slf4j.Slf4j;
import org.springframework.beans.factory.annotation.Autowired;

import java.io.IOException;

@Slf4j
public class ThreadHandler implements Runnable {
    @Autowired
    private  AreariskindexRtService areariskindexRtService = ApplicationContextProvider.getBean(AreariskindexRtService.class);

    private int count;
    private int new_count;
    private boolean stopMe = true;
    public void stopMe() {
        stopMe = false;
    }
    /**
     * 实现run方法
     */
    public void run()  {
        count = areariskindexRtService.selectCount();
        log.info("当前数据库数据条数：{}",count);
        while(stopMe){
            new_count = areariskindexRtService.selectCount();
            log.info("变更后数据库数据条数：{}",new_count);
            if(count != new_count){
                int step = new_count - count;
                log.info("数据库数据已改变，变化数量：{}",step);
                count = new_count;
                //获取业务数据
                EwsSaAreariskindexRt vo = areariskindexRtService.selectLatestTimeModel();
                //转换成json对象作为消息内容
                String json = JSONArray.toJSON(vo).toString();
                try {
                    WebSocketServer.sendMessageAll(json);
                } catch (IOException e) {
                    e.printStackTrace();
                    log.info("消息发送异常");
                }
            }
            try {
                Thread.sleep(10000);
            } catch (InterruptedException e) {
                e.printStackTrace();
            }
        }
    }
}
