package com.pps.websocket.server.listener;

import com.alibaba.fastjson.JSON;
import com.pps.websocket.server.chat.ChatFromProtocl;
import com.pps.websocket.server.event.ImEvent;
import com.pps.websocket.server.event.SocketEnvent;
import com.pps.websocket.server.hander.data.DataTransfer;
import com.pps.websocket.server.util.SendMsgUtil;
import io.netty.buffer.ByteBuf;
import io.netty.channel.ChannelHandlerContext;
import io.netty.util.CharsetUtil;


import java.util.Date;

/**
 * @author 自动进行事件判断处理
 * @discription;
 * @time 2020/12/17 14:08
 */
public interface  SocketAutoListenr extends SocketListener, DataTransfer {

    /**
     * webSocket连接成功事件
     * @param socketEnvent
     */
    default void  loginSuccessful(SocketEnvent socketEnvent){};

    /**
     * webSocket连接失败 超时事件
     * @param socketEnvent
     */
    default void  loginFail(SocketEnvent socketEnvent){};

    /**
     * 用户退出事件
     * @param socketEnvent
     */
    default void  loginOut(SocketEnvent socketEnvent){};

    /**
     * 异常发生事件
     */
    default void exceptionHappen(SocketEnvent socketEnvent) { };

    /**
     * 收到文本事件
     */
    abstract void acceptText(ChannelHandlerContext content, ChatFromProtocl chatFromProtocl, Date happenTime);
    /**
     * 收到二进制事件
     */
    abstract void acceptBin(ChannelHandlerContext content, CharSequence fileInfo, ByteBuf fileData, Date happenTime);

    /**
     * 其他事件
     * @param socketEnvent
     */
    default void  other(SocketEnvent socketEnvent){};

    @Override
    default void listenerSocketEvent(SocketEnvent socketEnvent){
        if(socketEnvent.getSourceEvent()== ImEvent.acceptText){

            String data = (String) socketEnvent.getDatas()[0];
            ChatFromProtocl chatFromProtocl=null;
            try {
                chatFromProtocl = JSON.parseObject(data, ChatFromProtocl.class);
            }catch (Exception e){
                SendMsgUtil.sendMsgToClientForErrorInfo(socketEnvent.getContent(),"发送的数据不符合规范");
                return;
            }
            acceptText(socketEnvent.getContent(), chatFromProtocl,socketEnvent.getHappenTime());
        } else if(socketEnvent.getSourceEvent()==ImEvent.acceptBin){
            //将二进制文件分解成两部分 一部分为信息 一部分为文件字节
            ByteBuf data = (ByteBuf) socketEnvent.getDatas()[0];
            int infoLength = data.readInt();
            CharSequence fileInfo = data.readCharSequence(infoLength, CharsetUtil.UTF_8);
            ByteBuf byteBuf = data.readBytes(infoLength);
            acceptBin(socketEnvent.getContent(),fileInfo,byteBuf,socketEnvent.getHappenTime());
        } else if(socketEnvent.isWebSocketConneton()){
            loginSuccessful(socketEnvent);
        }else if(socketEnvent.isTimeOut()||socketEnvent.getSourceEvent()==ImEvent.loginFail){
            loginFail(socketEnvent);
        }else if(socketEnvent.getSourceEvent()== ImEvent.loginOut){
            loginOut(socketEnvent);
        }else  if(socketEnvent.getSourceEvent()==ImEvent.errorHappen){
            exceptionHappen(socketEnvent);
        }else{
            other(socketEnvent);
        }
    }
}
