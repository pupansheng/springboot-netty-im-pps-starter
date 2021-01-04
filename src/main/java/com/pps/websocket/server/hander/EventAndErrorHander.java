package com.pps.websocket.server.hander;

import com.pps.websocket.server.event.ImEvent;
import com.pps.websocket.server.event.SocketEnvent;
import com.pps.websocket.server.hander.data.DataTransfer;
import com.pps.websocket.server.hander.http.WebSocketConnetion;
import com.pps.websocket.server.listener.SocketListener;
import io.netty.channel.ChannelHandlerContext;
import io.netty.channel.ChannelInboundHandlerAdapter;
import org.springframework.beans.factory.annotation.Autowired;


import java.util.ArrayList;
import java.util.List;

/**
 * @author
 * @discription; 最后一个入站器 捕捉事件
 * @time 2020/12/17 15:37
 */

public class EventAndErrorHander extends ChannelInboundHandlerAdapter implements DataTransfer {


    private List<SocketListener> socketListeners=new ArrayList<>();

    public void  addListener(SocketListener socketListener){
        socketListeners.add(socketListener);
    }

    public void setSocketListeners(List<SocketListener> socketListeners) {
        this.socketListeners = socketListeners;
    }

    @Override
    public void userEventTriggered(ChannelHandlerContext ctx, Object evt) throws Exception {
        SocketEnvent socketEnvent=null;
        if(evt instanceof SocketEnvent){
            socketEnvent=(SocketEnvent)evt;
        }else {
         socketEnvent =new SocketEnvent(ctx,evt);
        }
        SocketEnvent finalSocketEnvent = socketEnvent;
        socketListeners.forEach(p->{
            p.listenerSocketEvent(finalSocketEnvent);
        });
    }

    @Override
    public void exceptionCaught(ChannelHandlerContext ctx, Throwable cause) throws Exception {
        SocketEnvent socketEnvent=new SocketEnvent(ctx, ImEvent.errorHappen);
        socketListeners.forEach(p->{
            p.listenerSocketEvent(socketEnvent);
        });
        cause.printStackTrace();
       // ctx.close();
    }

    @Override
    public void channelInactive(ChannelHandlerContext ctx) throws Exception {
        Boolean isLogin = getDataByKey(ctx.channel(), Boolean.class, WebSocketConnetion.LOGIN_FALG);
        if(isLogin!=null&&isLogin){
            ctx.close().addListener(p->{
                SocketEnvent socketEnvent=new SocketEnvent(ctx, ImEvent.loginOut);
                socketListeners.forEach(p2->{
                    p2.listenerSocketEvent(socketEnvent);
                });

            });
        }else {
            ctx.close();
        }
    }
}
