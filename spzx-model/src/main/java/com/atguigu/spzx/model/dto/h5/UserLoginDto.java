package com.atguigu.spzx.model.dto.h5;

import io.swagger.v3.oas.annotations.media.Schema;
import jakarta.validation.constraints.NotNull;
import lombok.Data;

@Data
@Schema(description = "用户登录请求参数")
public class UserLoginDto {

    @Schema(description = "用户名")
    @NotNull(message = "用户名不能为空")
    private String username ;

    @Schema(description = "密码")
    @NotNull(message = "密码不能为空")
    private String password ;
}