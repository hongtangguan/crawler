package com.juban.task.lagou;

import com.alibaba.fastjson.JSON;
import com.juban.pojo.IpPool;
import com.juban.pojo.JobInfo;
import com.juban.service.IPPoolService;
import org.apache.commons.collections.CollectionUtils;
import org.apache.commons.lang3.StringUtils;
import org.jsoup.Jsoup;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.scheduling.annotation.Scheduled;
import org.springframework.stereotype.Component;
import us.codecraft.webmagic.Page;
import us.codecraft.webmagic.Request;
import us.codecraft.webmagic.Site;
import us.codecraft.webmagic.Spider;
import us.codecraft.webmagic.downloader.HttpClientDownloader;
import us.codecraft.webmagic.model.HttpRequestBody;
import us.codecraft.webmagic.processor.PageProcessor;
import us.codecraft.webmagic.proxy.Proxy;
import us.codecraft.webmagic.proxy.SimpleProxyProvider;
import us.codecraft.webmagic.scheduler.BloomFilterDuplicateRemover;
import us.codecraft.webmagic.scheduler.QueueScheduler;
import us.codecraft.webmagic.selector.JsonPathSelector;
import us.codecraft.webmagic.utils.HttpConstant;

import java.util.Date;
import java.util.HashMap;
import java.util.List;
import java.util.Map;

@Component
public class LagouProcessor implements PageProcessor {

    @Autowired
    private SaveLaGouJobInfoPipeline saveLaGouJobInfoPipeline;
    @Autowired
    private IPPoolService ipPoolService;

    private Logger logger = LoggerFactory.getLogger(LagouProcessor.class);

    //
    private String url = "https://www.lagou.com/jobs/positionAjax.json?city=%E6%9D%AD%E5%B7%9E&needAddtionalResult=false";



    private static int temp = 2;//第二页开始往后
   // private static boolean flag = true;// 查询一共多少页标记
    private static String countNum = "30"; //总页码. 30是默认值.

    private static String IP = "";//IP
    private static String PORT = "";//端口

    int flag = 0;

    private Site site = Site.me()
            .setCharset("utf8")
            .setTimeOut(10*1000) //超时时间
            .setRetrySleepTime(3000) //重试时间
            .setRetryTimes(3)//尝试次数
            .setSleepTime(4000)
            .addHeader("Accept","application/json, text/javascript, */*; q=0.01")
            .addHeader("Accept-Encoding","gzip, deflate, br")
            .addHeader("Accept-Language","zh-CN,zh;q=0.9")
            .addHeader("Connection","keep-alive")
            //.addHeader("Content-Length","23")
            .addHeader("Content-Type","application/x-www-form-urlencoded; charset=UTF-8")
            .addHeader("Cookie","_ga=GA1.2.1367486095.1543215704; user_trace_token=20181126150144-223c1ca8-f149-11e8-8c01-5254005c3644; LGUID=20181126150144-223c213d-f149-11e8-8c01-5254005c3644; hasDeliver=0; index_location_city=%E6%9D%AD%E5%B7%9E; showExpriedIndex=1; showExpriedCompanyHome=1; showExpriedMyPublish=1; sensorsdata2015jssdkcross=%7B%22distinct_id%22%3A%22167553021f0488-0d1bdf816997d2-6313363-1327104-167553021f1884%22%2C%22%24device_id%22%3A%22167553021f0488-0d1bdf816997d2-6313363-1327104-167553021f1884%22%2C%22props%22%3A%7B%22%24latest_traffic_source_type%22%3A%22%E7%9B%B4%E6%8E%A5%E6%B5%81%E9%87%8F%22%2C%22%24latest_referrer%22%3A%22%22%2C%22%24latest_referrer_host%22%3A%22%22%2C%22%24latest_search_keyword%22%3A%22%E6%9C%AA%E5%8F%96%E5%88%B0%E5%80%BC_%E7%9B%B4%E6%8E%A5%E6%89%93%E5%BC%80%22%7D%7D; _gid=GA1.2.336187487.1543916360; LG_LOGIN_USER_ID=e37e2a36f844adb75eda618d4d78e0f4038ac62574271a6ebc46695eb76a2cb6; WEBTJ-ID=20181205000618-16779f85911288-0b0cba203ee7f4-6313363-1327104-16779f85912d9; Hm_lvt_4233e74dff0ae5bd0a3d81c6ccf756e6=1543369281,1543916360,1543924307,1543939578; LGSID=20181205000618-88ea8b2f-f7de-11e8-8ad8-525400f775ce; JSESSIONID=ABAAABAABEEAAJAB919DB8C82FACB4969BBC55B3F96AEA5; _putrc=72BEFF833A8357DC123F89F2B170EADC; login=true; unick=%E9%83%91%E9%AB%98%E5%8D%87; gate_login_token=3fdfc9dda1e60276ae6498f6c85cc9ec6e9d22efb9924532f9801df272b5c246; _gat=1; TG-TRACK-CODE=index_navigation; LGRID=20181205004528-01449893-f7e4-11e8-8cbc-5254005c3644; Hm_lpvt_4233e74dff0ae5bd0a3d81c6ccf756e6=1543941929; SEARCH_ID=2053a087014e4eae91be7f4b45899ecf")
            .addHeader("Host","www.lagou.com")
            .addHeader("Origin","https://www.lagou.com")
            .addHeader("Referer","https://www.lagou.com/jobs/list_Java")
            .addHeader("User-Agent","Mozilla/5.0 (Windows NT 10.0; Win64; x64) AppleWebKit/537.36 (KHTML, like Gecko) Chrome/70.0.3538.110 Safari/537.36")
            .addHeader("X-Anit-Forge-Code","0")
            .addHeader("X-Anit-Forge-Token","None")
            .addHeader("X-Requested-With","XMLHttpRequest");



