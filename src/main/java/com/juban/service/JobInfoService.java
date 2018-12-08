package com.juban.service;


import com.juban.pojo.GetAllJobsRequestDto;
import com.juban.pojo.JobInfo;
import com.juban.common.PagesDto;

import javax.servlet.http.HttpServletResponse;
import javax.validation.constraints.NotNull;
import java.util.List;

public interface JobInfoService {


    void insertJob(JobInfo jobInfo);

    PagesDto<JobInfo> getAllJobs(GetAllJobsRequestDto getAllJobsRequestDto);

    List<JobInfo> getAllJob();

    void exportExcel(HttpServletResponse response) throws Exception;

    JobInfo getJobById(@NotNull(message = "id不能为空") Integer id);
}
