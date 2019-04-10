package com.juban.utils;

import com.alibaba.fastjson.JSON;
import com.alibaba.fastjson.JSONObject;
import com.juban.pojo.IpPool;

import java.util.List;

public class IPPoolUtils {


    public static List<IpPool> getIPs(){

        String res = HttpClientUtils.doGet("http://api.xdaili.cn/xdaili-api//greatRecharge/getGreatIp?spiderId=eafbee03e0584391b7f4a6cb8cea286e&orderno=YZ201811289790YvQMoM&returnType=2&count=2");

        System.out.println("请求获取新的ip开始.....................................");
        //解析数据
        JSONObject jsonObject = JSONObject.parseObject(res);
        String result = jsonObject.getString("obj");
        List<IpPool> ipPools = JSONObject.parseArray(result, IpPool.class);
        System.out.println("获取到的ip:....."+ JSON.toJSONString(ipPools));
        return ipPools;
    }


    public static String getIPs2(){

        String res = HttpClientUtils.doGet("http://api.xdaili.cn/xdaili-api//greatRecharge/getGreatIp?spiderId=eafbee03e0584391b7f4a6cb8cea286e&orderno=YZ201811289790YvQMoM&returnType=2&count=2");

        return res;
    }


    public static void main(String[] args) {

        String rss = getIPs2();

        System.out.println(rss);

    }


}
