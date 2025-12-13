package com.atguigu.spzx.manager.service;

import com.atguigu.spzx.model.dto.system.SysRoleDto;
import com.atguigu.spzx.model.entity.system.SysRole;
import com.baomidou.mybatisplus.core.metadata.IPage;
import com.baomidou.mybatisplus.extension.plugins.pagination.Page;
import com.baomidou.mybatisplus.extension.service.IService;
import org.springframework.stereotype.Service;

import java.util.Map;

@Service
public interface SysRoleService extends IService<SysRole> {
    IPage<SysRole> findByPage(IPage page, SysRoleDto sysRoleDto);

    Map<String, Object> findAllRoles(Long userId);
}
