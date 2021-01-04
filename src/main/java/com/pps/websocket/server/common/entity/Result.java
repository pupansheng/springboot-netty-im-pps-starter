package com.pps.websocket.server.common.entity;



import com.pps.websocket.server.common.model.HttpStatus;

import java.io.Serializable;

/**
 * @author
 * @discription;
 * @time 2020/12/15 16:52
 */
public class Result implements Serializable {

    private Integer code;
    private boolean  serviceSucess;
    private String  error;
    private Object  data;
    public static Result Ok(Object t){
        Result result=new Result();
        result.code= HttpStatus.Ok.getCode();
        result.data=t;
        result.serviceSucess=true;
        return  result;
    }
    public static Result error(HttpStatus httpStatus){
        Result result=new Result();
        result.code= HttpStatus.Ok.getCode();
        result.error=httpStatus.getMessage();
        result.serviceSucess=false;
        return  result;
    }
    public static Result error(HttpStatus httpStatus,String ... msg){
        Result result=new Result();
        result.code= httpStatus.getCode();
        if(msg.length>0){
            result.error=msg[0];
        }else {
            result.error = httpStatus.getMessage();
        }
        result.serviceSucess=false;
        return  result;
    }

    public Integer getCode() {
        return code;
    }

    public void setCode(Integer code) {
        this.code = code;
    }

    public boolean isServiceSucess() {
        return serviceSucess;
    }

    public void setServiceSucess(boolean serviceSucess) {
        this.serviceSucess = serviceSucess;
    }

    public String getError() {
        return error;
    }

    public void setError(String error) {
        this.error = error;
    }

    public Object getData() {
        return data;
    }

    public void setData(Object data) {
        this.data = data;
    }
}
