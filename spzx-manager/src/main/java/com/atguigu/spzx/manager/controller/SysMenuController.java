package com.atguigu.spzx.manager.controller;

import com.atguigu.spzx.manager.service.SysMenuService;
import com.atguigu.spzx.model.entity.system.SysMenu;
import com.atguigu.spzx.model.vo.common.Result;
import com.atguigu.spzx.model.vo.common.ResultCodeEnum;
import io.swagger.v3.oas.annotations.Operation;
import io.swagger.v3.oas.annotations.tags.Tag;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.web.bind.annotation.*;

import java.util.List;

@Tag(name = "菜单接口")
@RestController
@RequestMapping(value = "admin/system/sysMenu")
public class SysMenuController {

    @Autowired
    private SysMenuService sysMenuService;

    @Operation(summary = "菜单列表")
    @GetMapping("/findNodes")
    public Result findNodes() {
        List<SysMenu> sysMenuList = sysMenuService.findNodes();
        return Result.build(sysMenuList, ResultCodeEnum.SUCCESS);

    }

    @Operation(summary = "添加菜单")
    @PostMapping("/save")
    public Result save(@RequestBody SysMenu sysMenu) {
        sysMenuService.save(sysMenu);
        return Result.build(null, ResultCodeEnum.SUCCESS);
    }

    @Operation(summary = "修改菜单")
    @PutMapping("/update")
    public Result update(@RequestBody SysMenu sysMenu) {
        sysMenuService.updateById(sysMenu);
        return Result.build(null, ResultCodeEnum.SUCCESS);
    }

    @Operation(summary = "删除菜单")
    @DeleteMapping("/removeById/{id}")
    public Result removeById(@PathVariable Long id) {
        Boolean isDelete = sysMenuService.removeById(id);
        if (!isDelete){
            return Result.build(null, ResultCodeEnum.NODE_ERROR);
        }
        return Result.build(null, ResultCodeEnum.SUCCESS);
    }
}
