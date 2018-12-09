package com.juban.pojo;

import io.swagger.annotations.ApiModel;
import io.swagger.annotations.ApiModelProperty;
import lombok.Data;
import lombok.experimental.Accessors;

import java.util.Date;

/**
*
*/
@Data
@Accessors(chain = true)
@ApiModel("job信息实体")
public class JobInfo {
    /**
    *主键
    */
    @ApiModelProperty("主键")
    private Integer id;

    /**
    *公司名称
    */
    @ApiModelProperty("公司名称")
    private String companyName;

    /**
    *所属行业
    */
    @ApiModelProperty("所属行业")
    private String companyIndustry;

    /**
    *公司人数
    */
    @ApiModelProperty("公司人数")
    private String companyPeople;

    /**
    *融资情况
    */
    @ApiModelProperty("融资情况")
    private String companyFinancing;

    /**
    *工作名称
    */
    @ApiModelProperty("工作名称")
    private String jobName;

    /**
    *工作描述
    */
    @ApiModelProperty("工作描述")
    private String jobInfo;

    /**
    *岗位职责
    */
    @ApiModelProperty("岗位职责")
    private String jobRes;

    /**
    *工作地址
    */
    @ApiModelProperty("工作地址")
    private String jobAddress;

    /**
    *薪资
    */
    @ApiModelProperty("薪资")
    private String salary;

    /**
    *数据来源
    */
    @ApiModelProperty("数据来源")
    private String sources;

    /**
    *创建时间
    */
    private Date createTime;

    /**
    *修改时间
    */
    private Date updateTime;



}