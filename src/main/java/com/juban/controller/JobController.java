package com.juban.controller;

import com.juban.pojo.GetAllJobsRequestDto;
import com.juban.pojo.JobInfo;
import com.juban.service.IPPoolService;
import com.juban.service.JobInfoService;
import com.juban.utils.ExcelData;
import com.juban.utils.ExportExcelUtils;
import com.juban.utils.PagesDto;
import io.swagger.annotations.ApiOperation;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RequestMethod;
import org.springframework.web.bind.annotation.RestController;

import javax.servlet.http.HttpServletResponse;
import java.util.ArrayList;
import java.util.List;

@RestController
@RequestMapping("/job")
public class JobController {
    @Autowired
    private JobInfoService jobInfoService;

    @Autowired
    private IPPoolService ipPoolService;


    @RequestMapping(value = "/getAllJobs",method = RequestMethod.GET)
    @ApiOperation("分页获取工作信息列表")
    public PagesDto<JobInfo> getAllJobs(GetAllJobsRequestDto params){
        PagesDto<JobInfo> allJobs = jobInfoService.getAllJobs(params);
        return  allJobs;
    }


    @RequestMapping(value = "/excel", method = RequestMethod.GET)
    @ApiOperation(value = "测试导出到excel",produces="application/octet-stream")
    public void excel(HttpServletResponse response) throws Exception {

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

        List<JobInfo> allJobs = jobInfoService.getAllJob();

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

/*        ExcelData data = new ExcelData();
        data.setName("hello");
        List<String> titles = new ArrayList();
        titles.add("a1");
        titles.add("a2");
        titles.add("a3");
        data.setTitles(titles);

        List<List<Object>> rows = new ArrayList();
        List<Object> row = new ArrayList();
        row.add("11111111111");
        row.add("22222222222");
        row.add("3333333333");
        rows.add(row);

        data.setRows(rows);


        //生成本地
*//*        File f = new File("c:/test.xlsx");
        FileOutputStream out = new FileOutputStream(f);
        ExportExcelUtils.exportExcel(data, out);
        out.close();*//*
        ExportExcelUtils.exportExcel(response,"hello.xlsx",data);*/
    }



}


