package com.atguigu.spzx.manager.service;

import com.atguigu.spzx.model.dto.system.LoginDto;
import com.atguigu.spzx.model.entity.system.SysUser;
import com.atguigu.spzx.model.vo.system.LoginVo;
import com.atguigu.spzx.model.vo.system.ValidateCodeVo;
import com.baomidou.mybatisplus.extension.service.IService;
import org.springframework.stereotype.Service;

@Service
public interface SysUserService extends IService<SysUser> {
    LoginVo login(LoginDto loginDto);

    ValidateCodeVo generateVerifyCode();

    SysUser getUserInfo(String token);

    void logout(String token);
}
