package com.juban.task.zhilian;

import com.juban.pojo.JobInfo;
import com.juban.service.IPPoolService;
import org.apache.commons.lang3.StringUtils;
import org.jsoup.Jsoup;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.scheduling.annotation.Scheduled;
import us.codecraft.webmagic.Page;
import us.codecraft.webmagic.Site;
import us.codecraft.webmagic.Spider;
import us.codecraft.webmagic.processor.PageProcessor;
import us.codecraft.webmagic.scheduler.BloomFilterDuplicateRemover;
import us.codecraft.webmagic.scheduler.QueueScheduler;
import us.codecraft.webmagic.selector.Html;
import us.codecraft.webmagic.selector.JsonPathSelector;

import java.util.Date;

/**
 * 类的说明
 *
 * @author zhenggaosheng
 * @version 1.0
 * @date 2018/12/3 13:59
 **/
//@Component
public class ZhiLian implements PageProcessor{

    @Autowired
    private IPPoolService ipPoolService;

    private Logger logger = LoggerFactory.getLogger(ZhiLian.class);
    @Autowired
    private SaveZhiLianJonInfoPipeline saveZhiLianJonInfoPipeline;

    private Site site = Site
            .me()
            .setRetryTimes(2)
            .setTimeOut(2000)
            .setSleepTime(5000);


    //java private String url = "https://sou.zhaopin.com/?jl=653&kw=java&kt=3";
    //前端 private String url = "https://sou.zhaopin.com/?jl=653&kw=web%E5%89%8D%E7%AB%AF&kt=3";
    // 测试 private String url = "https://sou.zhaopin.com/?jl=653&kw=%E6%B5%8B%E8%AF%95%E5%B7%A5%E7%A8%8B%E5%B8%88&kt=3";
    private String url = "https://sou.zhaopin.com/?jl=653&kw=%E5%AE%89%E8%83%BD&kt=3";


    @Scheduled(initialDelay = 1000,fixedDelay = 100*1000)
    public void crawlerTask() {

        //List<IpPool> iPs = ipPoolService.getIPs();
        //HttpClientDownloader httpClientDownloader = new HttpClientDownloader();
        //httpClientDownloader.setProxyProvider(SimpleProxyProvider.from(new Proxy(iPs.get(0).getIp(),Integer.parseInt(iPs.get(0).getPort())),new Proxy(iPs.get(1).getIp(),Integer.parseInt(iPs.get(1).getPort()))));


//                         //https://sou.zhaopin.com/?jl=653&kw=java&kt=3
        Spider.create(new ZhiLian())
                .addUrl(url)//.setDownloader(httpClientDownloader)
                .setScheduler(new QueueScheduler().setDuplicateRemover(new BloomFilterDuplicateRemover(100000)))
                .thread(15)
                .addPipeline(saveZhiLianJonInfoPipeline)
                .run();

    }


