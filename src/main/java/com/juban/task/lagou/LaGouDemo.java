package com.juban.task.lagou;

import lombok.extern.slf4j.Slf4j;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import us.codecraft.webmagic.Page;
import us.codecraft.webmagic.Request;
import us.codecraft.webmagic.Site;
import us.codecraft.webmagic.Spider;
import us.codecraft.webmagic.model.HttpRequestBody;
import us.codecraft.webmagic.processor.PageProcessor;
import us.codecraft.webmagic.selector.JsonPathSelector;
import us.codecraft.webmagic.utils.HttpConstant;

import java.util.HashMap;
import java.util.List;
import java.util.Map;
@Slf4j
public class LaGouDemo implements PageProcessor {

    private Logger logger = LoggerFactory.getLogger(getClass());

    int flag = 0;
    int mark = 0;
    int sun = 0;
    int sub = 0;
    int ty = 0;
    int tr = 0;

    private Site site = Site.me()
            .setCharset("utf8")
            .setTimeOut(2000) //超时时间
            .setRetrySleepTime(2000) //重试时间
            .setRetryTimes(1)//尝试次数
            .setSleepTime(3000)
            .addHeader("Accept","application/json, text/javascript, */*; q=0.01")
            .addHeader("Accept-Encoding","gzip, deflate, br")
            .addHeader("Accept-Language","zh-CN,zh;q=0.9")
            .addHeader("Connection","keep-alive")
            //.addHeader("Content-Length","23")
            .addHeader("Content-Type","application/x-www-form-urlencoded; charset=UTF-8")
            .addHeader("Cookie","_ga=GA1.2.1367486095.1543215704; user_trace_token=20181126150144-223c1ca8-f149-11e8-8c01-5254005c3644; LGUID=20181126150144-223c213d-f149-11e8-8c01-5254005c3644; hasDeliver=0; index_location_city=%E6%9D%AD%E5%B7%9E; showExpriedIndex=1; showExpriedCompanyHome=1; showExpriedMyPublish=1; sensorsdata2015jssdkcross=%7B%22distinct_id%22%3A%22167553021f0488-0d1bdf816997d2-6313363-1327104-167553021f1884%22%2C%22%24device_id%22%3A%22167553021f0488-0d1bdf816997d2-6313363-1327104-167553021f1884%22%2C%22props%22%3A%7B%22%24latest_traffic_source_type%22%3A%22%E7%9B%B4%E6%8E%A5%E6%B5%81%E9%87%8F%22%2C%22%24latest_referrer%22%3A%22%22%2C%22%24latest_referrer_host%22%3A%22%22%2C%22%24latest_search_keyword%22%3A%22%E6%9C%AA%E5%8F%96%E5%88%B0%E5%80%BC_%E7%9B%B4%E6%8E%A5%E6%89%93%E5%BC%80%22%7D%7D; WEBTJ-ID=20181204173920-167789611d1e81-079e86f8168bf-6313363-1327104-167789611d275d; _gid=GA1.2.336187487.1543916360; JSESSIONID=ABAAABAAAGGABCB9936EE8E43C6135C1212C3DE2E12E602; X_HTTP_TOKEN=3289f6ca9d2491e90a445cdbd88229c7; Hm_lvt_4233e74dff0ae5bd0a3d81c6ccf756e6=1543331248,1543369281,1543916360,1543924307; LG_LOGIN_USER_ID=e37e2a36f844adb75eda618d4d78e0f4038ac62574271a6ebc46695eb76a2cb6; _putrc=72BEFF833A8357DC123F89F2B170EADC; login=true; unick=%E9%83%91%E9%AB%98%E5%8D%87; gate_login_token=a4228e4ba8848eb0850c38dbd6e922dc7c438f8543f59f33e54b18a3f39caee1; _gat=1; LGSID=20181204214236-75e85b1b-f7ca-11e8-8ad2-525400f775ce; PRE_UTM=; PRE_HOST=; PRE_SITE=https%3A%2F%2Fwww.lagou.com%2F%3Fmsg%3Dvalidation%26uStatus%3D3%26clientIp%3D218.75.38.2; PRE_LAND=https%3A%2F%2Fwww.lagou.com%2Fjobs%2Flist_java%25E5%2590%258E%25E7%25AB%25AF%3FlabelWords%3D%26fromSearch%3Dtrue%26suginput%3D; Hm_lpvt_4233e74dff0ae5bd0a3d81c6ccf756e6=1543931362; LGRID=20181204215012-85613fd4-f7cb-11e8-8ad2-525400f775ce; TG-TRACK-CODE=index_search; SEARCH_ID=edcd4df972d24826a15f9785e03a2225")
            .addHeader("Host","www.lagou.com")
            .addHeader("Origin","https://www.lagou.com")
            .addHeader("Referer","https://www.lagou.com/jobs/list_%E5%9F%8E%E5%B8%82%E7%BB%8F%E7%90%86?labelWords=&fromSearch=true&suginp")
            .addHeader("User-Agent","Mozilla/5.0 (Windows NT 10.0; Win64; x64) AppleWebKit/537.36 (KHTML, like Gecko) Chrome/70.0.3538.110 Safari/537.36")
            .addHeader("X-Anit-Forge-Code","0")
            .addHeader("X-Anit-Forge-Token","None")
            .addHeader("X-Requested-With","XMLHttpRequest");


