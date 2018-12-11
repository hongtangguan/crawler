package com.juban.utils;

import com.alibaba.fastjson.JSON;
import com.alibaba.fastjson.JSONObject;
import com.juban.pojo.IpPool;

import java.util.List;

public class IPPoolUtils {


    public static List<IpPool> getIPs(){

        String res = HttpClientUtils.doGet("http://www.xiongmaodaili.com/xiongmao-web/api/glip?secret=660b6832c161ce6a4eae6f4056faf39e&orderNo=GL20181211142127nChLHQRQ&count=1&isTxt=0&proxyType=1");

        System.out.println("请求获取新的ip开始.....................................");
        //解析数据
        JSONObject jsonObject = JSONObject.parseObject(res);
        String result = jsonObject.getString("obj");
        List<IpPool> ipPools = JSONObject.parseArray(result, IpPool.class);
        System.out.println("获取到的ip:....."+ JSON.toJSONString(ipPools));
        return ipPools;
    }


}
