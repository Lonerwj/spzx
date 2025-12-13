package com.atguigu.spzx.manager.service.Impl;

import com.atguigu.spzx.manager.mapper.SysRoleMapper;
import com.atguigu.spzx.manager.service.SysRoleService;
import com.atguigu.spzx.model.dto.system.SysRoleDto;
import com.atguigu.spzx.model.entity.system.SysRole;
import com.baomidou.mybatisplus.core.metadata.IPage;
import com.baomidou.mybatisplus.extension.plugins.pagination.Page;
import com.baomidou.mybatisplus.extension.service.impl.ServiceImpl;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

import java.util.HashMap;
import java.util.List;
import java.util.Map;

@Service
public class SysRoleServiceImpl extends ServiceImpl<SysRoleMapper, SysRole> implements SysRoleService {

    @Autowired
    private SysRoleMapper sysRoleMapper;


    @Override
    public IPage<SysRole> findByPage(IPage page, SysRoleDto sysRoleDto) {

        //根据条件查询所有数据
        IPage<SysRole> pageResult  = sysRoleMapper.findByPage(page,sysRoleDto);

        return pageResult;
    }

    @Override
    public Map<String, Object> findAllRoles(Long userId) {

        List<SysRole> sysRoleList = sysRoleMapper.selectList(null);
        Map<String,Object> map = new HashMap<>();
        map.put("allRolesList",sysRoleList);

        //获取当前用户已授权角色
        List<Long> roleIdList = sysRoleMapper.findRoleIdByUserId(userId);
        map.put("sysUserRoles",roleIdList);
        return map;
    }
}
