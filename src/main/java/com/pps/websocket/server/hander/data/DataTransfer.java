package com.pps.websocket.server.hander.data;

import io.netty.channel.Channel;
import io.netty.util.AttributeKey;

import java.util.HashMap;
import java.util.Map;

public interface DataTransfer {
    AttributeKey<Map> attrKey=AttributeKey.valueOf("channel-data-transfer");


    default void  putData(Channel channel, Object k, Object v){
        boolean b = channel.hasAttr(attrKey);
        if(b){
            Map map= channel.attr(attrKey).get();
            map.put(k,v);
        }else {
            Map map=new HashMap();
            map.put(k,v);
            channel.attr(attrKey).set(map);
        }
    }

    default Map getData(Channel channel){
        boolean b = channel.hasAttr(attrKey);
        if(b){
            Map map= channel.attr(attrKey).get();
            return map;
        }else {
            return  new HashMap();
        }
    }

    default <T> T getDataByKey(Channel channel, Class<T> t, Object k){
        boolean b = channel.hasAttr(attrKey);
        if(b){
            Map map= channel.attr(attrKey).get();
            return (T)map.get(k);
        }else {
            return  null;
        }
    }
    default Object getDataByKey(Channel channel, Object k){
        boolean b = channel.hasAttr(attrKey);
        if(b){
            Map map= channel.attr(attrKey).get();
            return map.get(k);
        }else {
            return  null;
        }
    }
}
