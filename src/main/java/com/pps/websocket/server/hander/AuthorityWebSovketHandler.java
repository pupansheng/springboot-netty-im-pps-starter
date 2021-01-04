package com.pps.websocket.server.hander;

import com.pps.websocket.server.common.entity.Result;
import com.pps.websocket.server.common.model.HttpStatus;
import com.pps.websocket.server.hander.data.DataTransfer;
import com.pps.websocket.server.hander.http.HttpService;
import com.pps.websocket.server.hander.http.util.HttpUtll;
import io.netty.channel.ChannelHandlerContext;
import io.netty.channel.ChannelInboundHandlerAdapter;
import io.netty.handler.codec.http.*;
import io.netty.util.ReferenceCountUtil;


import java.util.ArrayList;
import java.util.List;

/**
 * @author
 * @discription;权限过滤
 * @time 2020/12/16 14:03
 */
public class AuthorityWebSovketHandler extends ChannelInboundHandlerAdapter implements DataTransfer {

    private List<HttpService> httpServices=new ArrayList<>();

    public void addHttpService(HttpService httpService){
        httpServices.add(httpService);
    }

    public void setHttpServices(List<HttpService> httpServices) {
        this.httpServices = httpServices;
    }

    @Override
    public void channelRead(ChannelHandlerContext ctx, Object msg) throws Exception {



    if (msg instanceof FullHttpMessage) {

            FullHttpRequest fullHttpRequest=(FullHttpRequest)msg;
            HttpService httpService=null;
            FullHttpResponse response=null;
            for(HttpService httpServiceT:httpServices){
                if(httpServiceT.isMatch(fullHttpRequest.uri(),fullHttpRequest)){
                    httpService=httpServiceT;
                    break;
                }
            }
            if(httpService==null){
                response = HttpUtll.responseHandlerApplicationJsonWithObj(Result.error(HttpStatus.ERROR2));
                ReferenceCountUtil.release(msg);
                ctx.writeAndFlush(response);
                ctx.close();
            }else {
                if (fullHttpRequest.method() == HttpMethod.GET) {
                     httpService.doGet(fullHttpRequest,HttpUtll.getGetParamasFromChannel(fullHttpRequest),ctx);
                } else if (fullHttpRequest.method() == HttpMethod.POST) {
                     httpService.doPost(fullHttpRequest,HttpUtll.getPostParamsFromChannel(fullHttpRequest),ctx);
                } else {
                    response=HttpUtll.responseHandlerApplicationJsonWithObj(Result.error(HttpStatus.BuessError,"不支持请求方式"));
                    ReferenceCountUtil.release(msg);
                    ctx.writeAndFlush(response);
                    ctx.close();
                }
            }
        }else {
            //other protocols
            super.channelRead(ctx, msg);
        }
    }
}
