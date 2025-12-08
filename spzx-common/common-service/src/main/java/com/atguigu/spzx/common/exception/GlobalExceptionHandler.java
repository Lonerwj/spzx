package com.atguigu.spzx.common.exception;


import com.atguigu.spzx.model.vo.common.Result;
import com.atguigu.spzx.model.vo.common.ResultCodeEnum;
import org.springframework.web.bind.annotation.ControllerAdvice;
import org.springframework.web.bind.annotation.ExceptionHandler;
import org.springframework.web.bind.annotation.ResponseBody;

@ControllerAdvice
@ResponseBody
public class GlobalExceptionHandler{

    //全局异常处理
    @ExceptionHandler(SelfException.class)
    public Result error(SelfException e) {
        e.printStackTrace();
        return Result.build(e.getMsg(),e.getResultCodeEnum());
    }
}
