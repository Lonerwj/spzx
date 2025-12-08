package com.atguigu.spzx.manager.controller;

import com.atguigu.spzx.manager.service.SysRoleService;
import com.atguigu.spzx.model.dto.system.SysRoleDto;
import com.atguigu.spzx.model.entity.system.SysRole;
import com.atguigu.spzx.model.vo.common.Result;
import com.atguigu.spzx.model.vo.common.ResultCodeEnum;
import com.baomidou.mybatisplus.core.metadata.IPage;
import com.baomidou.mybatisplus.extension.plugins.pagination.Page;
import io.swagger.v3.oas.annotations.Operation;
import io.swagger.v3.oas.annotations.tags.Tag;
import org.apache.ibatis.annotations.Delete;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.web.bind.annotation.*;

@Tag(name = "角色接口")
@RestController
@RequestMapping(value = "admin/system/sysRole")
public class SysRoleController {

    @Autowired
    private SysRoleService sysRoleService;

    // 查询所有角色
    @Operation(summary = "查询所有角色")
    @PostMapping("/findByPage/{current}/{limit}")
    public Result findByPage(@PathVariable Integer current,
                             @PathVariable Integer limit,
                             @RequestBody SysRoleDto sysRoleDto) {
        IPage<SysRole> pageParam = new Page<>(current, limit);
        IPage<SysRole> page = sysRoleService.findByPage(pageParam, sysRoleDto);
        return Result.build(page, ResultCodeEnum.SUCCESS);
    }

    @Operation(summary = "添加角色")
    @PostMapping("/saveSysRole")
    public Result saveSysRole(@RequestBody SysRole sysRole) {
        sysRoleService.save(sysRole);
        return Result.build(null, ResultCodeEnum.SUCCESS);
    }

    @Operation(summary = "修改角色")
    @PutMapping("/updateSysRole")
    public Result updateSysRole(@RequestBody SysRole sysRole) {
        if (sysRole.getId() == null) {
            return Result.build(null, ResultCodeEnum.DATA_ERROR);
        }
        boolean result = sysRoleService.updateById(sysRole);
        if (result) {
            return Result.build(null, ResultCodeEnum.SUCCESS);
        } else {
            return Result.build(null, ResultCodeEnum.DATA_ERROR);
        }
    }

    //删除角色
    @Operation(summary = "删除角色")
    @DeleteMapping("/deleteById/{roleId}")
    public Result deleteByid(@PathVariable Long roleId) {
        boolean result = sysRoleService.removeById(roleId);
        if (result) {
            return Result.build(null, ResultCodeEnum.SUCCESS);
        } else {
            return Result.build(null, ResultCodeEnum.DATA_ERROR);
        }
    }

}
