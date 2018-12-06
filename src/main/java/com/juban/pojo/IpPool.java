package com.juban.pojo;

import java.util.Date;

/**
*
*/
public class IpPool {
    /**
    *
    */
    private Integer id;

    /**
    *ip
    */
    private String ip;

    /**
    *端口
    */
    private String port;

    /**
    *0:已用. 1:未用
    */
    private Byte ipStatus;

    /**
    *
    */
    private Date createTime;

    /**
    *
    */
    private Date updateTime;

    public Integer getId() {
        return id;
    }

    public void setId(Integer id) {
        this.id = id;
    }

    public String getIp() {
        return ip;
    }

    public void setIp(String ip) {
        this.ip = ip == null ? null : ip.trim();
    }

    public String getPort() {
        return port;
    }

    public void setPort(String port) {
        this.port = port == null ? null : port.trim();
    }

    public Byte getIpStatus() {
        return ipStatus;
    }

    public void setIpStatus(Byte ipStatus) {
        this.ipStatus = ipStatus;
    }

    public Date getCreateTime() {
        return createTime;
    }

    public void setCreateTime(Date createTime) {
        this.createTime = createTime;
    }

    public Date getUpdateTime() {
        return updateTime;
    }

    public void setUpdateTime(Date updateTime) {
        this.updateTime = updateTime;
    }
}