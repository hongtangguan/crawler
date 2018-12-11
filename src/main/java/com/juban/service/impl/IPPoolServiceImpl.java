package com.juban.service.impl;

import com.alibaba.fastjson.JSON;
import com.alibaba.fastjson.JSONObject;
import com.juban.mapper.IpPoolMapper;
import com.juban.pojo.IpPool;
import com.juban.service.IPPoolService;
import com.juban.utils.HttpClientUtils;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

import java.util.Date;
import java.util.List;

@Service
public class IPPoolServiceImpl implements IPPoolService {

    @Autowired
    private IpPoolMapper ipPoolMapper;

    //获取代理ip请求地址
    //private static String PROXYIPURL = "http://api.xdaili.cn/xdaili-api//greatRecharge/getGreatIp?spiderId=eafbee03e0584391b7f4a6cb8cea286e&orderno=YZ201811289790YvQMoM&returnType=2&count=2";
    private static String PROXYIPURL = "http://www.xiongmaodaili.com/xiongmao-web/api/glip?secret=660b6832c161ce6a4eae6f4056faf39e&orderNo=GL20181211142127nChLHQRQ&count=3&isTxt=0&proxyType=1";

    private Logger logger = LoggerFactory.getLogger(IPPoolServiceImpl.class);

    @Override
    public List<IpPool> getIPs(){

        String res = HttpClientUtils.doGet(PROXYIPURL);

        logger.info("请求获取新的ip开始.....................................");
        //解析数据
        JSONObject jsonObject = JSONObject.parseObject(res);
        String result = jsonObject.getString("obj");
        List<IpPool> ipPools = JSONObject.parseArray(result, IpPool.class);
        logger.info("获取到的ip:....."+JSON.toJSONString(ipPools));
        return ipPools;
    }


    @Override
    public synchronized IpPool getIP() {

        //数据库查询可用的ip返回
        IpPool ipPool = ipPoolMapper.getAvailableIP();
        if (ipPool == null) {
            //如果没有就请求新的ip
            String res = HttpClientUtils.doGet(PROXYIPURL);
            logger.info("请求ip返回的结果....................................."+res);
            if (res == null) {
                logger.error("=======================请求获取IP失败=============================");
                return null;
            } else {

                logger.info("请求获取新的ip开始.....................................");
                //解析数据
                JSONObject jsonObject = JSONObject.parseObject(res);
                String result = jsonObject.getString("RESULT");

                List<IpPool> ipPools = JSONObject.parseArray(result, IpPool.class);
                Date date = new Date();
                ipPools.forEach(i -> {
                    Integer ipStatus = 1;
                    i.setIpStatus(ipStatus.byteValue());
                    i.setCreateTime(date);
                    i.setUpdateTime(date);
                    ipPoolMapper.insert(i);

                });
            }
            ipPool = ipPoolMapper.getAvailableIP();

            return ipPool;
        }

        logger.info("返回的ipPool......."+ JSON.toJSONString(ipPool));
        return ipPool;
    }

    @Override
    public void updateIPStatus(IpPool ipPool) {
        ipPool.setIpStatus(new Integer(0).byteValue());
        ipPoolMapper.updateByPrimaryKey(ipPool);
    }

    @Override
    public List<IpPool> getAllIPs() {
        return ipPoolMapper.getAllIPs();
    }
}
