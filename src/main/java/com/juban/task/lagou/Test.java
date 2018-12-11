package com.juban.task.lagou;

import us.codecraft.webmagic.Page;
import us.codecraft.webmagic.Site;
import us.codecraft.webmagic.Spider;
import us.codecraft.webmagic.downloader.HttpClientDownloader;
import us.codecraft.webmagic.processor.PageProcessor;
import us.codecraft.webmagic.proxy.Proxy;
import us.codecraft.webmagic.proxy.SimpleProxyProvider;
import us.codecraft.webmagic.scheduler.BloomFilterDuplicateRemover;
import us.codecraft.webmagic.scheduler.QueueScheduler;

public class Test implements PageProcessor {

    private Site site = Site
            .me()
            .setSleepTime(10000)//爬取的间隔时间.
            .setTimeOut(6000) //超时时间
            .setRetrySleepTime(3000) //重试时间
            .setRetryTimes(2);//尝试次数

    @Override
    public void process(Page page) {

        System.out.println(page.getUrl().toString());

    }

    @Override
    public Site getSite() {
        return site;
    }


    public static void main(String[] args) {
        HttpClientDownloader httpClientDownloader = new HttpClientDownloader();

        httpClientDownloader.setProxyProvider(SimpleProxyProvider.from(new Proxy("115.223.123.130",41175)));
        Spider.create(new Test())
                .addUrl("https://www.baidu.com/")
                .setDownloader(httpClientDownloader)
                .setScheduler(new QueueScheduler().setDuplicateRemover(new BloomFilterDuplicateRemover(100000)))
                .thread(1)

                .run();
    }
}
