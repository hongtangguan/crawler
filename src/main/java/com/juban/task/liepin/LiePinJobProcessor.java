package com.juban.task.liepin;

import com.juban.pojo.IpPool;
import com.juban.pojo.JobInfo;
import com.juban.service.IPPoolService;
import com.juban.service.JobInfoService;
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
public class LiePinJobProcessor implements PageProcessor {

    private Logger logger = LoggerFactory.getLogger(LiePinJobProcessor.class);

    @Autowired
    private SaveLiePinJonInfoPipeline saveLiePinJonInfoPipeline;

    @Autowired
    private IPPoolService ipPoolService;

    @Autowired
    private JobInfoService jobInfoService;

    private static String IP = "";//IP
    private static String PORT = "";//端口

    //private String url = "https://www.liepin.com/zhaopin/?init=1&imscid=R000000058&d_sfrom=search_fp_bar&key=java";
    //https://www.liepin.com/zhaopin/?isAnalysis=&dqs=&pubTime=&jobTitles=410022&salary=&industryType=&compscale=&key=&init=-1&searchType=1&headckid=caa8ebff308ddde7&compkind=&fromSearchBtn=2&sortFlag=15&ckid=caa8ebff308ddde7&degradeFlag=0&jobKind=&industries=&clean_condition=&siTag=1B2M2Y8AsgTpgAmY7PhCfg~fA9rXquZc5IkJpXC-Ycixw&d_sfrom=search_prime&d_ckId=83ae7ef43461f651059fc62900160d72&d_curPage=10&d_pageSize=40&d_headId=83ae7ef43461f651059fc62900160d72&curPage=0
    //private String url = "https://www.liepin.com/zhaopin/?init=-1&headckid=fc904682675eef4d&fromSearchBtn=2&imscid=R000000058&ckid=fc904682675eef4d&degradeFlag=0&key=java&siTag=k_cloHQj_hyIn0SLM9IfRg~fA9rXquZc5IkJpXC-Ycixw&d_sfrom=search_fp_bar&d_ckId=63ed22dfdda2d25375e7009a00704db4&d_curPage=0&d_pageSize=40&d_headId=63ed22dfdda2d25375e7009a00704db4&curPage=0";

    //B2B资源管理
    private String url ="https://www.liepin.com/zhaopin/?industries=250&dqs=&salary=&jobKind=2&pubTime=&compkind=&compscale=&industryType=industry_09&searchType=1&clean_condition=&isAnalysis=&init=1&sortFlag=15&flushckid=0&fromSearchBtn=1&headckid=a28e5eb41d3ef97a&d_headId=b1dad03b1aa6838026b755e21cfe66a0&d_ckId=2816ba952df9ee0112ef56cc06fa7744&d_sfrom=search_prime&d_curPage=1&d_pageSize=40&siTag=i9Jq-FcUGTpC9QESjC5G3Q~ZJD6Eu9a0hYZwbIB9yR3VQ&key=%E9%A1%B9%E7%9B%AE%E5%AE%9E%E6%96%BD";

    private static Integer countPage = 10;//总页码

    private static Integer temp = 2;//页码临时记录


