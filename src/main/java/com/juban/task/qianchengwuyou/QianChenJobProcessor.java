package com.juban.task.qianchengwuyou;


import com.juban.pojo.JobInfo;
import com.juban.service.IPPoolService;
import org.apache.commons.collections.CollectionUtils;
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
import us.codecraft.webmagic.selector.Selectable;

import java.util.Date;
import java.util.List;

//@Component
public class QianChenJobProcessor implements PageProcessor {
    private Logger logger = LoggerFactory.getLogger(QianChenJobProcessor.class);
    @Autowired
    private IPPoolService ipPoolService;
    @Autowired
    private SaveJonInfoPipeline saveJonInfoPipeline;


    // java private String URL = "https://search.51job.com/list/080200,000000,0000,00,9,99,java,2,1.html?lang=c&stype=1&postchannel=0000&workyear=99&cotype=99&degreefrom=99&jobterm=99&companysize=99&lonlat=0%2C0&radius=-1&ord_field=0&confirmdate=9&fromType=&dibiaoid=0&address=&line=&specialarea=00&from=&welfare=";

     // 测试 private String URL = "https://search.51job.com/list/080200,000000,0000,00,9,99,%25E8%25BD%25AF%25E4%25BB%25B6%25E6%25B5%258B%25E8%25AF%2595%25E5%25B7%25A5%25E7%25A8%258B%25E5%25B8%2588,2,1.html?lang=c&stype=&postchannel=0000&workyear=99&cotype=99&degreefrom=99&jobterm=99&companysize=99&providesalary=99&lonlat=0%2C0&radius=-1&ord_field=0&confirmdate=9&fromType=&dibiaoid=0&address=&line=&specialarea=00&from=&welfare=";
     // 前端 private String URL = "https://search.51job.com/list/080200,000000,0000,00,9,99,%25E5%2589%258D%25E7%25AB%25AF%25E5%25BC%2580%25E5%258F%2591%25E5%25B7%25A5%25E7%25A8%258B%25E5%25B8%2588,2,1.html?lang=c&stype=&postchannel=0000&workyear=99&cotype=99&degreefrom=99&jobterm=99&companysize=99&providesalary=99&lonlat=0%2C0&radius=-1&ord_field=0&confirmdate=9&fromType=&dibiaoid=0&address=&line=&specialarea=00&from=&welfare=";

    //产品经理 private String URL = "https://search.51job.com/list/080200,000000,0000,00,9,99,%25E4%25BA%25A7%25E5%2593%2581%25E7%25BB%258F%25E7%2590%2586,2,1.html?lang=c&stype=&postchannel=0000&workyear=99&cotype=99&degreefrom=99&jobterm=99&companysize=99&providesalary=99&lonlat=0%2C0&radius=-1&ord_field=0&confirmdate=9&fromType=&dibiaoid=0&address=&line=&specialarea=00&from=&welfare=";

    //品牌经理
    //private String URL = "https://search.51job.com/list/080200,000000,0000,01,9,99,%25E5%2593%2581%25E7%2589%258C%25E7%25BB%258F%25E7%2590%2586,2,1.html?lang=c&stype=&postchannel=0000&workyear=99&cotype=99&degreefrom=99&jobterm=99&companysize=99&providesalary=99&lonlat=0%2C0&radius=-1&ord_field=0&confirmdate=9&fromType=&dibiaoid=0&address=&line=&specialarea=00&from=&welfare=";


    // 云鸟 private String URL = "https://search.51job.com/list/000000,000000,0800,21,9,99,%25E5%258C%2597%25E4%25BA%25AC%25E4%25BA%2591%25E9%25B8%259F%25E7%25A7%2591%25E6%258A%2580%25E6%259C%2589%25E9%2599%2590%25E5%2585%25AC%25E5%258F%25B8,2,1.html?lang=c&stype=&postchannel=0000&workyear=99&cotype=99&degreefrom=99&jobterm=99&companysize=99&providesalary=99&lonlat=0%2C0&radius=-1&ord_field=0&confirmdate=9&fromType=&dibiaoid=0&address=&line=&specialarea=00&from=&welfare=";

    //德邦
    //private String URL = "https://search.51job.com/list/000000,000000,0800,21,9,99,%25E5%25BE%25B7%25E9%2582%25A6%25E5%25BF%25AB%25E9%2580%2592,2,1.html?lang=c&stype=&postchannel=0000&workyear=99&cotype=99&degreefrom=99&jobterm=99&companysize=99&providesalary=99&lonlat=0%2C0&radius=-1&ord_field=0&confirmdate=9&fromType=&dibiaoid=0&address=&line=&specialarea=00&from=&welfare=";
    //
    //private String jidong = "https://search.51job.com/list/000000,000000,0800,00,9,99,%25E4%25BA%25AC%25E4%25B8%259C,2,1.html?lang=c&stype=&postchannel=0000&workyear=99&cotype=99&degreefrom=99&jobterm=99&companysize=99&providesalary=99&lonlat=0%2C0&radius=-1&ord_field=0&confirmdate=9&fromType=&dibiaoid=0&address=&line=&specialarea=00&from=&welfare=";
    //private String yimidida = "https://search.51job.com/list/000000,000000,0800,00,9,99,%25E4%25B8%258A%25E6%25B5%25B7%25E5%25A3%25B9%25E7%25B1%25B3%25E6%25BB%25B4%25E7%25AD%2594%25E4%25BE%259B%25E5%25BA%2594%25E9%2593%25BE%25E7%25AE%25A1%25E7%2590%2586%25E6%259C%2589%25E9%2599%2590%25E5%2585%25AC%25E5%258F%25B8,2,1.html?lang=c&stype=&postchannel=0000&workyear=99&cotype=99&degreefrom=99&jobterm=99&companysize=99&providesalary=99&lonlat=0%2C0&radius=-1&ord_field=0&confirmdate=9&fromType=&dibiaoid=0&address=&line=&specialarea=00&from=&welfare=";
    private String URL = "https://search.51job.com/list/000000,000000,0000,21,9,99,%25E8%25B4%25A8%25E6%258E%25A7%25E4%25B8%2593%25E5%2591%2598,2,1.html?lang=c&stype=&postchannel=0000&workyear=99&cotype=99&degreefrom=99&jobterm=99&companysize=99&providesalary=99&lonlat=0%2C0&radius=-1&ord_field=0&confirmdate=9&fromType=&dibiaoid=0&address=&line=&specialarea=00&from=&welfare=";


    private Site site = Site.me()
            .setCharset("gbk") //编码
            .setTimeOut(3000) //超市时间
            .setRetrySleepTime(3000) //重试时间
            .setRetryTimes(3); //尝试次数


    @Override
    public void process(Page page) {

        List<Selectable> nodes = page.getHtml().css("div#resultList div.el").nodes();

        if (CollectionUtils.isEmpty(nodes)) {
            //如果为空,表示是招聘详情页
            saveJobInfo(page);

        } else {
            //不为空列表页 , 解析出详情页的url放入任务队列

            for (Selectable node : nodes) {
                //获取URL地址
                String jobInfoUrl = node.links().toString();
                //把获取到jobInfoUrl获取到队列
                page.addTargetRequest(jobInfoUrl);
            }
            //获取下一页的URL
            String nextPage = page.getHtml().css("div.p_in li.bk").nodes().get(1).links().toString();
            logger.info("前程无忧下一页的链接......"+nextPage);
            //把url放入任务队列中
            page.addTargetRequest(nextPage);
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
