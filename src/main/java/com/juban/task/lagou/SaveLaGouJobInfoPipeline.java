package com.juban.task.lagou;

import com.alibaba.fastjson.JSON;
import com.juban.pojo.JobInfo;
import com.juban.service.JobInfoService;
import org.apache.commons.collections.CollectionUtils;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Component;
import us.codecraft.webmagic.ResultItems;
import us.codecraft.webmagic.Task;
import us.codecraft.webmagic.pipeline.Pipeline;

import java.util.List;

@Component
public class SaveLaGouJobInfoPipeline implements Pipeline {
    private Logger logger = LoggerFactory.getLogger(SaveLaGouJobInfoPipeline.class);
    @Autowired
    private JobInfoService jobInfoService;

    @Override
    public void process(ResultItems resultItems, Task task) {

       /* JobInfo jobInfo = resultItems.get("jobInfo");
        if (jobInfo != null) {
            jobInfoService.insertJob(jobInfo);
            logger.info("=============入库完成==================");
        }
*/

        List<String> companyFullName = resultItems.get("companyFullName");
        List<String> positionname = resultItems.get("positionname");
        List<String> salary = resultItems.get("salary");
        List<String> industryField = resultItems.get("industryField");
        List<String> financeStage = resultItems.get("financeStage");
        List<String> companySize = resultItems.get("companySize");
        List<String> firstType = resultItems.get("firstType");

        if (CollectionUtils.isNotEmpty(companyFullName)) {

            for (int i = 0; i < companyFullName.size(); i++) {
                JobInfo jobInfo = new JobInfo();
                jobInfo.setCompanyName(companyFullName.get(i));
                jobInfo.setJobName(positionname.get(i));
                jobInfo.setSalary(salary.get(i));
                jobInfo.setCompanyIndustry(industryField.get(i));
                jobInfo.setCompanyFinancing(financeStage.get(i));
                jobInfo.setCompanyPeople(companySize.get(i));
                jobInfo.setSources("拉钩");
                jobInfo.setJobInfo(firstType.get(i));
                logger.info("入库........"+ JSON.toJSONString(jobInfo));
                jobInfoService.insertJob(jobInfo);

            }

        }

    }
}
