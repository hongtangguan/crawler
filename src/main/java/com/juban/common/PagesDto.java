package com.juban.common;

import com.github.pagehelper.Page;
import com.github.pagehelper.PageInfo;
import io.swagger.annotations.ApiModel;
import io.swagger.annotations.ApiModelProperty;

import java.util.List;

/**
 * @Author: hejiang
 * @Description: 分页类 返回公用参数
 * @Date: 17:39 2018/3/30
 */
@ApiModel("分页类 返回公用参数")
public class PagesDto<T> implements java.io.Serializable {

    @ApiModelProperty(value = "当前第几页", required = true)
    private int page; //当前第几页

    @ApiModelProperty(value = "分页页码最大条件", required = true)
    private int size;//分页页码最大条件

    @ApiModelProperty(value = "分页列表", required = true)
    private List<T> rows;//分页列表

    @ApiModelProperty(value = "记录总数", required = true)
    private long total;//记录总数

    @ApiModelProperty(value = "总页数", required = true)
    private long totalPage; //总页数

    /**
     * @param list
     * @param total_
     * @param offset_
     * @param length_
     */
    public PagesDto(List<T> list, long total_, int offset_, int length_) {
        this.rows = list;
        this.total = total_;
        this.page = offset_;
        this.size = length_;

        this.totalPage = total_ / length_;
        if (total_ % length_ > 0) {
            this.totalPage++;
        }

    }

    public PagesDto(List<T> list, int offset_, int length_) {
        long total_ = new PageInfo<T>(list).getTotal();
        this.rows = list;
        this.total = total_;
        this.page = offset_;
        this.size = length_;
        this.totalPage = total_ / length_;
        if (total_ % length_ > 0) {
            this.totalPage++;
        }
    }

    public PagesDto(Page<T> page) {
        this.rows = page.getResult();
        this.total = page.getTotal();
        this.page = page.getPageNum();
        this.size = page.getPageSize();

        this.totalPage = total / size;
        if (total % size > 0) {
            this.totalPage++;
        }

    }

    public PagesDto() {

    }

    public int getOffset() {
        return page;
    }


    public int getPage() {
        return page;
    }

    public void setPage(int page) {
        this.page = page;
    }

    public void setOffset(int offset) {
        this.page = offset;
    }

    public int getLength() {
        return size;
    }

    public void setLength(int length) {
        this.size = length;
    }

    public List<T> getRows() {
        return rows;
    }

    public void setRows(List<T> rows) {
        this.rows = rows;
    }

    public long getTotal() {
        return total;
    }

    public void setTotal(long total) {
        this.total = total;
    }

    public long getTotalPage() {
        return totalPage;
    }

    public void setTotalPage(long totalPage) {
        this.totalPage = totalPage;
    }
}
