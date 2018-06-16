package com.genesis.microservice.business.exception;

import com.microservice.rest.vo.ErrorCode;
import com.microservice.rest.vo.Result;
import com.microservice.rest.exception.BusException;
import lombok.extern.slf4j.Slf4j;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.ControllerAdvice;
import org.springframework.web.bind.annotation.ResponseBody;

import javax.servlet.http.HttpServletRequest;

/**
 * Created by Aizhanglin on 2017/12/4.
 *
 */

@ControllerAdvice
@Slf4j
public class ExceptionHandler{

    @org.springframework.web.bind.annotation.ExceptionHandler({BusException.class,RuntimeException.class})
    @ResponseBody
    ResponseEntity handleBusException(HttpServletRequest request, Exception ex) {
        return createResult(ErrorCode.FAILE,ex.getMessage());
    }

    @org.springframework.web.bind.annotation.ExceptionHandler(Exception.class)
    @ResponseBody
    ResponseEntity handleException(HttpServletRequest request, Throwable ex) {
        log.error("系统未知错误",ex);
        return createResult(ErrorCode.UNKNOWN,ex.getMessage());
    }

    private ResponseEntity createResult(Integer code,String message){
        ResponseEntity responseEntity = new ResponseEntity(new Result(code,null,message), HttpStatus.OK);
        return responseEntity;
    }

}