    //private static String string = HttpClientUtils.doGet("https://www.liepin.com/zhaopin/?init=1&imscid=R000000058&d_sfrom=search_fp_bar&key=java");
    //private static Html html = new Html(string);
    private Site site = Site.me()
                            .setCharset("utf8")
                            .setTimeOut(1000) //超时时间
                            .setRetrySleepTime(1000) //重试时间
                            .setRetryTimes(1)//尝试次数
                            .setSleepTime(5000)
                            .addHeader("Accept","text/html,application/xhtml+xml,application/xml;q=0.9,image/webp,image/apng,*/*;q=0.8")
                            .addHeader("Accept-Encoding","gzip, deflate, br")
                            .addHeader("Accept-Language","zh-CN,zh;q=0.9")
                            .addHeader("Cache-Control","max-age=0")
                            .addHeader("Connection","keep")
                            .addHeader("Connection","keep")
                            //.addHeader("Content-Length","2690")
                            .addHeader("Content-Type","application/x-www-form-urlencoded")
                            .addHeader("Cookie","subscribe_guide=1; __uuid=1543315772565.76; _uuid=54C9D58FFFA8421946866232D5899D57; user_kind=0; is_lp_user=true; c_flag=29d236c72b0d4a9be39a3a01ee11e7eb; need_bind_tel=false; gr_user_id=bba82b5c-cb80-40dc-8bfe-6536a7a39eb5; bad1b2d9162fab1f80dde1897f7a2972_gr_last_sent_cs1=24357754859cb4651a667e00ad0d9493; imClientId=f8e632f8a1cbae45c0e6e15d7536aa68; imId=f8e632f8a1cbae45121ef0c29786a69c; grwng_uid=83cc2dbf-8e45-4ff5-b7e4-7c105ebf1b16; ADHOC_MEMBERSHIP_CLIENT_ID1.0=de87a5a5-13c3-24cc-4722-4a2bfd63ac53; abtest=0; _fecdn_=1; firsIn=1; verifycode=e4a4b932a6214f8cbc1d023f4c156331; user_name=%E9%83%91%E9%AB%98%E5%8D%87; lt_auth=7%2B8MPCZRnluq5HeK2DYL4v4Yi4ioBWjI9i4M0BsJ0Na0Cffh4PviQAOEp7UAxBMhwUkgIsULNLT9%0D%0AMOz9wXRD60sTwGqnn4C3uOW52WECduM2Iv%2Bg0PT8k83SEckix30BynBjoy4exUv2thAiM4a%2F8ErI%0D%0Ap6HV17Ss%2Bw%3D%3D%0D%0A; UniqueKey=24357754859cb4651a667e00ad0d9493; user_vip=0; user_photo=55557f3b28ee44a8919620ce01a.gif; new_user=false; bad1b2d9162fab1f80dde1897f7a2972_gr_session_id=19fca416-9fe7-49dc-928a-08644cd1f9a3; Hm_lvt_a2647413544f5a04f00da7eee0d5e200=1543315926,1543541105,1543541116,1543543881; openChatWin=; _mscid=s_00_pz0; curConnectPageId=webim_pageid_247928102081.0014; bad1b2d9162fab1f80dde1897f7a2972_gr_last_sent_sid_with_cs1=19fca416-9fe7-49dc-928a-08644cd1f9a3; slide_goldcard_times20181130=2; bad1b2d9162fab1f80dde1897f7a2972_gr_session_id_19fca416-9fe7-49dc-928a-08644cd1f9a3=true; __tlog=1543541105013.19%7C00000000%7CR000000058%7Cs_00_pz0%7Cs_00_f00; JSESSIONID=3278084882677681C2E0DE456F039C40; __session_seq=29; __uv_seq=29; Hm_lpvt_a2647413544f5a04f00da7eee0d5e200=1543547651; bad1b2d9162fab1f80dde1897f7a2972_gr_cs1=24357754859cb4651a667e00ad0d9493")
                            .addHeader("Host","www.liepin.com")
                            .addHeader("Upgrade-Insecure-Requests","1")
                            .addHeader("User-Agent","Mozilla/5.0 (Windows NT 10.0; WOW64) AppleWebKit/537.36 (KHTML, like Gecko) Chrome/66.0.3359.139 Safari/537.36");




    @Override
    public Site getSite() {
        return site;
    }

