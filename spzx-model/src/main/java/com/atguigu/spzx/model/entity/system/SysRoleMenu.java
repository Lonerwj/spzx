package com.atguigu.spzx.model.entity.system;

import com.atguigu.spzx.model.entity.base.BaseEntity;
import com.baomidou.mybatisplus.annotation.TableName;
import com.baomidou.mybatisplus.annotation.IdType;
import com.baomidou.mybatisplus.annotation.TableId;
import lombok.Data;

import java.time.LocalDateTime;
import java.io.Serializable;

/**
 * <p>
 * 角色菜单
 * </p>
 *
 * @author author
 * @since 2025-12-14
 */
@Data
@TableName("sys_role_menu")
public class SysRoleMenu extends BaseEntity{

    private static final long serialVersionUID = 1L;


    private Long roleId;

    private Long menuId;


    private Integer isHalf;


}
