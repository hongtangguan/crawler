package com.juban.task.boss;

public class Data5uReturnContent {

    public Data5uReturnContent() {
    }

    public Data5uReturnContent(String ip, int port, int ttl) {
        this.ip = ip;
        this.port = port;
        this.ttl = ttl;
    }

    private String ip;

    private int port;

    private int ttl;

    public String getIp() {
        return ip;
    }

    public void setIp(String ip) {
        this.ip = ip;
    }

    public int getPort() {
        return port;
    }

    public void setPort(int port) {
        this.port = port;
    }

    public int getTtl() {
        return ttl;
    }

    public void setTtl(int ttl) {
        this.ttl = ttl;
    }
}


