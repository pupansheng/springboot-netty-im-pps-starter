package com.pps.websocket.server.hander.http;

import com.pps.websocket.server.common.entity.Result;
import com.pps.websocket.server.common.model.HttpStatus;
import com.pps.websocket.server.hander.data.DataTransfer;
import com.pps.websocket.server.hander.http.util.HttpUtll;
import io.netty.channel.ChannelHandlerContext;
import io.netty.handler.codec.http.FullHttpRequest;
import io.netty.util.ReferenceCountUtil;


import java.util.Map;

public interface HttpService extends DataTransfer {


   boolean isMatch(String url, FullHttpRequest fullHttpRequest);

   default void doPost(FullHttpRequest fullHttpReques, Map<String, Object> requestParams, ChannelHandlerContext ctx){
    doGet(fullHttpReques,requestParams,ctx);
   }

   default void doGet(FullHttpRequest fullHttpRequest, Map<String, Object> requestParams, ChannelHandlerContext ctx){
      ctx.writeAndFlush(HttpUtll.responseHandlerApplicationJsonWithObj(Result.error(HttpStatus.ERROR2)));
      release(ctx,fullHttpRequest);
   }
   default void release(ChannelHandlerContext ctx, Object msg){
      ReferenceCountUtil.release(msg);
      ctx.close();
   }

   default int index(){
      return Integer.MAX_VALUE;
   };

}
