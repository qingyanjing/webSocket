package com.huangpeng.messages.constants;

import com.huangpeng.messages.handler.ThreadHandler;
import lombok.extern.slf4j.Slf4j;
import org.springframework.stereotype.Component;

import javax.websocket.*;
import javax.websocket.server.ServerEndpoint;
import java.io.IOException;
import java.util.concurrent.CopyOnWriteArraySet;

/**
 * @Date 2020-05-28
 * @author huangpeng
 * @ServerEndpoint 注解是一个类层次的注解，它的功能主要是将目前的类定义成一个websocket服务器端,
 * 注解的值将被用于监听用户连接的终端访问URL地址,客户端可以通过这个URL来连接到WebSocket服务器端
 */
@ServerEndpoint("/ws/wbSocket")
@Component
@Slf4j
public class WebSocketServer {
    //程序加载的时候切加载一次
    static{
        System.out.println(WebSocketServer.class.getName() + "已加载");
    }
    //静态变量，用来记录当前在线连接数。应该把它设计成线程安全的。
    private static int onlineCount = 0;
    //与某个客户端的连接会话，需要通过它来给客户端发送数据
    private static Session session;
    //concurrent包的线程安全Set，用来存放每个客户端对应的MyWebSocket对象。若要实现服务端与单一客户端通信的话，可以使用Map来存放，其中Key可以为用户标识
    public static CopyOnWriteArraySet<WebSocketServer> wbSockets = new CopyOnWriteArraySet<WebSocketServer>(); //此处定义静态变量，以在其他方法中获取到所有连接
    // concurrent包的线程安全Set，用来存放每个客户端对应的Session对象。
    private static CopyOnWriteArraySet<Session> SessionSet = new CopyOnWriteArraySet<Session>();
    // 实例化线程
    ThreadHandler mythread = new ThreadHandler();
    Thread thread = new Thread(mythread);

    /**
     * 连接建立成功调用的方法
     *
     * @param session 可选的参数。session为与某个客户端的连接会话，需要通过它来给客户端发送数据
     */
    @OnOpen
    public void onOpen(Session session){
        this.session = session;
        wbSockets.add(this); //将此对象存入集合中以在之后广播用，如果要实现一对一订阅，则类型对应为Map。由于这里广播就可以了随意用Set
        SessionSet.add(session);
        addOnlineCount();//在线数加1
        System.out.println("有新连接加入！当前在线人数为 " + getOnlineCount());
        System.out.println("有新连接加入！,加入sessionId是 "+ session.getId());
        thread.start();//启动线程
        System.out.println("线程ID：" + thread.getId() + "线程名称：" + thread.getName() + "已启动");
    }

    /**
     * 关闭连接
     */
    @OnClose
    public void onClose(){
        wbSockets.remove(this);//将socket对象从集合中移除，以便广播时不发送次连接。如果不移除会报错(需要测试)
        SessionSet.remove(session);
        subOnlineCount();//在线数减1
        System.out.println("有一连接关闭！当前在线人数为 " + getOnlineCount());
        System.out.println("有一连接关闭！,关闭sessionId是 "+ session.getId());
        mythread.stopMe();//关闭线程
        System.out.println("线程ID：" + thread.getId() + "线程名称：" + thread.getName() + "已关闭");
    }
    /**
     * 接收前端传过来的数据。
     * 虽然在实现推送逻辑中并不需要接收前端数据，但是作为一个webSocket的教程或叫备忘，还是将接收数据的逻辑加上了。
     */
    @OnMessage
    public void onMessage(String message ,Session session){
        System.out.println("来自客户端的消息:" + message + "且sessionId是" + session.getId());
        //群发消息
        for (WebSocketServer item : wbSockets) {
            try {
                item.sendMessage(message);
            } catch (IOException e) {
                e.printStackTrace();
                continue;
            }
        }
    }
    /**
     * 发生错误时调用
     * @param session
     * @param error
     */
    @OnError
    public void onError(Session session, Throwable error) {
        System.out.println("发生错误");
        error.printStackTrace();
    }
    /**
     * 这个方法与上面几个方法不一样。没有用注解，是根据自己需要添加的方法。
     * @param message json string化后的字符串 传输
     * @throws IOException
     */
    public void sendMessage(String message) throws IOException {
        this.session.getBasicRemote().sendText(message);
    }
    /**
     * 群发消息
     * @param message
     * @throws IOException
     */
    public static void sendMessageAll(String message) throws IOException {
        for (Session session : SessionSet) {
            if(session.isOpen()){
                session.getBasicRemote().sendText(message);
            }
        }
    }
    /**
     * 指定Session发送消息
     * @param sessionId
     * @param message
     * @throws IOException
     */
    public static void SendMessage(String sessionId,String message) throws IOException {
        for (Session s : SessionSet) {
            if(s.getId().equals(sessionId)){
                session = s;
                break;
            }
        }
        if(session != null){
            session.getBasicRemote().sendText(message);
        }
        else{
            log.warn("没有找到你指定ID的会话：{}，没有消息推送：{}",sessionId,message);
        }
    }

    private static synchronized int addOnlineCount() {
        return WebSocketServer.onlineCount ++;
    }

    private static synchronized int getOnlineCount(){
        return onlineCount;
    }

    private static synchronized int subOnlineCount(){
        return WebSocketServer.onlineCount--;
    }

}
