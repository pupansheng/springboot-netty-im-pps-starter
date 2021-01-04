package com.pps;

import org.springframework.boot.context.properties.ConfigurationProperties;

/**
 * @author
 * @discription;
 * @time 2020/12/24 9:42
 */
@ConfigurationProperties(prefix = "pps.netty.im")
public class PpsNettyImProperty {

    private boolean enable;

    private String  websocketPath;

    private int port;

    private int bossThread=1;

    private int workthead=1;


    public String getWebsocketPath() {
        return websocketPath;
    }

    public void setWebsocketPath(String websocketPath) {
        this.websocketPath = websocketPath;
    }

    public int getPort() {
        return port;
    }

    public void setPort(int port) {
        this.port = port;
    }

    public int getBossThread() {
        return bossThread;
    }

    public void setBossThread(int bossThread) {
        this.bossThread = bossThread;
    }

    public int getWorkthead() {
        return workthead;
    }

    public void setWorkthead(int workthead) {
        this.workthead = workthead;
    }
}
