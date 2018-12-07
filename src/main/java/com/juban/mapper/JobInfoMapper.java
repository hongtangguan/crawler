package com.juban.mapper;

import com.juban.pojo.GetAllJobsRequestDto;
import com.juban.pojo.JobInfo;
import org.apache.ibatis.annotations.Select;

import java.util.List;

public interface JobInfoMapper {
    int deleteByPrimaryKey(Integer id);

    int insert(JobInfo record);

    int insertSelective(JobInfo record);

    JobInfo selectByPrimaryKey(Integer id);

    int updateByPrimaryKeySelective(JobInfo record);

    int updateByPrimaryKey(JobInfo record);


    List<JobInfo> getAllJobs(GetAllJobsRequestDto params);

    @Select("select * from job_info")
    List<JobInfo> getAllJob();
}