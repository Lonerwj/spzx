package com.atguigu.spzx.manager.controller;

import com.atguigu.spzx.manager.service.SysUserService;
import com.atguigu.spzx.model.dto.system.LoginDto;
import com.atguigu.spzx.model.entity.system.SysUser;
import com.atguigu.spzx.model.vo.common.Result;
import com.atguigu.spzx.model.vo.common.ResultCodeEnum;
import com.atguigu.spzx.model.vo.system.LoginVo;
import io.swagger.v3.oas.annotations.Operation;
import io.swagger.v3.oas.annotations.tags.Tag;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.web.bind.annotation.*;

@Tag(name = "用户接口")
@RestController
@RequestMapping(value = "admin/system/index")
public class IndexController {


    @Autowired
    private SysUserService sysUserService;

    //用户登录
    @Operation(summary = "登录")
    @PostMapping("/login")
    public Result login(@RequestBody LoginDto loginDto) {
        LoginVo loginVo = sysUserService.login(loginDto);
        return Result.build(loginVo, ResultCodeEnum.SUCCESS);
    }

    // 生成图形验证码
    @Operation(summary = "生成验证码")
    @GetMapping("/generateValidateCode")
    public Result generateValidateCode() {
        return Result.build(sysUserService.generateVerifyCode(), ResultCodeEnum.SUCCESS);
    }

    // 获取用户信息
    @Operation(summary = "获取用户信息")
    @GetMapping(value = "/getUserInfo")
    public Result<SysUser> getUserInfo(@RequestHeader(name = "token") String token) {
        SysUser sysUser = sysUserService.getUserInfo(token) ;
        return Result.build(sysUser , ResultCodeEnum.SUCCESS) ;
    }

    // 登出
    @Operation(summary = "登出")
    @GetMapping(value = "/logout")
    public Result logout(@RequestHeader(value = "token") String token) {
        sysUserService.logout(token) ;
        return Result.build(null , ResultCodeEnum.SUCCESS) ;
    }



}
