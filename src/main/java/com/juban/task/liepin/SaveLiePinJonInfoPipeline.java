package com.juban.task.liepin;


import com.juban.pojo.JobInfo;
import com.juban.service.JobInfoService;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Component;
import us.codecraft.webmagic.ResultItems;
import us.codecraft.webmagic.Task;
import us.codecraft.webmagic.pipeline.Pipeline;

@Component
public class SaveLiePinJonInfoPipeline implements Pipeline {

    private Logger logger = LoggerFactory.getLogger(SaveLiePinJonInfoPipeline.class);
    @Autowired
    private JobInfoService jobInfoService;

    @Override
    public void process(ResultItems resultItems, Task task) {
        //JobBoss jobInfo = resultItems.get("jobBoss");
/*

        if (jobInfo != null) {
            //根据公司名称和岗位名称查询表中数据是否存在
            String companyName = jobInfo.getCompanyName();
            String jobName = jobInfo.getJobName();
            String salary = jobInfo.getSalary();

            int jobByCompanNameAndJobName = jobBossService.getJobByCompanNameAndJobName(companyName, jobName,salary);
            if (jobByCompanNameAndJobName == 0) {
                //入库
                jobBossService.saveBossJob(jobInfo);
            } else {
                logger.error("jobInfo该记录已存在................................");
            }
        } else {
            logger.error("jobInfo为空................................");
        }

*/
        JobInfo jobInfo = resultItems.get("jobInfo");
        if (jobInfo != null) {
            jobInfoService.insertJob(jobInfo);
            logger.info("入库完成..............."+ jobInfo);
        }


    }
}
