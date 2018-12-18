package com.juban.task.boss;

import com.alibaba.fastjson.JSON;
import com.juban.pojo.IpPool;
import com.juban.pojo.JobInfo;
import com.juban.service.IPPoolService;
import org.apache.commons.lang3.StringUtils;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.util.CollectionUtils;
import us.codecraft.webmagic.Page;
import us.codecraft.webmagic.Site;
import us.codecraft.webmagic.Spider;
import us.codecraft.webmagic.downloader.HttpClientDownloader;
import us.codecraft.webmagic.processor.PageProcessor;
import us.codecraft.webmagic.proxy.Proxy;
import us.codecraft.webmagic.proxy.SimpleProxyProvider;
import us.codecraft.webmagic.scheduler.BloomFilterDuplicateRemover;
import us.codecraft.webmagic.scheduler.QueueScheduler;
import us.codecraft.webmagic.selector.Html;
import us.codecraft.webmagic.selector.Selectable;

import java.util.Date;
import java.util.List;


//@Component
public class BossJobProcessor implements PageProcessor {

    private Logger logger = LoggerFactory.getLogger(BossJobProcessor.class);

    @Autowired
    private SaveBossJonInfoPipeline saveBossJonInfoPipeline;

    @Autowired
    private IPPoolService ipPoolService;

    private static String IP = "";//IP
    private static String PORT = "";//端口

    private static Date ENDDATE = new Date();

                        //https://www.zhipin.com/c101210100-p100101/?ka=hot-position-1
    private String url = "https://www.zhipin.com/c101210100-p100103/?page=1&ka=page-1";

    private static Integer countPage = 10;//总页码

    private static Integer temp = 2;//页码临时记录

    private Site site = Site.me()
                            .setCharset("utf8")
                            .setTimeOut(10*1000) //超时时间
                            .setRetrySleepTime(3000) //重试时间
                            .setRetryTimes(3)//尝试次数
                .addHeader(":authority","www.zhipin.com")
                .addHeader(":method","GET")
                .addHeader(":path","/c101210100-p100101/")
                .addHeader(":scheme","https")
                .addHeader("accept","text/html,application/xhtml+xml,application/xml;q=0.9,image/webp,image/apng,*/*;q=0.8")
                .addHeader("accept-encoding","gzip, deflate, br")
                .addHeader("accept-language","zh-CN,zh;q=0.9")
                .addHeader("cache-control","max-age=0")
                .addHeader("cookie","_uab_collina=154295897596565831373587; lastCity=101210100; JSESSIONID=\"\"; __c=1543592686; __g=-; __l=l=%2Fwww.zhipin.com%2F&r=https%3A%2F%2Fwww.baidu.com%2Flink%3Furl%3DCA4BzAN30850dTsltgBofJL3gTzNpNYlWycst3fnUdb10oxt2lA92w6kNQvJGZhC%26wd%3D%26eqid%3Dfbe5c3a400022fd3000000045c015ae8; Hm_lvt_194df3105ad7148dcf2b98a91b5e727a=1543368723,1543459003,1543490653,1543592687; toUrl=https%3A%2F%2Fwww.zhipin.com%2Fc101210100-p100101%2F; __a=77075259.1542958975.1543490653.1543592686.201.14.2.136; Hm_lpvt_194df3105ad7148dcf2b98a91b5e727a=1543592690")
                .addHeader("referer","https://www.zhipin.com/")
                .addHeader("upgrade-insecure-requests","1")
                .addHeader("user-agent","Mozilla/5.0 (Windows NT 10.0; Win64; x64) AppleWebKit/537.36 (KHTML, like Gecko) Chrome/70.0.3538.110 Safari/537.36");





    @Override
    public Site getSite() {
        return site;
    }

    @Override
    public void process(Page page) {

        page.getHtml().css("div.job-list div.info-primary").nodes() ;
        List<Selectable> nodes = page.getHtml().css("div.job-list div.info-primary").nodes();

        if (CollectionUtils.isEmpty(nodes)) {
            saveJobInfo(page);
        } else {

            for (Selectable node : nodes) {
                String jobInfoUrl = node.links().toString();
                logger.info("爬取boss的链接...."+jobInfoUrl);
                page.addTargetRequest(jobInfoUrl);
            }

            //获取下一页链接
            String nextPage = page.getHtml().css("div.page a.next").links().toString();
            logger.info("下一页的链接......"+nextPage);
            if (StringUtils.isNotBlank(nextPage)) {
                page.addTargetRequest(nextPage);
            }
            //logger.info("======================");

  /*          if (temp <= countPage) {
                String nextPage ="https://www.zhipin.com/c101210100-p100103/?page="+temp+"&ka=page-"+temp;
                logger.info("下一页的链接★"+temp+"+......"+nextPage);
                page.addTargetRequest(nextPage);
                temp++;
            }*/

        }

    }