    @Override
    public void process(Page page) {

        //获取页面所有连接
        //List<Selectable> nodes = page.getHtml().css("div.sojob-item-main div.job-info h3 a").nodes();
        List<Selectable> nodes = page.getHtml().css("ul.sojob-list li div.job-info").nodes();

        if (CollectionUtils.isEmpty(nodes)) {
            //保存数据
            this.saveJobInfo(page);
            logger.info("保存数据...........................保存数据");
        } else {
            for (Selectable node : nodes) {
                Html html = new Html(node.toString());
                //String s = Jsoup.parse(html.css("i.icon").toString()).text();

                String s = html.css("i.icon").toString();
                if (StringUtils.isNotBlank(s)) {
                    s = Jsoup.parse(s).text();
                }
                if (("猎").equals(s)) {
                    continue;
                }

                String jobInfoUrl = html.css("div.job-info").links().nodes().get(0).toString();
                logger.info("猎聘的链接...."+jobInfoUrl);

                if (jobInfoUrl.contains("https://")) {
                    page.addTargetRequest(jobInfoUrl);
                }

            }

            //获取下一页的链接
            if (temp <= countPage) {
                String nextPage = "https://www.liepin.com/zhaopin/?isAnalysis=&init=-1&searchType=1&headckid=e503dcb7ab9da65c&dqs=&pubTime=&compkind=&fromSearchBtn=2&salary=&sortFlag=15&ckid=e503dcb7ab9da65c&degradeFlag=0&industryType=industry_09&jobKind=2&industries=250&compscale=&clean_condition=&key=%E9%A1%B9%E7%9B%AE%E5%AE%9E%E6%96%BD&siTag=63_FpJMXC-GhCqybeWKICA~ZJD6Eu9a0hYZwbIB9yR3VQ&d_sfrom=search_prime&d_ckId=14f9d589c7a56753b145c785bd9fc60c&d_curPage=0&d_pageSize=40&d_headId=14f9d589c7a56753b145c785bd9fc60c&curPage="+temp;
                logger.info("当前页..."+temp);
                logger.info("猎聘下一页链接......."+nextPage);
                page.addTargetRequest(nextPage);
                temp++;
            }


        }

    }

