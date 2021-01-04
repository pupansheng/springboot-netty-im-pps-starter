package com.pps.websocket.server.chat;

public enum MsgAction {

    sendToSingle(1),
    sendToAll(2),
    other(3);
    static MsgAction[] msgTypes= MsgAction.values();
    private int type;
    MsgAction(int n){
        this.type=n;
    }

    public int getType() {
        return type;
    }
    public void setType(int type) {
        this.type = type;
    }
    public static MsgAction getActionByType(int n){
        for(MsgAction msgType:msgTypes){
            if(msgType.type==n){
                return  msgType;
            }
        }
        return other;
    }
}
