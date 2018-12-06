package com.juban.service;


import com.juban.pojo.GetAllJobsRequestDto;
import com.juban.pojo.JobInfo;
import com.juban.utils.PagesDto;

public interface JobInfoService {


    JobInfo getById(int id);


    void insertJob(JobInfo jobInfo);

    PagesDto<JobInfo> getAllJobs(GetAllJobsRequestDto getAllJobsRequestDto);


}
