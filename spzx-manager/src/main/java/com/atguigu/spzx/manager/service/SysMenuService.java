package com.atguigu.spzx.manager.service;

import com.atguigu.spzx.model.entity.system.SysMenu;
import com.baomidou.mybatisplus.extension.service.IService;
import org.springframework.stereotype.Service;

import java.util.List;

@Service
public interface SysMenuService extends IService<SysMenu> {
    List<SysMenu> findNodes();

    Boolean removeById(Long id);
}
