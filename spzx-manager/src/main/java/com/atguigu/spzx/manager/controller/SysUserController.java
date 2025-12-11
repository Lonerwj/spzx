package com.atguigu.spzx.manager.controller;

import com.atguigu.spzx.manager.service.SysUserService;
import com.atguigu.spzx.model.dto.system.SysUserDto;
import com.atguigu.spzx.model.entity.system.SysRole;
import com.atguigu.spzx.model.entity.system.SysUser;
import com.atguigu.spzx.model.vo.common.Result;
import com.atguigu.spzx.model.vo.common.ResultCodeEnum;
import com.baomidou.mybatisplus.core.metadata.IPage;
import io.swagger.v3.oas.annotations.Operation;
import io.swagger.v3.oas.annotations.tags.Tag;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.web.bind.annotation.*;
import org.springframework.web.multipart.MultipartFile;

@Tag(name = "用户管理接口")
@RestController
@RequestMapping("/admin/system/sysUser")
public class SysUserController {

    @Autowired
    private SysUserService sysUserService;

    @Operation(summary = "用户条件分页查询")
    @GetMapping("findByPage/{pageNum}/{pageSize}")
    public Result<IPage<SysUser>> findByPage(SysUserDto sysUserDto,
                                             @PathVariable Integer pageNum,
                                             @PathVariable Integer pageSize) {
        return Result.build(sysUserService.findByPage(sysUserDto, pageNum, pageSize), ResultCodeEnum.SUCCESS);
    }

    @Operation(summary = "添加用户")
    @PostMapping("saveSysUser")
    public Result saveSysUser(@RequestBody SysUser sysUser) {
        return sysUserService.saveSysUser(sysUser);
    }

    @Operation(summary = "修改用户")
    @PutMapping("updateSysUser")
    public Result updateSysUser(@RequestBody SysUser sysUser) {
        return sysUserService.updateSysUser(sysUser);
    }

    @Operation(summary = "删除用户")
    @DeleteMapping("deleteById/{id}")
    public Result deleteById(@PathVariable Long id) {
        return Result.build(sysUserService.removeById(id), ResultCodeEnum.SUCCESS);
    }

    @Operation(summary = "头像上传")
    @PostMapping("uploadFile")
    public Result uploadFile(@RequestParam("file") MultipartFile file) {
        String url = sysUserService.uploadFile(file);
        return Result.build(url, ResultCodeEnum.SUCCESS);
    }

}