    private void saveJobInfo(Page page) {

        String url = page.getUrl().toString();
        logger.info("猎聘招聘详情页......."+url);

        //招聘职位
        String jobName = page.getHtml().css("#job-view-enterprise > div.wrap.clearfix > div.clearfix > div.main > div.about-position > div.title-info > h1","text").toString();
        //薪资带宽
        //String salary = page.getHtml().css("#job-view-enterprise > div.wrap.clearfix > div.clearfix > div.main > div.about-position > div:nth-child(2) > div.clearfix > div.job-title-left > p.job-item-title").toString().replaceAll("<em.*", "").replaceAll("<span.*", "").replaceAll("<p.*","");
        //String salary = page.getHtml().xpath("//*[@id=\"job-view-enterprise\"]/div[1]/div[1]/div[1]/div[1]/div[2]/div[1]/div[1]/p[1]").toString();
        String salary = page.getHtml().css("#job-view-enterprise > div.wrap.clearfix " +
                "> div.clearfix > div.main > div.about-position > div:nth-child(2) > div.clearfix " +
                "> div.job-title-left > p.job-item-title").xpath("//p/text()").toString();

        logger.info("薪资带宽..."+salary);

        //公司名字
        String companyName = page.getHtml().css("#job-view-enterprise > div.wrap.clearfix > div.clearfix > div.main > div.about-position > div.title-info > h3 > a","text").toString();


        List<Selectable> nodes = page.getHtml().css("div.new-compwrap ul.new-compintro li").nodes();
        String companyIndustry = ""; //行业.
        String companyFinancing = "";//融资情况
        String companyPeople = ""; //公司人数
        String jobAddress = "";//地址
        if (CollectionUtils.isNotEmpty(nodes)) {

            if (nodes.size() == 3) {
                String text = Jsoup.parse(nodes.get(0).toString()).text();
                if (text.contains("领域/融资：")) {
                    String[] split = text.replaceAll("领域/融资：", "").split("，");
                    if (split.length == 2) {
                        companyIndustry = split[0];
                        companyFinancing = split[1];
                    }
                } else {
                    companyIndustry = text.replace("行业：", "");
                }
                companyPeople = Jsoup.parse(nodes.get(1).toString()).text().replace("公司规模：", "");
                jobAddress = Jsoup.parse(nodes.get(2).toString()).text().replace("公司地址：", "");
            } else {
                //只有行业, 公司地址
                companyIndustry = Jsoup.parse(nodes.get(0).toString()).text();
                jobAddress = Jsoup.parse(nodes.get(1).toString()).text();
            }

        }
        logger.info("行业..."+companyIndustry);
        logger.info("人数..."+companyPeople);
        logger.info("融资..."+companyFinancing);
        logger.info("地址..."+jobAddress);

        String jobdetail = page.getHtml().css("#job-view-enterprise > div.wrap.clearfix > div.clearfix > div.main > div.about-position > div.job-item.main-message.job-description > div").toString();

        String job_info= "";
        String jobres = "";
        if (StringUtils.isBlank(jobdetail)) {
            String s = page.getHtml().css("#job-hunter > div.wrap.clearfix > div.clearfix.content > div.main > div.about-position > div > div.job-main.job-description.main-message").toString();
            if (StringUtils.isNotBlank(s)) {
                jobdetail = Jsoup.parse(s).text();
            }
        } else {
            jobdetail = Jsoup.parse(jobdetail).text();
        }

        if (StringUtils.isNotBlank(jobdetail) && jobdetail.contains("要求：")) {

            jobres = jobdetail.substring(0, jobdetail.indexOf("要求："));

            job_info = jobdetail.substring(jobdetail.indexOf("要求："));
        } else {
            job_info = jobdetail;
        }

        Date date = new Date();

        JobInfo jobInfo = new JobInfo();
        jobInfo.setCompanyName(companyName)
                .setCompanyIndustry(companyIndustry)
                .setCompanyPeople(companyPeople)
                .setCompanyFinancing(companyFinancing)
                .setJobName(jobName)
                .setJobInfo(job_info).setJobRes(jobres)
                .setJobAddress(jobAddress)
                .setSalary(salary)
                .setSources("猎聘")
                .setCreateTime(date)
                .setUpdateTime(date);
        page.putField("jobInfo",jobInfo);

    }


    //initialDelay 任务启动多久开始
    //fixedDelay 每隔多久
    @Scheduled(initialDelay = 1000,fixedDelay = 100*1000)
    public void getLiePieJobInfo(){
        //List<IpPool> iPs = ipPoolService.getIPs();
        //HttpClientDownloader httpClientDownloader = new HttpClientDownloader();
        //httpClientDownloader.setProxyProvider(SimpleProxyProvider.from(new Proxy(iPs.get(0).getIp(),Integer.parseInt(iPs.get(0).getPort())),new Proxy(iPs.get(1).getIp(),Integer.parseInt(iPs.get(1).getPort()))));

        Spider.create(new LiePinJobProcessor())
                .addUrl(url)//.setDownloader(httpClientDownloader)
                .setScheduler(new QueueScheduler().setDuplicateRemover(new BloomFilterDuplicateRemover(5000)))
                .thread(20)
                .addPipeline(saveLiePinJonInfoPipeline)
                .run();

    }

    //@Scheduled(fixedDelay=2*60*1000)
    //@Scheduled(fixedDelay=1*60*1000)
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

/*

    public static void main(String[] args) {

        */
/*String s = "<p class=\"job-item-title\">20-33万<em></em><span class=\"title=&quot;反馈时间以工作日为准，周末和假期时间不会计算在内&quot;\">24小时反馈</span></p>";

        Html html = new Html(s);
        String xpath = html.xpath("//p/text()").toString();
        System.out.println(xpath);*//*





    }
*/



}
