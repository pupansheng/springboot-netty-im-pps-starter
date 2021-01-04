package com.pps.websocket.server.util;

import com.alibaba.fastjson.JSON;
import com.pps.websocket.server.chat.ChatProtocl;
import com.pps.websocket.server.chat.ChatSendProtocl;
import com.pps.websocket.server.chat.MsgType;
import io.netty.buffer.ByteBuf;
import io.netty.buffer.Unpooled;
import io.netty.channel.ChannelHandlerContext;
import io.netty.handler.codec.http.websocketx.BinaryWebSocketFrame;
import io.netty.handler.codec.http.websocketx.TextWebSocketFrame;
import io.netty.util.CharsetUtil;

/**
 * @author
 * @discription;
 * @time 2020/12/21 17:18
 */
public class SendMsgUtil {


    /**
     * 发送警告信息
     * @param content
     * @param error
     */
    public static void sendMsgToClientForErrorInfo(ChannelHandlerContext content, String error){
        ChatSendProtocl chatSendProtocl=new ChatSendProtocl();
        chatSendProtocl.setData(error);
        chatSendProtocl.setMsgType(MsgType.serverError.getType());
        chatSendProtocl.setFrom("server");
        content.writeAndFlush(new TextWebSocketFrame(JSON.toJSONString(chatSendProtocl)));
    }


    /**
     * 发送文本信息
     * @param content
     * @param chatProtocl
     */
    public static void sendMsgToClientForText(ChannelHandlerContext content, ChatProtocl chatProtocl){
        content.writeAndFlush(new TextWebSocketFrame(JSON.toJSONString(chatProtocl)));
    }


    private static void sendMsgToClientForBinnary(ChannelHandlerContext content, ByteBuf byteBuf){
        content.writeAndFlush(new BinaryWebSocketFrame(byteBuf));
    }

    /**
     * 发送字节流
     * @param content
     * @param fileInfo
     * @param byteBuf
     */
    public static void sendMsgToClientForBinnary(ChannelHandlerContext content,String fileInfo,ByteBuf byteBuf){
        ByteBuf buffer2 = Unpooled.buffer();
        buffer2.writeInt(fileInfo.length());
        buffer2.writeCharSequence(fileInfo, CharsetUtil.UTF_8);
        ByteBuf byteBuf1 = buffer2.writeBytes(byteBuf);
        sendMsgToClientForBinnary(content,byteBuf1);
    }

}
