package com.pps.websocket.server.hander;

import com.pps.websocket.server.event.ImEvent;
import com.pps.websocket.server.event.SocketEnvent;
import io.netty.channel.ChannelHandlerContext;
import io.netty.channel.SimpleChannelInboundHandler;
import io.netty.handler.codec.http.websocketx.BinaryWebSocketFrame;


/**
 * @author
 * @discription; 二进制处理
 * @time 2020/12/17 16:00
 */

public class BinaryWebSocketFrameHandler  extends SimpleChannelInboundHandler<BinaryWebSocketFrame> {

    @Override
    protected void channelRead0(ChannelHandlerContext ctx, BinaryWebSocketFrame msg) throws Exception {
        SocketEnvent socketEnvent=new SocketEnvent(ctx, ImEvent.acceptBin,msg.content(),msg);
        ctx.fireUserEventTriggered(socketEnvent);
    }
}