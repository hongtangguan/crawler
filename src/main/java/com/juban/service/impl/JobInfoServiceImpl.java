package com.juban.service.impl;


import com.github.pagehelper.PageHelper;
import com.github.pagehelper.PageInfo;
import com.juban.common.PagesDto;
import com.juban.mapper.JobInfoMapper;
import com.juban.pojo.GetAllJobsRequestDto;
import com.juban.pojo.JobInfo;
import com.juban.service.JobInfoService;
import com.juban.utils.ExcelData;
import com.juban.utils.ExportExcelUtils;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

import javax.servlet.http.HttpServletResponse;
import java.util.ArrayList;
import java.util.List;

@Service
public class JobInfoServiceImpl implements JobInfoService {

    private Logger logger =  LoggerFactory.getLogger(JobInfoServiceImpl.class);

    @Autowired
    private JobInfoMapper jobInfoMapper;



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

    @Override
    public void exportExcel(HttpServletResponse response) throws Exception {

        logger.info("导出excel开始...............................");
        ExcelData data = new ExcelData();
        data.setName("zhaopin");
        //标题
        List<String> titles = new ArrayList();
        titles.add("公司名称");
        titles.add("公司行业");
        titles.add("公司人数");
        titles.add("融资情况");
        titles.add("工作名称");
        titles.add("薪资带宽");
        titles.add("招聘要求");
        titles.add("工作地址");
        titles.add("数据来源");
        data.setTitles(titles);

        List<JobInfo> allJobs = getAllJob();
        logger.info("总行数:"+allJobs.size());
        List<List<Object>> rows = new ArrayList();

        for (int i = 0; i < allJobs.size(); i++) {
            List<Object> row = new ArrayList();
            row.add(allJobs.get(i).getCompanyName());
            row.add(allJobs.get(i).getCompanyIndustry());
            row.add(allJobs.get(i).getCompanyPeople());
            row.add(allJobs.get(i).getCompanyFinancing());
            row.add(allJobs.get(i).getJobName());
            row.add(allJobs.get(i).getSalary());
            row.add(allJobs.get(i).getJobInfo());
            row.add(allJobs.get(i).getJobAddress());
            row.add(allJobs.get(i).getSources());
            rows.add(row);
        }
        data.setRows(rows);
        ExportExcelUtils.exportExcel(response,"zhaopin.xlsx",data);

        logger.info("导出完成................");
    }
}
