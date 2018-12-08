package com.juban.common;

public class MyException extends RuntimeException
{

    private static final long serialVersionUID = 8223922895313207232L;


    private Integer code;
    private String msg;

    public MyException(){}

    public MyException(String msg){
        this.msg = msg ;
    }

    public MyException( Integer code, String msg) {
        this.code = code;
        this.msg = msg;
    }

    public Integer getCode() {
        return code;
    }

    public void setCode(Integer code) {
        this.code = code;
    }

    public String getMsg() {
        return msg;
    }

    public void setMsg(String msg) {
        this.msg = msg;
    }
}
