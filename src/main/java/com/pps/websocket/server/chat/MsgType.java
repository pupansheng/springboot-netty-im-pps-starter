package com.pps.websocket.server.chat;

public enum MsgType {

    text(1),
    audio(2),
    video(3),
    image(4),
    serverError(5),
    other(6);
    static MsgType [] msgTypes=MsgType.values();

    private int type;
    MsgType(int n){
        this.type=n;
    }

    public int getType() {
        return type;
    }

    public void setType(int type) {
        this.type = type;
    }
    public static MsgType gettByMsgType(int n){
        for(MsgType msgType:msgTypes){
            if(msgType.type==n){
                return  msgType;
            }
        }
        return other;

    }
}
