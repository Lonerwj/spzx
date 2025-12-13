package com.atguigu.spzx.manager.service.Impl;

import com.atguigu.spzx.manager.mapper.SysMenuMapper;
import com.atguigu.spzx.manager.service.SysMenuService;
import com.atguigu.spzx.model.entity.system.SysMenu;
import com.baomidou.mybatisplus.core.conditions.query.QueryWrapper;
import com.baomidou.mybatisplus.extension.service.impl.ServiceImpl;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;
import org.springframework.util.CollectionUtils;

import java.util.ArrayList;
import java.util.List;

@Service
public class SysMenuServiceImpl extends ServiceImpl<SysMenuMapper, SysMenu> implements SysMenuService {

    @Autowired
    private SysMenuMapper sysMenuMapper;
    @Override
    public List<SysMenu> findNodes() {
        //查询所有菜单数据
        List<SysMenu> sysMenuList = sysMenuMapper.selectList(null);

        if (CollectionUtils.isEmpty(sysMenuList)){
            return null;
        }
        //构建树形菜单
        return MenuHelp(sysMenuList);
    }

    @Override
    public Boolean removeById(Long id) {
        QueryWrapper<SysMenu> queryWrapper = new QueryWrapper<>();
        queryWrapper.eq("parent_id",id);
        if (sysMenuMapper.selectCount(queryWrapper) > 0){
            return false;
        }else {
            sysMenuMapper.deleteById(id);
            return true;
        }
    }


    private List<SysMenu> MenuHelp(List<SysMenu> sysMenuList){
        List<SysMenu> treeMenuList = new ArrayList<>();
        for (SysMenu sysMenu : sysMenuList) {
            //父级菜单 parent_id = 0
            if (sysMenu.getParentId() == 0L) {
                treeMenuList.add(findChildren(sysMenu,sysMenuList));
            }
        }
        return treeMenuList;
    }

    private SysMenu findChildren(SysMenu sysMenu, List<SysMenu> sysMenuList) {
        sysMenu.setChildren(new ArrayList<SysMenu>());
        for (SysMenu menu : sysMenuList) {
            if (sysMenu.getId().equals(menu.getParentId())) {
                //递归到底层添加子菜单
                sysMenu.getChildren().add(findChildren(menu,sysMenuList));
            }
        }
        return sysMenu;
    }
}
