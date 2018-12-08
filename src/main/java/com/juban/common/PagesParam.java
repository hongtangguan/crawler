package com.juban.common;


import io.swagger.annotations.ApiModel;
import io.swagger.annotations.ApiModelProperty;

import java.io.Serializable;

/**
 * 分页请求参数
 */
@ApiModel("分页请求公共参数")
public class PagesParam implements Serializable {

    @ApiModelProperty(value = "页码", required = true)
    private int page = 1;

    @ApiModelProperty(value = "行数", required = true)
    private int rows = 10;

    public int getPage() {
        return page;
    }

    public void setPage(int page) {
        this.page = page;
    }

    public int getRows() {
        return rows;
    }

    public void setRows(int rows) {
        this.rows = rows;
    }
}
