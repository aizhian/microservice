package com.microservice.rest.vo;

/**
 *  Created by Aizhanglin on 2016/8/11.
 *
 *  错误级别:
 *  code           对应含义

 *  模块编码:
 *  code           模块名称
 *
 *  错误码含义：转二进制后 低二位为错误级别，高二位为模块编码
 *     十进制算法
 *      错误码=模块编号*4+错误级别
 *     位运算算法
 *      错误码=(模块编号<<2)+错误级别
 *
 **/
public class ErrorCode {
    public final static int SUCCESS =0;
    public final static int FAILE =1;
    public final static int UNKNOWN =2;
    /**
     * 获取错误码
     * @param model
     * @param errorType
     * @return
     */
    public static Integer getErrorCode(Model model,ErrorType errorType){
        return (model.getCode()<<2)+ errorType.getCode();
    }

    /**
     * 获取错误模块
     * @param errorCode
     * @return
     */
    public static Model getErrorModel(int errorCode){
        int n=errorCode>>2;//取高二位
        for (Model model : Model.values()) {
            if (model.getCode()==n){
                return model;
            }
        }
        return null;
    }

    /**
     * 获取错误级别
     * @param errorCode
     * @return
     */
    public static ErrorType getErrorLevel(int errorCode){
        int n=errorCode&3;//取低二位
        for (ErrorType errorType : ErrorType.values()) {
            if (errorType.ordinal()==n){
                return errorType;
            }
        }

        return null;
    }
    public enum Model{
        BUS(1,"业务中心"),ACTIVITY(2,"活动中心"),APP(3,"app后台"),MIS(4,"管理后台"),DB(5,"数据库中间层");
        private int code;
        private String name;
        Model(Integer code,String name){
            this.code=code;
            this.name=name;
        }
        @Override
        public String toString() {
            return this.name;
        }
        public Integer getCode(){
            return this.code;
        }
    }
    public enum ErrorType {
        UNKNOWN(1,"系统未知异常"),PARAM(2,"不合法的参数");
        private int code;
        private String name;
        ErrorType(Integer code,String name){
            this.code=code;
            this.name=name;
        }

        @Override
        public String toString() {
            return this.name;
        }
        public Integer getCode(){
            return this.code;
        }
    }
}