    @Override
    public void process(Page page) {




        if (page.getUrl().toString().contains("start")) {
            page.addTargetRequests(new JsonPathSelector("$.data.results[*].positionURL").selectList(page.getRawText()));
        }else if (page.getUrl().toString().contains("htm")) {
            //招聘详情页链接
            page.addTargetRequest(page.getUrl().toString());

        }else {
            int pageNum = 10;
            int startNum = 0;
            for (int i = 0; i < pageNum; i++) {
            //北京    //page.addTargetRequest("https://fe-api.zhaopin.com/c/i/sou?start=" + startNum + "&pageSize=60&cityId=530&workExperience=-1&education=-1&companyType=-1&employmentType=-1&jobWelfareTag=-1&kw=java&kt=3&_v=0.95822583&x-zp-page-request-id=476a554e1ae54a1e84599ba24152fb33-1543811878482-914300");

                // 杭州               //https://fe-api.zhaopin.com/c/i/sou?pageSize=60&cityId=653&workExperience=-1&education=-1&companyType=-1&employmentType=-1&jobWelfareTag=-1&kw=java&kt=3&at=6349341f87bd41cc93c0026b0845b11f&rt=4cba1f33680b4992bfa6f677dc1994de&_v=0.20912725&userCode=662587809&x-zp-page-request-id=fac09b2a30194bc88ebbb52e9e9184d9-1543820197297-125940
                //page.addTargetRequest("https://fe-api.zhaopin.com/c/i/sou?start="+startNum+"&pageSize=60&cityId=653&workExperience=-1&education=-1&companyType=-1&employmentType=-1&jobWelfareTag=-1&kw=java&kt=3&at=6349341f87bd41cc93c0026b0845b11f&rt=4cba1f33680b4992bfa6f677dc1994de&_v=0.20912725&userCode=662587809&x-zp-page-request-id=fac09b2a30194bc88ebbb52e9e9184d9-1543820197297-125940");
                // java page.addTargetRequest("https://fe-api.zhaopin.com/c/i/sou?start="+startNum+"&pageSize=60&cityId=653&workExperience=-1&education=-1&companyType=-1&employmentType=-1&jobWelfareTag=-1&kw=java&kt=3&at=66ee165b12b941a098e4eca99fe6ad5a&rt=f00a1a390b064084a582f7074e22fa94&_v=0.45161577&userCode=662587809&x-zp-page-request-id=c214de0757c247ea806cdd429350bc30-1543997924861-366188");
                // 前端 page.addTargetRequest("https://fe-api.zhaopin.com/c/i/sou?start="+startNum+"&pageSize=60&cityId=653&workExperience=-1&education=-1&companyType=-1&employmentType=-1&jobWelfareTag=-1&kw=web%E5%89%8D%E7%AB%AF&kt=3&at=66ee165b12b941a098e4eca99fe6ad5a&rt=f00a1a390b064084a582f7074e22fa94&_v=0.09572245&userCode=662587809&x-zp-page-request-id=d6b2823e958b4ab7a4bb5238926c9e87-1544001014394-278120");
                // 测试 page.addTargetRequest("https://fe-api.zhaopin.com/c/i/sou?start="+startNum+"&pageSize=60&cityId=653&workExperience=-1&education=-1&companyType=-1&employmentType=-1&jobWelfareTag=-1&kw=%E6%B5%8B%E8%AF%95%E5%B7%A5%E7%A8%8B%E5%B8%88&kt=3&at=66ee165b12b941a098e4eca99fe6ad5a&rt=f00a1a390b064084a582f7074e22fa94&_v=0.09572245&userCode=662587809&x-zp-page-request-id=d6b2823e958b4ab7a4bb5238926c9e87-1544001014394-278120");
                //产品 page.addTargetRequest("https://fe-api.zhaopin.com/c/i/sou?start="+startNum+"&pageSize=60&cityId=653&workExperience=-1&education=-1&companyType=-1&employmentType=-1&jobWelfareTag=-1&kw=%E4%BA%A7%E5%93%81%E7%BB%8F%E7%90%86&kt=3&at=66ee165b12b941a098e4eca99fe6ad5a&rt=f00a1a390b064084a582f7074e22fa94&_v=0.09572245&userCode=662587809&x-zp-page-request-id=d6b2823e958b4ab7a4bb5238926c9e87-1544001014394-278120");
                 page.addTargetRequest("https://fe-api.zhaopin.com/c/i/sou?start="+startNum+"&pageSize=60&cityId=489&industry=11500&workExperience=-1&education=-1&companyType=-1&employmentType=-1&jobWelfareTag=-1&kw=%E9%87%87%E8%B4%AD%E7%BB%8F%E7%90%86&kt=3&_v=0.30933585&x-zp-page-request-id=1e5b02523a714137955a0dec2def9d18-1544078768289-477594");

                startNum+=60;
            }
        }
        if (page.getUrl().toString().contains("htm")) {
            logger.info("招聘详情页链接..............."+page.getUrl().toString());
            this.saveJobInfo(page);
        }
    }

    private void saveJobInfo(Page page) {

        if (page != null) {
            String jobName = page.getHtml().css("body > div.wrap > div.main > div.main1.cl.main1-stat > div > ul > li:nth-child(1) > h1", "text").toString();

            String companyName = page.getHtml().css("body > div.wrap > div.main > div.main1.cl.main1-stat > div > ul > li.clearfix > div.company.l > a", "text").toString();

            String salary = page.getHtml().css("body > div.wrap > div.main > div.main1.cl.main1-stat > div > ul > li:nth-child(1) > div.l.info-money > strong", "text").toString();

            System.out.println();

            //公司行业
            String companyIndustry = page.getHtml().css("body > div.wrap > div.main > div.pos-info.cl > div.l.pos-info-right > div.promulgator-info.clearfix > ul > li:nth-child(1) > strong > a", "text").toString();

            //融资情况
            String companyFinancing = page.getHtml().css("body > div.wrap > div.main > div.pos-info.cl > div.l.pos-info-right > div.promulgator-info.clearfix > ul > li:nth-child(2) > strong", "text").toString();

            //公司人数
            String companyPeople = page.getHtml().css("body > div.wrap > div.main > div.pos-info.cl > div.l.pos-info-right > div.promulgator-info.clearfix > ul > li:nth-child(3) > strong", "text").toString();

            String jobAddress = page.getHtml().css("body > div.wrap > div.main > div.pos-info.cl > div.l.pos-info-right > div.promulgator-info.clearfix > ul > li:nth-child(5) > strong", "text").toString();
            ///html/body/div[1]/div[3]/div[5]/div[1]/div[3]
            String css = page.getHtml().css("div.pos-ul").toString();
            String jobInfo = "";
            if (StringUtils.isNotBlank(css)) {
                jobInfo = Jsoup.parse(new Html(css).toString()).text();
            }
            Date date = new Date();
            JobInfo info = new JobInfo()
                    .setCompanyName(companyName)
                    .setCompanyPeople(companyPeople)
                    .setCompanyIndustry(companyIndustry)
                    .setCompanyFinancing(companyFinancing)
                    .setJobInfo(jobInfo)
                    .setJobAddress(jobAddress)
                    .setJobName(jobName)
                    .setSalary(salary)
                    .setSources("智联招聘")
                    .setCreateTime(date)
                    .setUpdateTime(date);


            logger.info("入库......................"+info);
            page.putField("jobInfo",info);
        }
    }


    @Override
    public Site getSite() {
        return site;
    }
/*
    public static void main(String[] args) {
        Spider.create(new Test())
                .addUrl("https://sou.zhaopin.com/?p=303&jl=530&kw=java&kt=3")
                .thread(5)
                .run();
    }
*/

}
