package com.microservice.rest.exception;

/**
 * Created by Aizhanglin on 2017/12/4.
 */
public class BusException extends RuntimeException{
    public static Integer SYSTEM_ERROR=1;
    public static Integer TRUCK_CHECK_WAITING=201;//等待车辆审核
    public static Integer TRUCK_CHECK_NO_ONE=202;//没有车辆
    public static Integer HAS_NO_PRIZE_CHANCE=203;//大转盘没有奖励机会
    public static Integer TRUCK_CHECK_UNPASS=204;//车辆没通过审核
    public static Integer TRUCK_OLD_DRIVER=205;//大转盘老司机没有抽奖机会
    public static Integer TOO_MANY_SHARE=206;//大转盘一天最多只能分享两次
    /**
     *
     */
    private static final long serialVersionUID = 1L;
    private Integer errorCode;

    public Object getData() {
        return data;
    }

    private Object data;
    public void setErrorCode(Integer errorCode) {
        this.errorCode = errorCode;
    }
    public Integer getErrorCode() {
        return errorCode;
    }

    public BusException(){
        super();
    }
    public BusException(String msg){
        super(msg);
    }
    public BusException(Integer errorCode,String msg){
        super(msg);
        if (errorCode==null){
            this.errorCode=SYSTEM_ERROR;
        }else {
            this.errorCode=errorCode;
        }
    }
    public BusException(Integer errorCode,Object data,String msg){
        super(msg);
        if (errorCode==null){
            this.errorCode=SYSTEM_ERROR;
        }else {
            this.errorCode=errorCode;
        }
        this.data=data;
    }
    public BusException(Throwable e){
        super(e);
    }
    public BusException(String msg,Throwable e){
        super(msg, e);
    }
    public BusException(Integer errorCode,String msg,Throwable e){
        super(msg, e);
        this.errorCode=errorCode;
    }
}
