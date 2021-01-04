package com.pps.websocket.server.listener;


import com.pps.websocket.server.event.SocketEnvent;

public interface SocketListener {
    void listenerSocketEvent(SocketEnvent socketEnvent);
}
