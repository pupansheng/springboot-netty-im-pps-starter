package com.pps.websocket.server.hander.http.util;

import com.alibaba.fastjson.JSON;
import com.alibaba.fastjson.JSONObject;
import io.netty.buffer.ByteBuf;
import io.netty.buffer.Unpooled;
import io.netty.handler.codec.http.*;
import io.netty.handler.codec.http.multipart.DefaultHttpDataFactory;
import io.netty.handler.codec.http.multipart.HttpPostRequestDecoder;
import io.netty.handler.codec.http.multipart.InterfaceHttpData;
import io.netty.handler.codec.http.multipart.MemoryAttribute;
import io.netty.util.CharsetUtil;

import java.io.UnsupportedEncodingException;
import java.util.HashMap;
import java.util.List;
import java.util.Map;
import java.util.function.Consumer;

import static io.netty.buffer.Unpooled.copiedBuffer;

/**
 * @author
 * @discription;
 * @time 2020/12/15 15:03
 */
public class HttpUtll {

    public static String getPathWithoutParam(String url){
        int i = url.indexOf("?");
        if(i!=-1) {
            String substring = url.substring(0, i);
            return substring;
        }else {
            return url;
        }
    }

    public static Map<String, Object> getGetParamasFromChannel(FullHttpRequest fullHttpRequest) {
        Map<String, Object> params = new HashMap<String, Object>();
        if (fullHttpRequest.method() == HttpMethod.GET) {
            QueryStringDecoder decoder = new QueryStringDecoder(fullHttpRequest.uri());
            Map<String, List<String>> paramList = decoder.parameters();
            for (Map.Entry<String, List<String>> entry : paramList.entrySet()) {
                params.put(entry.getKey(), entry.getValue().get(0));
            }
            return params;
        } else {
            return null;
        }
    }

    public static  Map<String, Object> getPostParamsFromChannel(FullHttpRequest fullHttpRequest) {
        Map<String, Object> params = new HashMap<String, Object>();
        if (fullHttpRequest.method() == HttpMethod.POST) {
            String strContentType = fullHttpRequest.headers().get("Content-type").trim();
//            if (strContentType.contains("x-www-form-urlencoded")) {
            if (strContentType.contains("form")) {
                params = getFormParams(fullHttpRequest);
            } else if (strContentType.contains("application/json")) {
                try {
                    params = getJSONParams(fullHttpRequest);
                } catch (UnsupportedEncodingException e) {
                    return null;
                }
            } else {
                return null;
            }
            return params;
        }
        return null;
    }

    public static  Map<String, Object> getFormParams(FullHttpRequest fullHttpRequest) {
        Map<String, Object> params = new HashMap<String, Object>();
        // HttpPostMultipartRequestDecoder
        HttpPostRequestDecoder decoder = new HttpPostRequestDecoder(new DefaultHttpDataFactory(false), fullHttpRequest);
        List<InterfaceHttpData> postData = decoder.getBodyHttpDatas();
        for (InterfaceHttpData data : postData) {
            if (data.getHttpDataType() == InterfaceHttpData.HttpDataType.Attribute) {
                MemoryAttribute attribute = (MemoryAttribute) data;
                params.put(attribute.getName(), attribute.getValue());
            }
        }
        return params;
    }

    public static Map<String, Object> getJSONParams(FullHttpRequest fullHttpRequest) throws UnsupportedEncodingException {
        Map<String, Object> params = new HashMap<String, Object>();
        ByteBuf content = fullHttpRequest.content();
        byte[] reqContent = new byte[content.readableBytes()];
        content.readBytes(reqContent);
        String strContent = new String(reqContent, "UTF-8");
        JSONObject jsonObject = JSONObject.parseObject(strContent);
        for (String key : jsonObject.keySet()) {
            params.put(key, jsonObject.get(key));
        }
        return params;
    }

    public static  FullHttpResponse responseHandlerTextPlain(String responseContent) {
        ByteBuf content = copiedBuffer(responseContent, CharsetUtil.UTF_8);
        FullHttpResponse response = new DefaultFullHttpResponse(HttpVersion.HTTP_1_1,HttpResponseStatus.OK, content);
        response.headers().set("Content-Type", "text/plain;charset=UTF-8;");
        response.headers().set("Content-Length", response.content().readableBytes());
        return response;
    }
    public static  FullHttpResponse responseHandlerApplicationJson(String responseContent) {
        ByteBuf content = copiedBuffer(responseContent, CharsetUtil.UTF_8);
        FullHttpResponse response = new DefaultFullHttpResponse(HttpVersion.HTTP_1_1,HttpResponseStatus.OK, content);
        response.headers().set("Content-Type", "application/json;charset=utf-8");
        response.headers().set("Content-Length", response.content().readableBytes());
        return response;
    }
    public static  FullHttpResponse responseHandlerApplicationJsonWithObj(Object responseContent) {
        String string = JSON.toJSONString(responseContent);
        ByteBuf content = copiedBuffer(string, CharsetUtil.UTF_8);
        FullHttpResponse response = new DefaultFullHttpResponse(HttpVersion.HTTP_1_1,HttpResponseStatus.OK, content);
        response.headers().set("Content-Type", "application/json;charset=utf-8");
        response.headers().set("Content-Length", response.content().readableBytes());
        response.headers().set("Access-Control-Allow-Origin","*");
        response.headers().set("Access-Control-Allow-Methods","*");
        response.headers().set("Access-Control-Max-Age","3600");
        return response;
    }
    public static  FullHttpResponse responseHandlerTextPlain(byte [] responseContent) {
        ByteBuf content = Unpooled.wrappedBuffer(responseContent);
        FullHttpResponse response = new DefaultFullHttpResponse(HttpVersion.HTTP_1_1,HttpResponseStatus.OK, content);
        response.headers().set("Content-Type", "text/plain;charset=UTF-8;");
        response.headers().set("Content-Length", response.content().readableBytes());
        return response;
    }
    public static  FullHttpResponse responseHandlerApplicationJson(byte [] responseContent) {
        ByteBuf content = Unpooled.wrappedBuffer(responseContent);
        FullHttpResponse response = new DefaultFullHttpResponse(HttpVersion.HTTP_1_1,HttpResponseStatus.OK, content);
        response.headers().set("Content-Type", "application/json;charset=utf-8");
        response.headers().set("Content-Length", response.content().readableBytes());
        return response;
    }
    public static  FullHttpResponse responseHandler(byte [] responseContent, Consumer<HttpHeaders> httpHeadersConsumer) {
        ByteBuf content = Unpooled.wrappedBuffer(responseContent);
        FullHttpResponse response = new DefaultFullHttpResponse(HttpVersion.HTTP_1_1,HttpResponseStatus.OK, content);
        httpHeadersConsumer.accept(response.headers());
        return response;
    }


}