    @Override
    public void process(Page page) {
        this.processBeiJing(page);
        page.putField("positionname",new JsonPathSelector("$.content.positionResult.result[*].positionName").selectList(page.getRawText()));
        page.putField("workYear",new JsonPathSelector("$.content.positionResult.result[*].workYear").selectList(page.getRawText()));
        page.putField("salary",new JsonPathSelector("$.content.positionResult.result[*].salary").selectList(page.getRawText()));
        page.putField("address",new JsonPathSelector("$.content.positionResult.result[*].city").selectList(page.getRawText()));
        page.putField("district",new JsonPathSelector("$.content.positionResult.result[*].district").selectList(page.getRawText()));
        page.putField("createTime",new JsonPathSelector("$.content.positionResult.result[*].createTime").selectList(page.getRawText()));
        page.putField("companyName",new JsonPathSelector("$.content.positionResult.result[*].companyFullName").selectList(page.getRawText()));
        page.putField("discription",new JsonPathSelector("$.content.positionResult.result[*].secondType").selectList(page.getRawText()));


        List<String> list = new JsonPathSelector("$.content.positionResult.result[*].positionId").selectList(page.getRawText());

        list.forEach( i -> {
            logger.info("★............★https://www.lagou.com/jobs/"+i+".html");
        } );



    }

    @Override
    public Site getSite() {
        return site;
    }


    //爬取北京的java职位信息
    public void processBeiJing(Page page)
    {

        System.out.println("★==="+page.getUrl());

        if(flag==0)
        {

            Request[] requests = new Request[3];
            Map<String,Object> map = new HashMap<String, Object>();
            for(int i=0;i<requests.length;i++)
            {
                requests[i] = new Request("https://www.lagou.com/jobs/positionAjax.json?city=%E6%9D%AD%E5%B7%9E&needAddtionalResult=false");
                requests[i].setMethod(HttpConstant.Method.POST);
                if(i==0)
                {
                    map.put("first","true");
                    map.put("pn",i+1);
                    map.put("kd","java");
                    requests[i].setRequestBody(HttpRequestBody.form(map,"utf-8"));
                    page.addTargetRequest(requests[i]);
                }
                else
                {
                    map.put("first","false");
                    map.put("pn",i+1);
                    map.put("kd","java");
                    requests[i].setRequestBody(HttpRequestBody.form(map,"utf-8"));
                    page.addTargetRequest(requests[i]);
                }
            }

            flag++;
        }
    }
    public static void main(String[] args) {

        Spider.create(new LaGouDemo())
                .addUrl("https://www.lagou.com/jobs/positionAjax.json?city=%E6%9D%AD%E5%B7%9E&needAddtionalResult=false")//.setDownloader(httpClientDownloader)
                //.setScheduler(new QueueScheduler().setDuplicateRemover(new BloomFilterDuplicateRemover(100000)))
                .thread(1)

                //.addPipeline(saveLaGouJobInfoPipeline)
                .run();

    }
}
//https://www.lagou.com/jobs/positionAjax.json?city=%E6%9D%AD%E5%B7%9E&needAddtionalResult=false

