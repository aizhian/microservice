package com.microservice.rest.vo;

import lombok.Data;

/**
 * Created by Aizhanglin on 2017/5/17.
 */
@Data
public class Result {
    private Integer code;
    private Object data;
    private String message;
    public Result(){
    }
    public Result(Integer code, Object data, String message){
        this.code=code;
        this.data=data;
        this.message=message;
    }
    public static Result success(Object data){
        return new Result(ErrorCode.SUCCESS,data,"操作成功");
    }

    public static Result faile(){
        return new Result(ErrorCode.FAILE,null,"操作失败");
    }
}

