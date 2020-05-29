package com.huangpeng.messages.controller;

import com.alibaba.fastjson.JSONArray;
import com.huangpeng.messages.constants.WebSocketServer;
import com.huangpeng.messages.entities.EwsSaAreariskindexRt;
import com.huangpeng.messages.service.IAreariskindexRtService;
import lombok.extern.slf4j.Slf4j;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RequestMethod;
import org.springframework.web.bind.annotation.RequestParam;
import org.springframework.web.bind.annotation.RestController;

import java.io.IOException;

/**
 * @Date 2020-05-28
 * @author huangpeng
 * 请求控制层
 */
@Slf4j
@RestController
@RequestMapping("/api/ws")
public class WebsocketController {

    @Autowired
    private IAreariskindexRtService areariskindexRtService;
    /**
     * 群发消息内容
     * @param message
     * @return
     */
    @RequestMapping(value = "/sendMessageAll", method = RequestMethod.GET)
    public String sendAllMessage(@RequestParam(required = true) String message,@RequestParam(required = true) String areaId) {
        try {
            //获取业务数据
            EwsSaAreariskindexRt vo = this.areariskindexRtService.getAreariskindexRt(areaId);
            //转换成json对象作为消息内容
            String json = JSONArray.toJSON(vo).toString();
            //执行消息发送
            WebSocketServer.sendMessageAll(message);
        } catch (IOException e) {
            e.printStackTrace();
        }
        return "ok";
    }

    /**
     * 指定会话ID发消息
     * @param areaId 消息内容
     * @param sessionId      连接会话ID
     * @return
     */
    @RequestMapping(value = "/sendMessageOne", method = RequestMethod.GET)
    public String sendOneMessage(@RequestParam(required = true) String sessionId, @RequestParam(required = true) String areaId) {
        // @RequestParam(required = true) String message
        try {
            //获取业务数据
            EwsSaAreariskindexRt vo = this.areariskindexRtService.getAreariskindexRt(areaId);
            //转换成json对象作为消息内容
            String json = JSONArray.toJSON(vo).toString();
            //执行消息发送
            WebSocketServer.SendMessage(sessionId,json);
        } catch (IOException e) {
            e.printStackTrace();
            log.info("###############################异常信息：{}",e.getMessage());
        }
        return "ok";
    }
}
