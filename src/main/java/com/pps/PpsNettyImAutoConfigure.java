package com.pps;

import com.pps.websocket.server.WebSocketNettyServer;
import com.pps.websocket.server.hander.http.HttpService;
import com.pps.websocket.server.listener.SocketListener;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.autoconfigure.condition.ConditionalOnClass;
import org.springframework.boot.autoconfigure.condition.ConditionalOnProperty;
import org.springframework.boot.context.properties.EnableConfigurationProperties;
import org.springframework.context.annotation.Bean;

import java.util.ArrayList;
import java.util.List;

/**
 * @author
 * @discription;
 * @time 2020/6/21 12:50
 */
@EnableConfigurationProperties({PpsNettyImProperty.class})//注入该类进入容器
@ConditionalOnClass(WebSocketNettyServer.class)
@ConditionalOnProperty(prefix = "pps.netty.im",value = "enable",havingValue = "true")
public class PpsNettyImAutoConfigure {

    private final PpsNettyImProperty ppsNettyImProperty;

    @Autowired(required = false)
    private PpsNettyImChannelPipelineCostom ppsNettyImPipleCostom;

    @Autowired(required =false)
    private List<HttpService> httpServices=new ArrayList<>();

    @Autowired(required = false)
    private List<SocketListener> socketListeners=new ArrayList<>();


    public PpsNettyImAutoConfigure(PpsNettyImProperty ppsNettyImProperty) {
        this.ppsNettyImProperty = ppsNettyImProperty;
    }

    @Bean
    public WebSocketNettyServer startWebImSocket(){


        WebSocketNettyServer webSocketNettyServer=new WebSocketNettyServer(
                 ppsNettyImProperty.getPort(),
                  ppsNettyImProperty.getBossThread(),
                 ppsNettyImProperty.getWorkthead(),
                 ppsNettyImProperty.getWebsocketPath());
        if(ppsNettyImPipleCostom!=null){
            webSocketNettyServer.customChannelPipeline(ppsNettyImPipleCostom.custommChannelPipeline());
        }
        webSocketNettyServer.setHttpService(httpServices).setListener(socketListeners);

        new Thread(()->{
            webSocketNettyServer.startImServer();
        }).start();
        return webSocketNettyServer;
    }


}
