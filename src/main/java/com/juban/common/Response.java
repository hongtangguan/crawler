package com.juban.common;

import io.swagger.annotations.ApiModel;
import io.swagger.annotations.ApiModelProperty;

import java.io.Serializable;
@ApiModel("综合返回实体")
public class Response<T> implements Serializable {


    private static final long serialVersionUID = -5927509695578705811L;

    /**
     * 返回结果集
     */
    @ApiModelProperty("返回数据")
    private T result;
    /**
     * 返回消息
     */
    @ApiModelProperty("返回消息")
    private String msg;
    /**
     * 响应码
     */
    @ApiModelProperty("响应码")
    private Integer code;


    public Response() {
    }


    public Response(T result, String msg, Integer code) {
        this.result = result;
        this.msg = msg;
        this.code = code;
    }

    public T getResult() {
        return result;
    }

    public void setResult(T result) {
        this.result = result;
    }

    public String getMsg() {
        return msg;
    }

    public void setMsg(String msg) {
        this.msg = msg;
    }

    public Integer getCode() {
        return code;
    }

    public void setCode(Integer code) {
        this.code = code;
    }
}
