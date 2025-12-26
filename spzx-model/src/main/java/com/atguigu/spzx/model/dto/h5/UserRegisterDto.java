package com.atguigu.spzx.model.dto.h5;

import io.swagger.v3.oas.annotations.media.Schema;
import jakarta.validation.constraints.NotNull;
import lombok.Data;

@Data
@Schema(description="注册对象")
public class UserRegisterDto {

    @Schema(description = "用户名")
    @NotNull(message = "用户名不能为空")
    private String username;

    @Schema(description = "密码")
    @NotNull(message = "密码不能为空")
    private String password;

    @Schema(description = "昵称")
    @NotNull(message = "昵称不能为空")
    private String nickName;

    @Schema(description = "手机验证码")
    @NotNull(message = "手机验证码不能为空")
    private String code ;

}