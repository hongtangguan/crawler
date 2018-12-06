package com.juban;

import com.juban.pojo.GetAllJobsRequestDto;
import com.juban.pojo.JobInfo;
import com.juban.service.JobInfoService;
import com.juban.task.boss.BossTest;
import com.juban.task.lagou.LagouProcessor;
import com.juban.task.liepin.LiePinJobProcessor;
import com.juban.task.qianchengwuyou.QianChenJobProcessor;
import com.juban.utils.PagesDto;
import org.junit.Test;
import org.junit.runner.RunWith;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.context.SpringBootTest;
import org.springframework.test.context.junit4.SpringRunner;

@RunWith(SpringRunner.class)
@SpringBootTest
public class CrawlerApplicationTests {

	//@Autowired
	//private ZhiLian zhiLian;
    @Autowired
    private JobInfoService jobInfoService;

	@Autowired
	private QianChenJobProcessor qianChenJobProcessor;

	@Autowired
	private LiePinJobProcessor liePinJobProcessor;

	@Autowired
	private LagouProcessor lagouProcessor;

	//@Autowired
	//private BossJobProcessor bossJobProcessor;

	@Autowired
	private BossTest bossTest;



	//拉钩
	@Test
	public void getLaGouJob(){
		lagouProcessor.getLaGouJob();
	}


	//前程无忧
	@Test
	public void get51jobInfo(){
		qianChenJobProcessor.getQianChengJob();
	}

	//智联
    @Test
	public void getZhilianJob(){
       //zhiLian.crawlerTask();
    }

    //猎聘
    @Test
    public void getLiePieJobInfo(){
		liePinJobProcessor.getLiePieJobInfo();
	}



	//BOSS
	@Test
	public void bossTest(){
		bossTest.boss();
	}




	@Test
	public void getBossJob(){
		//bossJobProcessor.getBossJob();
	}


	@Test
	public void getAllJobs(){
		GetAllJobsRequestDto params = new GetAllJobsRequestDto();
        PagesDto<JobInfo> allJobs = jobInfoService.getAllJobs(params);
        System.out.println(allJobs.getRows());
    }

}