    //获取代理ip请求地址

    private static String PROXYIPURL = "http://api.xdaili.cn/xdaili-api//greatRecharge/getGreatIp?spiderId=eafbee03e0584391b7f4a6cb8cea286e&orderno=YZ201811289790YvQMoM&returnType=2&count=10";


            //这里注意.  先登录拉钩. 获取cookie.   不然加入自动登录功能.
    @Override
    public Site getSite() {
        return site;
    }

    @Override
    public void process(Page page)  {

        logger.info("========="+page.getUrl().toString());

        if (page.getUrl().toString().contains(".html")) {
            this.saveJobInfo(page);
        }

        List<String> list = null;
        if (page.getUrl().toString().contains("=false")) {
            this.processRequest(page);
            list = new JsonPathSelector("$.content.positionResult.result[*].positionId").selectList(page.getRawText());
        }


        try {
            Thread.sleep(2000);
        } catch (InterruptedException e) {
            e.printStackTrace();
        }

        if (CollectionUtils.isNotEmpty(list)) {

            for (String s : list) {
                page.addTargetRequest("https://www.lagou.com/jobs/"+s+".html");
                logger.info("★............★https://www.lagou.com/jobs/"+s+".html");
            }
        }



    }

    private void processRequest(Page page) {

        System.out.println("★==="+page.getUrl());


            if(flag==0)
            {

                Request[] requests = new Request[1];
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





    private void saveJobInfo(Page page) {


        String url = page.getUrl().toString();
        logger.error("★★★★★....................招聘详情页................"+url);
        //工作名称
        String jobName = page.getHtml().xpath("/html/body/div[2]/div/div[1]/div/span/text()").toString();

        //公司名称
        String companyName = page.getHtml().css("div.position-head div.company", "text").toString();
        //String companyName = page.getHtml().xpath("/html/body/div[2]/div/div[1]/div/div[1]/text()").toString();

        //公司行业
        String companyIndustry = page.getHtml().xpath("//*[@id=\"job_company\"]/dd/ul/li[1]/text()").toString();

        //公司人数
        //String companyPeople = Jsoup.parse(page.getHtml().css(".c_feature > li:nth-child(3)").toString()).text();
        String companyPeople = page.getHtml().xpath("//*[@id=\"job_company\"]/dd/ul/li[3]").toString();
        if (StringUtils.isNotBlank(companyPeople)) {
            companyPeople = Jsoup.parse(companyPeople).text();
        }

       /* if (!companyPeople.contains("-") && !companyPeople.contains("以上") && !companyPeople.contains("少于")){
            companyPeople = Jsoup.parse(page.getHtml().css(".c_feature > li:nth-child(4)").toString()).text();
        }
        if (companyPeople.contains("规模")) {
            companyPeople = companyPeople.replace("规模", "").trim();
        }*/
        //公司融资情况
        String companyFinace = page.getHtml().xpath("//*[@id=\"job_company\"]/dd/ul/li[2]").toString();
        if (StringUtils.isNotBlank(companyFinace)) {
            companyFinace = Jsoup.parse(companyFinace).text();
        }

        //工作描述  //*[@id="job_detail"]/dd[2]
        String jobInfo = "";
        String jobRes = "";
        String jobDetail = page.getHtml().xpath("//*[@id=\"job_detail\"]/dd[2]").toString();
        if (StringUtils.isNotBlank(jobDetail)) {
            jobDetail = Jsoup.parse(jobDetail).text();
            if (jobDetail.contains("任职要求")) {
                jobInfo = jobDetail.substring(jobDetail.indexOf("任职要求"));
                jobRes = jobDetail.substring(0, jobDetail.indexOf("任职要求"));
            } else {
                jobInfo = jobDetail;
            }
        }
        //上班地址
        String jobAddress = page.getHtml().xpath("//*[@id=\"job_detail\"]/dd[3]/div[1]").toString();
        if (StringUtils.isNotBlank(jobAddress)) {
            jobAddress = Jsoup.parse(jobAddress).text();
        }

        //薪资带宽
        String salary = page.getHtml().xpath("/html/body/div[2]/div/div[1]/dd/p[1]/span[1]/text()").toString();
        Date date = new Date();
        //System.out.println();
        JobInfo job = new JobInfo();
        job.setCompanyName(companyName)
                .setCompanyIndustry(companyIndustry)
                .setCompanyPeople(companyPeople)
                .setCompanyFinancing(companyFinace)
                .setJobInfo(jobInfo)
                .setJobRes(jobRes)
                .setJobName(jobName)
                .setJobAddress(jobAddress)
                .setSalary(salary)
                .setSources("拉钩")
                .setCreateTime(date)
                .setUpdateTime(date);

        logger.info("保存信息....."+ JSON.toJSONString(job));
        page.putField("jobInfo",job);

    }

    //initialDelay 任务启动多久开始
    //fixedDelay 每隔多久,
   @Scheduled(initialDelay = 1000,fixedDelay = 24*60*1000)
    public void getLaGouJob(){

       List<IpPool> iPs = ipPoolService.getIPs();

       HttpClientDownloader httpClientDownloader = new HttpClientDownloader();
        httpClientDownloader.setProxyProvider(SimpleProxyProvider.from(new Proxy(iPs.get(0).getIp(),Integer.parseInt(iPs.get(0).getPort())),
                                                     new Proxy(iPs.get(1).getIp(),Integer.parseInt(iPs.get(1).getPort()))));

        Spider.create(new LagouProcessor())
                .addUrl(url).setDownloader(httpClientDownloader)
                .setScheduler(new QueueScheduler().setDuplicateRemover(new BloomFilterDuplicateRemover(100000)))
                .thread(15)
                .addPipeline(saveLaGouJobInfoPipeline)
                .run();
    }


  /*  @Scheduled(fixedDelay=2*60*1000)
    public void changeIp(){
        logger.info("获取代理ip开始...............................................");
        IpPool ipPool = ipPoolService.getIP();
        if (ipPool != null) {
            IP = ipPool.getIp();
            PORT = ipPool.getPort();
            ipPoolService.updateIPStatus(ipPool);//更改ip状态为1
            logger.info("获取新的ip....IP:"+IP+"   +PORT:"+PORT );
        } else {
            logger.error("获取代理ip失败...............................................");
        }
    }*/




}
