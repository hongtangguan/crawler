package com.juban.task.wuba;


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
public class SaveJonInfoPipeline implements Pipeline {

    private Logger logger = LoggerFactory.getLogger(SaveJonInfoPipeline.class);


    @Autowired
    private JobInfoService jobInfoService;

    @Override
    public void process(ResultItems resultItems, Task task) {

        JobInfo jobInfo = resultItems.get("jobInfo");

        if (jobInfo != null) {
            jobInfoService.insertJob(jobInfo);
            logger.info("入库成功...."+jobInfo);
        }
    }
}
