package com.juban.service;

import com.juban.pojo.IpPool;

import java.util.List;

public interface IPPoolService {


    List<IpPool> getIPs();
    IpPool getIP();

    void updateIPStatus(IpPool ipPool);

}
