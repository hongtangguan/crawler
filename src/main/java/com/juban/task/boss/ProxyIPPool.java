package com.juban.task.boss;

import com.alibaba.fastjson.JSON;
import com.alibaba.fastjson.JSONObject;
import com.juban.pojo.IpPool;
import com.juban.utils.HttpClientUtils;
import org.apache.commons.collections.CollectionUtils;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import us.codecraft.webmagic.Page;
import us.codecraft.webmagic.Task;
import us.codecraft.webmagic.proxy.Proxy;
import us.codecraft.webmagic.proxy.ProxyProvider;

import java.util.Date;
import java.util.List;

public class ProxyIPPool implements ProxyProvider {

    private Logger logger = LoggerFactory.getLogger(ProxyIPPool.class);

    private String apiUrl;

    private Long previousGetTime;

    private int ttl;

    private Proxy proxy;

    public ProxyIPPool(String apiUrl) {
        this.apiUrl = apiUrl;
    }

    public static ProxyIPPool from(String apiUrl) {
        return new ProxyIPPool(apiUrl);
    }

    @Override
    public void returnProxy(Proxy proxy, Page page, Task task) {

    }

    @Override
    public Proxy getProxy(Task task) {
        try {
            long currentTime = System.currentTimeMillis();
            if (proxy != null && previousGetTime != null && (currentTime - previousGetTime < ttl - 10)) {
                return proxy;
            }
            //.getSite().
            String asString = HttpClientUtils.doGet(apiUrl);
            //String asString = Request.Get(apiUrl).execute().returnContent().asString(Charset.forName("UTF-8"));
            logger.info("获取ip=========="+asString);
            Data5uReturnContent data5uReturnContent = decodeReturnText(asString);
            if (data5uReturnContent != null) {
                previousGetTime = currentTime;
                ttl = data5uReturnContent.getTtl();
                proxy = new Proxy(data5uReturnContent.getIp(), data5uReturnContent.getPort());
                return proxy;
            }
        } catch (Exception e) {
            logger.error("get proxy error,apiUrl:{},previousGetTime:{},ttl:{},proxy:{}", apiUrl, previousGetTime, ttl, proxy, e);
        }

        return null;
    }

    private Data5uReturnContent decodeReturnText(String returnText) {

        logger.info("请求获取新的ip开始.....................................");
        //解析数据
        JSONObject jsonObject = JSONObject.parseObject(returnText);
        String result = jsonObject.getString("RESULT");
        List<IpPool> ipPools = JSONObject.parseArray(result, IpPool.class);
        logger.info("获取到的ip:....."+ JSON.toJSONString(ipPools));



        if (CollectionUtils.isNotEmpty(ipPools)) {
//            String[] proxyTtl = StringUtils.split(returnText, ",");
////            String proxyText = proxyTtl[0].trim();
////            int ttl = Integer.parseInt(proxyTtl[1].trim());
////
////            String[] ipPort = StringUtils.split(proxyText, ":");
////            String ip = ipPort[0].trim();
////            int port = Integer.parseInt(ipPort[1].trim());

            String ip = ipPools.get(0).getIp();
            String port = ipPools.get(0).getPort();

            return new Data5uReturnContent(ip, Integer.parseInt(port), (int) new Date().getTime());
        }
        return null;
    }
}
