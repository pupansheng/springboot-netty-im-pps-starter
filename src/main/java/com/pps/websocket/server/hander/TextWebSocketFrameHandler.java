package com.pps.websocket.server.hander;

import com.pps.websocket.server.event.ImEvent;
import com.pps.websocket.server.event.SocketEnvent;
import com.pps.websocket.server.hander.data.DataTransfer;
import io.netty.channel.ChannelHandlerContext;
import io.netty.channel.SimpleChannelInboundHandler;
import io.netty.handler.codec.http.websocketx.TextWebSocketFrame;



//处理文本协议数据，处理TextWebSocketFrame类型的数据，websocket专门处理文本的frame就是TextWebSocketFrame
public class TextWebSocketFrameHandler extends SimpleChannelInboundHandler<TextWebSocketFrame> implements DataTransfer {


    //读到客户端的内容并且向客户端去写内容
    @Override
    protected void channelRead0(ChannelHandlerContext ctx, TextWebSocketFrame msg) throws Exception {

         SocketEnvent socketEnvent=new SocketEnvent(ctx, ImEvent.acceptText,msg.text(),msg);
         ctx.fireUserEventTriggered(socketEnvent);

    }





}