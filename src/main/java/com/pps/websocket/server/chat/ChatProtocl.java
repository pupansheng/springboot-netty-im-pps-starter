package com.pps.websocket.server.chat;

/**
 * @author
 * @discription;
 * @time 2020/12/22 14:03
 */
public class ChatProtocl {

    /**
     * 内容
     */
    String data;

    /**
     * 消息类型
     */
    int  msgType;


    public MsgType getType(int n){
        return MsgType.gettByMsgType(n);
    }


    public String getData() {
        return data;
    }

    public void setData(String data) {
        this.data = data;
    }

    public int getMsgType() {
        return msgType;
    }

    public void setMsgType(int msgType) {
        this.msgType = msgType;
    }
}
