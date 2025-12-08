package com.atguigu.spzx.common.exception;

import com.atguigu.spzx.model.vo.common.ResultCodeEnum;
import lombok.Data;

@Data
public class SelfException extends RuntimeException{

    private Integer code;

    private String msg;

    private ResultCodeEnum resultCodeEnum;

    public SelfException(ResultCodeEnum resultCodeEnum) {
        super(resultCodeEnum.getMessage());
        this.code = resultCodeEnum.getCode();
        this.resultCodeEnum = resultCodeEnum;
    }
}
