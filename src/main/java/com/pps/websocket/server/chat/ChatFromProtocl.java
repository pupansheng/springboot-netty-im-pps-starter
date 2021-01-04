package com.pps.websocket.server.chat;

/**
 * @author
 * @discription; 聊天接收协议
 * @time 2020/12/22 11:16
 */
public class ChatFromProtocl extends ChatProtocl {


    /**
     * 目标
     */
    private String to;

    /*
      动作
     */
    private int action;

    public int getAction() {
        return action;
    }

    public void setAction(int action) {
        this.action = action;
    }

    public String getTo() {
        return to;
    }

    public void setTo(String to) {
        this.to = to;
    }
}
