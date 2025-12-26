package com.atguigu.spzx.user.controller;

import com.atguigu.spzx.model.dto.h5.UserLoginDto;
import com.atguigu.spzx.model.dto.h5.UserRegisterDto;
import com.atguigu.spzx.model.vo.common.Result;
import com.atguigu.spzx.model.vo.common.ResultCodeEnum;
import com.atguigu.spzx.user.service.UserInfoService;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.web.bind.annotation.*;

@RestController
@RequestMapping("/api/user/userInfo")
public class UserInfoController {

    @Autowired
    private UserInfoService userInfoService;

    @PostMapping("/register")
    public Result register(@RequestBody UserRegisterDto userRegisterDto) {
        userInfoService.register(userRegisterDto);
        return Result.build(null , ResultCodeEnum.SUCCESS);
    }

    @PostMapping("/login")
    public Result login(@RequestBody UserLoginDto userLoginDto) {
        String token = userInfoService.login(userLoginDto);

        return Result.build(token , ResultCodeEnum.SUCCESS);
    }
}
