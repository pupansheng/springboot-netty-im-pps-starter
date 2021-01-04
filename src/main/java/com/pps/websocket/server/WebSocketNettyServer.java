package com.pps.websocket.server;

import com.pps.websocket.server.hander.AuthorityWebSovketHandler;
import com.pps.websocket.server.hander.BinaryWebSocketFrameHandler;
import com.pps.websocket.server.hander.EventAndErrorHander;
import com.pps.websocket.server.hander.TextWebSocketFrameHandler;
import com.pps.websocket.server.hander.http.HttpService;
import com.pps.websocket.server.listener.SocketListener;
import io.netty.bootstrap.ServerBootstrap;
import io.netty.channel.*;
import io.netty.channel.nio.NioEventLoopGroup;
import io.netty.channel.socket.SocketChannel;
import io.netty.channel.socket.nio.NioServerSocketChannel;
import io.netty.handler.codec.http.HttpObjectAggregator;
import io.netty.handler.codec.http.HttpServerCodec;
import io.netty.handler.codec.http.websocketx.WebSocketServerProtocolHandler;
import io.netty.handler.stream.ChunkedWriteHandler;


import java.util.List;
import java.util.function.Consumer;


/**
 * @author
 * @discription;
 * @time 2020/12/11 17:21
 */

public class WebSocketNettyServer {
     int port;
     EventLoopGroup bossGroup;
     EventLoopGroup workerGroup;
     ServerBootstrap serverBootstrap;
     String  websocketPath;
     ChannelInitializer<SocketChannel> channelChannelInitializer;
     Consumer<ChannelPipeline> consumer;
     List<SocketListener> socketListeners;
     List<HttpService> httpServices;
    public WebSocketNettyServer(int port, String websocketPath){
         this.port=port;
         this.websocketPath=websocketPath;
         bossGroup=new NioEventLoopGroup(1);
         workerGroup=new NioEventLoopGroup();
         serverBootstrap=new ServerBootstrap();
    }
    public WebSocketNettyServer(int port, int workThread, String websocketPath){
        this.port=port;
        this.websocketPath=websocketPath;
        bossGroup=new NioEventLoopGroup(1);
        workerGroup=new NioEventLoopGroup(workThread);
        serverBootstrap=new ServerBootstrap();
    }
    public WebSocketNettyServer(int port, int boosThread, int workThread, String websocketPath){
        this.port=port;
        this.websocketPath=websocketPath;
        bossGroup=new NioEventLoopGroup(boosThread);
        workerGroup=new NioEventLoopGroup(workThread);
        serverBootstrap=new ServerBootstrap();

    }
    public void initChannelInitializer(){

        channelChannelInitializer=new ChannelInitializer<SocketChannel>() {
            @Override
            public void initChannel(SocketChannel ch) throws Exception {

                ChannelPipeline pipeline = ch.pipeline();

                //websocket协议本身是基于http协议的，所以这边也要使用http解编码器
                pipeline.addLast(new HttpServerCodec());

                //以块的方式来写的处理器
                pipeline.addLast(new ChunkedWriteHandler());

                //netty是基于分段请求的，HttpObjectAggregator的作用是将请求分段再聚合,参数是聚合字节的最大长度
                pipeline.addLast(new HttpObjectAggregator(8192));

                //权限与 http处理
                AuthorityWebSovketHandler authorityWebSovketHandler = new AuthorityWebSovketHandler();
                authorityWebSovketHandler.setHttpServices(httpServices);
                pipeline.addLast(authorityWebSovketHandler);


                pipeline.addLast(new WebSocketServerProtocolHandler(websocketPath));


                //文本处理
                pipeline.addLast(new TextWebSocketFrameHandler());

                //二进制文件处理
                pipeline.addLast(new BinaryWebSocketFrameHandler());

                //全局错误与事件捕捉处理
                EventAndErrorHander eventAndErrorHander = new EventAndErrorHander();
                eventAndErrorHander.setSocketListeners(socketListeners);
                pipeline.addLast(eventAndErrorHander);

                //自定义通道处理
                if(consumer!=null) {
                    consumer.accept(pipeline);
                }

            };
        };
    }
    public WebSocketNettyServer customChannelPipeline(Consumer<ChannelPipeline> consumer){
        this.consumer=consumer;
        return  this;
    }
    public WebSocketNettyServer setListener(List<SocketListener> socketListeners){
        this.socketListeners=socketListeners;
        return  this;
    }
    public WebSocketNettyServer setHttpService(List<HttpService> list){
        this.httpServices=list;
        return  this;
    }
    public void startImServer(){
        try {
            initChannelInitializer();
            serverBootstrap.group(bossGroup, workerGroup)
                    .channel(NioServerSocketChannel.class)
                    .childHandler(channelChannelInitializer)
                    .option(ChannelOption.SO_BACKLOG, 128)
                    .childOption(ChannelOption.SO_KEEPALIVE, true);
            // Bind and start to accept incoming connections.
            ChannelFuture f=serverBootstrap.bind(port).sync();
            System.out.println("Pps-netty- Im 服务器启动成功("+port+")--------");
            f.channel().closeFuture().sync().addListener((e)->{
                System.out.println("Pps-netty- Im 服务器关闭成功--------");
            });
        } catch (InterruptedException e) {
            e.printStackTrace();
        } finally{
            //资源优雅释放
            bossGroup.shutdownGracefully();
            workerGroup.shutdownGracefully();
        }


    }



}
