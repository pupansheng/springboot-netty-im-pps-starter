package com.pps.websocket.server.event;

import io.netty.channel.ChannelHandlerContext;
import io.netty.handler.codec.http.websocketx.WebSocketServerProtocolHandler;

import java.util.Date;

/**
 * @author
 * @discription;
 * @time 2020/12/17 13:50
 */
public  class SocketEnvent {

  private   ChannelHandlerContext  content;
  private   Object sourceEvent;
  private   Date   happenTime;
  private  Object []  datas;

  public SocketEnvent(ChannelHandlerContext content, Object sourceEvent) {
        this.content = content;
        this.sourceEvent = sourceEvent;
        happenTime=new Date();
  }
    public SocketEnvent(ChannelHandlerContext content, Object sourceEvent,Object ... datas) {
        this.content = content;
        this.sourceEvent = sourceEvent;
        this.datas=datas;
        happenTime=new Date();
    }
    /**
     * 握手成功 成功升级wbsocket协议
     * @return
     */
  public boolean isWebSocketConneton(){
      return this.sourceEvent== WebSocketServerProtocolHandler.ServerHandshakeStateEvent.HANDSHAKE_COMPLETE;
  }

    /**
     * 握手超时
     * @return
     */
  public boolean isTimeOut(){
      return  this.sourceEvent==WebSocketServerProtocolHandler.ServerHandshakeStateEvent.HANDSHAKE_TIMEOUT;
  }


    public ChannelHandlerContext getContent() {
        return content;
    }

    public void setContent(ChannelHandlerContext content) {
        this.content = content;
    }

    public Object getSourceEvent() {
        return sourceEvent;
    }

    public void setSourceEvent(Object sourceEvent) {
        this.sourceEvent = sourceEvent;
    }

    public Date getHappenTime() {
        return happenTime;
    }

    public void setHappenTime(Date happenTime) {
        this.happenTime = happenTime;
    }

    public Object [] getDatas() {
        return datas;
    }

    public void setData(Object ... datas) {
        this.datas = datas;
    }
}
