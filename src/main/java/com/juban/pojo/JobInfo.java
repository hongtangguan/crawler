package com.juban.pojo;

import lombok.Data;
import lombok.experimental.Accessors;

import java.util.Date;

/**
*
*/
@Data
@Accessors(chain = true)
public class JobInfo {
    /**
    *主键
    */
    private Integer id;

    /**
    *公司名称
    */
    private String companyName;

    /**
    *所属行业
    */
    private String companyIndustry;

    /**
    *公司人数
    */
    private String companyPeople;

    /**
    *融资情况
    */
    private String companyFinancing;

    /**
    *工作名称
    */
    private String jobName;

    /**
    *工作描述
    */
    private String jobInfo;

    /**
    *岗位职责
    */
    private String jobRes;

    /**
    *工作地址
    */
    private String jobAddress;

    /**
    *薪资
    */
    private String salary;

    /**
    *数据来源
    */
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