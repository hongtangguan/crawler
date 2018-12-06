package com.juban.task.boss;

import com.alibaba.fastjson.JSON;
import com.juban.pojo.IpPool;
import com.juban.pojo.JobInfo;
import com.juban.service.IPPoolService;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Component;
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
@Component
public class BossTest implements PageProcessor {



    private Logger logger = LoggerFactory.getLogger(BossTest.class);

    @Autowired
    private SaveBossJonInfoPipeline saveBossJonInfoPipeline;
    @Autowired
    private IPPoolService ipPoolService;

    private static String IP = "";//IP
    private static String PORT = "";//端口

    private static Integer countPage = 10;//总页码

    private static Integer temp = 2;//页码临时记录

    public void boss() {

        List<IpPool> iPs = ipPoolService.getIPs();//获取ip

        HttpClientDownloader httpClientDownloader = new HttpClientDownloader();

        httpClientDownloader.setProxyProvider(SimpleProxyProvider.from(new Proxy(iPs.get(0).getIp(),Integer.parseInt(iPs.get(0).getPort())),
                                                                       new Proxy(iPs.get(1).getIp(),Integer.parseInt(iPs.get(1).getPort()))));
        //httpClientDownloader.setProxyProvider(new ProxyIPPool("http://api.xdaili.cn/xdaili-api//greatRecharge/getGreatIp?spiderId=eafbee03e0584391b7f4a6cb8cea286e&orderno=YZ201811289790YvQMoM&returnType=2&count=2"));
        //httpClientDownloader.setProxyProvider(ProxyIPPool.from("http://api.xdaili.cn/xdaili-api//greatRecharge/getGreatIp?spiderId=eafbee03e0584391b7f4a6cb8cea286e&orderno=YZ201811289790YvQMoM&returnType=2&count=2"));


        //logger.info("★★★★★"+IP+"  "+PORT);
        //System.setProperty("selenuim_config", "C:\\workproject\\crawler\\src\\main\\resources\\config.ini");

        Spider.create(new BossTest())
                .addUrl("https://www.zhipin.com/c101210100-p100103/?page=1&ka=page-1")
                .setDownloader(httpClientDownloader)
                .setScheduler(new QueueScheduler().setDuplicateRemover(new BloomFilterDuplicateRemover(100000)))
                .thread(10)
                .addPipeline(saveBossJonInfoPipeline)
                .run();
    }



    @Override
    public void process(Page page) {


        if (page.getUrl().toString().contains("page")){
            //Selectable xpath = page.getHtml().xpath("//*[@id=\"main\"]/div/div[3]/ul/li[1]/div/div[1]/h3/a/@href");

            //获取列表页所有链接
            List<Selectable> nodes = page.getHtml().css("div.job-list div.info-primary").nodes();
            for (Selectable node : nodes) {
                String jobInfoUrl = node.links().toString();
                logger.info("爬取boss的链接...."+jobInfoUrl);
                page.addTargetRequest(jobInfoUrl);
            }

            //获取下一页链接
/*            String nextPage = page.getHtml().css("div.page a.next").links().toString();
            logger.info("下一页的链接......"+nextPage);
            if (StringUtils.isNotBlank(nextPage)) {
                page.addTargetRequest(nextPage);
            }*/

            if (temp <= countPage) {
                String nextPage ="https://www.zhipin.com/c101210100-p100103/?page="+temp+"&ka=page-"+temp;
                logger.info("下一页的链接★"+temp+"+......"+nextPage);
                page.addTargetRequest(nextPage);
                temp++;
            }

        }else {
            String title = page.getHtml().xpath("//*[@id=\"main\"]/div[1]/div/div/div[2]/div[2]/h1/text()").get();
            String salary = page.getHtml().xpath("//*[@id=\"main\"]/div[1]/div/div/div[2]/div[2]/span/text()").get();
            String companyName = page.getHtml().xpath("//*[@id=\"main\"]/div[1]/div/div/div[3]/h3/a/text()").get();


            //公司行业
            String companyIndustry = page.getHtml().xpath("/html/body/div[1]/div[2]/div[1]/div/div/div[3]/p[1]/a/text()").toString();
            //工作名称
            String jobName = page.getHtml().css("#main > div.job-banner > div > div > div.info-primary > div.name > h1", "text").toString();
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

            logger.info("boss保存的信息......"+ JSON.toJSONString(jobInfo));
            page.putField("jobInfo",jobInfo);

        }
    }

    private Site site = Site
            .me()
            .setSleepTime(10000)//爬取的间隔时间.
            .setTimeOut(3*1000) //超时时间
            .setRetrySleepTime(3000) //重试时间
            .setRetryTimes(0);//尝试次数

    @Override
    public Site getSite() {
        return site;
    }

    //@Scheduled(fixedDelay=1*10*1000)
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
