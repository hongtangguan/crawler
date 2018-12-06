package com.juban.controller;

import com.juban.pojo.GetAllJobsRequestDto;
import com.juban.pojo.JobInfo;
import com.juban.service.JobInfoService;
import com.juban.utils.PagesDto;
import io.swagger.annotations.ApiOperation;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RequestMethod;
import org.springframework.web.bind.annotation.RestController;

@RestController
@RequestMapping("/job")
public class JobController {
@Autowired
private JobInfoService jobInfoService;

    @RequestMapping(value = "/getAllJobs",method = RequestMethod.GET)
    @ApiOperation("分页获取工作信息列表")
    public PagesDto<JobInfo> getAllJobs(GetAllJobsRequestDto params){
        PagesDto<JobInfo> allJobs = jobInfoService.getAllJobs(params);
        return  allJobs;
    }



}


