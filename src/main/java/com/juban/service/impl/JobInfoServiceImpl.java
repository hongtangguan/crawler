package com.juban.service.impl;


import com.github.pagehelper.PageHelper;
import com.github.pagehelper.PageInfo;
import com.juban.mapper.JobInfoMapper;
import com.juban.pojo.GetAllJobsRequestDto;
import com.juban.pojo.JobInfo;
import com.juban.service.JobInfoService;
import com.juban.common.PagesDto;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

import java.util.List;

@Service
public class JobInfoServiceImpl implements JobInfoService {

    @Autowired
    private JobInfoMapper jobInfoMapper;


    @Override
    public JobInfo getById(int id) {
        return jobInfoMapper.selectByPrimaryKey(id);
    }


    @Override
    public void insertJob(JobInfo jobInfo) {

        jobInfoMapper.insertSelective(jobInfo);
/*
        //先查询该jobInfo是否存在,  根据URL和发布时间来判断
        JobInfoExample jobInfoExample = new JobInfoExample();
        JobInfoExample.Criteria criteria = jobInfoExample.createCriteria();
        criteria.andUrlEqualTo(jobInfo.getUrl());
        criteria.andTimeEqualTo(jobInfo.getTime());

        List<JobInfo> jobInfos = jobInfoMapper.selectByExample(jobInfoExample);

        if (CollectionUtils.isEmpty(jobInfos)) {
            //不存在, 就直接保存
            jobInfoMapper.insertSelective(jobInfo);

        } else {
            jobInfos.stream().forEach( i -> {
                i.setUrl(jobInfo.getUrl());
                i.setTime(jobInfo.getTime());
                i.setUpdateTime(new Date());
                jobInfoMapper.updateByPrimaryKey(i);
            } );
        }
        */




    }


    @Override
    public PagesDto<JobInfo> getAllJobs(GetAllJobsRequestDto getAllJobsRequestDto) {
        PageHelper.startPage(getAllJobsRequestDto.getPage(),getAllJobsRequestDto.getRows());
        List<JobInfo> allJobs = jobInfoMapper.getAllJobs(getAllJobsRequestDto);
        PageInfo<JobInfo> info=new PageInfo<>(allJobs);
        return new PagesDto<>(info.getList(),info.getTotal(),getAllJobsRequestDto.getPage(),getAllJobsRequestDto.getRows());

    }

    @Override
    public List<JobInfo> getAllJob() {
        return jobInfoMapper.getAllJob();
    }
}
