package com.atguigu.spzx.manager.mapper;

import com.atguigu.spzx.model.dto.system.AssginMenuDto;
import com.atguigu.spzx.model.entity.system.SysRoleMenu;
import com.baomidou.mybatisplus.core.mapper.BaseMapper;
import org.apache.ibatis.annotations.Mapper;
import org.apache.ibatis.annotations.Param;
import org.apache.ibatis.annotations.Select;

import java.util.List;

@Mapper
public interface SysRoleMenuMapper extends BaseMapper<SysRoleMenu> {

    @Select("select menu_id from sys_role_menu where role_id = #{roleId} and is_deleted = 0 and is_half = 0")
    List<Long> findSysRoleMenuByRoleId(Long roleId);

    void doAssign(@Param("dto") AssginMenuDto dto);
}
