package com.juban.pojo;

import com.juban.common.PagesParam;
import io.swagger.annotations.ApiModel;
import io.swagger.annotations.ApiModelProperty;


@ApiModel("获取招聘列表")
public class GetAllJobsRequestDto extends PagesParam{

    @ApiModelProperty("公司名称")
    private String companyName;

    @ApiModelProperty("工作名字")
    private String jobName;

    @ApiModelProperty("工资")
    private String salary;


    public String getCompanyName() {
        return companyName;
    }

    public void setCompanyName(String companyName) {
        this.companyName = companyName;
    }

    public String getJobName() {
        return jobName;
    }

    public void setJobName(String jobName) {
        this.jobName = jobName;
    }

    public String getSalary() {
        return salary;
    }

    public void setSalary(String salary) {
        this.salary = salary;
    }
}