    private void saveJobInfo(Page page) {


        Selectable urlInfo = page.getUrl();
        logger.info("boss招聘详情页url★★★★★....."+ urlInfo);

        //工作名称
        String jobName = page.getHtml().css("#main > div.job-banner > div > div > div.info-primary > div.name > h1", "text").toString();
        logger.info("jobName..."+jobName);
        //薪资带宽
        String salary = page.getHtml().css("#main > div.job-banner > div > div > div.info-primary > div.name > span", "text").toString();
        logger.info("salary..."+salary);
        //公司名称                                            ////*[@id="main"]/div[1]/div/div/div[3]/h3/a
        String companyName = page.getHtml().xpath("//*[@id=\"main\"]/div[1]/div/div/div[3]/h3/a/text()").toString();
        logger.info("companyName..."+companyName);
        //公司行业
        String companyIndustry = page.getHtml().xpath("/html/body/div[1]/div[2]/div[1]/div/div/div[3]/p[1]/a/text()").toString();
        logger.info("companyIndustry..."+companyIndustry);
        String jobAddress = page.getHtml().xpath("//*[@id=\"main\"]/div[3]/div/div[2]/div[3]/div[6]/div/div[1]/text()").toString();
        logger.info("工作地址......."+jobAddress);

        String companyFiance = ""; //上市情况
        String companyPeople = ""; //公司人数
        String s = page.getHtml().xpath("//*[@id=\"main\"]/div[1]/div/div/div[3]/p").toString();
        if (s != null) {
            Html html = new Html(s);
            companyFiance = html.xpath("//p/html()").toString().replaceAll("<em.*","");

            companyPeople = html.xpath("//p/text()").toString().replaceAll(companyFiance,"");
            if (companyFiance.contains("人")) {
                companyFiance = "未融资";
                companyPeople = companyFiance;
            }
        }

        //工作描述
        String detail = page.getHtml().xpath("//*[@id=\"main\"]/div[3]/div/div[2]/div[3]/div[1]/div/text()").toString();
        Date date = new Date();
        JobInfo jobInfo = new JobInfo();
        jobInfo.setCompanyName(companyName)
                .setCompanyFinancing(companyFiance)
                .setCompanyIndustry(companyIndustry)
                .setCompanyPeople(companyPeople)
                .setJobName(jobName)
                .setJobInfo(detail)
                .setSalary(salary)
                .setJobAddress(jobAddress)
                .setSources("Boss直聘")
                .setCreateTime(date)
                .setUpdateTime(date);

        logger.info("boss保存的信息......"+JSON.toJSONString(jobInfo));
        page.putField("jobInfo",jobInfo);

    }

    //initialDelay 任务启动多久开始
    //fixedDelay 每隔多久
    //@Scheduled(initialDelay = 1000,fixedDelay = 100*1000)
    public void getBossJob(){


        if (StringUtils.isBlank(IP) || StringUtils.isBlank(PORT)) {
            IpPool ipPool = ipPoolService.getIP();
            if (ipPool != null) {
                IP = ipPool.getIp();
                PORT = ipPool.getPort();
                ipPoolService.updateIPStatus(ipPool);//更改ip状态为1
                logger.info("boss获取新的ip....IP:"+IP+"   +PORT:"+PORT );
            } else {
                logger.error("boss获取代理ip失败...............................................");
            }
        }

        HttpClientDownloader httpClientDownloader = new HttpClientDownloader();
        httpClientDownloader.setProxyProvider(SimpleProxyProvider.from(new Proxy(IP,Integer.parseInt(PORT))));

        Spider.create(new BossJobProcessor())
                .addUrl(url)//.setDownloader(new SeleniumDownloader("E:\\chromedriver.exe"))
                .setScheduler(new QueueScheduler().setDuplicateRemover(new BloomFilterDuplicateRemover(100000)))
                .thread(10)
                .addPipeline(saveBossJonInfoPipeline)
                .run();

    }

  // @Scheduled(fixedDelay=1*20*1000)
    public void changeIp(){
        IpPool ipPool = ipPoolService.getIP();
        if (ipPool != null) {
            IP = ipPool.getIp();
            PORT = ipPool.getPort();
            ipPoolService.updateIPStatus(ipPool);//更改ip状态为1
            logger.info("boss获取新的ip....IP:"+IP+"   +PORT:"+PORT );
        } else {
            logger.error("boss获取代理ip失败...............................................");
        }
    }




}
