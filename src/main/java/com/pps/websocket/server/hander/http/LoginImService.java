package com.pps.websocket.server.hander.http;

import com.pps.websocket.server.common.entity.Result;
import com.pps.websocket.server.common.model.HttpStatus;
import com.pps.websocket.server.hander.http.util.HttpUtll;
import io.netty.channel.ChannelHandlerContext;
import io.netty.handler.codec.http.FullHttpRequest;


import java.util.Map;

/**
 * @author Im登录接口
 * @discription;
 * @time 2020/12/15 16:21
 */
public abstract   class LoginImService implements HttpService {

    private String url;

    public LoginImService(String url){
        this.url=url;
    }

    @Override
    public boolean isMatch(String url, FullHttpRequest fullHttpRequest) {
        return this.url.equals(HttpUtll.getPathWithoutParam(url));
    }

    /**
     * 验证用户是否合法
     * @param requestParams 登录请求带的参数 若用户不合法返回空 否则返回希望返回客户端的内容
     * @return
     */
    public abstract Object  validUser( Map<String, Object> requestParams,ChannelHandlerContext ctx);


    @Override
    public void   doPost(FullHttpRequest fullHttpReques, Map<String, Object> requestParams, ChannelHandlerContext ctx) {

        Object o = validUser(requestParams,ctx);
        if(o!=null){
            ctx.writeAndFlush(HttpUtll.responseHandlerApplicationJsonWithObj(Result.Ok(o)));
        }else {
            ctx.writeAndFlush(HttpUtll.responseHandlerApplicationJsonWithObj(Result.error(HttpStatus.BuessError, "用户不存在或密码错误")));
        }
        //释放资源
        release(ctx,fullHttpReques);
    }

    @Override
    public void   doGet(FullHttpRequest fullHttpRequest, Map<String, Object> requestParams, ChannelHandlerContext ctx) {
        doPost(fullHttpRequest,requestParams,ctx);
    }
}
