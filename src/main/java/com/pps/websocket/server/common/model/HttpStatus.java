package com.pps.websocket.server.common.model;

/**
 * @author
 * @discription;
 * @time 2020/12/15 16:53
 */
public enum  HttpStatus {

    Ok(20000,"请求成功"),
    ERROR1(20001,"未授权"),
    ERROR2(20002,"请求方法不支持或无服务"),
    BuessError(20003,"业务错误");
    private int code;
    private String message;
    HttpStatus(int code,String message){
        this.code=code;
        this.message=message;
    }

    public int getCode() {
        return code;
    }

    public String getMessage() {
        return message;
    }
}
