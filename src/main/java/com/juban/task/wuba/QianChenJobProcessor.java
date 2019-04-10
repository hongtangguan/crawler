package com.juban.task.wuba;


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
import us.codecraft.webmagic.Site;
import us.codecraft.webmagic.Spider;
import us.codecraft.webmagic.processor.PageProcessor;
import us.codecraft.webmagic.scheduler.BloomFilterDuplicateRemover;
import us.codecraft.webmagic.scheduler.QueueScheduler;
import us.codecraft.webmagic.selector.Html;
import us.codecraft.webmagic.selector.Selectable;

import java.util.Date;
import java.util.List;

@Component
public class QianChenJobProcessor implements PageProcessor {
    private Logger logger = LoggerFactory.getLogger(QianChenJobProcessor.class);
    @Autowired
    private IPPoolService ipPoolService;
    @Autowired
    private SaveJonInfoPipeline saveJonInfoPipeline;

    private Integer num = 2;

    //private String URL = "https://hz.58.com/huoyun/?PGTID=0d0000d4-0000-01e1-42b8-3c6d031c31c2&ClickID=1";
    private String URL = "https://hz.58.com/huoyun/pn2/?PGTID=0d3000d4-0004-fc91-700a-0481dd7dca67&ClickID=1";


    private Site site = Site.me()
            .setCharset("utf8") //编码
            .setTimeOut(3000) //超市时间
            .setRetrySleepTime(3000) //重试时间
            .setRetryTimes(3); //尝试次数


    @Override
    public void process(Page page) {

        //List<Selectable> nodes = page.getHtml().css("table#jingzhun tr div.ac_linkurl").nodes();
        List<Selectable> nodes = page.getHtml().css("table#jingzhun tr div.tdiv").nodes();

        if (CollectionUtils.isEmpty(nodes)) {
            //解析页面
            System.out.println("完毕");
        }

        for (Selectable node : nodes) {
            if (StringUtils.isNotBlank(node.links().toString()) && node.links().toString().contains("https://")) {
                //
                System.out.println(node.links().toString());
            }
        }


        for (int i = 2 ; i<=10;i++) {
            //下一页链接
            logger.info("第{}页..........",i);
            page.addTargetRequest("https://hz.58.com/huoyun/pn"+i+"/");
        }






    }

    //解析页面
    private void saveJobInfo(Page page) {

        //当前地址
        String url = page.getUrl().toString();
        logger.info("前程无忧......job详情页链接"+url);

        JobInfo jobInfo = new JobInfo();
        //解析页面
        Html html = page.getHtml();

        String companyName = html.css("div.cn p.cname a","text").toString();
        String jobName = html.css("div.cn h1", "text").toString();

        //公司地址.
        String address = html.xpath("/html/body/div[3]/div[2]/div[3]/div[2]/div/p").toString();
        if (StringUtils.isNotBlank(address)) {
            address = Jsoup.parse(address).text().toString();
        }

        //公司信息
        //String companyInfo = Jsoup.parse(html.css("div.tmsg").toString()).text();

        //融资情况
        String companyFiance = html.xpath("/html/body/div[3]/div[2]/div[4]/div[1]/div[2]/p[1]/text()").toString() ;//上市情况
        //公司人数
        String companyPeople = html.xpath("/html/body/div[3]/div[2]/div[4]/div[1]/div[2]/p[2]").toString();
        if (StringUtils.isNotBlank(companyPeople)) {
            companyPeople = Jsoup.parse(companyPeople).text(); //公司人数
        }

        //公司行业
        String companyIndustry = html.xpath("/html/body/div[3]/div[2]/div[4]/div[1]/div[2]/p[3]").toString();
        if (StringUtils.isNotBlank(companyIndustry)) {
            companyIndustry = Jsoup.parse(companyIndustry).text();
        }
        ///html/body/div[3]/div[2]/div[4]/div[1]/div[2]/p[3]


        //职位描述
        String jobinfo = html.css("div.job_msg").toString();
        if (StringUtils.isNotBlank(jobinfo)) {
            jobinfo = Jsoup.parse(jobinfo).text();
        }

        //薪资
        String salary = html.css("div.cn strong").toString();
        if (StringUtils.isNotBlank(salary)) {
            salary = Jsoup.parse(salary).text();
        }

      /*  String pubTime = null;
        String replace = pubInfo.replace("|", ",");
        List<String> list = Arrays.asList(replace.split(","));
        if (CollectionUtils.isNotEmpty(list)) {
            if (list.size() == 5) {
                pubTime = list.get(4);
            }
            if (list.size() == 4) {
                pubTime = list.get(3);
            }
        }*/

        //获取数据
        jobInfo.setCompanyName(companyName);
        jobInfo.setCompanyFinancing(companyFiance);
        jobInfo.setCompanyIndustry(companyIndustry);
        jobInfo.setCompanyPeople(companyPeople);
        jobInfo.setJobName(jobName);
        jobInfo.setJobAddress(address);
        jobInfo.setJobInfo(jobinfo);
        jobInfo.setSalary(salary);
        jobInfo.setSources("前程无忧");
        Date date = new Date();
        jobInfo.setCreateTime(date);
        jobInfo.setUpdateTime(date);

        //jobInfoService.insertJob(jobInfo);
        page.putField("jobInfo",jobInfo);

    }

    @Override
    public Site getSite() {
        return site;
    }
    //initialDelay 任务启动多久开始
    //fixedDelay 每隔多久
   @Scheduled(initialDelay = 1000,fixedDelay = 100*1000)
    public void getQianChengJob(){

       //List<IpPool> iPs = ipPoolService.getIPs();
       //HttpClientDownloader httpClientDownloader = new HttpClientDownloader();
       //httpClientDownloader.setProxyProvider(SimpleProxyProvider.from(new Proxy(iPs.get(0).getIp(),Integer.parseInt(iPs.get(0).getPort())),new Proxy(iPs.get(1).getIp(),Integer.parseInt(iPs.get(1).getPort()))));


       Spider.create(new QianChenJobProcessor())
              .addUrl(URL)//.setDownloader(httpClientDownloader)
              .setScheduler(new QueueScheduler().setDuplicateRemover(new BloomFilterDuplicateRemover(100000)))
              .thread(15)
                .addPipeline(saveJonInfoPipeline)
              .run()
        ;
    }


}
