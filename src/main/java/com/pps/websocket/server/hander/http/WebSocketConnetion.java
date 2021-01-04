package com.pps.websocket.server.hander.http;

import com.pps.websocket.server.event.ImEvent;
import com.pps.websocket.server.event.SocketEnvent;
import com.pps.websocket.server.hander.http.util.HttpUtll;
import io.netty.channel.ChannelHandlerContext;
import io.netty.handler.codec.http.FullHttpRequest;
import io.netty.util.ReferenceCountUtil;


import java.util.Map;

/**
 * @author webSocket 第一次连接过程 https升级过程
 * @discription;
 * @time 2020/12/16 16:15
 */

public abstract class WebSocketConnetion implements HttpService {

    private static final String FLAG="websocket";
    public static final String LOGIN_FALG="isLogin";
    private String wsUrl;
    public WebSocketConnetion(String wsUrl){
        this.wsUrl=wsUrl;
    }
    @Override
    public boolean isMatch(String url, FullHttpRequest fullHttpRequest) {
        String upgrade = fullHttpRequest.headers().getAsString("Upgrade");
        return FLAG.equals(upgrade)&&wsUrl.equals(HttpUtll.getPathWithoutParam(url));
    }

    /**
     * 是否允许webSocket连接
     * @param requestParams
     * @return
     */
    public abstract Boolean webSocketCanConnetion(Map<String, Object> requestParams,ChannelHandlerContext ctx);

    /**
     * 若允许连接 那么自定义操作内容
     * @param ctx
     */
    public abstract void customOpreate(ChannelHandlerContext ctx);

    @Override
    public void doGet(FullHttpRequest fullHttpRequest, Map<String, Object> requestParams, ChannelHandlerContext ctx) {

        if(webSocketCanConnetion(requestParams,ctx)){
            customOpreate(ctx);
            fullHttpRequest.setUri(wsUrl);
            putData(ctx.channel(),LOGIN_FALG,true);
            ctx.fireChannelRead(fullHttpRequest);
        }else {
            SocketEnvent socketEnvent=new SocketEnvent(ctx, ImEvent.loginFail);
            ctx.fireUserEventTriggered(socketEnvent);
            ctx.close();
            ReferenceCountUtil.release(fullHttpRequest);
        }

    }
}
