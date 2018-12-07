//package com.juban.task.zhilian;
//
//import com.juban.pojo.IpPool;
//import com.juban.pojo.JobInfo;
//import com.juban.service.IPPoolService;
//import com.juban.service.JobInfoService;
//import org.apache.commons.collections4.CollectionUtils;
//import org.apache.commons.lang3.StringUtils;
//import org.jsoup.Jsoup;
//import org.openqa.selenium.By;
//import org.openqa.selenium.WebDriver;
//import org.openqa.selenium.WebElement;
//import org.openqa.selenium.chrome.ChromeDriver;
//import org.openqa.selenium.chrome.ChromeOptions;
//import org.slf4j.Logger;
//import org.slf4j.LoggerFactory;
//import org.springframework.beans.factory.annotation.Autowired;
//import org.springframework.scheduling.annotation.Scheduled;
//import us.codecraft.webmagic.Page;
//import us.codecraft.webmagic.Site;
//import us.codecraft.webmagic.Spider;
//import us.codecraft.webmagic.processor.PageProcessor;
//import us.codecraft.webmagic.scheduler.BloomFilterDuplicateRemover;
//import us.codecraft.webmagic.scheduler.QueueScheduler;
//import us.codecraft.webmagic.selector.Html;
//import us.codecraft.webmagic.selector.Selectable;
//
//import java.util.Date;
//import java.util.List;
//
//
////@Component
//public class ZhiLianJobProcessor2 implements PageProcessor {
//
//    private Logger logger = LoggerFactory.getLogger(ZhiLianJobProcessor2.class);
//
//    @Autowired
//    private SaveZhiLianJonInfoPipeline saveZhiLianJonInfoPipeline;
//
//    @Autowired
//    private IPPoolService ipPoolService;
//
//    @Autowired
//    private JobInfoService jobInfoService;
//
//    private static String IP = "";//IP
//    private static String PORT = "";//端口
//
//    //private String url = "https://www.liepin.com/zhaopin/?init=1&imscid=R000000058&d_sfrom=search_fp_bar&key=java";
//    //https://www.liepin.com/zhaopin/?isAnalysis=&dqs=&pubTime=&jobTitles=410022&salary=&industryType=&compscale=&key=&init=-1&searchType=1&headckid=caa8ebff308ddde7&compkind=&fromSearchBtn=2&sortFlag=15&ckid=caa8ebff308ddde7&degradeFlag=0&jobKind=&industries=&clean_condition=&siTag=1B2M2Y8AsgTpgAmY7PhCfg~fA9rXquZc5IkJpXC-Ycixw&d_sfrom=search_prime&d_ckId=83ae7ef43461f651059fc62900160d72&d_curPage=10&d_pageSize=40&d_headId=83ae7ef43461f651059fc62900160d72&curPage=0
//    private String url = "https://sou.zhaopin.com/?jl=530&kw=java&kt=3";
//
//    private static Integer countPage = 30;//总页码
//
//    private static Integer temp = 2;//页码临时记录
//
//
//    //private static String string = HttpClientUtils.doGet("https://www.liepin.com/zhaopin/?init=1&imscid=R000000058&d_sfrom=search_fp_bar&key=java");
//    //private static Html html = new Html(string);
//    private Site site = Site.me()
//                            .setCharset("utf8")
//                            .setTimeOut(10*1000) //超时时间
//                            .setRetrySleepTime(3000) //重试时间
//                            .setRetryTimes(3)//尝试次数
//                            .addHeader("Accept","text/html,application/xhtml+xml,application/xml;q=0.9,image/webp,image/apng,*/*;q=0.8")
//                            //.addHeader("Accept-Encoding","gzip, deflate, br")
//                            .addHeader("Accept-Language","zh-CN,zh;q=0.9")
//                            .addHeader("Cache-Control","max-age=0")
//                            .addHeader("Connection","keep-alive")
//                            //.addHeader("Content-Length","2690")
//                            .addHeader("Cookie","urlfrom2=121113803; adfbid2=0; sts_deviceid=1675441381b2a7-09ee3ad9062698-6313363-1327104-1675441381c45c;" +
//                                    " ZP_OLD_FLAG=false; acw_tc=3ccdc16815434979889892378e5cabf7395220ded41c11bb39835653629224; LastCity=%E6%9D%AD%E5%B7%9E; LastCity%5Fid=653; urlfrom=121113803; adfbid=0; dywea=95841923.1682221999093389300.1543306819.1543497863.1543593571.3; dywec=95841923; sts_sg=1; sts_sid=1676558b47e388-0fffd586e80d45-6313363-1327104-1676558b4802c; sts_chnlsid=121113803; zp_src_url=https%3A%2F%2Fsp0.baidu.com%2F9q9JcDHa2gU2pMbgoY3K%2Fadrc.php%3Ft%3D06KL00c00fZ" +
//                                    "mx9C0v4w60KqiAsKug67T00000cQiq7b00000TtA-YW.THLyktAJdIjA80K85yF9pywd0ZnquADLnvc4nvnsnj0znhcYmfKd5RuDwHuaPb7KwbnLf16kwHm1nWf4PRfLwRnzwDfYrRDd0ADqI1YhUyPGujY1nHRYrj0YnWnYFMKzUvwGujYkP6K-5y9YIZK1rBtEmv4YQMGCmyqspy38mvqVQYd9ThV-IaqLpAq_uNqWULN8IANzQhG1Tjq1pyfqnHcknHD1rj01FMNzUjdCIZwsT1CEQLw1QMGCmyqspy38mvqVQvGdUg0Epy4bug9xUhNLQh9YUysOIgwVgLPEIgFWuHdBmy-bIgKWTZChIgwVgvd-uA-dUHdWTZf0mLFW5Hn4njbs%26tpl%3Dtpl_11535_18459_14447%26l%3D1509216898%26attach%3Dlocation%253D%2526linkName%253D%2525E6%2525A0%252587%2525E5%252587%252586%2525E5%2525A4%2525B4%2525E9%252583%2525A8-%2525E6%2525A0%252587%2525E9%2525A2%252598-%2525E4%2525B8%2525BB%2525E6%2525A0%252587%2525E9%2525A2%252598%2526linkText%253D%2525E3%252580%252590%2525E6%252599%2525BA%2525E8%252581%252594%2525E6%25258B%25259B%2525E8%252581%252598%2525E3%252580%252591%2525E5%2525AE%252598%2525E6%2525" +
//                                    "96%2525B9%2525E7%2525BD%252591%2525E7%2525AB%252599%252520%2525E2%252580%252593%252520%2525E5%2525A5%2525BD%2525E5%2525B7%2525A5%2525E4%2525BD%25259C%2525EF%2525BC%25258C%2525E4%2525B8%25258A%2525E6%252599%2525BA%2525E8%252581%252594%2525E6%25258B%25259B%2525E8%252581%252598%2525EF%2525BC%252581%2526xp%253Did(%252522m3154804234_canvas%252522)%25252FDIV%25255B1%25255D%25252FDIV%25255B1%25255D%25252FDIV%25255B1%25255D%25252FDIV%25255B1%25255D%25252FDIV%25255B1%25255D%25252FH2%25255B1%25255D%25252FA%25255B1%25255D%2526linkType%253D%2526checksum%253D250%26ie%3Dutf-8%26f%3D8%26tn%3Dbaidu%26wd%3D%25E6%2599%25BA%25E8%2581%2594%25E6%258B%259B%25E8%2581%2598%26rqlang%3Dcn%26" +
//                                    "inputT%3D2902; __utmc=269921210; _jzqa=1.3710603168636376000.1543306820.1543497863.1543593572.3; _jzqc=1; _jzqy=1.1543306820.1543593572.3.jzqsr=baidu|jzqct=zhilian.jzqsr=baidu|jzqct=%E6%99%BA%E8%81%94%E6%8B%9B%E8%81%98; _jzqckmp=1; firstchannelurl=https%3A//passport.zhaopin.com/login; lastchannelurl=https%3A//ts.zhaopin.com/jump/index_new.html%3Futm_source%3Dbaidupcpz%26utm_medium%3Dcpt%26utm_provider%3Dpartner%26sid%3D121113803%26site%3Dnull; dywez=95841923.1543593616.3.5.dywecsr=baidupcpz|dyweccn=(not%20set)|dywecmd=cpt|dywectr=%E6%99%BA%E8%81%94%E6%8B%9B%E8%81%98; __utma=269921210.66169618.1543306820.1543593603.1543593616.5; __utmz=269921210.1543593616.5.5.utmcsr=baidupcpz|utmccn=(not%20set)|utmcmd=cpt|utmctr=%E6%99%BA%E8%81%94%E6%8B%9B%E8%81%98; __xsptplus30=30.5.1543593616.1543593616.1%232%7Csp0.baidu.com%7C%7C%7C%2" +
//                                    "5E6%2599%25BA%25E8%2581%2594%25E6%258B%259B%25E8%2581%2598%7C%23%23YGPmoWssImIRrIxP3GH0HY0ls" +
//                                    "q94VGDK%23; Hm_lvt_38ba284938d5eddca645bb5e02a02006=1543306820,1543497863,1543593571,1543593616; sensorsdata2015jssdkcross=%7B%22distinct_id%22%3A%22662587809%22%2C%22%24device_id%22%3A%2216754418aa76eb-043cec0184035e-6313363-1327104-16754418aa8116%22%2C%22props%22%3A%7B%22%24latest_traffic_source_type%22%3A%22%E4%BB%98%E8%B4%B9%E5%B9%BF%E5%91%8A%E6%B5%81%E9%87%8F%22%2C%22%24latest_referrer%22%3A%22https%3A%2F%2Fsp0.baidu.com%2F9q9JcDHa2gU2pMbgoY3K%2Fadrc.php%3Ft%3D06KL00c00fZmx9C0v4w60KqiAsKug67T00000cQiq7b00000TtA-YW.THLyktAJdIjA80K85yF9pywd0ZnquADLnvc4nvnsnj0znhcYmfKd5RuDwHuaPb7KwbnLf16kwHm1nWf4PRfLwRnzwDf%22%2C%22%24latest_referrer_host%22%3A%22sp0.baidu.com%22%2C%22%24latest_search_keyword%22%3A%22%E6%99%BA%E8%81%94%E6%8B%9B%E8%81%98%22%2C%22" +
//                                    "%24latest_utm_source%22%3A%22baidupcpz%22%2C%22%24latest_utm_medium%22%3A%22cpt%22%7D%2C%22first_id%22%3A%2216754418aa76eb-043cec0184035e-6313363-1327104-16754418aa8116%22%7D; _jzqb=1.5.10.1543593572.1; index-c=1; jobRiskWarning=true; GUID=c38bec4f917147369384b0ae1ac05f39; Hm_lpvt_38ba284938d5eddca645bb5e02a02006=1543593674; ZL_REPORT_GLOBAL={%22sou%22:{%22actionid%22:%224b397d89-c05f-4d2d-8191-76f618e8ccc6-sou%22%2C%22funczone%22:%22smart_matching%22}%2C%22//jobs%22:{%22actionid%22:%22846db776-9638-4c4b-87f2-359cc690530e-jobs%22%2C%22funczone%" +
//                                    "22:%22dtl_best_for_you%22}}; dyweb=95841923.9.10.1543593571; __utmb=269921210.6.10.1543593616; sts_evtseq=17")
//                            //.addHeader("Host","sou.zhaopin.com")
//                           // .addHeader("Referer","https://www.zhaopin.com/")
//                            //.addHeader("Upgrade-Insecure-Requests","1")
//                            .addHeader("User-Agent","Mozilla/5.0 (Windows NT 10.0; Win64; x64) AppleWebKit/537.36 (KHTML, like Gecko) Chrome/70.0.3538.110 Safari/537.36");
//
//
//
//
//    @Override
//    public Site getSite() {
//        return site;
//    }
//
//
//    private static   Html HTML;
//
//    private static List<Selectable> NODES = null;
//
//
//
//    @Override
//    public void process(Page page) {
//        HTML = null;
//        NODES = null;
//        String url = page.getUrl().toString();
//        logger.info("当前页链接....."+url);
//
//        //包含说明是列表页
//        if (url.contains("?jl=530&")) {
//            System.getProperties().setProperty("webdriver.chrome.driver", "E:\\chromedriver.exe");
//            //取消 chrome正受到自动测试软件的控制的信息栏
//            ChromeOptions options = new ChromeOptions();
//            options.addArguments("disable-infobars");
//            options.addArguments("--headless");
//            WebDriver webDriver = new ChromeDriver(options);
//
//            webDriver.get(url);
//            try {
//                Thread.sleep(2000);
//            } catch (InterruptedException e) {
//                e.printStackTrace();
//            }
//            WebElement webElement = webDriver.findElement(By.xpath("/html"));
//            String htmlStr = webElement.getAttribute("outerHTML");
//            webDriver.close();
//            HTML = new Html(htmlStr);
//
//        }
//
//        if (HTML != null) {
//            NODES = HTML.css("div.contentpile__content__wrapper").nodes();
//            logger.info("nodes..." + NODES.size());
//        }
//
//        if (CollectionUtils.isEmpty(NODES)) {
//            this.saveJobInfo(page);
//        } else {
//            for (Selectable node : NODES) {
//                String links = new Html(node.toString()).css("a.contentpile__content__wrapper__item__info").links().toString();
//                logger.info("智联工作详情页....."+links);
//                page.addTargetRequest(links);
//            }
//        }
//
//        //https://sou.zhaopin.com/?p=4&jl=530&kw=java&kt=3
//
//        //获取下一页的链接
//        if (temp <= countPage) {
//            String nextPage = "https://sou.zhaopin.com/?p=" + temp + "&jl=530&kw=java&kt=3";
//            logger.info("当前页..." + temp);
//            logger.info("智联下一页链接......." + nextPage);
//            page.addTargetRequest(nextPage);
//            temp++;
//        }
//
//    }
//
//    private void saveJobInfo(Page page) {
//
//
//        String jobName = page.getHtml().css("body > div.wrap > div.main > div.main1.cl.main1-stat > div > ul > li:nth-child(1) > h1", "text").toString();
//
//        String companyName = page.getHtml().css("body > div.wrap > div.main > div.main1.cl.main1-stat > div > ul > li.clearfix > div.company.l > a", "text").toString();
//
//        String salary = page.getHtml().css("body > div.wrap > div.main > div.main1.cl.main1-stat > div > ul > li:nth-child(1) > div.l.info-money > strong", "text").toString();
//
//        System.out.println();
//
//        //公司行业
//        String companyIndustry = page.getHtml().css("body > div.wrap > div.main > div.pos-info.cl > div.l.pos-info-right > div.promulgator-info.clearfix > ul > li:nth-child(1) > strong > a", "text").toString();
//
//        //融资情况
//        String companyFinancing = page.getHtml().css("body > div.wrap > div.main > div.pos-info.cl > div.l.pos-info-right > div.promulgator-info.clearfix > ul > li:nth-child(2) > strong", "text").toString();
//
//        //公司人数
//        String companyPeople = page.getHtml().css("body > div.wrap > div.main > div.pos-info.cl > div.l.pos-info-right > div.promulgator-info.clearfix > ul > li:nth-child(3) > strong", "text").toString();
//
//        String jobAddress = page.getHtml().css("body > div.wrap > div.main > div.pos-info.cl > div.l.pos-info-right > div.promulgator-info.clearfix > ul > li:nth-child(5) > strong", "text").toString();
//
//        String css = page.getHtml().css("body > div.wrap > div.main > div.pos-info.cl > div.l.pos-info-in > div.responsibility.pos-common > div.pos-ul > div:nth-child(1)").toString();
//        String jobInfo = "";
//        if (StringUtils.isNotBlank(css)) {
//            jobInfo = Jsoup.parse(new Html(css).toString()).text();
//        }
//        Date date = new Date();
//        JobInfo info = new JobInfo()
//                .setCompanyName(companyName)
//                .setCompanyPeople(companyPeople)
//                .setCompanyIndustry(companyIndustry)
//                .setCompanyFinancing(companyFinancing)
//                .setJobInfo(jobInfo)
//                .setJobAddress(jobAddress)
//                .setJobName(jobName)
//                .setSalary(salary)
//                .setSources("智联招聘")
//                .setCreateTime(date)
//                .setUpdateTime(date);
//
//        page.putField("jobInfo",info);
//    }
//
//
//
//    //initialDelay 任务启动多久开始
//    //fixedDelay 每隔多久
//    @Scheduled(initialDelay = 1000,fixedDelay = 100*1000)
//    public void crawlerTask() throws InterruptedException {
//
//        //System.setProperty("selenuim_config", "C:\\workproject\\crawler\\src\\main\\resources\\config.ini");
//        //HttpClientDownloader httpClientDownloader = new HttpClientDownloader();
//        //httpClientDownloader.setProxyProvider(SimpleProxyProvider.from(new Proxy(IP,Integer.parseInt(PORT))));
//        //"C:\\Program Files (x86)\\Google\\Chrome\\Application\\"
//        Spider.create(new ZhiLianJobProcessor2())
//                .addUrl(url)//.setDownloader(new SeleniumDownloader("E:\\chromedriver.exe"))
//                .setScheduler(new QueueScheduler().setDuplicateRemover(new BloomFilterDuplicateRemover(100000)))
//                .thread(10)
//                .addPipeline(saveZhiLianJonInfoPipeline)
//                .run();
//
//        //Thread.sleep(100);
//
//    }
//
//
//    //@Scheduled(fixedDelay=2*60*1000)
//    //@Scheduled(fixedDelay=1*60*1000)
//    public void changeIp(){
//        IpPool ipPool = ipPoolService.getIP();
//        if (ipPool != null) {
//            IP = ipPool.getIp();
//            PORT = ipPool.getPort();
//            ipPoolService.updateIPStatus(ipPool);//更改ip状态为1
//            logger.info("boss获取新的ip....IP:"+IP+"   +PORT:"+PORT );
//        } else {
//            logger.error("boss获取代理ip失败...............................................");
//        }
//    }
//
//    public static void main(String[] args) {
//
//        /*
//
//        // 第一步： 设置chromedriver地址。一定要指定驱动的位置。
//        System.setProperty("webdriver.chrome.driver",
//                "E:\\chromedriver.exe");
//        // 第二步：初始化驱动
//        WebDriver driver = new ChromeDriver();
//        // 第三步：获取目标网页
//        driver.get("https://sou.zhaopin.com/?jl=653&kw=java&kt=3");
//        // 第四步：解析。以下就可以进行解了。使用webMagic、jsoup等进行必要的解析。
//        System.out.println("Page title is: " + driver.getTitle());
//        System.out.println("Page title is: " + driver.getPageSource());
//        */
//
//
//
//
//
///*
//
//
//        System.getProperties().setProperty("webdriver.chrome.driver", "E:\\chromedriver.exe");
//        //取消 chrome正受到自动测试软件的控制的信息栏
//        ChromeOptions options = new ChromeOptions();
//        options.addArguments("disable-infobars");
//        options.addArguments("--headless");
//        WebDriver webDriver = new ChromeDriver(options);
//
//        webDriver.get("https://sou.zhaopin.com/?jl=653&kw=java&kt=3");
//        try {
//            Thread.sleep(2000);
//        } catch (InterruptedException e) {
//            e.printStackTrace();
//        }
//        WebElement webElement = webDriver.findElement(By.xpath("/html"));
//
//        System.out.println(webElement.getAttribute("outerHTML"));
//        webDriver.close();
//
//*/
//
//
//
//    }
//
//}
