package com.atguigu.spzx.manager.mapper;


import com.atguigu.spzx.model.dto.system.SysUserDto;
import com.atguigu.spzx.model.entity.system.SysUser;
import com.baomidou.mybatisplus.core.mapper.BaseMapper;
import com.baomidou.mybatisplus.core.metadata.IPage;
import org.apache.ibatis.annotations.Mapper;
import org.apache.ibatis.annotations.Param;
import org.apache.ibatis.annotations.Select;

@Mapper
public interface SysUserMapper extends BaseMapper<SysUser> {

    /**
     * 根据用户名查询用户信息
     * @param userName 用户名
     * @return 用户对象
     */
    @Select("select * from sys_user where username = #{userName}")
    SysUser findByUserName(String userName);

    IPage<SysUser> findByPage(IPage pageParam,
                              @Param("dto") SysUserDto sysUserDto);
}
