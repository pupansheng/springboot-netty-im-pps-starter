package com.pps.websocket.server.event;

public enum ImEvent {
    loginOut,//登录退出事件
    loginFail,//登陆失败事件
    errorHappen,//错误发生事件
    acceptText,//接收到文本事件
    acceptBin;//接收到二进制事件

}
