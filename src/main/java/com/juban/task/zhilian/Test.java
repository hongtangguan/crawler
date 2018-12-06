package com.juban.task.zhilian;

import us.codecraft.webmagic.Page;
import us.codecraft.webmagic.Site;
import us.codecraft.webmagic.Spider;
import us.codecraft.webmagic.processor.PageProcessor;
import us.codecraft.webmagic.selector.JsonPathSelector;

/**
 * 类的说明
 *
 * @author 路大仙儿
 * @version 1.0
 * @date 2018/12/3 13:59
 **/
public class Test implements PageProcessor{

    public static void main(String[] args) {
        Spider.create(new Test())
                .addUrl("https://sou.zhaopin.com/?p=303&jl=530&kw=java&kt=3")
                .thread(5)
                .run();
    }



    @Override
    public void process(Page page) {
        if (page.getUrl().toString().contains("start")) {
            page.addTargetRequests(new JsonPathSelector("$.data.results[*].positionURL").selectList(page.getRawText()));
        }else if (page.getUrl().toString().contains("htm")) {
            System.out.println(page.getUrl().toString());
        }else {
            int pageNum = 6;
            int startNum = 0;
            for (int i = 0; i < pageNum; i++) {
                page.addTargetRequest("https://fe-api.zhaopin.com/c/i/sou?start=" + startNum + "&pageSize=60&cityId=530&workExperience=-1&education=-1&companyType=-1&employmentType=-1&jobWelfareTag=-1&kw=java&kt=3&_v=0.95822583&x-zp-page-request-id=476a554e1ae54a1e84599ba24152fb33-1543811878482-914300");
                startNum+=60;
            }
        }
    }

    private Site site = Site
            .me()
            .setRetryTimes(3)
            .setSleepTime(1000);

    @Override
    public Site getSite() {
        return site;
    }


}
