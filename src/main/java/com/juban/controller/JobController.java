package com.juban.controller;

import com.juban.common.ConstantCode;
import com.juban.common.MyException;
import com.juban.common.PagesDto;
import com.juban.common.Response;
import com.juban.pojo.GetAllJobsRequestDto;
import com.juban.pojo.JobInfo;
import com.juban.service.JobInfoService;
import io.swagger.annotations.Api;
import io.swagger.annotations.ApiOperation;
import io.swagger.annotations.ApiParam;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.validation.annotation.Validated;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RequestMethod;
import org.springframework.web.bind.annotation.RequestParam;
import org.springframework.web.bind.annotation.RestController;

import javax.servlet.http.HttpServletResponse;
import javax.validation.constraints.NotNull;

@RestController
@RequestMapping("/job")
@Api("获取信息相关接口")
public class JobController {

    @Autowired
    private JobInfoService jobInfoService;


    @RequestMapping(value = "/getAllJobs",method = RequestMethod.GET)
    @ApiOperation("分页获取工作信息列表")
    public Response<PagesDto<JobInfo>> getAllJobs(GetAllJobsRequestDto params){
        PagesDto<JobInfo> allJobs = jobInfoService.getAllJobs(params);
        return new Response<>(allJobs,"查询成功", ConstantCode.SUCCESSED);
    }


    @RequestMapping(value = "/excel", method = RequestMethod.GET)
    @ApiOperation(value = "导出到excel",produces="application/octet-stream")
    public Response excel(HttpServletResponse response) throws Exception {
        jobInfoService.exportExcel(response);
        return new Response<>(null,"导出成功",ConstantCode.SUCCESSED);
    }

    @RequestMapping(value = "/getJobById",method = RequestMethod.GET)
    @ApiOperation("根据id查询招聘信息")
    public Response<JobInfo> getJobById(@NotNull(message = "id不能为空") @ApiParam(value="id",required = true) @RequestParam Integer id){
        JobInfo jobInfo = jobInfoService.getJobById(id);
        return new Response<>(jobInfo,"查询成功",ConstantCode.SUCCESSED);
    }

    @RequestMapping(value = "/异常测试",method = RequestMethod.POST)
    @ApiOperation("测试接口")
    public String test(@Validated GetAllJobsRequestDto aaa){
        String s = null;
        s.length();//
        if (true) {
            throw new MyException(1212,"没有查到");
        }
        //int i= 1/0;
        return "";

    }



}


