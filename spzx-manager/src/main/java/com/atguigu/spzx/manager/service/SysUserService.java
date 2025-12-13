package com.atguigu.spzx.manager.service;

import com.atguigu.spzx.model.dto.system.AssginRoleDto;
import com.atguigu.spzx.model.dto.system.LoginDto;
import com.atguigu.spzx.model.dto.system.SysUserDto;
import com.atguigu.spzx.model.entity.system.SysRole;
import com.atguigu.spzx.model.entity.system.SysUser;
import com.atguigu.spzx.model.vo.common.Result;
import com.atguigu.spzx.model.vo.system.LoginVo;
import com.atguigu.spzx.model.vo.system.ValidateCodeVo;
import com.baomidou.mybatisplus.core.metadata.IPage;
import com.baomidou.mybatisplus.extension.service.IService;
import org.springframework.stereotype.Service;
import org.springframework.web.multipart.MultipartFile;

@Service
public interface SysUserService extends IService<SysUser> {
    LoginVo login(LoginDto loginDto);

    ValidateCodeVo generateVerifyCode();

    SysUser getUserInfo(String token);

    void logout(String token);

    IPage<SysUser> findByPage(SysUserDto sysUserDto, Integer pageNum, Integer pageSize);

    Result saveSysUser(SysUser sysUser);

    Result updateSysUser(SysUser sysUser);

    String uploadFile(MultipartFile file);

    void doAssign(AssginRoleDto assginRoleDto);
}
