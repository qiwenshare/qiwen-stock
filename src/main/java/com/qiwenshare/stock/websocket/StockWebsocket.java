package com.qiwenshare.stock.websocket;

import com.alibaba.fastjson2.JSON;
import com.qiwenshare.stock.common.TaskProcess;
import lombok.extern.slf4j.Slf4j;
import org.springframework.stereotype.Component;

import javax.websocket.*;
import javax.websocket.server.ServerEndpoint;
import java.io.IOException;

@Slf4j
@ServerEndpoint(value = "/websocket/stock")
@Component
public class StockWebsocket {

    //与某个客户端的连接会话，需要通过它来给客户端发送数据
    private static Session session;

    /**
     * 推送任务状态
     *
     * @param message
     * @param isRunTask
     */
    public static void pushTaskState(String message, boolean isRunTask) {
        TaskProcess taskProcess = new TaskProcess();
        taskProcess.setRunTask(isRunTask);
        taskProcess.setTaskInfo(message);
        try {
            new StockWebsocket().sendMessage(JSON.toJSONString(taskProcess));
        } catch (Exception e) {
            e.printStackTrace();
        }
    }

    /**
     * 推送任务进度信息
     *
     * @param taskProcess
     */
    public static void pushTaskProcess(TaskProcess taskProcess) {
        try {
            new StockWebsocket().sendMessage(JSON.toJSONString(taskProcess));
        } catch (Exception e) {
            e.printStackTrace();
        }
    }

    public Session getSession() {
        return session;
    }

    public void setSession(Session session) {
        this.session = session;
    }

    /**
     * 连接建立成功调用的方法
     *
     * @param session 可选的参数。session为与某个客户端的连接会话，需要通过它来给客户端发送数据
     */
    @OnOpen
    public void onOpen(Session session, EndpointConfig config) {
        setSession(session);
        //this.session = session;
    }

    /**
     * 连接关闭调用的方法
     */
    @OnClose
    public void onClose(Session session) {
        try {
            session.close();
        } catch (IOException e) {
            log.error("websocket会话关闭失败");
        }
    }

    /**
     * 收到客户端消息后调用的方法
     *
     * @param message 客户端发送过来的消息
     * @param session 可选的参数
     */
    @OnMessage
    public void onMessage(String message, Session session) {

    }

    /**
     * 发生错误时调用
     *
     * @param session
     * @param error
     */
    @OnError
    public void onError(Session session, Throwable error) {
        log.error("webSocket连接发生错误");
    }

    public synchronized void sendMessage(String message) throws Exception {
//        synchronized (this) {
        try {
            getSession().getBasicRemote().sendText(message);
        } catch (Exception e) {
//            log.error("消息推送失败");
        }

        //     session.getBasicRemote().sendText(message);
        //  }
    }

    public synchronized void sendMessage(Object messageObj) throws IOException, EncodeException {
        getSession().getBasicRemote().sendObject(messageObj);
    }
}
