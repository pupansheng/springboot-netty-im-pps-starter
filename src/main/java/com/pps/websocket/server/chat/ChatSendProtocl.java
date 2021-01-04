package com.pps.websocket.server.chat;

/**
 * @author
 * @discription; 聊天发送协议
 * @time 2020/12/22 11:16
 */
public class ChatSendProtocl extends ChatProtocl{

    private String from;

    public String getFrom() {
        return from;
    }

    public void setFrom(String from) {
        this.from = from;
    }
}
